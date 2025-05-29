package cn.org.alice.socket.core.event;

import jakarta.websocket.Session;
import lombok.Getter;
import org.springframework.context.ApplicationEvent;


@Getter
public class OnOpenEvent extends ApplicationEvent {

    private final String primary;

    private final Session session;

    public OnOpenEvent(Object source, String primary, Session session) {
        super(source);
        this.primary = primary;
        this.session = session;
    }
}
