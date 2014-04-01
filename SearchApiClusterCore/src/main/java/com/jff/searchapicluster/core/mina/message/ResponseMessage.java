package com.jff.searchapicluster.core.mina.message;

import com.jff.searchapicluster.core.api.entity.json.result.SearchResult;
import com.jff.searchapicluster.core.api.entity.json.task.SearchTask;

/**
 * Created by Yevhenii Zapletin on 01.04.14.
 */
public class ResponseMessage extends AbstractMessage {
    private long requestId;
    private SearchTask searchTask;
    private int requestNumber;
    private SearchResult[] searchResult;

    public ResponseMessage(long requestId, SearchTask searchTask, int requestNumber, SearchResult[] searchResult) {
        this.requestId = requestId;
        this.searchTask = searchTask;
        this.requestNumber = requestNumber;
        this.searchResult = searchResult;
    }

    public long getRequestId() {
        return requestId;
    }

    public void setRequestId(long requestId) {
        this.requestId = requestId;
    }

    public SearchTask getSearchTask() {
        return searchTask;
    }

    public void setSearchTask(SearchTask searchTask) {
        this.searchTask = searchTask;
    }

    public int getRequestNumber() {
        return requestNumber;
    }

    public void setRequestNumber(int requestNumber) {
        this.requestNumber = requestNumber;
    }

    public SearchResult[] getSearchResult() {
        return searchResult;
    }

    public void setSearchResult(SearchResult[] searchResult) {
        this.searchResult = searchResult;
    }
}
