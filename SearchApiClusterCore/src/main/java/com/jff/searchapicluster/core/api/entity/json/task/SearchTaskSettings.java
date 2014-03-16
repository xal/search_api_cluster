package com.jff.searchapicluster.core.api.entity.json.task;

import java.util.Arrays;

/**
 * Created by Yevhenii Zapletin on 15.03.14.
 */
public class SearchTaskSettings {
    public int countResponses;
    public String[] searchEngines;

    @Override
    public String toString() {
        return "SearchTaskSettings{" +
                "countResponses=" + countResponses +
                ", searchEngines=" + Arrays.toString(searchEngines) +
                '}';
    }
}
