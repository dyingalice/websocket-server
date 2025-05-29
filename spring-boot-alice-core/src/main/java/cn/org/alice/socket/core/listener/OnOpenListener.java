package cn.org.alice.socket.core.listener;

import cn.org.alice.socket.core.event.OnOpenEvent;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.ApplicationListener;
import org.springframework.data.redis.core.RedisTemplate;

public class OnOpenListener implements ApplicationListener<OnOpenEvent> {

    @Autowired
    private RedisTemplate<String, String> redisTemplate;
    @Value("${alice.wss.redis-primary-prefix}")
    private String primaryPrefix;
    @Value("${alice.wss.client-prefix}-${spring.application.uuid}")
    private String localServer;

    @Override
    public void onApplicationEvent(OnOpenEvent event) {
        String primary = event.getPrimary();

        redisTemplate.opsForValue().set(primaryPrefix + ":" + primary, localServer);
    }
}
