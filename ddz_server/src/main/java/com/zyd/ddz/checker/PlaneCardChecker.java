package com.zyd.ddz.checker;

import com.zyd.ddz.constant.CardContext;
import com.zyd.ddz.entity.Card;

import java.util.List;

/**
 * @author zyd
 * @date 2023/4/21 14:03
 */
public class PlaneCardChecker implements Checker{
    @Override
    public String show() {
        return "飞机";
    }
    //todo 飞机策略抽象
    @Override
    public boolean test(List<Card> cardList) {
        if(cardList.size() < 8){
            return false;
        }

        int[] cardsNum = new int[20];

        for (Card card : cardList) {
            int value = card.getCardValue();
            if(CardContext.Joker.contains(value)){
                return false;
            }
            cardsNum[value]++;

        }
        return false;
    }

    @Override
    public boolean compare(List<Card> castCardList, List<Card> targetCardList) {
        return false;
    }
}
