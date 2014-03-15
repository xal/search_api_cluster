package com.jff.searchapicluster.core.api.model;

import com.google.gson.Gson;
import com.jff.searchapicluster.core.api.entity.json.result.JsonResult;
import com.jff.searchapicluster.core.api.entity.json.task.JsonTask;

/**
 * Created by Yevhenii Zapletin on 15.03.14.
 */
public class JsonHelper {

    private final Gson gson;

    public JsonHelper() {

        gson = new Gson();
    }

    public JsonTask parseTask(String input) {

        JsonTask jsonTask = gson.fromJson(input, JsonTask.class);

        return jsonTask;
    }

    public JsonResult parseResult(String input) {

        JsonResult jsonResult = gson.fromJson(input, JsonResult.class);

        return jsonResult;
    }
}
