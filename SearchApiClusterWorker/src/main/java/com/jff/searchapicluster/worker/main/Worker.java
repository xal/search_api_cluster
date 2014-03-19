package com.jff.searchapicluster.worker.main;

import com.google.gson.Gson;
import com.jff.searchapicluster.core.api.Logger;
import com.jff.searchapicluster.core.api.entity.json.result.SearchResult;
import com.jff.searchapicluster.core.api.entity.json.task.SearchTask;
import com.jff.searchapicluster.core.api.entity.json.task.SearchTaskSettings;
import com.jff.searchapicluster.worker.model.entity.SearchEngine;
import com.jff.searchapicluster.worker.searchengine.google.GoogleEngine;
import com.jff.searchapicluster.worker.searchengine.yandex.YandexEngine;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Created by Yevhenii Zapletin on 16.03.14.
 */
public class Worker {

    private static final String LOG_TAG = Worker.class.getCanonicalName();
//    public static final String MY_IP = "62.182.67.92";
    public static final String MY_IP = "77.52.28.208";
    private List<SearchEngine> engines = new ArrayList<SearchEngine>();

    public static void main(String args[]) {

        Worker worker = new Worker();

        worker.init();
        worker.go();
    }

    private void init() {

        engines.add(new GoogleEngine(MY_IP));
        engines.add(new YandexEngine());
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
