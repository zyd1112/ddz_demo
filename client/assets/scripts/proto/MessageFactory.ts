import { _decorator, Component, Node } from 'cc';
import { MessageHander } from './MessageHanderl';
const { ccclass, property } = _decorator;

export class MessageFactory {
    private static messagePool: {[opcode: number]: MessageHander} = []

    public static register(opcode: number, message: MessageHander){
        this.messagePool[opcode] = message;
    }

    public static getHandler(opcode: number){
        return this.messagePool[opcode];
    }
}


