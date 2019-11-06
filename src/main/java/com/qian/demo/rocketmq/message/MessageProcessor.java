package com.qian.demo.rocketmq.message;

import org.apache.rocketmq.common.message.MessageExt;

/**
 * @author qxz
 */
public interface MessageProcessor {

    /**
     * 出来message
     * @param messageExt
     * @return
     */
    boolean handle(MessageExt messageExt);
}
