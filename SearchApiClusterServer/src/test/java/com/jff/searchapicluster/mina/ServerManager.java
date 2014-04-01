package com.jff.searchapicluster.mina;

import com.jff.searchapicluster.core.api.entity.json.task.SearchTask;
import org.apache.mina.core.session.IoSession;

import java.net.SocketAddress;
import java.util.*;

/**
 * Created by Yevhenii Zapletin on 01.04.14.
 */
public class ServerManager {


    private Comparator<? super SessionWrapper> comparator = new Comparator<SessionWrapper>() {
        @Override
        public int compare(SessionWrapper sessionWrapper, SessionWrapper sessionWrapper2) {

            return ((Integer) sessionWrapper.countJobs).compareTo(sessionWrapper2.countJobs);


        }
    };

    private class SessionWrapper {
        public IoSession ioSession;
        public int countJobs;

        private SessionWrapper(IoSession ioSession) {
            this.ioSession = ioSession;
        }
    }

    private static ServerManager instance;

    public static ServerManager getInstance() {
        if (instance == null) {
            instance = new ServerManager();
        }
        return instance;
    }

    private Hashtable<SocketAddress, SessionWrapper> sessions = new Hashtable<SocketAddress, SessionWrapper>();


    public void addSession(IoSession ioSession) {
        sessions.put(ioSession.getRemoteAddress(), new SessionWrapper(ioSession));
    }

    public SessionWrapper removeSessions(IoSession ioSession) {
        return sessions.remove(ioSession.getRemoteAddress());
    }

    public void startProcessTask(SearchTask task) {

        synchronized (sessions) {
            Collection<SessionWrapper> values = sessions.values();

            List<SessionWrapper> list = new ArrayList<SessionWrapper>();

            list.addAll(values);

            Collections.sort(list, comparator);

            for(int i = 0; i < task.requests.length; i++) {

            }
        }


    }
}
