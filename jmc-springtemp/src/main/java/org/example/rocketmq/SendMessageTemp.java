package org.example.rocketmq;

import org.apache.rocketmq.spring.core.RocketMQTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class SendMessageTemp {
    @Autowired
    private RocketMQTemplate rocketMQTemplate;

    public void sendMessage(String message) {
        rocketMQTemplate.convertAndSend("test-topic", message);
    }
}
