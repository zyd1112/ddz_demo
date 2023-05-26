package com.zyd.ddz.common.constant;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * @author zyd
 * @date 2023/4/6 17:43
 */
public interface CardContext {

    @Getter
    @AllArgsConstructor
     enum Value{
        THREE(1, "3"),
         FOUR(2, "4"),
         FIVE(3, "5"),
         SIX(4, "6"),
         SEVEN(5, "7"),
         EIGHT(6, "8"),
         NINE(7, "9"),
         TEN(8, "10"),
         J(9, "J"),
         Q(10, "Q"),
         K(11, "k"),
         A(12, "A"),
         TWO(13, "2");

        int value;
        String content;


        public static boolean contains(int value){
            for (Value value1 : values()) {
                if(value1.getValue() == value){
                    return true;
                }
            }

            return false;
        }

    }
    @Getter
    @AllArgsConstructor
    enum Shape{
        SPADE(4, "黑桃"),
        HEART(3, "红桃"),
        CLUB(2, "梅花"),
        DIAMOND(1, "方块");

        int value;
        String content;

        public static boolean contains(int value){
            for (Shape value1 : values()) {
                if(value1.getValue() == value){
                    return true;
                }
            }

            return false;
        }
    }
    @Getter
    @AllArgsConstructor
    enum Joker{
        MIN(14, "小王"),
        MAX(15, "大王");

        int value;
        String content;

        public static boolean contains(int value){
            for (Joker value1 : values()) {
                if(value1.getValue() == value){
                    return true;
                }
            }

            return false;
        }
    }
}
