import { _decorator, Button, Component, Node, NodeEventType, Prefab, SystemEvent } from 'cc';
import { GameClientNet } from '../net/GameClientNet';
import { MessageFactory } from '../proto/MessageFactory';
import { MessageUtils } from '../net/MessageUtils';
import { Gloabal } from '../Global';
const { ccclass, property } = _decorator;


@ccclass('GameManager')
export class GameManager extends Component {

    @property(Node)
    playerNodes: Node[] = [];

    @property(Node)
    playerSelf: Node = null;

    @property(Node)
    gabage: Node = null;

    @property(Node)
    clock: Node = null;

    @property(Prefab)
    headImages: Prefab[] = [];

    @property(Node)
    countDown: Node = null;

    @property(Prefab)
    roomHostImage: Prefab = null;

    public curId: number = 0;

    public gameStart: boolean = false;


    start() {
        GameClientNet.startClient("10.40.4.208", 10001);
        GameClientNet.getConnection().onopen = () => {
            MessageUtils.send(11, {});
        }
        GameClientNet.getConnection().onmessage = (event) => {
            let data = event.data;
            try{
                data = JSON.parse(data);
            }catch {
                let json = data.replace(/(\w+):/g, "\"$1\":");
                data = JSON.parse(json);
            }
            
            let handler = MessageFactory.getHandler(data.opcode)
            if(handler){
                handler.handler(data.protocol, this);
            }
        }
        
        setTimeout(() => {
            let msg = {
                opcode: 10,
                roomType: 1,
                uid: Gloabal.uid
            };
            Gloabal.roomType = msg.roomType
            MessageUtils.send(msg.opcode, msg);
        }, 3000);
    }
    
}


