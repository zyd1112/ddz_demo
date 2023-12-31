package com.zyd.ddz.common.factory;

import com.zyd.ddz.common.checker.*;
import com.zyd.ddz.common.entity.Card;
import com.zyd.ddz.common.entity.CardGroupType;

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
        put(3, new BoomChecker());
        put(4, new ThreeCardChecker());
        put(5, new ThreeWithOneCardChecker());
        put(6, new ThreeWithTwoCardChecker());
        put(7, new ShunZiChecker());
        put(8, new PlaneCardWithOneChecker());
        put(9, new PlaneCardWithTwoChecker());
        put(10, new ConsecutivePairsChecker());
        put(11, new JokerBoomChecker());
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
