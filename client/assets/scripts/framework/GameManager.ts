import { _decorator, Button, Component, Node, NodeEventType, SystemEvent } from 'cc';
import { GameClientNet } from '../net/GameClientNet';
import { MessageFactory } from '../proto/MessageFactory';
import { ResUserLoginMessage } from '../proto/message/ResUserLoginMessge';
const { ccclass, property } = _decorator;

@ccclass('GameManager')
export class GameManager extends Component {
    start() {
        GameClientNet.startClient("127.0.0.1", 10001);
        MessageFactory.register(1002, new ResUserLoginMessage());
    }
    
}


