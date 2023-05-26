package com.zyd.ddz.common.checker;



import com.zyd.ddz.common.constant.CardContext;
import com.zyd.ddz.common.entity.Card;

import java.util.List;

/**
 * @author zyd
 * @date 2023/4/6 17:35
 */
public class ShunZiChecker implements Checker{
    @Override
    public String show() {
        return "顺子";
    }

    @Override
    public boolean test(List<Card> cardList) {
        int size = cardList.size();
        if(size < 5){
            return false;
        }
        int temp = cardList.get(0).getCardValue();
        for (int i = 0; i < cardList.size(); i++) {
            int cardValue = cardList.get(i).getCardValue();
            if(cardValue == CardContext.Joker.MAX.getValue()
                    || cardValue == CardContext.Joker.MIN.getValue()
                    || cardValue == CardContext.Value.TWO.getValue()){
                return false;
            }

            if(i > 0){
                if(cardValue != temp + 1){
                    return false;
                }
                temp = cardValue;
            }
        }

        return true;
    }

    @Override
    public boolean compare(List<Card> castCardList, List<Card> targetCardList) {
        return castCardList.size() == targetCardList.size() && castCardList.get(0).getCardValue() > targetCardList.get(0).getCardValue();
    }
}
