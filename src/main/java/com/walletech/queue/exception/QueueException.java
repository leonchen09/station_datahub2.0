package com.walletech.queue.exception;

/**
 * 消息队列异常
 * @since 2018.5.22
 * @version 1.0
 *
 */
public class QueueException extends Exception {

    public QueueException(){super();}

    public QueueException(String message){
        super(message);
    }
    public QueueException(Throwable throwable){
        super(throwable);
    }

    public QueueException(String message,Throwable throwable){
        super(message,throwable);
    }
}
