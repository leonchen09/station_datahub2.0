package com.walletech.queue;

import com.walletech.queue.message.Message;
import org.springframework.beans.factory.annotation.Autowired;

import javax.annotation.Resource;
import java.util.concurrent.Executor;
import java.util.concurrent.LinkedBlockingQueue;

/**
 * 最为Sender和Reciver的父类，分装共有的线程池和Queue
 * @author
 * @version 1.0
 * @description
 * @since
 */
public abstract class AbstractEndPoint {

    @Autowired
    private LinkedBlockingQueue<Message> queue;

    public LinkedBlockingQueue<Message> getQueue() {
        return queue;
    }

    public void setQueue(LinkedBlockingQueue<Message> queue) {
        this.queue = queue;
    }
}
