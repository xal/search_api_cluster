package com.jff.searchapicluster.worker.searchengine.yandex;

import com.jff.searchapicluster.core.api.Logger;
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

    private static final String LOG_TAG = YandexEngine.class.getCanonicalName();
    private static final int RESPONSES_PER_REQUEST = 10;
    private static final int ERROR_TIMEOUT = 5000;
    private static final int QUERY_TIMEOUT = 1000;
    private String yandexUsername;
    private String yandexPassword;

    public YandexEngine(String yandexUsername, String yandexPassword) {
        super(SEARCH_ENGINE_YANDEX_NAME);
        this.yandexUsername = yandexUsername;
        this.yandexPassword = yandexPassword;
    }


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
            searchResult.request = query;
            List<Result> resultList = null;

            for (int j = 0; j < searchTask.settings.count_responses + RESPONSES_PER_REQUEST; j += RESPONSES_PER_REQUEST) {
                int startRank = j;

                int pageNumber = j % RESPONSES_PER_REQUEST;
                try {

                    resultList = Parser.executeQuery(yandexUsername, yandexPassword, query, pageNumber);
                } catch (Exception e) {
                    e.printStackTrace();
                }


                if (resultList == null || resultList.isEmpty()) {
                    Logger.d(LOG_TAG, String.format("Error received from yandex. Wait = %d", ERROR_TIMEOUT));
                    sleep(ERROR_TIMEOUT);
                } else {
                    Logger.d(LOG_TAG, String.format("Success received from yandex."));
                }
                for (int k = 0; k < RESPONSES_PER_REQUEST; k++) {
                    Result result = resultList.get(k);
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

    private void sleep(int errorTimeout) {
        try {
            Thread.sleep(errorTimeout);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}
