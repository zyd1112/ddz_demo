package com.zyd.ddz.factory;

import com.zyd.ddz.checker.*;
import com.zyd.ddz.entity.Card;
import com.zyd.ddz.entity.CardGroupType;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author zyd
 * @date 2023/4/6 13:19
 */
public class CardFactory {
    private static final Map<Integer, Checker> CARD_TYPE = new HashMap<Integer, Checker>(){{
        put(1, new OneCardChecker());
        put(2, new DoubleCardChecker());
        put(3, new ThreeCardChecker());
        put(4, new ThreeWithOneCardChecker());
        put(5, new ThreeWithTwoCardChecker());
        put(6, new ShunZiChecker());
        put(7, new ConsecutivePairsChecker());
        put(8, new BoomChecker());
        put(9, new JokerBoomChecker());
    }};


    public static CardGroupType getCardType(List<Card> cardList){
        for (Map.Entry<Integer, Checker> entry : CARD_TYPE.entrySet()) {
            if(entry.getValue().test(cardList)){
                return new CardGroupType(entry.getKey(), entry.getValue(), cardList);
            }
        }
        return null;
    }
}
