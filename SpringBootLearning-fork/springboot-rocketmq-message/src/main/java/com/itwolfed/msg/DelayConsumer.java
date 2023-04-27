package com.itwolfed.msg;

import lombok.extern.slf4j.Slf4j;
import org.apache.rocketmq.spring.annotation.RocketMQMessageListener;
import org.apache.rocketmq.spring.core.RocketMQListener;
import org.springframework.stereotype.Component;

@Component
@Slf4j
@RocketMQMessageListener(topic = "Topic-Delay",consumerGroup = "consumer-delay-group")
public class DelayConsumer implements RocketMQListener<String> {

    @Override
    public void onMessage(String message) {
        log.info("Receive message：{}" , message);
    }

}
