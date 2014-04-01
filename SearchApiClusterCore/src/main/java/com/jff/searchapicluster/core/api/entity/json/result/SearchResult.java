package com.jff.searchapicluster.core.api.entity.json.result;

import java.io.Serializable;
import java.util.Arrays;

/**
 * Created by Yevhenii Zapletin on 15.03.14.
 */
public class SearchResult implements Serializable {
    public String request;
    public String searchEngine;
    public SearchResultResponse[] responses;

    @Override
    public String toString() {
        return "\nSearchResult{" +
                "request='" + request + '\'' +
                ", searchEngine='" + searchEngine + '\'' +
                ", responses=" + Arrays.toString(responses) +
                "\n}";
    }
}
