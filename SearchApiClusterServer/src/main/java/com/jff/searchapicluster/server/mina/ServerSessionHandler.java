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

import com.jff.searchapicluster.core.mina.message.ResponseMessage;
import com.jff.searchapicluster.server.ServerManager;
import org.apache.mina.core.service.IoHandlerAdapter;
import org.apache.mina.core.session.IoSession;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ServerSessionHandler extends IoHandlerAdapter {
    
    private static final String SUM_KEY = "sum";

    private final static Logger LOGGER = LoggerFactory.getLogger(ServerSessionHandler.class);


    private ServerManager serverManager = ServerManager.getInstance();
    @Override
    public void sessionOpened(IoSession session) {

           serverManager.addSession(session);

    }

    @Override
    public void messageReceived(IoSession session, Object message) {
        serverManager.handleResult(session, (ResponseMessage) message);
    }

    @Override
    public void sessionClosed(IoSession session) throws Exception {
        super.sessionClosed(session);
        serverManager.removeSessions(session);
    }

    @Override
    public void exceptionCaught(IoSession session, Throwable cause) {
        // close the connection on exceptional situation
        session.close(true);
    }
}