package com.zyd.ddz.checker;


import com.zyd.ddz.entity.Card;
import java.util.List;

/**
 * @author zyd
 * @date 2023/4/6 13:21
 */
public class OneCardChecker implements Checker{

    @Override
    public String show() {
        return "单张";
    }

    @Override
    public boolean test(List<Card> cardList) {
        return cardList.size() == 1;
    }

    @Override
    public boolean compare(List<Card> castCardList, List<Card> targetCardList) {
        return castCardList.get(0).getCardValue() > targetCardList.get(0).getCardValue();
    }
}
