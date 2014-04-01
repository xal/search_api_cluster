package com.jff.searchapicluster.worker.searchengine.google;

import com.google.gson.Gson;
import com.jff.searchapicluster.core.api.Logger;
import com.jff.searchapicluster.core.api.entity.json.result.SearchResult;
import com.jff.searchapicluster.core.api.entity.json.result.SearchResultResponse;
import com.jff.searchapicluster.core.api.entity.json.task.SearchTask;
import com.jff.searchapicluster.worker.model.entity.SearchEngine;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.Reader;
import java.net.URL;
import java.net.URLEncoder;
import java.util.List;

/**
 * Created by Yevhenii Zapletin on 16.03.14.
 */
public class GoogleEngine extends SearchEngine {

    private static final String GOOGLE_ENGINE_NAME = "Google";
    public static final String UTF_8_ENCODING = "UTF-8";
    public static final int RESPONSES_PER_REQUEST = 4;
    private static final String LOG_TAG = GoogleEngine.class.getCanonicalName();
    private static final long ERROR_TIMEOUT = 5000;
    private static final long QUERY_TIMEOUT = 1000;
    private String ip;

    public GoogleEngine(String ip) {
        super(GOOGLE_ENGINE_NAME);
        this.ip = ip;
    }

    private Gson gson = new Gson();

    @Override
    protected SearchResult[] performSearch(SearchTask searchTask) throws Exception {
        SearchResult[] searchResultArray = new SearchResult[searchTask.requests.length];


        for (int i = 0; i < searchTask.requests.length; i++) {
            SearchResult searchResult = new SearchResult();
            searchResult.searchEngine = getName();
            searchResult.request = searchTask.requests[i];

            searchResultArray[i] = searchResult;

            int countResponses = searchTask.settings.count_responses;
            searchResult.responses = new SearchResultResponse[countResponses];

            String query = searchTask.requests[i];
            String charset = UTF_8_ENCODING;

            searchResult.request = query;

            for (int j = 0; j < searchTask.settings.count_responses + RESPONSES_PER_REQUEST; j += RESPONSES_PER_REQUEST) {
                int startRank = j;

                String address = String.format("http://ajax.googleapis.com/ajax/services/search/web?v=1.0&start=%d&q=%s&userip=%s", startRank, URLEncoder.encode(query, charset), ip);
                URL url = new URL(address);
                Reader reader = new InputStreamReader(url.openStream(), charset);

                Logger.d(LOG_TAG, String.format("Execute query : %s", url.toString()));

                BufferedReader bufferedReader = new BufferedReader(reader);

                GoogleResults results = gson.fromJson(reader, GoogleResults.class);


                if (results.getResponseData() == null) {
                    Logger.d(LOG_TAG, String.format("Error received from google. Wait = %d", ERROR_TIMEOUT));
                    sleep(ERROR_TIMEOUT);
                } else {
                    Logger.d(LOG_TAG, String.format("Success received from google."));
                }
                for (int k = 0; k < RESPONSES_PER_REQUEST; k++) {
                    GoogleResults.ResponseData responseData = results.getResponseData();
                    List<GoogleResults.Result> responseDataResults = responseData.getResults();

                    GoogleResults.Result result = responseDataResults.get(k);

                    int rank = startRank + k;

                    if (rank < searchResult.responses.length) {

                        SearchResultResponse searchResultResponse = new SearchResultResponse();
                        searchResultResponse.rank = rank;
                        searchResultResponse.response_url = result.getUrl();
                        searchResultResponse.response_title = result.getTitle();

                        searchResult.responses[rank] = searchResultResponse;

                    }
                }

                Logger.d(LOG_TAG, String.format("Wait after success request = %d", QUERY_TIMEOUT));

                sleep(QUERY_TIMEOUT);
            }
        }
        return searchResultArray;
    }

    private void sleep(long timeout) throws InterruptedException {
        Thread.sleep(timeout);
    }
}
