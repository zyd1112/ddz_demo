package com.zyd.ddz.common.constant;

import lombok.Getter;

/**
 * @author zyd
 * @date 2023/4/11 13:30
 */
public enum CharacterType {
    /**
     * 地主
     */
    LANDOWNER(1),
    /**
     * 农民
     */
    FARMER(2),
    /**
     * 搭档
     */
    PARTNER(3);

    @Getter
    int type;

    CharacterType(int type){
        this.type = type;
    }

    public static CharacterType getType(int type){
        return values()[type - 1];
    }
}
