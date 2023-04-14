import { _decorator, Component, Node } from 'cc';
const { ccclass, property } = _decorator;

export class MessageFactory {
    private static messagePool: {[opcode: number]: Message} = []

    public static register(opcode: number, message: Message){
        this.messagePool[opcode] = message;
    }

    public static getMessage(opcode: number){
        return this.messagePool[opcode];
    }
}


