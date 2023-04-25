import { _decorator, Component, Node } from 'cc';
import { MessageHander } from './MessageHanderl';
import { UserLoginHandler } from './messageHandler/user/UserLoginHandler';
import { CountDownHandler, 
    GameOverRewardsHandler, 
    PlayerCardHandler, 
    PlayerCharacterHandler, 
    PlayerEnterRoomHandler, 
    PlayerLeaveRoomHandler, 
    PlayerReadyHandler, PlayerSuggestHandler, 
    RoomReadyCountDownHandler } from './messageHandler/room/GameRoomHandler';
const { ccclass, property } = _decorator;

export class MessageFactory {
    private static messagePool: {[opcode: number]: MessageHander} = []

    static{
        MessageFactory.register(1000, new PlayerCharacterHandler());
        MessageFactory.register(1001, new PlayerCardHandler());
        MessageFactory.register(1002, new UserLoginHandler());
        MessageFactory.register(1003, new PlayerSuggestHandler());
        MessageFactory.register(1004, new PlayerEnterRoomHandler());
        MessageFactory.register(1005, new CountDownHandler());
        MessageFactory.register(1006, new PlayerReadyHandler());
        MessageFactory.register(1007, new RoomReadyCountDownHandler());
        MessageFactory.register(1008, new PlayerLeaveRoomHandler())
        MessageFactory.register(1009, new GameOverRewardsHandler())
    }

    public static register(opcode: number, message: MessageHander){
        this.messagePool[opcode] = message;
    }

    public static getHandler(opcode: number){
        return this.messagePool[opcode];
    }
}


