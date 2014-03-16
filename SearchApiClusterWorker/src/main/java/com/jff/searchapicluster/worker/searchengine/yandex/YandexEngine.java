package com.jff.searchapicluster.worker.searchengine.yandex;

import com.jff.searchapicluster.core.api.entity.json.result.SearchResult;
import com.jff.searchapicluster.core.api.entity.json.task.SearchTask;
import com.jff.searchapicluster.worker.model.entity.SearchEngine;

/**
 * Created by Yevhenii Zapletin on 16.03.14.
 */
public class YandexEngine extends SearchEngine {

    public static final String SEARCH_ENGINE_YANDEX_NAME = "Yandex";

    public YandexEngine() {
        super(SEARCH_ENGINE_YANDEX_NAME);
    }

    @Override
    protected SearchResult[] performSearch(SearchTask searchTask) throws Exception {
        return new SearchResult[0];
    }
}
