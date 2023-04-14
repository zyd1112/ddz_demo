import { _decorator, Component, Node, Socket } from 'cc';
import { MessageFactory } from '../proto/MessageFactory';
const { ccclass, property } = _decorator;

export class GameClientNet {
    private static websocket: WebSocket = null;

    public static startClient(ip: string, port: number){
        this.websocket = new WebSocket("ws://" + ip + ":" + port);
        this.websocket.onopen = () => {
            console.log("client connect")
            this.sendMessge();
        }
        this.websocket.onmessage = (event) => {
            let data = JSON.parse(event.data);
            let message = MessageFactory.getMessage(data.opcode)
            message = data.protocol;
            console.log(message)
        }
        let that = this;
        
    }

    public static sendMessge(){
        this.websocket.send(JSON.stringify({
            opcode: 1001,
            // uid: 123,
            // roomType: 1
        }));
    }
}


