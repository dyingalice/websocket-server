package cn.org.alice.socket.core.anno;

import cn.org.alice.socket.core.consumer.AdapterErrorContextConsumer;
import cn.org.alice.socket.core.consumer.AdapterRemoteMessageContextConsumer;
import cn.org.alice.socket.core.consumer.AdapterSocketContextConsumer;
import cn.org.alice.socket.core.WebSocket;
import cn.org.alice.socket.core.executor.WebSocketExecutor;
import cn.org.alice.socket.core.listener.OnCloseListener;
import cn.org.alice.socket.core.listener.OnMessageListener;
import cn.org.alice.socket.core.listener.OnOpenListener;
import cn.org.alice.socket.core.listener.OnRemoteMessageListener;
import cn.org.alice.socket.core.receiver.MessageReceiver;
import cn.org.alice.socket.core.sender.MessageSender;
import org.springframework.context.annotation.Import;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target({ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Import({
        WebSocket.class,
        WebSocketExecutor.class,
        MessageReceiver.class,
        MessageSender.class,
        OnOpenListener.class,
        OnMessageListener.class,
        OnCloseListener.class,
        OnRemoteMessageListener.class,
        AdapterSocketContextConsumer.class,
        AdapterErrorContextConsumer.class,
        AdapterRemoteMessageContextConsumer.class
})
public @interface EnableSocket {
}
