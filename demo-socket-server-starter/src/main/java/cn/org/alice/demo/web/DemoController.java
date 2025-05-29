package cn.org.alice.demo.web;

import cn.org.alice.demo.bean.User;
import cn.org.alice.socket.core.anno.WssClientController;
import cn.org.alice.socket.core.anno.WssPrimary;
import cn.org.alice.socket.core.anno.WssRequestBody;
import cn.org.alice.socket.core.anno.WssRequestMapping;
import cn.org.alice.socket.core.sender.MessageSender;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;


@Slf4j
@WssClientController
@WssRequestMapping(value = "/test")
public class DemoController {

    @Autowired
    private MessageSender messageSender;

    @WssRequestMapping(value = "/save")
    public void save(@WssRequestBody User user, @WssPrimary String id) {
        log.info("客户端：id {}, 发来消息:{} ", id, user);
        // 发送消息给主服务器, wss服务不做业务处理
        messageSender.convertAndSend(id, "main-server", "save-user-info", user);
    }
}
