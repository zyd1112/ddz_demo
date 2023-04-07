package com.zyd.ddz.checker;


import com.zyd.ddz.constant.CardContext;
import com.zyd.ddz.entity.Card;
import java.util.List;

/**
 * @author zyd
 * @date 2023/4/6 17:18
 */
public class ThreeWithTwoCardChecker extends ThreeWithOneCardChecker{
    @Override
    public String show() {
        return "三带二";
    }

    @Override
    public boolean test(List<Card> cardList) {
        if(cardList.size() != 5){
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
        return (threeCnt == 2 && oneCnt == 2) || (threeCnt == 3 && oneCnt == 1);
    }
}
