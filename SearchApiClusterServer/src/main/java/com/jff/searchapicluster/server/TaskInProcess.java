package com.jff.searchapicluster.server;

import com.jff.searchapicluster.core.api.entity.json.result.SearchResult;
import com.jff.searchapicluster.core.api.entity.json.task.SearchTask;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Yevhenii Zapletin on 01.04.14.
 */
public class TaskInProcess {
    private long requestId;
    private SearchTask searchTask;
    private List<SearchResult[]> results = new ArrayList<SearchResult[]>();



    public TaskInProcess(long requestId, SearchTask searchTask) {
        this.requestId = requestId;
        this.searchTask = searchTask;
    }

    public long getRequestId() {
        return requestId;
    }

    public void setRequestId(int requestId) {
        this.requestId = requestId;
    }

    public SearchTask getSearchTask() {
        return searchTask;
    }

    public void setSearchTask(SearchTask searchTask) {
        this.searchTask = searchTask;
    }

    public List<SearchResult[]> getResults() {
        return results;
    }


    public boolean add(SearchResult[] searchResult) {
        return results.add(searchResult);
    }

    public boolean isCompleted() {
        int size = results.size();
        int length = searchTask.requests.length * searchTask.settings.search_engines.length;
        boolean completed = (size == length);
        return completed;
    }
}
