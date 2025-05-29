package cn.org.alice.demo.listener;

import cn.org.alice.demo.bean.Body;
import cn.org.alice.demo.service.ReplyService;
import cn.org.alice.socket.core.beans.RemoteMessageData;
import cn.org.alice.socket.core.event.OnRemoteMessageEvent;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationListener;
import org.springframework.stereotype.Component;

@Component
public class OnRemoteMessageListener implements ApplicationListener<OnRemoteMessageEvent> {

    @Autowired
    private ReplyService replyService;

    @Override
    public void onApplicationEvent(OnRemoteMessageEvent event) {
        RemoteMessageData remoteMessageData = event.getRemoteMessageData();
        Object data = remoteMessageData.getData();
        String link = remoteMessageData.getLink();
        String primary = remoteMessageData.getPrimary();
        replyService.reply(Body.builder().message("接收到main-server的消息了").build(), primary);
    }
}
