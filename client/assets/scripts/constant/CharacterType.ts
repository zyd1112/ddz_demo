import { _decorator, Component, Node } from 'cc';
const { ccclass, property } = _decorator;

export class CharacterType {
    public static LANDOWNER = 1;

    public static FARMER = 2;

    public static PARTNER = 3;

    public static role = {
        SELF: 1,
        SIDE: 2
    }
}


