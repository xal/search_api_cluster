package com.jff.searchapicluster.worker.main;

import com.google.gson.Gson;
import com.jff.searchapicluster.core.api.Logger;
import com.jff.searchapicluster.core.api.entity.json.result.SearchResult;
import com.jff.searchapicluster.core.api.entity.json.task.SearchTask;
import com.jff.searchapicluster.core.mina.message.ResponseMessage;
import com.jff.searchapicluster.worker.mina.WorkerSessionHandler;
import com.jff.searchapicluster.worker.model.entity.SearchEngine;
import com.jff.searchapicluster.worker.searchengine.google.GoogleEngine;
import com.jff.searchapicluster.worker.searchengine.yandex.YandexEngine;
import org.apache.commons.configuration.Configuration;
import org.apache.commons.configuration.ConfigurationException;
import org.apache.commons.configuration.PropertiesConfiguration;
import org.apache.mina.core.RuntimeIoException;
import org.apache.mina.core.future.ConnectFuture;
import org.apache.mina.core.session.IoSession;
import org.apache.mina.filter.codec.ProtocolCodecFilter;
import org.apache.mina.filter.codec.serialization.ObjectSerializationCodecFactory;
import org.apache.mina.filter.logging.LoggingFilter;
import org.apache.mina.transport.socket.nio.NioSocketConnector;

import java.net.InetSocketAddress;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Created by Yevhenii Zapletin on 16.03.14.
 */
public class WorkerManager {

    private static final String LOG_TAG = WorkerManager.class.getCanonicalName();

    private List<SearchEngine> engines = new ArrayList<SearchEngine>();
    private static WorkerManager instance;
    private IoSession session;

    public WorkerManager() {
        init();
    }

    public static WorkerManager getInstance() {
        if (instance == null) {
            instance = new WorkerManager();
        }
        return instance;
    }

    private void init() {

        try {
            Configuration config = new PropertiesConfiguration("settings.txt");
            String localIP = config.getString("localIP");
            String yandexUsername = config.getString("yandexUsername");
            String yandexPassword = config.getString("yandexPassword");
            engines.add(new GoogleEngine(localIP));
            engines.add(new YandexEngine(yandexUsername, yandexPassword));
        } catch (ConfigurationException e) {
            e.printStackTrace();
        }
    }

    public void search(final long requestId, final int requestNumber, final SearchTask searchTask) {


        for (final SearchEngine engine : engines)
            if (isNeedUseEngine(engine, searchTask)) {
                SearchEngine.SearchEngineCallback callback = new SearchEngine.SearchEngineCallback() {
                    @Override
                    public void onResult(SearchResult searchResultArray[]) {

                        String engineName = engine.getName();
                        Logger.i(LOG_TAG, String.format("\nSuccess %s result = %d \n%s\n", engineName, searchResultArray.length, searchTask));

                        for (SearchResult searchResult : searchResultArray) {
                            Logger.i(LOG_TAG, String.format("%s", Arrays.toString(searchResultArray)));
                        }

                        Gson gson = new Gson();

                        String json = gson.toJson(searchResultArray);
                        System.out.println(json);

                        sendResponse(requestId, requestNumber, searchTask, searchResultArray);
                    }

                    @Override
                    public void onError() {
                        Logger.e(LOG_TAG, String.format("On execute : %s", searchTask));
                    }
                };
                engine.executeTask(searchTask, callback);
            }

    }

    private void sendResponse(long requestId, int requestNumber, SearchTask searchTask, SearchResult[] searchResultArray) {


        ResponseMessage responseMessage = new ResponseMessage(requestId, searchTask, requestNumber, searchResultArray);
        session.write(responseMessage);

    }

    private boolean isNeedUseEngine(SearchEngine engine, SearchTask searchTask) {
        String engineName = engine.getName();

        boolean isNeedUse = false;

        for (String taskEngine : searchTask.settings.search_engines) {

            if (taskEngine != null && taskEngine.equalsIgnoreCase(engineName)) {
                isNeedUse = true;
                break;
            }
        }

        return isNeedUse;
    }

    public void setSession(IoSession session) {
        this.session = session;
    }

    public IoSession getSession() {
        return session;
    }

    public void connect() {
        NioSocketConnector connector = new NioSocketConnector();

        try {
            Configuration config = new PropertiesConfiguration("settings.txt");
            long connectTimeout = config.getLong("timeout");
            String hostname = config.getString("serverIP");
            int port = config.getInt("serverPort");


            // Configure the service.
            connector.setConnectTimeoutMillis(connectTimeout);


            connector.getFilterChain().addLast(
                    "com/jff/searchapicluster/core/mina/codec",
                    new ProtocolCodecFilter(
                            new ObjectSerializationCodecFactory()));

            connector.getFilterChain().addLast("logger", new LoggingFilter());

            connector.setHandler(new WorkerSessionHandler());

            IoSession session;
            for (; ; ) {
                try {

                    ConnectFuture future = connector.connect(new InetSocketAddress(
                            hostname, port));
                    future.awaitUninterruptibly();
                    session = future.getSession();

                    WorkerManager instance = WorkerManager.getInstance();
                    instance.setSession(session);
                    break;
                } catch (RuntimeIoException e) {
                    System.err.println("Failed to connect.");
                    e.printStackTrace();
                    try {
                        Thread.sleep(5000);
                    } catch (InterruptedException e1) {
                        e1.printStackTrace();
                    }
                }
            }


        } catch (ConfigurationException e) {
            e.printStackTrace();
        }
    }

}
