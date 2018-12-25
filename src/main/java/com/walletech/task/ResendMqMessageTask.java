package com.walletech.task;

import com.alibaba.fastjson.JSON;
import com.walletech.po.ModifyGprsIdInfo;
import com.walletech.util.CacheUtil;
import com.walletech.util.RabbitUtil;
import io.netty.util.HashedWheelTimer;
import io.netty.util.Timeout;
import io.netty.util.TimerTask;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;

import java.util.Iterator;
import java.util.concurrent.TimeUnit;

/**
 * 重发MQ消息任务类
 */
@Component
public class ResendMqMessageTask {
    private HashedWheelTimer timer = new HashedWheelTimer();
    private static final String ROUTING_KEY = "resultOfUpdateGprsId";
    @Autowired
    private RabbitTemplate rabbitTemplate;

    public void start() {
        TimerTask task = new TimerTask() {
            @Override
            public void run(Timeout timeout) throws Exception {
                addTask(this);
                if (CollectionUtils.isEmpty(CacheUtil.SEND_MODIFYGPRSID_ACK_FAILED_LIST)) {
                    return;
                }
                //重发消息
                synchronized (CacheUtil.SEND_MODIFYGPRSID_ACK_FAILED_LIST) {
                    Iterator<ModifyGprsIdInfo> it = CacheUtil.SEND_MODIFYGPRSID_ACK_FAILED_LIST.iterator();
                    while (it.hasNext()) {
                        ModifyGprsIdInfo info = it.next();
                        RabbitUtil.sendResultOfModifyGprsIdMessage2Mq(rabbitTemplate, JSON.toJSONString(info).getBytes(), ROUTING_KEY);
                        it.remove();
                    }
                }
            }
        };
        addTask(task);
    }

    private void addTask(TimerTask task) {
        timer.newTimeout(task, 1, TimeUnit.MINUTES);
    }
}
