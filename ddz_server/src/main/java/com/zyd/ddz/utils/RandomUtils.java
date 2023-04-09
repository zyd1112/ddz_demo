package com.zyd.ddz.utils;

import java.util.concurrent.ThreadLocalRandom;

public class RandomUtils {

    private static final ThreadLocalRandom random = ThreadLocalRandom.current();

    public static int getRandomNum(int bounds){
        return random.nextInt(bounds);
    }
}
