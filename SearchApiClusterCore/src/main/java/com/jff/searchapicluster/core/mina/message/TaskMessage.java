package com.jff.searchapicluster.core.mina.message;

import com.jff.searchapicluster.core.api.entity.json.task.SearchTask;

/**
 * Created by Yevhenii Zapletin on 01.04.14.
 */
public class TaskMessage extends  AbstractMessage {
    private long requestId;
    private SearchTask searchTask;
    private int requestNumber;

    public TaskMessage(long requestId, SearchTask searchTask, int requestNum) {
        this.requestId = requestId;
        this.searchTask = searchTask;
        this.requestNumber = requestNum;
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
}
