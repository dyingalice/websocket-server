package cn.org.alice.demo.mqlistener;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.Data;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.jms.annotation.JmsListener;
import org.springframework.jms.core.JmsMessagingTemplate;
import org.springframework.stereotype.Component;

import java.io.Serializable;
import java.util.Objects;

/**
 * wss-server 发送消息到MQ, 在这里会收到消息, 然后根据业务逻辑做相应的处理
 */
@Slf4j
@Component
public class MessageListener implements Serializable {

    @Autowired
    private JmsMessagingTemplate jmsMessagingTemplate;
    @Autowired
    private RedisTemplate<String, String> redisTemplate;
    @Autowired
    private ObjectMapper om;
    @Value("${alice.wss.redis-primary-prefix}")
    private String primaryPrefix;

    @SneakyThrows
    @JmsListener(destination = "${spring.application.name}")
    public void receiveMessage(String message) {
        log.info("接收到wss-server发来的消息:{}", message);

        MessageData messageData = om.readValue(message, MessageData.class);
        String primary = messageData.getPrimary();
        String business = messageData.getBusiness();

        switch (business) {
            //根据业务类型进行分支
            case "save-user-info": {
                log.info("来源:{}", primary);
                //根据primary获取client的MQ路由键
                String fromServer = redisTemplate.opsForValue().get(primaryPrefix + ":" + primary);
                if (Objects.isNull(fromServer)) {
                    return;
                }
                RemoteMessageData remoteMessageData = new RemoteMessageData();
                remoteMessageData.setPrimary(primary);
                remoteMessageData.setLink("/save.user");

                User user = new User();
                user.setName("alice");
                user.setAge(18);
                remoteMessageData.setData(user);

                //发送消息到wss-server,然后wss-server会发送消息到客户端
                jmsMessagingTemplate.convertAndSend(fromServer, om.writeValueAsString(remoteMessageData));
                break;
            }
            default: {
                log.info("无事发生");
            }
        }
    }

    @Data
    public static class MessageData implements Serializable {

        //客户端union-key
        private String primary;
        //业务,主要用来分支
        private String business;
        //客户端MQ路由键
        private String fromServer;
        //主要的数据
        private Object data;
    }

    @Data
    public static class RemoteMessageData implements Serializable {

        //客户端union-key
        private String primary;
        //业务,主要用来分支
        private String link;
        //主要的数据
        private Object data;
    }

    @Data
    public static class User implements Serializable {

        private String name;

        private Integer age;
    }
}
