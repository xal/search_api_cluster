package com.jff.searchapicluster.core.api;

import org.slf4j.LoggerFactory;

/**
 * Created by Yevhenii Zapletin on 16.03.14.
 */
public class Logger {

    private enum Type {
        ERROR, INFO, DEBUG;
    }

    public static void i(String tag, String message) {
        printMessage(Type.INFO, tag, message);
    }

    public static void d(String tag, String message) {
        printMessage(Type.DEBUG, tag, message);
    }

    public static void e(String tag, String message) {
        printMessage(Type.ERROR, tag, message);
    }

    private static void printMessage(Type type, String tag, String message) {
        org.slf4j.Logger logger = LoggerFactory.getLogger(tag);


        switch (type) {

            case ERROR:

            case INFO:

            case DEBUG:
                logger.info(message);
                break;
        }
    }
}
