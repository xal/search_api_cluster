package com.jff.searchapicluster.worker.searchengine.yandex;

import com.jff.searchapicluster.core.api.entity.json.result.SearchResult;
import com.jff.searchapicluster.core.api.entity.json.result.SearchResultResponse;
import com.jff.searchapicluster.core.api.entity.json.task.SearchTask;
import com.jff.searchapicluster.worker.model.entity.SearchEngine;

import java.util.List;

/**
 * Created by Yevhenii Zapletin on 16.03.14.
 */
public class YandexEngine extends SearchEngine {

    public static final String SEARCH_ENGINE_YANDEX_NAME = "Yandex";
    private static final String YANDEX_USERNAME = "";
    private static final String YANDEX_PASSWORD = "";

    public YandexEngine() {
        super(SEARCH_ENGINE_YANDEX_NAME);
    }

    @Override
    protected SearchResult[] performSearch(SearchTask searchTask) throws Exception {
        SearchResult[] searchResultArray = new SearchResult[searchTask.requests.length];


        for (int i = 0; i < searchTask.requests.length; i++) {
            SearchResult searchResult = new SearchResult();
            searchResult.searchEngine = getName();
            searchResult.request = searchTask.requests[i];

            searchResultArray[i] = searchResult;

            int countResponses = searchTask.settings.countResponses;
            searchResult.responses = new SearchResultResponse[countResponses];

            String query = searchTask.requests[i];
            searchResult.request = query;

            int count = countResponses;
            List<Result> resultList = Parser.executeQuery(YANDEX_USERNAME, YANDEX_PASSWORD, query, count);
//
//
//            String charset = UTF_8_ENCODING;
//            for (int j = 0; j < searchTask.settings.countResponses + RESPONSES_PER_REQUEST; j += RESPONSES_PER_REQUEST) {
//                int startRank = j;
//
//                String address = String.format("http://ajax.googleapis.com/ajax/services/search/web?v=1.0&start=%d&q=%s&userip=%s", startRank, URLEncoder.encode(query, charset), ip);
//                URL url = new URL(address);
//                Reader reader = new InputStreamReader(url.openStream(), charset);
//
//                Logger.d(LOG_TAG, String.format("Execute query : %s", url.toString()));
//
//                BufferedReader bufferedReader = new BufferedReader(reader);
//
//                GoogleResults results = gson.fromJson(reader, GoogleResults.class);
//
//
//                if (results.getResponseData() == null) {
//                    Logger.d(LOG_TAG, String.format("Error received from google. Wait = %d", ERROR_TIMEOUT));
//                    sleep(ERROR_TIMEOUT);
//                } else {
//                    Logger.d(LOG_TAG, String.format("Success received from google."));
//                }
//                for (int k = 0; k < RESPONSES_PER_REQUEST; k++) {
//                    GoogleResults.ResponseData responseData = results.getResponseData();
//                    List<GoogleResults.Result> responseDataResults = responseData.getResults();
//
//                    GoogleResults.Result result = responseDataResults.get(k);
//
//                    int rank = startRank + k;
//
//                    if (rank < searchResult.responses.length) {
//
//                        SearchResultResponse searchResultResponse = new SearchResultResponse();
//                        searchResultResponse.rank = rank;
//                        searchResultResponse.response_url = result.getUrl();
//                        searchResultResponse.response_title = result.getTitle();
//
//                        searchResult.responses[rank] = searchResultResponse;
//
//                    }
//                }
//
//                Logger.d(LOG_TAG, String.format("Wait after success request = %d", QUERY_TIMEOUT));
//
//                sleep(QUERY_TIMEOUT);
//            }
        }
        return searchResultArray;
    }
}
