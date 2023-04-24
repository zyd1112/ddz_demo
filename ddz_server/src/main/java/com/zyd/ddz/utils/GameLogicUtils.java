package com.zyd.ddz.utils;

import com.zyd.ddz.constant.CardContext;
import com.zyd.ddz.entity.Card;
import com.zyd.ddz.entity.CardGroupType;
import com.zyd.ddz.factory.CardFactory;

import java.util.*;

/**
 * @author zyd
 * @date 2023/4/10 11:26
 */
public class GameLogicUtils {

    /**
     * 当前牌面是否大于目标牌面
     * @param cast 出的牌
     * @param target 目标牌
     * @return 是否大于目标牌面
     */
    public static boolean check(List<Card> cast, List<Card> target){
        if(cast == null){
            return false;
        }
        CardGroupType curType = CardFactory.getCardType(cast);
        if(curType == null){
            return false;
        }
        if(target == null){
            return true;
        }
        return check(cast, CardFactory.getCardType(target));
    }

    private static boolean check(List<Card> cast, CardGroupType target){
        CardGroupType curType = CardFactory.getCardType(cast);
        if(curType == null){
            return false;
        }
        return curType.compare(target);
    }

    /**
     * 获取有效牌集
     * @param own 手牌
     * @param target 目标牌
     * @return 有效牌集
     */
    public static List<List<Card>> getAvailableCards(List<Card> own, List<Card> target){
        List<List<Card>> res = new ArrayList<>();
        CardGroupType type = CardFactory.getCardType(target);
        if(type == null){
            return res;
        }
        findCardGroup(own, type, res, new ArrayList<>(), 0);
        return res;
    }

    /**
     * 发牌
     * @return (1 - 3) -> 玩家手牌
     *            4    -> 底牌
     */
    public static List<List<Card>> sendCard() {
        //洗牌
        List<Card> cardList = createCard();
        //发牌
        return sendCard(cardList);
    }

    private static List<Card> createCard(){
        List<Card> cardList = new ArrayList<>();
        for (CardContext.Value value : CardContext.Value.values()) {
            for (CardContext.Shape shape : CardContext.Shape.values()) {
                cardList.add(new Card(value.getValue(), shape.getValue(), shape.getContent() + value.getContent(), false));
            }
        }
        for (CardContext.Joker value : CardContext.Joker.values()) {
            cardList.add(new Card(value.getValue(), 5, value.getContent(), false));
        }

        Collections.shuffle(cardList);

        return cardList;
    }

    private static List<List<Card>> sendCard(List<Card> cardList){
        List<List<Card>> cardGroup = new ArrayList<>();
        if(cardList.size() % 3 != 0){
            return cardGroup;
        }
        //三张底牌
        List<Card> hideCard = new ArrayList<>();
        for (int i = 0; i < 3; i++) {
            hideCard.add(cardList.remove(cardList.size() - 1));
        }
        List<Card> tempList = new ArrayList<>();
        for (Card card : cardList) {
            tempList.add(card);
            if (tempList.size() == 17) {
                cardGroup.add(new ArrayList<>(tempList));
                tempList.clear();
            }
        }
        cardGroup.add(hideCard);
        for (List<Card> cards : cardGroup) {
            cards.sort((o1, o2) -> {
                if(o1.getCardValue() == o2.getCardValue()){
                    return o1.getShape() - o2.getShape();
                }
                return o1.getCardValue() - o2.getCardValue();
            });
        }
        return cardGroup;
    }

    private static void findCardGroup(List<Card> own, CardGroupType target, List<List<Card>> resList, List<Card> curList, int begin){
        if(check(curList, target)){
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
