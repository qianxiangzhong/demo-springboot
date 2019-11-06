package com.qian.demo.rocketmq.message.impl;

import com.qian.demo.rocketmq.message.MessageProcessor;
import org.apache.rocketmq.common.message.MessageExt;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

/**
 * @author qxz
 */
@Service
public class MessageProcessorImpl implements MessageProcessor {
    public static final Logger logger = LoggerFactory.getLogger(MessageProcessorImpl.class);
    @Override
    public boolean handle(MessageExt messageExt) {
        String result = new String(messageExt.getBody());
        logger.info("收到消息：body:{},topic:{},tags:{}",result,messageExt.getTopic(),messageExt.getTags());
        return true;
    }
}