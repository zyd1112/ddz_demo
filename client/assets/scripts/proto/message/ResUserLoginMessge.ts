import { _decorator, Component, Node } from 'cc';
const { ccclass, property } = _decorator;

export class ResUserLoginMessage implements Message {
    id: bigint;
    nickname: string;
    password: string;
    username: string;

}
