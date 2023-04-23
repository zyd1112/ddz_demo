import { _decorator, Button, Camera, Component, EventHandler, EventTouch, geometry, Node, NodeEventType, PhysicsSystem, UITransform } from 'cc';
import { MessageUtils } from '../net/MessageUtils';
import { Gloabal } from '../Global';
import { CardLoader } from '../player/CardLoader';
import { CardManager } from '../player/CardManager';
import { reqNoSend, reqReady, reqSendCard, reqStart, reqSuggest } from '../request/request';
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
        reqSendCard(remove);
    }

    public noSendCard(){
        console.log("不出");
        this.cardSelf.getComponent(CardManager).reset()
        reqNoSend(Gloabal.uid);
    }

    public suggest(){
        reqSuggest(Gloabal.uid);
    }

    public ready(){
        reqReady()
    }

    public gameStart(){
        reqStart();
    }

}




