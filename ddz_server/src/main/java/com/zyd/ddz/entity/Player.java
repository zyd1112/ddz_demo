package com.zyd.ddz.entity;

import com.zyd.ddz.constant.CharacterType;
import com.zyd.ddz.message.room.dto.PlayerDto;
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

    int imageIndex;

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

    long enterTime;

    CharacterType character = CharacterType.LANDOWNER;

    long scrambleTime;

    int scrambleCount;

    int suggestOffset = -1;

    boolean leave = false;

    boolean roomHost = false;

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

    public PlayerDto packPlayerDto(){
        PlayerDto playerDto = new PlayerDto();
        playerDto.setUid(this.getUid());
        playerDto.setName(this.getName());
        playerDto.setCharacterType(this.getCharacter().getType());
        playerDto.setRoomHost(this.isRoomHost());
        playerDto.setReady(this.isReady());
        playerDto.setEnterTime(this.getEnterTime());
        playerDto.setImageIndex(this.getImageIndex());
        playerDto.setJoyBeans(this.getJoyBeans());
        return playerDto;
    }
}
