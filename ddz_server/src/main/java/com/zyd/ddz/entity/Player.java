package com.zyd.ddz.entity;

import com.zyd.ddz.constant.CharacterType;
import lombok.Getter;
import lombok.Setter;
import xyz.noark.core.network.Session;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Setter
@Getter
public class Player {
    long uid;
    long roomId;
    String name;

    int joyBeans;

    /**
     * 手牌
     */
    List<Card> cardList = new ArrayList<>();

    /**
     * 出的牌
     */
    List<Card> sendCard = new ArrayList<>();

    /**
     * 是否是托管机器人
     */
    boolean auto;

    /**
     * 是否准备
     */
    boolean ready;

    Session session;

    CharacterType character = CharacterType.LANDOWNER;

    long scrambleTime;

    int scrambleCount;

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        Player player = (Player) o;
        return uid == player.uid;
    }

    @Override
    public int hashCode() {
        return Objects.hash(uid);
    }
}
