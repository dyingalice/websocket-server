package cn.org.alice.socket.core.listener;

import cn.org.alice.bean.Pair;
import cn.org.alice.socket.core.beans.RemoteMessageData;
import cn.org.alice.socket.core.consumer.AdapterRemoteMessageContextConsumer;
import cn.org.alice.socket.core.event.OnRemoteMessageEvent;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.SneakyThrows;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationListener;

public class OnRemoteMessageListener implements ApplicationListener<OnRemoteMessageEvent> {

    @Autowired
    private AdapterRemoteMessageContextConsumer adapterRemoteMessageContextConsumer;
    @Autowired
    private ObjectMapper om;

    @SneakyThrows
    @Override
    public void onApplicationEvent(OnRemoteMessageEvent event) {
        RemoteMessageData data = event.getRemoteMessageData();
        adapterRemoteMessageContextConsumer.accept(data.getLink(), new Pair<>(data.getPrimary(), om.writeValueAsString(data.getData())));
    }
}
