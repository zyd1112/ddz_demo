package com.zyd.ddz.checker;



import com.zyd.ddz.entity.Card;

import java.util.List;

/**
 * @author zyd
 * @date 2023/4/6 13:20
 */
public interface Checker {

    String show();

    boolean test(List<Card> cardList);

    boolean compare(List<Card> castCardList, List<Card> targetCardList);

}
