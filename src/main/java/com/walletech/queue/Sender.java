package com.walletech.queue;

import com.walletech.queue.message.Message;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * 将数据保存到Queue
 * @author
 * @version 1.0
 * @description
 * @since
 */
public class Sender extends AbstractEndPoint{
    private static final Logger logger = LoggerFactory.getLogger(Sender.class);

    /**
     * 数据存入队列
     * @param message
     */
    public void saveMessage(Message message){
        super.getQueue().offer(message);
    }


}
