package com.zyd.ddz.common.checker;


import com.zyd.ddz.common.constant.CardContext;
import com.zyd.ddz.common.entity.Card;

import java.util.*;
import java.util.stream.Collectors;

/**
 * @author zyd
 * @date 2023/4/21 14:03
 */
public class PlaneCardWithOneChecker implements Checker{

    @Override
    public String show() {
        return "飞机带单张";
    }
    @Override
    public boolean test(List<Card> cardList) {
        if(cardList.size() < 8){
            return false;
        }

        int[] cardsNum = new int[20];
        int val = 0;
        Set<Integer> threeCards = new HashSet<>();
        for (Card card : cardList) {
            int value = card.getCardValue();
            if(CardContext.Joker.contains(value)){
                return false;
            }
            cardsNum[value]++;
            if(cardsNum[value] > 3){
                return false;
            }
            if(cardsNum[value] == 3){
                if(val == 0){
                    val = value;
                }else if(value - val != 1) {
                    return false;
                }else {
                    val = value;
                }
                threeCards.add(value);
            }
        }
        if(threeCards.size() < 2) {
            return false;
        }
        List<Card> otherCards = cardList.stream()
                .filter(card -> !threeCards.contains(card.getCardValue()))
                .sorted(Comparator.comparing(Card::getCardValue))
                .collect(Collectors.toList());
        int olen = otherCards.size();
        int tlen = threeCards.size();
        if(olen == 0){
            return true;
        }

        return match(otherCards, tlen);
    }

    protected boolean match(List<Card> otherCards, int tlen){
        if(otherCards.size() == tlen){
            int curV = otherCards.get(0).getCardValue();
            for (int i = 1; i < otherCards.size(); i++) {
                int cardValue = otherCards.get(i).getCardValue();
                if(cardValue == curV){
                    return false;
                }
                curV = cardValue;
            }
            return true;
        }
        return false;
    }


    @Override
    public boolean compare(List<Card> castCardList, List<Card> targetCardList) {
        castCardList.sort(Comparator.comparing(Card::getCardValue));
        targetCardList.sort(Comparator.comparing(Card::getCardValue));
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
