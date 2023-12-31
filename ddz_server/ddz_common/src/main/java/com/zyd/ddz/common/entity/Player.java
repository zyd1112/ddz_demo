package com.zyd.ddz.common.entity;

import com.zyd.ddz.common.constant.CharacterType;
import com.zyd.ddz.common.message.room.dto.PlayerDto;
import lombok.Getter;
import lombok.Setter;

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
    int wait;

    /**
     * 是否准备
     */
    boolean ready;

    long enterTime;

    CharacterType character = CharacterType.LANDOWNER;

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
        playerDto.setAuto(this.isAuto());
        playerDto.getCards().addAll(this.getCardList());
        return playerDto;
    }

    public void init(){
        this.cardList.clear();
        this.sendCard.clear();
        this.ready = this.isRoomHost();
        this.setScrambleCount(0);
        this.setCharacter(CharacterType.LANDOWNER);
        this.setSuggestOffset(-1);
        this.setWait(0);
    }
}
