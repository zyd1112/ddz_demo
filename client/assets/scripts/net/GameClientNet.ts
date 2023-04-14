import { _decorator, Component, Node, Socket } from 'cc';
import { MessageFactory } from '../proto/MessageFactory';
const { ccclass, property } = _decorator;

export class GameClientNet {
    private static websocket: WebSocket = null;

    public static startClient(ip: string, port: number){
        this.websocket = new WebSocket("ws://" + ip + ":" + port);        
    }

    public static getConnection() : WebSocket{
        return this.websocket;
    }
}


