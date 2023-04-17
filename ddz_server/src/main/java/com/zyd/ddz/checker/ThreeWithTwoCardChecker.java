package com.zyd.ddz.checker;


import com.zyd.ddz.constant.CardContext;
import com.zyd.ddz.entity.Card;
import java.util.List;
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
    protected boolean match(IntStream stream) {
        return stream.anyMatch(v -> v == 3) && stream.anyMatch(v -> v == 2);
    }
}
