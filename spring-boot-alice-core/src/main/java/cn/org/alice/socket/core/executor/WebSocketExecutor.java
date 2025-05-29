package cn.org.alice.socket.core.executor;

import cn.org.alice.Executor;
import cn.org.alice.ProxyInterface;
import cn.org.alice.bean.Pair;
import cn.org.alice.socket.core.WebSocket;
import cn.org.alice.socket.core.anno.WssPrimary;
import cn.org.alice.socket.core.anno.WssRequestBody;
import cn.org.alice.socket.core.anno.WebSocketRegister;
import cn.org.alice.socket.core.anno.WssRequestMapping;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.annotation.AnnotationUtils;

import java.io.Serializable;
import java.lang.reflect.Method;
import java.lang.reflect.Parameter;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Stream;

public class WebSocketExecutor implements Executor<WebSocketRegister> {

    @Autowired
    private ObjectMapper om;

    @Override
    public Object invoke(WebSocketRegister annotation, ProxyInterface proxyInterface) throws JsonProcessingException {
        ConcurrentHashMap<Object, WebSocket> mapping = WebSocket.mapping;

        Method method = proxyInterface.getMethod();
        WssRequestMapping wssRequestMapping = AnnotationUtils.findAnnotation(method, WssRequestMapping.class);

        if (Objects.isNull(wssRequestMapping)) {
            return null;
        }
        String link = wssRequestMapping.value()[0];
        String primary = primary(proxyInterface.getPairs());

        //全部发送
        WebSocket webSocket = mapping.get(primary);
        if (Objects.nonNull(webSocket)) {
            webSocket.accept(om.writeValueAsString(WebSocket.RequestBody.builder().link(link).data(message(proxyInterface.getPairs())).build()));
        }
        return null;
    }

    private Object message(List<Pair<Parameter, Object>> pvs) {
        return pvs.stream()
                .filter(e -> Objects.nonNull(e.getKey().getAnnotation(WssRequestBody.class)))
                .findFirst()
                .map(Pair::getValue)
                .orElse(new Object());
    }

    private String primary(List<Pair<Parameter, Object>> pvs) {
        return pvs.stream()
                .filter(e -> Objects.nonNull(e.getKey().getAnnotation(WssPrimary.class)))
                .map(Pair::getValue)
                .flatMap(e -> {
                    if (e instanceof String) {
                        return Stream.of(e);
                    }
                    if (e instanceof Serializable) {
                        return Stream.of(e);
                    }
                    return Stream.of();
                })
                .filter(Objects::nonNull)
                .map(String::valueOf)
                .findFirst()
                .get();
    }
}
