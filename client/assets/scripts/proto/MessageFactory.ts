import { _decorator, Component, Node } from 'cc';
import { MessageHander } from './MessageHanderl';
import { UserLoginHandler } from './messageHandler/user/UserLoginHandler';
import { CountDownHandler, PlayerCardHandler, PlayerEnterRoomHandler, PlayerReadyHandler, PlayerSuggestHandler } from './messageHandler/room/GameRoomHandler';
const { ccclass, property } = _decorator;

export class MessageFactory {
    private static messagePool: {[opcode: number]: MessageHander} = []

    static{
        MessageFactory.register(1002, new UserLoginHandler());
        MessageFactory.register(1001, new PlayerCardHandler());
        MessageFactory.register(1003, new PlayerSuggestHandler());
        MessageFactory.register(1004, new PlayerEnterRoomHandler());
        MessageFactory.register(1005, new CountDownHandler());
        MessageFactory.register(1006, new PlayerReadyHandler());
    }

    public static register(opcode: number, message: MessageHander){
        this.messagePool[opcode] = message;
    }

    public static getHandler(opcode: number){
        return this.messagePool[opcode];
    }
}


