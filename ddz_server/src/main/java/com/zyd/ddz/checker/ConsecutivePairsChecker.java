package com.zyd.ddz.checker;

import com.zyd.ddz.entity.Card;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author zyd
 * @date 2023/4/6 20:32
 */
public class ConsecutivePairsChecker implements Checker{
    @Override
    public String show() {
        return "连对";
    }

    @Override
    public boolean test(List<Card> cardList) {
        int size = cardList.size();
        if(size < 6 || (size & 1) != 0){
            return false;
        }
        Map<Integer, Integer> map = new HashMap<>();
        int max = 0;
        int min = 100;
        for (Card card : cardList) {
            map.merge(card.getCardValue(), 1, Integer::sum);
            max = Math.max(card.getCardValue(), max);
            min = Math.min(card.getCardValue(), min);
        }

        return (max - min == 2) && map.values().stream().allMatch(count -> count == 2);
    }

    @Override
    public boolean compare(List<Card> castCardList, List<Card> targetCardList) {
        return castCardList.get(0).getCardValue() > targetCardList.get(0).getCardValue();
    }
}
