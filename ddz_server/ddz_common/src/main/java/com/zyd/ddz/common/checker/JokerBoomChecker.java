package com.zyd.ddz.common.checker;



import com.zyd.ddz.common.constant.CardContext;
import com.zyd.ddz.common.entity.Card;

import java.util.List;

/**
 * @author zyd
 * @date 2023/4/6 20:40
 */
public class JokerBoomChecker implements Checker{
    @Override
    public String show() {
        return "王炸";
    }

    @Override
    public boolean test(List<Card> cardList) {
        if(cardList.size() != 2){
            return false;
        }

        return cardList.get(0).getCardValue() == CardContext.Joker.MIN.getValue() && cardList.get(1).getCardValue() == CardContext.Joker.MAX.getValue();
    }

    @Override
    public boolean compare(List<Card> castCardList, List<Card> targetCardList) {
        return true;
    }
}
