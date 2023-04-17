package com.zyd.ddz.checker;


import com.zyd.ddz.constant.CardContext;
import com.zyd.ddz.entity.Card;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.IntStream;

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

        int[] cardsNum = new int[20];

        for (Card card : cardList) {
            int value = card.getCardValue();
            if(CardContext.Joker.contains(value)){
                return false;
            }
            cardsNum[value]++;

        }
        return match(Arrays.stream(cardsNum));
    }

    protected boolean match(IntStream stream){
        return stream.anyMatch(v -> v == 3) && stream.anyMatch(v -> v == 1);
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
