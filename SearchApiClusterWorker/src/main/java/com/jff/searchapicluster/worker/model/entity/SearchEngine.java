package com.jff.searchapicluster.worker.model.entity;

import com.jff.searchapicluster.core.api.entity.json.result.SearchResult;
import com.jff.searchapicluster.core.api.entity.json.task.SearchTask;

/**
 * Created by Yevhenii Zapletin on 16.03.14.
 */
public abstract class SearchEngine {

    private String name;

    public SearchEngine(String name) {
        this.name = name;
    }


    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public static interface SearchEngineCallback {

        public void onResult(SearchResult[] searchResult);

        public void onError();
    }

    public void executeTask(SearchTask searchTask, SearchEngineCallback callback) {

        try {
            SearchResult[] searchResult = performSearch(searchTask);
            callback.onResult(searchResult);
        } catch (Exception e) {
            e.printStackTrace();
            callback.onError();
        }

    }

    protected abstract SearchResult[] performSearch(SearchTask searchTask) throws Exception;
}
