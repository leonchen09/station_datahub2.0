package com.walletech.queue.listener;


import com.walletech.queue.message.Message;
import com.walletech.queue.exception.QueueException;

/**
 * 接收数据的监听接口
 * @author
 * @version 1.0
 * @description
 * @since
 */
public interface MessageListener {

    void onMessage(Message message) throws QueueException;

}
