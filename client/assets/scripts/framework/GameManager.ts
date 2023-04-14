import { _decorator, Button, Component, Node, NodeEventType, SystemEvent } from 'cc';
import { GameClientNet } from '../net/GameClientNet';
import { MessageFactory } from '../proto/MessageFactory';
import { UserLoginHandler } from '../proto/messageHandler/user/UserLoginHandler';
import { MessageUtils } from '../net/MessageUtils';
import { GameRoomHandler } from '../proto/messageHandler/room/GameRoomHandler';
import { ReqEnterRoomMessage } from '../proto/message/room/ReqEnterRoomMessage';
const { ccclass, property } = _decorator;

@ccclass('GameManager')
export class GameManager extends Component {

    @property(Node)
    cardSlef: Node = null;

    @property(Node)
    cardSide_1: Node = null;
    @property(Node)
    cardSide_2: Node = null;

    uid: number;
    start() {
        GameClientNet.startClient("127.0.0.1", 10001);
        MessageFactory.register(1002, new UserLoginHandler());
        MessageFactory.register(11, new GameRoomHandler());
        let that = this;
        GameClientNet.getConnection().onopen = () => {
            MessageUtils.send(1001, {});
        }
        GameClientNet.getConnection().onmessage = (event) => {
            let data = JSON.parse(event.data);
            let handler = MessageFactory.getHandler(data.opcode)
            if(handler){
                handler.handler(data.protocol, this);
            }
        }
        
        setTimeout(() => {
            let msg = new ReqEnterRoomMessage();
            msg.roomType = 1;
            msg.uid = that.uid;
            MessageUtils.send(msg.opcode, msg);
        }, 5000);
    }
    
}


