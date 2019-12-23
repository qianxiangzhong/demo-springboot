package com.qian.demo.rocketmq.consumer;

import com.qian.demo.rocketmq.message.MessageListener;
import com.qian.demo.rocketmq.message.MessageProcessor;
import org.apache.rocketmq.client.consumer.DefaultMQPushConsumer;
import org.apache.rocketmq.client.exception.MQClientException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;

/**
 * @author qxz
 */
@Component
public class RocketMqConsumer {
    public static final Logger logger = LoggerFactory.getLogger(RocketMqConsumer.class);

    @Autowired
    private MessageProcessor messageProcessor;

    @Value("${rocketmq.consumer.namesrvAddr}")
    private String namesrvAddr;
    @Value("${rocketmq.consumer.groupName}")
    private String groupName;
    @Value("${rocketmq.consumer.topic}")
    private String topic;
    @Value("${rocketmq.consumer.tag}")
    private String tag;
    @Value("${rocketmq.consumer.consumeThreadMin}")
    private int consumeThreadMin;
    @Value("${rocketmq.consumer.consumeThreadMax}")
    private int consumeThreadMax;

    @Bean
    public DefaultMQPushConsumer getRocketMqCunsumer(){
        DefaultMQPushConsumer consumer = new DefaultMQPushConsumer(groupName);
        consumer.setNamesrvAddr(namesrvAddr);
        consumer.setConsumeThreadMin(consumeThreadMin);
        consumer.setConsumeThreadMax(consumeThreadMax);
        consumer.setVipChannelEnabled(false);
        // 自己实现的监听器类
        MessageListener messageListener = new MessageListener();
        messageListener.setMessageProcessor(messageProcessor);
        consumer.registerMessageListener(messageListener);

        try {
            consumer.subscribe(topic,tag);
            consumer.start();
            logger.info("Consumer has started! groupName:{}, topic:{}",groupName, topic);
        } catch (MQClientException e) {
            logger.error("Consumer started error!!");
            e.printStackTrace();
        }
        return consumer;
    }
}
