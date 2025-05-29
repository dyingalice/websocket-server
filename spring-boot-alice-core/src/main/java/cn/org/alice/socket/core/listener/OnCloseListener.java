package cn.org.alice.socket.core.listener;

import cn.org.alice.socket.core.event.OnCloseEvent;
import cn.org.alice.socket.core.event.OnMessageEvent;
import jakarta.websocket.Session;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.ApplicationListener;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;

public class OnCloseListener implements ApplicationListener<OnCloseEvent> {

    @Autowired
    private RedisTemplate<String, String> redisTemplate;
    @Value("${alice.wss.redis-primary-prefix}")
    private String primaryPrefix;

    @Override
    public void onApplicationEvent(OnCloseEvent event) {
        String primary = event.getPrimary();
        redisTemplate.delete(primaryPrefix + ":" + primary);
    }
}
