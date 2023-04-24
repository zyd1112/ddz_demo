import { _decorator, Button, Camera, Component, EventHandler, EventTouch, geometry, Label, Node, NodeEventType, PhysicsSystem, UITransform } from 'cc';
import { Gloabal } from '../Global';
import { CardLoader } from '../player/CardLoader';
import { CardManager } from '../player/CardManager';
import { reqNoSend, reqReady, reqScramble, reqSendCard, reqStart, reqSuggest } from '../api/request';
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
            if(cardLoader.getCard().send){
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

    public scramble(event: Event,customEventData: string){
        console.log(customEventData);
        let flag = false;
        if(customEventData == "0"){
            flag = true;
        }
        reqScramble(flag);
    }

}




