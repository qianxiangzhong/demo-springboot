package com.qian.demo;

import org.apache.rocketmq.client.consumer.DefaultMQPushConsumer;
import org.apache.rocketmq.client.consumer.listener.*;
import org.apache.rocketmq.client.exception.MQBrokerException;
import org.apache.rocketmq.client.exception.MQClientException;
import org.apache.rocketmq.client.producer.DefaultMQProducer;
import org.apache.rocketmq.client.producer.MessageQueueSelector;
import org.apache.rocketmq.client.producer.SendCallback;
import org.apache.rocketmq.client.producer.SendResult;
import org.apache.rocketmq.common.consumer.ConsumeFromWhere;
import org.apache.rocketmq.common.message.Message;
import org.apache.rocketmq.common.message.MessageExt;
import org.apache.rocketmq.common.message.MessageQueue;
import org.apache.rocketmq.common.protocol.heartbeat.MessageModel;
import org.apache.rocketmq.remoting.exception.RemotingException;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicLong;

@SpringBootTest
class ProducerTest {

    @Value("${rocketmq.consumer.namesrvAddr}")
    private String namesrvAddr;
    @Value("${rocketmq.consumer.groupName")
    private String groupName;
    @Value("${rocketmq.consumer.topic}")
    private String topic;
    @Value("${rocketmq.consumer.tag}")
    private String tag;
    @Value("${rocketmq.consumer.consumeThreadMin}")
    private int consumeThreadMin;
    @Value("${rocketmq.consumer.consumeThreadMax}")
    private int consumeThreadMax;

    public static final Logger logger = LoggerFactory.getLogger(ProducerTest.class);
    @Test
    void asyncSend() throws MQClientException, RemotingException, InterruptedException {
        String grpName = "TEST_GROUP" + System.currentTimeMillis();
        DefaultMQProducer producer = new DefaultMQProducer(grpName);
        producer.setNamesrvAddr(namesrvAddr);
        producer.start();
        producer.setRetryTimesWhenSendAsyncFailed(0);
        String mqBody = "Hello RocketMQ:";
        Message message = new Message(topic, tag, mqBody.getBytes());
        producer.send(message, new SendCallback() {
            @Override
            public void onSuccess(SendResult sendResult) {
                logger.info("asyncSend success,msgId:{}", sendResult.getMsgId());
            }

            @Override
            public void onException(Throwable throwable) {
                logger.error("Send error!");
                throwable.printStackTrace();
            }
        });
        // 因为是异步模式，如果shutdown的话可能会导致发送失败
//        producer.shutdown();
//        logger.info("Producer shutdown!");

        DefaultMQPushConsumer consumer = new DefaultMQPushConsumer(grpName);
        consumer.setNamesrvAddr(namesrvAddr);
        consumer.setConsumeThreadMin(consumeThreadMin);
        consumer.setConsumeThreadMax(consumeThreadMax);
        consumer.setVipChannelEnabled(false);
        // 自己实现的监听器类
        consumer.registerMessageListener(new MessageListenerConcurrently() {
            @Override
            public ConsumeConcurrentlyStatus consumeMessage(List<MessageExt> list, ConsumeConcurrentlyContext consumeConcurrentlyContext) {
                try {
                    for (MessageExt messageExt: list) {
                        String msgBody = new String(messageExt.getBody());
                        logger.info("收到消息：topic:{},tags:{},msgId:{},body:{},",messageExt.getTopic(),messageExt.getTags(),messageExt.getMsgId(),msgBody);
                    }
                    return ConsumeConcurrentlyStatus.CONSUME_SUCCESS;
                } catch (Exception e) {
                    e.printStackTrace();
                    return ConsumeConcurrentlyStatus.RECONSUME_LATER;
                }
            }
        });
        consumer.start();
        consumer.subscribe(topic, tag);
        logger.info("Consumer has started! groupName:{}, topic:{}", grpName, topic);
    }

    @Test
    public void syncSend() throws MQClientException, RemotingException, InterruptedException, MQBrokerException {
        String grpName = "TEST_GROUP" + System.currentTimeMillis();
        DefaultMQProducer producer = new DefaultMQProducer(grpName);
        producer.setNamesrvAddr(namesrvAddr);
        producer.start();
        producer.setRetryTimesWhenSendAsyncFailed(0);
        String mqBody = "Hello RocketMQ:";
        Message message = new Message(topic, tag, mqBody.getBytes());
        SendResult result = producer.send(message);
        logger.info("syncSend Sendstatus:{}", result.getSendStatus());
        producer.shutdown();
        logger.info("Producer shutdown!");

        DefaultMQPushConsumer consumer = new DefaultMQPushConsumer(grpName);
        consumer.setNamesrvAddr(namesrvAddr);
        consumer.setConsumeThreadMin(consumeThreadMin);
        consumer.setConsumeThreadMax(consumeThreadMax);
        consumer.setVipChannelEnabled(false);
        // 自己实现的监听器类
        consumer.registerMessageListener(new MessageListenerConcurrently() {
            @Override
            public ConsumeConcurrentlyStatus consumeMessage(List<MessageExt> list, ConsumeConcurrentlyContext consumeConcurrentlyContext) {
                try {
                    for (MessageExt messageExt: list) {
                        String msgBody = new String(messageExt.getBody());
                        logger.info("收到消息：topic:{},tags:{},msgId:{},body:{},",messageExt.getTopic(),messageExt.getTags(),messageExt.getMsgId(),msgBody);
                    }
                    return ConsumeConcurrentlyStatus.CONSUME_SUCCESS;
                } catch (Exception e) {
                    e.printStackTrace();
                    return ConsumeConcurrentlyStatus.RECONSUME_LATER;
                }
            }
        });
        consumer.start();
        consumer.subscribe(topic, tag);
        logger.info("Consumer has started! groupName:{}, topic:{}", grpName, topic);
    }

    @Test
    public void oneWaySend() throws RemotingException, MQClientException, InterruptedException {
        String grpName = "TEST_GROUP" + System.currentTimeMillis();
        DefaultMQProducer producer = new DefaultMQProducer(grpName);
        producer.setNamesrvAddr(namesrvAddr);
        producer.start();
        producer.setRetryTimesWhenSendAsyncFailed(0);
        String mqBody = "Hello RocketMQ:";
        Message message = new Message(topic, tag, mqBody.getBytes());
        producer.sendOneway(message);
        producer.shutdown();
        logger.info("Producer shutdown!");

        DefaultMQPushConsumer consumer = new DefaultMQPushConsumer(grpName);
        consumer.setNamesrvAddr(namesrvAddr);
        consumer.setConsumeThreadMin(consumeThreadMin);
        consumer.setConsumeThreadMax(consumeThreadMax);
        consumer.setVipChannelEnabled(false);
        // 自己实现的监听器类
        consumer.registerMessageListener((MessageListenerConcurrently) (list, consumeConcurrentlyContext) -> {
            try {
                for (MessageExt messageExt: list) {
                    String msgBody = new String(messageExt.getBody());
                }
                logger.info("收到消息：list:{},",list);
                return ConsumeConcurrentlyStatus.CONSUME_SUCCESS;
            } catch (Exception e) {
                e.printStackTrace();
                return ConsumeConcurrentlyStatus.RECONSUME_LATER;
            }
        });
        consumer.start();
        consumer.subscribe(topic, tag);
        logger.info("Consumer has started! groupName:{}, topic:{}", grpName, topic);
    }

    @Test
    public void orderSend() throws MQClientException, RemotingException, InterruptedException, MQBrokerException {
        String grpName = "TEST_GROUP_20191105";
        DefaultMQProducer producer = new DefaultMQProducer(grpName);
        producer.setNamesrvAddr(namesrvAddr);
        producer.start();
        producer.setRetryTimesWhenSendAsyncFailed(0);
        String mqBody = "Hello RocketMQ:";
        String[] tags = new String[] {"TagA", "TagB", "TagC", "TagD", "TagE"};
        for (int i = 0; i < 100; i++) {
            int orderId = i % 10;
            //Create a message instance, specifying topic, tag and message body.
            Message message = new Message(topic, tags[i % tags.length], mqBody.getBytes());
            SendResult sendResult = producer.send(message, new MessageQueueSelector() {
                @Override
                public MessageQueue select(List<MessageQueue> mqs, Message msg, Object arg) {
                    Integer id = (Integer) arg;
                    int index = id % mqs.size();
                    return mqs.get(index);
                }
            }, orderId);
            logger.info("sendResult:{}", sendResult);
        }
        //server shutdown
        producer.shutdown();

        DefaultMQPushConsumer consumer = new DefaultMQPushConsumer(grpName);
        consumer.setNamesrvAddr(namesrvAddr);
        consumer.setConsumeThreadMin(consumeThreadMin);
        consumer.setConsumeThreadMax(consumeThreadMax);
        consumer.setVipChannelEnabled(false);
        // 自己实现的监听器类
        consumer.registerMessageListener(new MessageListenerOrderly() {

            AtomicLong consumeTimes = new AtomicLong(0);
            @Override
            public ConsumeOrderlyStatus consumeMessage(List<MessageExt> msgs,
                                                       ConsumeOrderlyContext context) {
                context.setAutoCommit(false);
                logger.info(Thread.currentThread().getName() + " Receive New Messages: " + msgs + "%n");
                this.consumeTimes.incrementAndGet();
                if ((this.consumeTimes.get() % 2) == 0) {
                    return ConsumeOrderlyStatus.SUCCESS;
                } else if ((this.consumeTimes.get() % 3) == 0) {
                    return ConsumeOrderlyStatus.ROLLBACK;
                } else if ((this.consumeTimes.get() % 4) == 0) {
                    return ConsumeOrderlyStatus.COMMIT;
                } else if ((this.consumeTimes.get() % 5) == 0) {
                    context.setSuspendCurrentQueueTimeMillis(3000);
                    return ConsumeOrderlyStatus.SUSPEND_CURRENT_QUEUE_A_MOMENT;
                }
                return ConsumeOrderlyStatus.SUCCESS;

            }
        });
        consumer.start();
        consumer.subscribe(topic, "TagA || TagC || TagD");
        System.out.printf("Consumer Started.%n");
        Map<String, String> subecriptions = consumer.getSubscription();
        logger.info("subscriptions:\n{}", subecriptions);
    }

    @Test
    public void broadcastSend() throws MQClientException, RemotingException, InterruptedException, MQBrokerException {
        String grpName = "BROADCAST_GROUP_1442";
        DefaultMQProducer producer = new DefaultMQProducer(grpName);
        producer.setNamesrvAddr(namesrvAddr);
        producer.start();
        for (int i = 0; i < 10; i++){
            Message msg = new Message("Topic_broadcast",
                    "Tag" + i,
                    "OrderID188",
                    "Hello world".getBytes());
            SendResult sendResult = producer.send(msg);
            System.out.printf("%s%n", sendResult);
        }
        producer.shutdown();

        DefaultMQPushConsumer consumer = new DefaultMQPushConsumer(grpName);
        consumer.setNamesrvAddr(namesrvAddr);
        consumer.setConsumeFromWhere(ConsumeFromWhere.CONSUME_FROM_FIRST_OFFSET);
        consumer.setMessageModel(MessageModel.BROADCASTING);
        consumer.subscribe("Topic_broadcast", "Tag1 || Tag2 || Tag3");
        consumer.registerMessageListener(new MessageListenerConcurrently() {
            @Override
            public ConsumeConcurrentlyStatus consumeMessage(List<MessageExt> msgs,
                                                            ConsumeConcurrentlyContext context) {
                System.out.printf(Thread.currentThread().getName() + " Receive New Messages: " + msgs + "%n");
                return ConsumeConcurrentlyStatus.CONSUME_SUCCESS;
            }
        });
        consumer.start();
        System.out.printf("Broadcast Consumer Started.%n");

        DefaultMQPushConsumer consumer2 = new DefaultMQPushConsumer(grpName);
        consumer2.setNamesrvAddr(namesrvAddr);
        consumer2.setConsumeFromWhere(ConsumeFromWhere.CONSUME_FROM_FIRST_OFFSET);
        consumer2.setMessageModel(MessageModel.BROADCASTING);
        consumer2.subscribe("Topic_broadcast", "Tag1 || Tag2 || Tag3");
        consumer2.registerMessageListener(new MessageListenerConcurrently() {
            @Override
            public ConsumeConcurrentlyStatus consumeMessage(List<MessageExt> msgs,
                                                            ConsumeConcurrentlyContext context) {
                System.out.printf(Thread.currentThread().getName() + " Receive New Messages: " + msgs + "%n");
                return ConsumeConcurrentlyStatus.CONSUME_SUCCESS;
            }
        });
        consumer2.start();
        System.out.printf("Broadcast Consumer2 Started.%n");
    }
}
