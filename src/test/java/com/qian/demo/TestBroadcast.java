package com.qian.demo;

import org.apache.rocketmq.client.consumer.DefaultMQPushConsumer;
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

public class TestBroadcast {
    public static void main(String[] args) throws MQClientException, RemotingException, InterruptedException, MQBrokerException {
        long n = 4;
        String grpName = "BROADCAST_PRODUCER_GROUP" + n;
        DefaultMQProducer producer = new DefaultMQProducer(grpName);
        producer.setNamesrvAddr("127.0.0.1:9876");
        producer.start();
        for (int i = 0; i < 10; i++){
            Message msg = new Message("TOPIC_BROADCAST" + n,
                    "TAG_BROADCAST" + n,
                    "KEYS_BROADCAST" + n,
                    ("Hello world! BROADCAST MODEL" + i).getBytes());
            SendResult sendResult = producer.send(msg);
            System.out.println("sendResult:" + sendResult);
        }
        producer.shutdown();
    }
}

class MyConsumer1 {
    public static void main(String[] args) throws MQClientException {
        long n = 4;
        String grpName = "BROADCAST_CONSUME_GROUP" + n;
        DefaultMQPushConsumer consumer = new DefaultMQPushConsumer(grpName);
        consumer.setNamesrvAddr("127.0.0.1:9876");
        consumer.setConsumeFromWhere(ConsumeFromWhere.CONSUME_FROM_FIRST_OFFSET);
        consumer.setMessageModel(MessageModel.BROADCASTING);
        consumer.subscribe("TOPIC_BROADCAST" + n, "TAG_BROADCAST" + n);
        consumer.registerMessageListener((MessageListenerConcurrently) (msgs, context) -> {
            for (MessageExt msg : msgs) {
                System.out.println(" Receive New Messages: " + new String(msg.getBody()));
            }
            return ConsumeConcurrentlyStatus.CONSUME_SUCCESS;
        });
        consumer.start();
    }
}

class MyConsumer2 {
    public static void main(String[] args) throws MQClientException {
        long n = 4;
        String grpName = "BROADCAST_CONSUME_GROUP" + n;
        DefaultMQPushConsumer consumer = new DefaultMQPushConsumer(grpName);
        consumer.setNamesrvAddr("127.0.0.1:9876");
        consumer.setConsumeFromWhere(ConsumeFromWhere.CONSUME_FROM_FIRST_OFFSET);
        consumer.setMessageModel(MessageModel.BROADCASTING);
        consumer.subscribe("TOPIC_BROADCAST" + n, "TAG_BROADCAST" + n);
        consumer.registerMessageListener((MessageListenerConcurrently) (msgs, context) -> {
            for (MessageExt msg : msgs) {
                System.out.println(" Receive New Messages: " + new String(msg.getBody()));
            }
            return ConsumeConcurrentlyStatus.CONSUME_SUCCESS;
        });
        consumer.start();
    }
}