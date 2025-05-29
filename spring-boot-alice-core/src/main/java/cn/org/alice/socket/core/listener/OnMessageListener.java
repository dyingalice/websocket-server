package cn.org.alice.socket.core.listener;

import cn.org.alice.socket.core.event.OnMessageEvent;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.ApplicationListener;
import org.springframework.data.redis.core.RedisTemplate;

/**
 * 监听onMessage事件, 保存 primary, server 信息到redis
 */
public class OnMessageListener implements ApplicationListener<OnMessageEvent> {

    @Autowired
    private RedisTemplate<String, String> redisTemplate;
    @Value("${alice.wss.redis-primary-prefix}")
    private String primaryPrefix;
    @Value("${alice.wss.client-prefix}-${spring.application.uuid}")
    private String localServer;

    @Override
    public void onApplicationEvent(OnMessageEvent event) {
        String primary = event.getPrimary();

        redisTemplate.opsForValue().set(primaryPrefix + ":" + primary, localServer);
    }
}
