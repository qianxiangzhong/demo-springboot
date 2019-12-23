package com.qian.demo;

import org.apache.rocketmq.client.consumer.DefaultMQPushConsumer;
import org.apache.rocketmq.client.consumer.listener.ConsumeConcurrentlyStatus;
import org.apache.rocketmq.client.consumer.listener.MessageListenerConcurrently;
import org.apache.rocketmq.client.exception.MQClientException;
import org.apache.rocketmq.client.producer.LocalTransactionState;
import org.apache.rocketmq.client.producer.SendResult;
import org.apache.rocketmq.client.producer.TransactionListener;
import org.apache.rocketmq.client.producer.TransactionMQProducer;
import org.apache.rocketmq.common.consumer.ConsumeFromWhere;
import org.apache.rocketmq.common.message.Message;
import org.apache.rocketmq.common.message.MessageExt;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Date;
import java.util.concurrent.*;
import java.util.concurrent.atomic.AtomicInteger;

public class TestTransaction {
    private static Logger logger = LoggerFactory.getLogger(TestTransaction.class);
    public static void main(String[] args) throws MQClientException {
        long n = 5;
        String grpName = "BROADCAST_PRODUCER_GROUP" + n;
        TransactionListener transactionListener = new TransactionListener() {
            private AtomicInteger transactionIndex = new AtomicInteger(0);

            @Override
            public LocalTransactionState executeLocalTransaction(Message message, Object o) {
                int value = transactionIndex.getAndIncrement();
                int status = value % 3;
                switch (status) {
                    case 0:
                        return LocalTransactionState.COMMIT_MESSAGE;
                    case 1:
                        return LocalTransactionState.ROLLBACK_MESSAGE;
                    default:
                        return LocalTransactionState.UNKNOW;
                }
            }

            @Override
            public LocalTransactionState checkLocalTransaction(MessageExt messageExt) {
                logger.info("msgBody:{}", new String(messageExt.getBody()));
                return LocalTransactionState.COMMIT_MESSAGE;
            }
        };

        TransactionMQProducer producer = new TransactionMQProducer(grpName);
        producer.setNamesrvAddr("127.0.0.1:9876");
        ExecutorService executorService = new ThreadPoolExecutor(2, 5, 100, TimeUnit.SECONDS, new ArrayBlockingQueue<Runnable>(2000), new ThreadFactory() {
            @Override
            public Thread newThread(Runnable r) {
                Thread thread = new Thread(r);
                thread.setName("client-transaction-msg-check-thread");
                return thread;
            }
        });
        producer.setExecutorService(executorService);
        producer.setTransactionListener(transactionListener);
        producer.start();
        for (int i = 0; i < 10; i++){
            Message msg = new Message("TOPIC_BROADCAST" + n,
                    "TAG_BROADCAST" + n,
                    "KEYS_BROADCAST" + n,
                    ("Hello world! BROADCAST MODEL" + i).getBytes());
            SendResult sendResult = producer.sendMessageInTransaction(msg, null);
            logger.info("now:{},sendResult:{}", new Date(), sendResult);
        }
        producer.shutdown();
    }
}

class TransactionConsumer {
    static Logger logger = LoggerFactory.getLogger(TransactionConsumer.class);
    public static void main(String[] args) throws MQClientException {
        long n = 5;
        String grpName = "BROADCAST_CONSUME_GROUP" + n;
        DefaultMQPushConsumer consumer = new DefaultMQPushConsumer(grpName);
        consumer.setNamesrvAddr("127.0.0.1:9876");
        consumer.setConsumeFromWhere(ConsumeFromWhere.CONSUME_FROM_FIRST_OFFSET);
        consumer.subscribe("TOPIC_BROADCAST" + n, "*");
        consumer.registerMessageListener((MessageListenerConcurrently) (msgs, context) -> {
            for (MessageExt msg : msgs) {
                logger.info("now:{}, Receive New Messages:{}", new Date(), new String(msg.getBody()));
            }
            return ConsumeConcurrentlyStatus.CONSUME_SUCCESS;
        });
        consumer.start();
    }
}