package cn.org.alice.demo.service;


import cn.org.alice.demo.bean.Body;
import cn.org.alice.socket.core.anno.WssPrimary;
import cn.org.alice.socket.core.anno.WssRequestBody;
import cn.org.alice.socket.core.anno.WebSocketRegister;
import cn.org.alice.socket.core.anno.WssRequestMapping;

@WebSocketRegister
public interface ReplyService {

    @WssRequestMapping(value = {"reply"})
    void reply(@WssRequestBody Body body, @WssPrimary String user1);

    @WssRequestMapping(value = {"reply2"})
    void reply2(@WssRequestBody Body body, @WssPrimary String user2);
}
