package com.zyd.ddz.common.checker;



import com.zyd.ddz.common.constant.CardContext;
import com.zyd.ddz.common.entity.Card;

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
        if(cardList.size() != length()){
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
        return match(Arrays.stream(cardsNum), Arrays.stream(cardsNum));
    }

    protected boolean match(IntStream stream1, IntStream stream2){
        return stream1.anyMatch(v -> v == 3) && stream2.anyMatch(v -> v == 1);
    }

    protected int length(){
        return 4;
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
