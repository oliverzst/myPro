
package com.actec.bsms.webscoket;

import com.actec.bsms.utils.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.websocket.Session;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

/**
 * Created by wdl on 14-10-8.
 * Altered by yc on 16-04-20
 */
public abstract class BaseWebsocket {
    public static Logger logger = LoggerFactory.getLogger(BaseWebsocket.class);

    protected static Long getSleepDuration() {
        return 1L;
    }

    private String requestMsg = "";

    /*map键值对，key设置成String，有时候切换页面，上一页面webscoket线程关不掉*/
    private static Map<Session, WebSocketRunnable> map = new HashMap<>();

    protected abstract String getMessage(String messageFromClient, Session session);

    private ScheduledExecutorService threadPool = Executors.newSingleThreadScheduledExecutor();

    public synchronized void start(Session session) {
        logger.debug("A new client is connected, sessionId is : " + session.getId());
    }

    public synchronized void close(Session session) {
        logger.debug("Session id " + session.getId() + " was closed.");
        if (map.containsKey(session) && map.get(session) != null) {
            map.get(session).flag = false;
        }
        try {
            session.close();
        } catch (IOException e) {
            logger.error(e.toString(), e);
        }
        map.remove(session);
    }

    public void incoming(Session session, String requestMsg) {
        this.requestMsg = requestMsg;
        WebSocketRunnable webSocketRunnable = new WebSocketRunnable(session);
        threadPool.schedule(webSocketRunnable, 0, TimeUnit.SECONDS);
        map.put(session, webSocketRunnable);
    }

    public void onError(Session currentSession, Throwable t) throws Throwable {
        logger.debug("WebSocket Error: " + t.toString(), t);
    }

    class WebSocketRunnable implements Runnable {
        private Session session;
        private boolean flag = true;
        private String responseMsg = "";
        private String prevRespMsg = "";

        WebSocketRunnable(Session session) {
            this.session = session;
        }

        @Override
        public void run() {
            if (flag) {
                logger.debug("current Session------>>>>>>>" + session.getId() + "\n");
                logger.debug("all session size------>>>>>>>" + map.size() + "\n");
                pushMessage();
                threadPool.schedule(this, getSleepDuration(), TimeUnit.SECONDS);
            }
        }

        private void pushMessage() {
            try {
                if (session.isOpen() && StringUtils.isNotEmpty(requestMsg)) {
//                    String userId = session.getUserPrincipal().toString();
//                    User currentUser = UserUtils.get(Integer.getInteger(userId));
                    responseMsg = getMessage(requestMsg, session);
                    if (!prevRespMsg.equals(responseMsg)) {
                        session.getBasicRemote().sendText(responseMsg);
                        prevRespMsg = responseMsg;
                    }
                }
            } catch (IOException e) {
                logger.error(e.toString(), e);
                try {
                    session.close();
                } catch (IOException e1) {
                    logger.error(e.toString(), e);
                }
            }
        }

    }

}
