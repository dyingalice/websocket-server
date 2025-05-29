package cn.org.alice.demo.remote;

import cn.org.alice.demo.bean.User;
import cn.org.alice.demo.service.ReplyService;
import cn.org.alice.socket.core.anno.WssPrimary;
import cn.org.alice.socket.core.anno.WssRemoteController;
import cn.org.alice.socket.core.anno.WssRequestBody;
import cn.org.alice.socket.core.anno.WssRequestMapping;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;

@Slf4j
@WssRemoteController
@WssRequestMapping(value = "/")
public class DemoRemoteMessageController {

    @Autowired
    private ReplyService replyService;

    @WssRequestMapping(value = "/test")
    public void test(@WssRequestBody User user, @WssPrimary String id) {
        throw new NullPointerException("空指针异常");
    }

    @WssRequestMapping(value = "/save.user")
    public void saveUser(@WssRequestBody User user, @WssPrimary String id) {
        log.info("收到到main-server的消息了,当前分支为:{}, primary为:{}", "save.user", id);
        log.info("接收到的数据为 :{}", user.getName());
    }
}
