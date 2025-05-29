package cn.org.alice.socket.core.event;

import cn.org.alice.socket.core.beans.RemoteMessageData;
import lombok.Getter;
import org.springframework.context.ApplicationEvent;

@Getter
public class OnRemoteMessageEvent extends ApplicationEvent {

    private final RemoteMessageData remoteMessageData;

    public OnRemoteMessageEvent(Object source, RemoteMessageData remoteMessageData) {
        super(source);
        this.remoteMessageData = remoteMessageData;
    }
}
