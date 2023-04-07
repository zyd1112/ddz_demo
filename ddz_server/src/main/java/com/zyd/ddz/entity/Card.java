package com.zyd.ddz.entity;

import lombok.*;

/**
 * @author zyd
 * @date 2023/4/6 13:23
 */
@Data
@AllArgsConstructor
public class Card {
    private int cardValue;
    private int shape;
    private String content;


    @Override
    public String toString() {
        return "Card{" + content + '}';
    }
}