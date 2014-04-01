package com.jff.searchapicluster.server;

import com.google.gson.Gson;
import com.jff.searchapicluster.core.api.Logger;
import com.jff.searchapicluster.core.api.entity.json.result.SearchResult;
import com.jff.searchapicluster.core.api.entity.json.task.SearchTask;
import com.jff.searchapicluster.core.mina.message.ResponseMessage;
import com.jff.searchapicluster.core.mina.message.TaskMessage;
import org.apache.mina.core.session.IoSession;

import java.io.FileWriter;
import java.io.IOException;
import java.net.SocketAddress;
import java.util.*;

/**
 * Created by Yevhenii Zapletin on 01.04.14.
 */
public class ServerManager {

    private static final String LOG_TAG = ServerManager.class.getCanonicalName();
    private Hashtable<Long, TaskInProcess> inProcess = new Hashtable<Long, TaskInProcess>();

    private Comparator<? super SessionWrapper> comparator = new Comparator<SessionWrapper>() {
        @Override
        public int compare(SessionWrapper sessionWrapper, SessionWrapper sessionWrapper2) {

            return ((Integer) sessionWrapper.countJobs).compareTo(sessionWrapper2.countJobs);


        }
    };
    private int requestIDCounter;

    private class SessionWrapper {
        public IoSession ioSession;
        public int countJobs;

        private SessionWrapper(IoSession ioSession) {
            this.ioSession = ioSession;
        }
    }

    private static ServerManager instance;

    public static ServerManager getInstance() {
        if (instance == null) {
            instance = new ServerManager();
        }
        return instance;
    }

    private Hashtable<SocketAddress, SessionWrapper> sessions = new Hashtable<SocketAddress, SessionWrapper>();


    public void addSession(IoSession ioSession) {
        synchronized (sessions) {
            sessions.put(ioSession.getRemoteAddress(), new SessionWrapper(ioSession));

        }
    }

    public SessionWrapper removeSessions(IoSession ioSession) {
        synchronized (sessions) {

            return sessions.remove(ioSession.getRemoteAddress());
        }
    }

    public void startProcessTask(SearchTask task) {



        if(sessions.isEmpty()) {
            Logger.e(LOG_TAG, "Sessions is empty. Abort");
            return;
        }

        List<SessionWrapper> list = new ArrayList<SessionWrapper>();
        synchronized (sessions) {
            Collection<SessionWrapper> values = sessions.values();
            list.addAll(values);
            Collections.sort(list, comparator);
        }
        long requestId = calculateRequestID();

        TaskInProcess taskInProcess = new TaskInProcess(requestId, task);

        inProcess.put(requestId, taskInProcess);

        int requestLength = task.requests.length;
        int sessionsLength = list.size();
        for (int i = 0; i < requestLength; i++) {

            int requestNumber = i;
            int position = requestNumber % sessionsLength;

            SessionWrapper sessionWrapper = list.get(position);


            sendRequestToSession(requestId, sessionWrapper, task, requestNumber);
        }
    }

    public void sendRequestToSession(long requestId, SessionWrapper sessionWrapper, SearchTask task, int requestNumber) {

        sessionWrapper.countJobs++;

        TaskMessage taskMessage = new TaskMessage(requestId, task, requestNumber);

        sessionWrapper.ioSession.write(taskMessage);
    }

    public void handleResult(IoSession ioSession, ResponseMessage result) {

        SessionWrapper sessionWrapper = getSessionWrapper(ioSession.getRemoteAddress());
        sessionWrapper.countJobs--;

        handleResult(result);
    }

    private void handleResult(ResponseMessage result) {


        long requestId = result.getRequestId();
        TaskInProcess taskInProcess = inProcess.get(requestId);

        SearchResult[] searchResult = result.getSearchResult();
        taskInProcess.add(searchResult);

        if (taskInProcess.isCompleted()) {

            inProcess.remove(requestId);
            saveResult(taskInProcess);
        }

    }

    private void saveResult(TaskInProcess taskInProcess) {


        List<SearchResult[]> results = taskInProcess.getResults();

        List<SearchResult> resultList = new ArrayList<SearchResult>();
        for (SearchResult[] result : results) {

            for (SearchResult searchResult : result) {
                resultList.add(searchResult);
            }

        }


        Gson gson = new Gson();


        String file = System.currentTimeMillis() + ".json";
        try {
            FileWriter fileWriter = new FileWriter(file);


            String json = gson.toJson(resultList.toArray());

            fileWriter.write(json);
            fileWriter.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private SessionWrapper getSessionWrapper(SocketAddress remoteAddress) {
        return sessions.get(remoteAddress);
    }


    private synchronized long calculateRequestID() {
        return requestIDCounter++;
    }
}
