package com.zyd.ddz.constant;

/**
 * @author zyd
 * @date 2023/4/11 13:30
 */
public enum CharacterType {
    /**
     * 地主
     */
    LANDOWNER,
    /**
     * 农民
     */
    FARMER,
    /**
     * 搭档
     */
    PARTNER;

    public static CharacterType getType(int type){
        return values()[type - 1];
    }
}
