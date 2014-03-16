package com.jff.searchapicluster.core.api;

/**
 * Created by Yevhenii Zapletin on 16.03.14.
 */
public class Logger {

    private static final String TYPE_ERROR = "Error";
    private static final String TYPE_INFO = "Info";
    private static final String TYPE_DEBUG = "Debug";

    public static void i(String tag, String message) {
        printMessage(TYPE_INFO, tag, message);
    }

    public static void d(String tag, String message) {
        printMessage(TYPE_DEBUG, tag, message);
    }

    public static void e(String tag, String message) {
        printMessage(TYPE_ERROR, tag, message);
    }

    private static void printMessage(String type, String tag, String message) {
        System.out.println(String.format("%s:%s:%s", type, tag, message));
    }
}
