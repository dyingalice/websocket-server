package cn.org.alice.socket.core;

import cn.org.alice.bean.Pair;
import cn.org.alice.socket.core.consumer.AdapterErrorContextConsumer;
import cn.org.alice.socket.core.consumer.AdapterSocketContextConsumer;
import cn.org.alice.socket.core.event.OnCloseEvent;
import cn.org.alice.socket.core.event.OnMessageEvent;
import cn.org.alice.socket.core.event.OnOpenEvent;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.websocket.*;
import jakarta.websocket.server.PathParam;
import jakarta.websocket.server.ServerEndpoint;
import lombok.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.util.StringUtils;

import java.io.IOException;
import java.io.Serializable;
import java.util.Objects;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.Consumer;

@Slf4j
@Getter
@ServerEndpoint(value = "/socket/{primary}")
public class WebSocket implements Consumer<String> {

    public static ConcurrentHashMap<Object, WebSocket> mapping = new ConcurrentHashMap<>();

    private static AdapterSocketContextConsumer adapterSocketContext;

    private static AdapterErrorContextConsumer adapterErrorContext;

    private static ApplicationContext applicationContext;

    private static ObjectMapper om;

    private Session session;

    @Autowired
    public void setApplicationContext(ApplicationContext applicationContext) {
        WebSocket.applicationContext = applicationContext;
    }

    @Autowired
    public void setAdapterSocketContext(AdapterSocketContextConsumer adapterSocketContext) {
        WebSocket.adapterSocketContext = adapterSocketContext;
    }

    @Autowired
    public void setAdapterErrorContext(AdapterErrorContextConsumer adapterErrorContext) {
        WebSocket.adapterErrorContext = adapterErrorContext;
    }

    @Autowired
    public void setObjectJsonConvert(ObjectMapper om) {
        WebSocket.om = om;
    }

    /**
     * 连接建立成功调用的方法
     */
    @OnOpen
    public void onOpen(Session session, @PathParam("primary") String primary) {
        this.session = session;
        mapping.put(primary, this);
        applicationContext.publishEvent(new OnOpenEvent(this, primary, session));
    }

    /**
     * 连接关闭调用的方法
     */
    @OnClose
    public void onClose(Session session, @PathParam("primary") String primary) {
        mapping.remove(primary);
        applicationContext.publishEvent(new OnCloseEvent(this, primary, session));
    }

    /**
     * 收到客户端消息后调用的方法
     *
     * @param message 客户端发送过来的消息
     */
    @OnMessage
    public void onMessage(String message, Session session, @PathParam("primary") String primary) {
        try {
            RequestBody deserialize = om.readValue(message, RequestBody.class);
            String link = deserialize.getLink();
            Object data = deserialize.getData();
            String json = om.writeValueAsString(data);
            adapterSocketContext.accept(pathComparison(link), new Pair<>(primary, json));
        } catch (Exception error) {
            onError(session, error, primary);
        }
        applicationContext.publishEvent(new OnMessageEvent(this, primary, session, message));
    }

    private String pathComparison(String path) {
        // 去掉路径前的所有 '/' 字符
        String newPath = StringUtils.trimLeadingCharacter(path, '/');
        // 在路径前添加一个 '/' 字符
        newPath = "/" + newPath;
        // 替换路径中的所有 "//" 为 "/"
        newPath = newPath.replace("//", "/");
        // 去掉路径末尾的 '/' 字符（如果长度大于1）
        if (newPath.length() > 1 && newPath.endsWith("/")) {
            newPath = newPath.substring(0, newPath.length() - 1);
        }
        return newPath;
    }

    @OnError
    public void onError(Session session, Throwable error, @PathParam("primary") String primary) {
        log.error("error:", error);
        adapterErrorContext.accept(exception(error), new Pair<>(primary, error));
    }

    @Override
    public void accept(String message) {
        try {
            this.session.getBasicRemote().sendText(message);
        } catch (IOException ex) {
            log.error("", ex);
        }
    }

    private Class<? extends Throwable> exception(Throwable error) {
        Throwable cause = error.getCause();
        if (Objects.isNull(cause)) {
            return error.getClass();
        }
        return exception(cause);
    }

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class RequestBody implements Serializable {

        private String link;

        private Object data;
    }
}
