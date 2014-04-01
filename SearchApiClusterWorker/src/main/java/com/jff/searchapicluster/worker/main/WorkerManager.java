package com.jff.searchapicluster.worker.main;

import com.google.gson.Gson;
import com.jff.searchapicluster.core.api.Logger;
import com.jff.searchapicluster.core.api.entity.json.result.SearchResult;
import com.jff.searchapicluster.core.api.entity.json.task.SearchTask;
import com.jff.searchapicluster.core.api.entity.json.task.SearchTaskSettings;
import com.jff.searchapicluster.worker.model.entity.SearchEngine;
import com.jff.searchapicluster.worker.searchengine.google.GoogleEngine;
import com.jff.searchapicluster.worker.searchengine.yandex.YandexEngine;
import org.apache.commons.configuration.Configuration;
import org.apache.commons.configuration.ConfigurationException;
import org.apache.commons.configuration.PropertiesConfiguration;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Created by Yevhenii Zapletin on 16.03.14.
 */
public class WorkerManager {

    private static final String LOG_TAG = WorkerManager.class.getCanonicalName();

    private List<SearchEngine> engines = new ArrayList<SearchEngine>();

    public static void main(String args[]) {

        WorkerManager worker = new WorkerManager();

        worker.init();
        worker.go();
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

    private void go() {

        String query = "Hello world";

        SearchTask searchTask = new SearchTask();

        searchTask.requests = new String[1];
        searchTask.requests[0] = query;

        searchTask.settings = new SearchTaskSettings();

        searchTask.settings.countResponses = 20;
        searchTask.settings.searchEngines = new String[2];

        searchTask.settings.searchEngines[0] = "Google";
        searchTask.settings.searchEngines[1] = "Yandex";


        this.search(searchTask);
    }

    private void search(final SearchTask searchTask) {


        for (final SearchEngine engine : engines) {
            if (isNeedUseEngine(engine, searchTask)) {
                SearchEngine.SearchEngineCallback callback = new SearchEngine.SearchEngineCallback() {
                    @Override
                    public void onResult(SearchResult searchResultArray[]) {

                        String engineName = engine.getName();
                        Logger.i(LOG_TAG, String.format("\nSuccess %s result = %d \n%s\n", engineName,searchResultArray.length, searchTask));

                        for(SearchResult searchResult  : searchResultArray) {
                            Logger.i(LOG_TAG, String.format("%s", Arrays.toString(searchResultArray)));
                        }

                        Gson gson = new Gson();

                        String json = gson.toJson(searchResultArray);
                        System.out.println(json);
                    }

                    @Override
                    public void onError() {
                        Logger.e(LOG_TAG, String.format("On execute : %s", searchTask));
                    }
                };
                engine.executeTask(searchTask, callback);
            }
        }

    }

    private boolean isNeedUseEngine(SearchEngine engine, SearchTask searchTask) {
        String engineName = engine.getName();

        boolean isNeedUse = false;

        for (String taskEngine : searchTask.settings.searchEngines) {

            if (taskEngine != null && taskEngine.equalsIgnoreCase(engineName)) {
                isNeedUse = true;
                break;
            }
        }

        return isNeedUse;
    }
}
