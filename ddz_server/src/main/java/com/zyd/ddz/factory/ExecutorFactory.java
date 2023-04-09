package com.zyd.ddz.factory;

import com.zyd.ddz.constant.ExecutorType;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ScheduledThreadPoolExecutor;

public class ExecutorFactory {
    private static final ScheduledThreadPoolExecutor ROOM_EXECUTOR = new ScheduledThreadPoolExecutor(4);


    private static final Map<Integer, ScheduledThreadPoolExecutor> SCHEDULED_THREAD_POOL_EXECUTOR = new HashMap<Integer, ScheduledThreadPoolExecutor>(){{
       put(ExecutorType.ROOM, ROOM_EXECUTOR);
    }};

    public static ScheduledThreadPoolExecutor getExecutor(int type){
        return SCHEDULED_THREAD_POOL_EXECUTOR.get(type);
    }
}
