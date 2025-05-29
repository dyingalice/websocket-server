package cn.org.alice.socket.core.sender;

import cn.org.alice.socket.core.beans.MessageData;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.Data;
import lombok.SneakyThrows;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.jms.core.JmsMessagingTemplate;

import java.io.Serializable;

public class MessageSender {

    @Autowired
    private JmsMessagingTemplate jmsMessagingTemplate;
    @Autowired
    private ObjectMapper om;
    @Value("${alice.wss.client-prefix}-${spring.application.uuid}")
    private String localServer;

    //发送消息
    @SneakyThrows
    public void convertAndSend(String primary, String queue, String business, Object data) {
        MessageData messageData = new MessageData();
        messageData.setPrimary(primary);
        messageData.setBusiness(business);
        messageData.setFromServer(localServer);
        messageData.setData(data);
        //设递消息
        jmsMessagingTemplate.convertAndSend(queue, om.writeValueAsString(messageData));
    }

}
