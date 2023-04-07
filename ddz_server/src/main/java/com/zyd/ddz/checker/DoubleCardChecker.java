package com.zyd.ddz.checker;


import com.zyd.ddz.entity.Card;

import java.util.List;

/**
 * @author zyd
 * @date 2023/4/6 13:27
 */
public class DoubleCardChecker implements Checker{
    @Override
    public String show() {
        return "对子";
    }

    @Override
    public boolean test(List<Card> cardList) {
        return cardList.size() == 2 && cardList.get(0).getCardValue() == cardList.get(1).getCardValue();
    }

    @Override
    public boolean compare(List<Card> castCardList, List<Card> targetCardList) {
        return castCardList.get(0).getCardValue() > targetCardList.get(0).getCardValue();
    }
}
