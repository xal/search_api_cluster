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
package com.jff.searchapicluster.worker.mina;

import com.jff.searchapicluster.core.api.entity.json.task.SearchTask;
import com.jff.searchapicluster.core.mina.message.TaskMessage;
import com.jff.searchapicluster.worker.main.WorkerManager;
import org.apache.mina.core.service.IoHandlerAdapter;
import org.apache.mina.core.session.IoSession;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class WorkerSessionHandler extends IoHandlerAdapter {

    private final static Logger LOGGER = LoggerFactory.getLogger(WorkerSessionHandler.class);


    private WorkerManager workerManager = WorkerManager.getInstance();

    public WorkerSessionHandler() {

    }



    @Override
    public void sessionOpened(IoSession session) {

    }

    @Override
    public void messageReceived(IoSession session, Object message) {

        TaskMessage searchTask = (TaskMessage) message;

        workerManager.search(searchTask.getRequestId(), searchTask.getRequestNumber(), searchTask.getSearchTask());

    }

    @Override
    public void exceptionCaught(IoSession session, Throwable cause) {
        session.close(true);
    }

    @Override
    public void sessionClosed(IoSession session) throws Exception {
        super.sessionClosed(session);
        workerManager.connect();
    }
}