package com.zyd.ddz.utils;

import java.util.concurrent.atomic.AtomicInteger;

/**
 * ID生成策略.
 * <p>
 * 需要持久化：角色ID，道具ID等...<br>
 * <p>
 * 持久化：区服编号（16位）+ 时间（29位）+ 自旋因子（14位）+ 类型（1位） <br>
 * <p>
 * 使用方案：创建一个ID生成工厂类，调用此实例.<br>
 * 需要捕获IdMaxSequenceException，超出一轮计次+1存档后再次使用
 *
 * @author zyd
 * @date 2023/1/10 17:44
 */
public class IdGenerator {
    private static final long MAX_SEQUENCE = 1L << 15;

    /**
     * 区服编号
     */
    private static long sid = 1;

    /**
     * 自选因子: 每次 +1
     */
    private static final AtomicInteger sequence = new AtomicInteger(0);

    public static void init(long sid){
        IdGenerator.sid = sid;
    }

    /**
     * 区服编号（16位）+ 时间（28位）+ 自旋因子（15位）+ 类型（1位）
     *
     */
    public static long generator(){
        return sid << 44 | (System.currentTimeMillis() / 1000L) << 16 | nextSequence() << 1 | 1;
    }

    private static long nextSequence() {
        long result = sequence.incrementAndGet();
        if(result > MAX_SEQUENCE){
            throw new RuntimeException("自增因子已达最大值,包装生成器要自增启动次数啦... sequence=" + result);
        }
        return result;
    }

    public static void main(String[] args) {
        IdGenerator.init(1L << 15);
        long id = IdGenerator.generator();
        System.out.println(Long.toBinaryString(System.currentTimeMillis() / 10000L).length());
        System.out.println(id);
        System.out.println(Long.toBinaryString(id).length());
    }
}