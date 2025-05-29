package cn.org.alice.socket.core.receiver;


import cn.org.alice.socket.core.beans.RemoteMessageData;
import cn.org.alice.socket.core.event.OnRemoteMessageEvent;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.SneakyThrows;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.jms.annotation.JmsListener;

import java.util.function.Consumer;

public class MessageReceiver implements Consumer<String> {

    @Autowired
    private ApplicationContext applicationContext;
    @Autowired
    private ObjectMapper om;

    @SneakyThrows
    @Override
    @JmsListener(destination = "${alice.wss.client-prefix}-${spring.application.uuid}")
    public void accept(String message) {
        RemoteMessageData remoteMessageData = om.readValue(message, RemoteMessageData.class);
        applicationContext.publishEvent(new OnRemoteMessageEvent(this, remoteMessageData));
    }

}
