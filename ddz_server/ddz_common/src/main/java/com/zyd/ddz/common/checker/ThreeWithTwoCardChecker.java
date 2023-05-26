package com.zyd.ddz.common.checker;


import java.util.stream.IntStream;

/**
 * @author zyd
 * @date 2023/4/6 17:18
 */
public class ThreeWithTwoCardChecker extends ThreeWithOneCardChecker{
    @Override
    public String show() {
        return "三带二";
    }

    @Override
    protected boolean match(IntStream stream1, IntStream stream2) {
        return stream1.anyMatch(v -> v == 3) && stream2.anyMatch(v -> v == 2);
    }

    @Override
    protected int length() {
        return 5;
    }
}
