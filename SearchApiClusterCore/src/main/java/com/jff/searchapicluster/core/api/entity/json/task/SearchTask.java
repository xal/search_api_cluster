package com.jff.searchapicluster.core.api.entity.json.task;

import java.util.Arrays;

/**
 * Created by Yevhenii Zapletin on 15.03.14.
 */
public class SearchTask {
    public SearchTaskSettings settings;
    public String[] requests;

    @Override
    public String toString() {
        return "SearchTask{" +
                "settings=" + settings +
                ", requests=" + Arrays.toString(requests) +
                '}';
    }
}
