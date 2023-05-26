package com.zyd.ddz.common.checker;

import com.zyd.ddz.common.entity.Card;

import java.util.List;

/**
 * @author zyd
 * @date 2023/4/27 15:13
 */
public class PlaneCardWithTwoChecker extends PlaneCardWithOneChecker{
    @Override
    public String show() {
        return "飞机带对子";
    }

    @Override
    protected boolean match(List<Card> otherCards, int tlen) {
        if(otherCards.size() == tlen * 2){
            int curV1 = otherCards.get(0).getCardValue();
            int curV2 = otherCards.get(1).getCardValue();
            if(curV1 != curV2) {
                return false;
            }
            for (int i = 2, j = 3; i < otherCards.size() && j < otherCards.size(); i += 2, j += 2) {
                int cardValue1 = otherCards.get(i).getCardValue();
                int cardValue2 = otherCards.get(j).getCardValue();
                if(curV1 == cardValue1 || curV2 == cardValue2){
                    return false;
                }
                curV1 = cardValue1;
                curV2 = cardValue2;
            }
            return true;
        }
        return false;
    }
}
