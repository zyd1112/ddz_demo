package com.zyd.ddz.checker;


import com.zyd.ddz.entity.Card;

import java.util.List;

/**
 * @author zyd
 * @date 2023/4/6 13:52
 */
public class ThreeCardChecker implements Checker{
    @Override
    public String show() {
        return "三带";
    }

    @Override
    public boolean test(List<Card> cardList) {
        if(cardList.size() != 3){
            return false;
        }
        Card card = cardList.get(0);
        for (Card c : cardList) {
            if(c.getCardValue() != card.getCardValue()){
                return false;
            }
        }
        return true;
    }

    @Override
    public boolean compare(List<Card> castCardList, List<Card> targetCardList) {
        return castCardList.get(0).getCardValue() > targetCardList.get(0).getCardValue();
    }
}
