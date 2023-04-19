import { _decorator, Button, Component, Node, NodeEventType, Prefab, SystemEvent } from 'cc';
import { GameClientNet } from '../net/GameClientNet';
import { MessageFactory } from '../proto/MessageFactory';
import { UserLoginHandler } from '../proto/messageHandler/user/UserLoginHandler';
import { MessageUtils } from '../net/MessageUtils';
import { PlayerCardHandler, PlayerCharacterHandler, PlayerSuggestHandler } from '../proto/messageHandler/room/GameRoomHandler';
import { Gloabal } from '../Global';
const { ccclass, property } = _decorator;

@ccclass('GameManager')
export class GameManager extends Component {

    @property(Node)
    cardNodes: Node[] = [];

    @property(Node)
    gabage: Node = null;


    start() {
        GameClientNet.startClient("127.0.0.1", 10001);
        MessageFactory.register(1002, new UserLoginHandler());
        MessageFactory.register(1001, new PlayerCardHandler());
        MessageFactory.register(1003, new PlayerSuggestHandler());
        MessageFactory.register(1004, new PlayerCharacterHandler());
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
        }, 5000);
    }
    
}


