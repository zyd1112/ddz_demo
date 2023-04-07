package com.zyd.ddz.service.impl;

import com.zyd.ddz.constant.CardContext;
import com.zyd.ddz.entity.Card;
import com.zyd.ddz.entity.CardGroupType;
import com.zyd.ddz.factory.CardFactory;
import com.zyd.ddz.service.DdzService;
import xyz.noark.core.annotation.Service;

import java.util.*;
import java.util.concurrent.ThreadLocalRandom;

/**
 * @author zyd
 * @date 2023/4/7 9:21
 */
@Service
public class DdzServiceImpl implements DdzService {

    @Override
    public List<List<Card>> reqPrompt(List<Card> own, List<Card> target){
        List<List<Card>> res = new ArrayList<>();
        findCardGroup(own, CardFactory.getCardType(target), res, new ArrayList<>(), 0);
        return res;
    }

    @Override
    public Map<Integer, List<Card>> reqSendCard() {
        //洗牌
        List<Card> cardList = createCard();
        //发牌
        return sendCard(cardList);
    }

    private List<Card> createCard(){
        List<Card> cardList = new ArrayList<>();
        for (CardContext.Value value : CardContext.Value.values()) {
            for (CardContext.Shape shape : CardContext.Shape.values()) {
                cardList.add(new Card(value.getValue(), shape.getValue(), shape.getContent() + value.getContent()));
            }
        }
        for (CardContext.Joker value : CardContext.Joker.values()) {
            cardList.add(new Card(value.getValue(), 5, value.getContent()));
        }

        Collections.shuffle(cardList);

        return cardList;
    }

    private Map<Integer, List<Card>> sendCard(List<Card> cardList){
        Map<Integer, List<Card>> cardGroup = new HashMap<>();
        if(cardList.size() % 3 != 0){
            return cardGroup;
        }
        //三张底牌
        List<Card> hideCard = new ArrayList<>();
        for (int i = 0; i < 3; i++) {
            hideCard.add(cardList.remove(cardList.size() - 1));
        }
        cardGroup.put(4, hideCard);
        int group = 1;
        for (int i = 1; i <= cardList.size(); i++) {
            cardGroup.computeIfAbsent(group, k -> new ArrayList<>()).add(cardList.get(i - 1));
            if(i % 17 == 0){
                group++;
            }
        }
        for (List<Card> cards : cardGroup.values()) {
            cards.sort((o1, o2) -> {
                if(o1.getCardValue() == o2.getCardValue()){
                    return o1.getShape() - o2.getShape();
                }
                return o1.getCardValue() - o2.getCardValue();
            });
        }
        return cardGroup;
    }

    private void findCardGroup(List<Card> own, CardGroupType target, List<List<Card>> resList, List<Card> curList, int begin){
        if (target == null){
            return;
        }
        CardGroupType curType = CardFactory.getCardType(curList);
        if(curType != null && curType.compare(target)){
            resList.add(new ArrayList<>(curList));
            return;
        }

        for (int i = begin; i < own.size(); i++) {
            Card card = own.get(i);
            if(target.getCardList().size() <= curList.size()){
                return;
            }
            curList.add(card);
            findCardGroup(own, target, resList, curList, i + 1);
            curList.remove(curList.size() - 1);
        }
    }




}
