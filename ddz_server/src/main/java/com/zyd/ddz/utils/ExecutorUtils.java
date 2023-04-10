package com.zyd.ddz.utils;

import com.zyd.ddz.constant.ExecutorType;
import com.zyd.ddz.event.GameTask;
import com.zyd.ddz.factory.ExecutorFactory;

import java.util.concurrent.TimeUnit;
import static xyz.noark.log.LogHelper.logger;

public class ExecutorUtils {

    public static void startScheduleTask(ExecutorType type, GameTask gameTask, int initialDelay, int period, TimeUnit unit){
        logger.info("{}, 启动", type.getName());
        ExecutorFactory.getExecutor(type).scheduleAtFixedRate(gameTask, initialDelay, period, unit);
    }
}
