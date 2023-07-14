package com.zyd.ddz.server.event;

import com.zyd.ddz.common.entity.UserMail;
import com.zyd.ddz.common.utils.MailUtils;
import com.zyd.zgame.common.utils.TimeUtils;
import com.zyd.zgame.core.thread.command.AbstractScheduleCommand;
import lombok.extern.slf4j.Slf4j;

import java.util.Iterator;
import java.util.Map;
import java.util.concurrent.TimeUnit;

/**
 * @author zyd
 * @date 2023/5/26 16:03
 */
@Slf4j
public class MailCheckEvent extends AbstractScheduleCommand {
    @Override
    public void exe() {
        log.info("邮箱超时检测");
        long now = TimeUtils.getNowTimeMillis();
        Iterator<Map.Entry<String, UserMail>> iterator = MailUtils.getMAILS_MAP().entrySet().iterator();
        while (iterator.hasNext()){
            Map.Entry<String, UserMail> next = iterator.next();
            UserMail mail = next.getValue();
            if(now > mail.getEnd()){
                iterator.remove();
            }
        }
    }

    @Override
    public long getInitialDelay() {
        return 0;
    }

    @Override
    public long getDelay() {
        return 300;
    }

    @Override
    public TimeUnit getUnit() {
        return TimeUnit.SECONDS;
    }
}
