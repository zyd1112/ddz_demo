package com.zyd.ddz.checker;


import com.zyd.ddz.entity.Card;

import java.util.List;

/**
 * @author zyd
 * @date 2023/4/6 20:36
 */
public class BoomChecker implements Checker{
    @Override
    public String show() {
        return "炸弹";
    }

    @Override
    public boolean test(List<Card> cardList) {
        if(cardList.size() != 4){
            return false;
        }

        Card card = cardList.get(0);
        return cardList.stream().allMatch(c -> c.getCardValue() == card.getCardValue());
    }

    @Override
    public boolean compare(List<Card> castCardList, List<Card> targetCardList) {
        return castCardList.get(0).getCardValue() > targetCardList.get(0).getCardValue();
    }
}
