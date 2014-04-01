/*
 *  Licensed to the Apache Software Foundation (ASF) under one
 *  or more contributor license agreements.  See the NOTICE file
 *  distributed with this work for additional information
 *  regarding copyright ownership.  The ASF licenses this file
 *  to you under the Apache License, Version 2.0 (the
 *  "License"); you may not use this file except in compliance
 *  with the License.  You may obtain a copy of the License at
 *
 *    http://www.apache.org/licenses/LICENSE-2.0
 *
 *  Unless required by applicable law or agreed to in writing,
 *  software distributed under the License is distributed on an
 *  "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
 *  KIND, either express or implied.  See the License for the
 *  specific language governing permissions and limitations
 *  under the License.
 *
 */
package com.jff.searchapicluster.server.mina;

import com.google.gson.Gson;
import com.jff.searchapicluster.core.api.Logger;
import com.jff.searchapicluster.core.api.entity.json.task.SearchTask;
import com.jff.searchapicluster.server.ServerManager;
import org.apache.commons.configuration.PropertiesConfiguration;
import org.apache.mina.filter.codec.ProtocolCodecFilter;
import org.apache.mina.filter.codec.serialization.ObjectSerializationCodecFactory;
import org.apache.mina.filter.logging.LoggingFilter;
import org.apache.mina.transport.socket.nio.NioSocketAcceptor;

import java.io.*;
import java.net.InetSocketAddress;

/**
 * (<strong>Entry Point</strong>) Starts SumUp mina.
 *
 * @author <a href="http://mina.apache.org">Apache MINA Project</a>
 */
public class Server {


    private static final String LOG_TAG = Server.class.getCanonicalName();

    public static void main(String[] args) throws Throwable {

        org.apache.commons.configuration.Configuration config = new PropertiesConfiguration("settings.txt");


        int serverPort = config.getInt("listenPort");


        NioSocketAcceptor acceptor = new NioSocketAcceptor();

        acceptor.getFilterChain().addLast("com/jff/searchapicluster/core/mina/codec",
                new ProtocolCodecFilter(new ObjectSerializationCodecFactory()));

        acceptor.getFilterChain().addLast("logger", new LoggingFilter());

        acceptor.setHandler(new ServerSessionHandler());
        acceptor.bind(new InetSocketAddress(serverPort));

        System.out.println("Listening on port " + serverPort);
        System.out.println("Please enter filename");

        //  open up standard input
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));

        while (true) {
            String filepath = br.readLine();

        filepath = "/Users/admin/repos/study_repos/kurs_sp/search_api_cluster/SearchApiClusterServer/task.json";
            File file = new File(filepath);
            Logger.d(LOG_TAG, file.getAbsolutePath());

            if(file.exists() && file.isFile()) {

                Gson gson = new Gson();

                SearchTask taskMessage = gson.fromJson(new FileReader(file), SearchTask.class);

                ServerManager serverManager = ServerManager.getInstance();

                Logger.d(LOG_TAG, taskMessage.toString());
                serverManager.startProcessTask(taskMessage);
            }      else {
                Logger.d(LOG_TAG, filepath + "not found");
            }
        }
    }
}
