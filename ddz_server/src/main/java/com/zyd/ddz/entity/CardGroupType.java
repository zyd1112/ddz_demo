package com.zyd.ddz.entity;

import com.zyd.ddz.checker.BoomChecker;
import com.zyd.ddz.checker.Checker;
import com.zyd.ddz.checker.JokerBoomChecker;
import lombok.*;

import java.util.List;

/**
 * @author zyd
 * @date 2023/4/6 14:29
 */
@Data
@AllArgsConstructor
public class CardGroupType {
    private int index;

    private Checker checker;

    private List<Card> cardList;


    public boolean compare(CardGroupType cardType){
        if(cardType.getChecker() == null){
            return true;
        }
        if(cardType.getIndex() != this.getIndex()){
            if(this.getChecker() instanceof JokerBoomChecker){
                return true;
            }
            if(this.getChecker() instanceof BoomChecker){
                return !(cardType.getChecker() instanceof JokerBoomChecker);
            }
            return false;
        }
        return this.getChecker().compare(this.getCardList(), cardType.getCardList());
    }
}
