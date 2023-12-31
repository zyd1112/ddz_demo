import { _decorator, Component, Label, Node } from 'cc';
import { MessageUtils } from '../net/MessageUtils';
import { Gloabal } from '../Global';
import { reqNoSend, reqCountdown, reqSuggest, reqSendCard } from '../api/request';
const { ccclass, property } = _decorator;

@ccclass('ClockManager')
export class ClockManager extends Component {

    @property
    private countdown = 10;


    updateTime(dt: number){
        this.updateLabel(this.countdown - dt);
    }

    updateLabel(_time: number){
        const children = this.node.children
        for(let i = 0; i < children.length; i++){
            children[i].getComponent(Label).string = _time + "";
        }
    }

    init(){
        this.updateLabel(this.countdown);
    }

}


