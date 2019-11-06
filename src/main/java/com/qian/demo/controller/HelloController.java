package com.qian.demo.controller;

import com.qian.demo.rocketmq.message.MessageListener;
import com.qian.demo.rocketmq.message.MessageProcessor;
import org.apache.rocketmq.client.consumer.DefaultMQPushConsumer;
import org.apache.rocketmq.client.consumer.listener.ConsumeConcurrentlyContext;
import org.apache.rocketmq.client.consumer.listener.ConsumeConcurrentlyStatus;
import org.apache.rocketmq.client.consumer.listener.MessageListenerConcurrently;
import org.apache.rocketmq.client.exception.MQBrokerException;
import org.apache.rocketmq.client.exception.MQClientException;
import org.apache.rocketmq.client.producer.DefaultMQProducer;
import org.apache.rocketmq.client.producer.SendCallback;
import org.apache.rocketmq.client.producer.SendResult;
import org.apache.rocketmq.common.message.Message;
import org.apache.rocketmq.common.message.MessageExt;
import org.apache.rocketmq.remoting.exception.RemotingException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

/**
 * @author qxz
 */
@Controller
public class HelloController {

    public static final Logger logger = LoggerFactory.getLogger(HelloController.class);
    @Autowired
    DefaultMQProducer producer;
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

    @RequestMapping("/sayHello")
    @ResponseBody
    public String sayHello() {
        String ret = "萨瓦迪卡";
        System.out.println(ret);
        return ret;
    }

    @RequestMapping("/mq")
    @ResponseBody
    public String testRocketMq() throws MQClientException, RemotingException, InterruptedException, MQBrokerException {
        DefaultMQProducer producer = new DefaultMQProducer(groupName);
        producer.setNamesrvAddr(namesrvAddr);
        producer.start();
        producer.setRetryTimesWhenSendAsyncFailed(0);
        String mqBody = "Hello RocketMQ:";
        Message message = new Message(topic, tag, mqBody.getBytes());
        producer.send(message, new SendCallback() {
            @Override
            public void onSuccess(SendResult sendResult) {
                logger.info("Send success,msgId:{}", sendResult.getMsgId());
            }

            @Override
            public void onException(Throwable throwable) {
                logger.error("Send error!");
                throwable.printStackTrace();
            }
        });
        producer.shutdown();
        logger.info("Producer shutdown!");

        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd-HH-mm-ss-S");
        String grpName = groupName + sdf.format(new Date());
        DefaultMQPushConsumer consumer = new DefaultMQPushConsumer(grpName);
        consumer.setNamesrvAddr(namesrvAddr);
        consumer.setConsumeThreadMin(consumeThreadMin);
        consumer.setConsumeThreadMax(consumeThreadMax);
        consumer.setVipChannelEnabled(false);
        // 自己实现的监听器类
        MessageListener messageListener = new MessageListener();
        messageListener.setMessageProcessor(messageProcessor);
        consumer.registerMessageListener(messageListener);

        consumer.start();
        consumer.subscribe(topic,tag);
        logger.info("Consumer has started! groupName:{}, topic:{}", grpName, topic);
        String ret = "萨瓦迪卡";
        System.out.println(ret);
        return ret;
    }

    @RequestMapping("/consume")
    @ResponseBody
    public void consume () {
        String grpName = groupName + System.currentTimeMillis();
        DefaultMQPushConsumer consumer = new DefaultMQPushConsumer(grpName);
        consumer.setNamesrvAddr(namesrvAddr);
        consumer.setConsumeThreadMin(consumeThreadMin);
        consumer.setConsumeThreadMax(consumeThreadMax);
        consumer.setVipChannelEnabled(false);
        // 自己实现的监听器类
        consumer.registerMessageListener(new MessageListenerConcurrently() {
            @Override
            public ConsumeConcurrentlyStatus consumeMessage(List<MessageExt> list, ConsumeConcurrentlyContext consumeConcurrentlyContext) {
                boolean result = true;
                for (MessageExt messageExt: list) {
                    String msgBody = new String(messageExt.getBody());
                    logger.info("收到消息：topic:{},tags:{},msgId:{},body:{},",messageExt.getTopic(),messageExt.getTags(),messageExt.getMsgId(),msgBody);
                    boolean r = messageProcessor.handle(messageExt);
                    logger.info("消费结果:{},", r);
                    result &= r;
                }
                if (result) {
                    return ConsumeConcurrentlyStatus.CONSUME_SUCCESS;
                } else {
                    return ConsumeConcurrentlyStatus.RECONSUME_LATER;
                }
            }
        });

        try {
            String topic = "alibaba";
            String tag = "aliyun";
            consumer.subscribe(topic, tag + 3);
            logger.info("Consumer has started! groupName:{}, topic:{}",grpName, topic);
            consumer.start();
        } catch (MQClientException e) {
            logger.error("Consumer started error!!");
            e.printStackTrace();
        }
    }
}