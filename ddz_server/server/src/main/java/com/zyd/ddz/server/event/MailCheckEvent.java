package com.zyd.ddz.server.event;

import com.zyd.ddz.common.entity.UserMail;
import com.zyd.ddz.common.event.monitor.GameTask;
import com.zyd.ddz.common.utils.MailUtils;
import com.zyd.ddz.common.utils.TimeUtils;
import xyz.noark.log.LogHelper;

import java.util.Iterator;
import java.util.Map;
import java.util.concurrent.TimeUnit;

/**
 * @author zyd
 * @date 2023/5/26 16:03
 */
public class MailCheckEvent extends GameTask {
    @Override
    public void doAction() {
        LogHelper.logger.info("邮箱超时检测");
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
    protected long getInitialDelay() {
        return 0;
    }

    @Override
    protected long getDelay() {
        return 300;
    }

    @Override
    protected TimeUnit getUnit() {
        return TimeUnit.SECONDS;
    }
}
