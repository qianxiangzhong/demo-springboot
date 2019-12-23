package com.qian.demo.rocketmq.message;

import org.apache.rocketmq.client.consumer.listener.ConsumeConcurrentlyContext;
import org.apache.rocketmq.client.consumer.listener.ConsumeConcurrentlyStatus;
import org.apache.rocketmq.client.consumer.listener.MessageListenerConcurrently;
import org.apache.rocketmq.common.message.MessageExt;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

/**
 * @author qxz
 */
public class MessageListener implements MessageListenerConcurrently {

    @Autowired
    MessageProcessor messageProcessor;

    public void setMessageProcessor(MessageProcessor messageProcessor) {
        this.messageProcessor = messageProcessor;
    }

    @Override
    public ConsumeConcurrentlyStatus consumeMessage(List<MessageExt> list, ConsumeConcurrentlyContext consumeConcurrentlyContext) {
        MessageExt messageExt = list.get(0);
        boolean result = messageProcessor.handle(messageExt);
        if (!result) {
            return ConsumeConcurrentlyStatus.RECONSUME_LATER;
        } else {
            return ConsumeConcurrentlyStatus.CONSUME_SUCCESS;
        }
    }
}
