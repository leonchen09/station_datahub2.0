package com.walletech.queue;

import com.walletech.queue.message.Message;
import com.walletech.queue.exception.QueueException;
import com.walletech.queue.listener.MessageListener;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import javax.annotation.Resource;

/**
 * 将Queue中的数据传递给监听，通过监听类来做类型处理
 * @author
 * @version 1.0
 * @description
 * @since
 */
public class Reciver extends AbstractEndPoint {
    private static final Logger logger = LoggerFactory.getLogger(Reciver.class);

    /**
     * 设置监听
     */
    @Autowired
    private MessageListener messageListener;

    /**
     * 数据的最大处理次数
     */
    private Integer maxDealTimes ;

    public Integer getMaxDealTimes() {
        return maxDealTimes;
    }

    public void setMaxDealTimes(Integer maxDealTimes) {
        this.maxDealTimes = maxDealTimes;
    }


    /**
     * 启动一个线程来循环监听队列中的数据，并发送到监听
     */
    public void start() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                while (true) {
                    Message message;
                    try {
                        message = Reciver.super.getQueue().take();
                        logger.debug("消息[{}]出队", message.getType());
                        messageListener.onMessage(message);
                    } catch (Throwable e) {
                        logger.error("数据处理异常.", e);
                    }
                }
            }
        }).start();
    }
}
