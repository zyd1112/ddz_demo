import { _decorator, Component, Node } from 'cc';
const { ccclass, property } = _decorator;

export enum CharacterType {
    LANDOWNER = 1,

    FARMER = 2,

    PARTNER = 3,

    
}

export class Role {
    public static SELF = 1;
    public static SIDE = 2;
}


