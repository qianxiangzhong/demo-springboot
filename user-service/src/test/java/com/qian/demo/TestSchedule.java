package com.qian.demo;

import org.apache.rocketmq.client.consumer.DefaultMQPushConsumer;
import org.apache.rocketmq.client.consumer.listener.ConsumeConcurrentlyContext;
import org.apache.rocketmq.client.consumer.listener.ConsumeConcurrentlyStatus;
import org.apache.rocketmq.client.consumer.listener.MessageListenerConcurrently;
import org.apache.rocketmq.client.exception.MQBrokerException;
import org.apache.rocketmq.client.exception.MQClientException;
import org.apache.rocketmq.client.producer.DefaultMQProducer;
import org.apache.rocketmq.client.producer.SendResult;
import org.apache.rocketmq.common.consumer.ConsumeFromWhere;
import org.apache.rocketmq.common.message.Message;
import org.apache.rocketmq.common.message.MessageExt;
import org.apache.rocketmq.common.protocol.heartbeat.MessageModel;
import org.apache.rocketmq.remoting.exception.RemotingException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Date;
import java.util.List;

public class TestSchedule {
    static Logger logger = LoggerFactory.getLogger(TestSchedule.class);
    public static void main(String[] args) throws MQClientException, RemotingException, InterruptedException, MQBrokerException {
        long n = 5;
        String grpName = "BROADCAST_PRODUCER_GROUP" + n;
        DefaultMQProducer producer = new DefaultMQProducer(grpName);
        producer.setNamesrvAddr("127.0.0.1:9876");
        producer.start();
        for (int i = 0; i < 5; i++){
            Message msg = new Message("TOPIC_BROADCAST" + n,
                    "TAG_BROADCAST" + n,
                    "KEYS_BROADCAST" + n,
                    ("Hello world! BROADCAST MODEL" + i).getBytes());
            msg.setDelayTimeLevel(3);
            SendResult sendResult = producer.send(msg);
            logger.info("now:{},sendResult:{}", new Date(), sendResult);
        }
        producer.shutdown();
    }
}

class MyConsumer {
    static Logger logger = LoggerFactory.getLogger(MyConsumer.class);
    public static void main(String[] args) throws MQClientException {
        long n = 5;
        String grpName = "BROADCAST_CONSUME_GROUP" + n;
        DefaultMQPushConsumer consumer = new DefaultMQPushConsumer(grpName);
        consumer.setNamesrvAddr("127.0.0.1:9876");
        consumer.setConsumeFromWhere(ConsumeFromWhere.CONSUME_FROM_FIRST_OFFSET);
        consumer.setMessageModel(MessageModel.BROADCASTING);
        consumer.subscribe("TOPIC_BROADCAST" + n, "TAG_BROADCAST" + n);
        consumer.registerMessageListener(new MessageListenerConcurrently() {
            @Override
            public ConsumeConcurrentlyStatus consumeMessage(List<MessageExt> msgs,
                                                            ConsumeConcurrentlyContext context) {
                for (MessageExt msg : msgs) {
                    logger.info("now:{}, Receive New Messages:{}", new Date(), new String(msg.getBody()));
                }
                return ConsumeConcurrentlyStatus.CONSUME_SUCCESS;
            }
        });
        consumer.start();
    }
}
