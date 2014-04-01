package com.jff.searchapicluster.core.api.entity.json.task;

import java.io.Serializable;
import java.util.Arrays;

/**
 * Created by Yevhenii Zapletin on 15.03.14.
 */
public class SearchTaskSettings implements Serializable {
    public int count_responses;
    public String[] search_engines;

    @Override
    public String toString() {
        return "SearchTaskSettings{" +
                "count_responses=" + count_responses +
                ", search_engines=" + Arrays.toString(search_engines) +
                '}';
    }
}
