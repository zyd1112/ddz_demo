package com.zyd.ddz.common.utils;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * @author zyd
 * @date 2023/2/16 17:05
 */
public class TimeUtils {

    public static String getFormatNowTime(){
        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        return df.format(new Date());
    }

    public static long getNowTimeMillis(){
        return System.currentTimeMillis();
    }
}
