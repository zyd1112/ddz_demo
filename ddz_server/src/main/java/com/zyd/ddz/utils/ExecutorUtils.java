package com.zyd.ddz.utils;

import com.zyd.ddz.event.GameTask;
import com.zyd.ddz.factory.ExecutorFactory;

import java.util.concurrent.TimeUnit;

public class ExecutorUtils {

    public static void startScheduleTask(int type, GameTask gameTask, int initialDelay, int period, TimeUnit unit){
        ExecutorFactory.getExecutor(type).scheduleAtFixedRate(gameTask, initialDelay, period, unit);
    }
}
