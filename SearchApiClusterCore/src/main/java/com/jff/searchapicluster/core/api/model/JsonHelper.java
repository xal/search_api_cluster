package com.jff.searchapicluster.core.api.model;

import com.google.gson.Gson;
import com.jff.searchapicluster.core.api.entity.json.result.SearchResult;
import com.jff.searchapicluster.core.api.entity.json.task.SearchTask;

/**
 * Created by Yevhenii Zapletin on 15.03.14.
 */
public class JsonHelper {

    private final Gson gson;

    public JsonHelper() {

        gson = new Gson();
    }

    public SearchTask parseTask(String input) {

        SearchTask jsonTask = gson.fromJson(input, SearchTask.class);

        return jsonTask;
    }

    public SearchResult parseResult(String input) {

        SearchResult jsonResult = gson.fromJson(input, SearchResult.class);

        return jsonResult;
    }

    public String toJson(SearchTask searchTask) {
        return gson.toJson(searchTask);
    }
    public String toJson(SearchResult searchResult) {
        return gson.toJson(searchResult);
    }
}
