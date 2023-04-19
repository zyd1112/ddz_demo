import { _decorator, Button, Camera, Component, EventHandler, EventTouch, geometry, Node, NodeEventType, PhysicsSystem, UITransform } from 'cc';
import { CardLoader } from '../card/CardLoader';
import { MessageUtils } from '../net/MessageUtils';
import { Gloabal } from '../Global';
import { CardManager } from '../card/CardManager';
const { ccclass, property } = _decorator;

@ccclass('ButtonEvent')
export class ButtonEvent extends Component {

    @property(Node)
    private cardSelf: Node = null;

    public sendCard(){
        console.log("出牌");
        const children = this.cardSelf.children;
        const remove= [];
        for(let i = 0; i < children.length; i++){
            let cardLoader = children[i].getComponent(CardLoader);
            if(cardLoader.isSend()){
                remove.push(cardLoader.getCard());
            }
        }
        let message = {
            opcode: 12,
            uid: Gloabal.uid,
            roomType: Gloabal.roomType,
            cardList: remove
        }
        MessageUtils.send(message.opcode, message)
    }

    public noSendCard(){
        console.log("不出");
        this.cardSelf.getComponent(CardManager).reset()
    }

    public suggest(){
            let message = {
                opcode: 13,
                uid: Gloabal.uid,
                roomType: Gloabal.roomType,
            }
            MessageUtils.send(message.opcode, message)
    }

}


