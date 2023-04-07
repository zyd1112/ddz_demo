package com.zyd.ddz.checker;


import com.zyd.ddz.constant.CardContext;
import com.zyd.ddz.entity.Card;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author zyd
 * @date 2023/4/6 13:29
 */
public class ThreeWithOneCardChecker implements Checker{
    @Override
    public String show() {
        return "三带一";
    }

    @Override
    public boolean test(List<Card> cardList) {
        if(cardList.size() != 4){
            return false;
        }
        int threeCnt = 0;
        int oneCnt = 0;
        int firstValue = cardList.get(0).getCardValue();
        if(CardContext.Joker.contains(firstValue)){
            return false;
        }
        for (int i = 1; i < cardList.size(); i++) {
            int total = cardList.get(i).getCardValue();
            if(CardContext.Joker.contains(total)){
                return false;
            }
            if(total == firstValue){
                threeCnt++;
            }else{
                oneCnt++;
                firstValue = total;
            }
        }
        return (threeCnt == 2 && oneCnt == 1) || (threeCnt == 3 && oneCnt == 0);
    }

    @Override
    public boolean compare(List<Card> castCardList, List<Card> targetCardList) {
        Map<Integer, Integer> map = new HashMap<>();
        int castValue = 0;
        int targetValue = 0;
        for (Card card : castCardList) {
            int value = card.getCardValue();
            map.merge(value, 1, Integer::sum);
            if(map.getOrDefault(value, 0) == 3){
                castValue = value;
                break;
            }
        }
        for (Card card : targetCardList) {
            int value = card.getCardValue();
            map.merge(value, 1, Integer::sum);
            if(map.getOrDefault(value, 0) == 3){
                targetValue = value;
                break;
            }
        }

        return castValue > targetValue;
    }
}
