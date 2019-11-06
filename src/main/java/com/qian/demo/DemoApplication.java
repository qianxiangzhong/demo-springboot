package com.qian.demo;

import org.apache.rocketmq.client.exception.MQBrokerException;
import org.apache.rocketmq.client.exception.MQClientException;
import org.apache.rocketmq.client.producer.DefaultMQProducer;
import org.apache.rocketmq.client.producer.SendResult;
import org.apache.rocketmq.common.message.Message;
import org.apache.rocketmq.remoting.exception.RemotingException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ApplicationContext;

/**
 * @author qxz
 */
@SpringBootApplication
public class DemoApplication {

    public static final Logger logger = LoggerFactory.getLogger(DemoApplication.class);
    public static void main(String[] args) {
        // MQ发消息
        ApplicationContext context = SpringApplication.run(DemoApplication.class, args);
        DefaultMQProducer producer = context.getBean(DefaultMQProducer.class);
//        try {
//            String mqBody = "mqBody";
//            String topic = "alibaba";
//            String tag = "aliyun";
//            for (int i = 0; i < 10; i++) {
//                Message message = new Message(topic,tag + i, mqBody.getBytes());
//                SendResult sendResult = producer.send(message);
//                Thread.sleep(100);
//                logger.info("发送消息：body:{},topic:{},tags:{}",mqBody,topic,tag+i);
//            }
//        } catch (MQClientException | RemotingException | MQBrokerException | InterruptedException e) {
//            e.printStackTrace();
//        }

//        producer.shutdown();
//        logger.info("Producer shutdown!");
    }

}