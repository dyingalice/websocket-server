package cn.org.alice.socket.core.event;

import jakarta.websocket.Session;
import lombok.Getter;
import org.springframework.context.ApplicationEvent;


@Getter
public class OnMessageEvent extends ApplicationEvent {

    private final String message;

    private final String primary;

    private final Session session;

    public OnMessageEvent(Object source, String primary, Session session, String message) {
        super(source);
        this.message = message;
        this.primary = primary;
        this.session = session;
    }
}
