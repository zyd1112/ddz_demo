package com.zyd.ddz.common.entity;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.Objects;

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

    private boolean send;

    public Card(int cardValue, int shape, String content){
        this.cardValue = cardValue;
        this.shape = shape;
        this.content = content;
        this.send = false;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        Card card = (Card) o;
        return cardValue == card.cardValue && shape == card.shape;
    }

    @Override
    public int hashCode() {
        return Objects.hash(cardValue, shape);
    }

    @Override
    public String toString() {
        return "Card{" + content + '}';
    }
}