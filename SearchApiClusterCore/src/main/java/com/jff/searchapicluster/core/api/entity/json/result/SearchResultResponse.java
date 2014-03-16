package com.jff.searchapicluster.core.api.entity.json.result;

/**
 * Created by Yevhenii Zapletin on 15.03.14.
 */
public class SearchResultResponse {

    public int rank;
    public String response_url;
    public String response_title;

    @Override
    public String toString() {
        return "\nSearchResultResponse{" +
                "\nrank=" + rank +
                ", \nresponse_url='" + response_url + '\'' +
                ", \nresponse_title='" + response_title + '\'' +
                "\n}";
    }
}
