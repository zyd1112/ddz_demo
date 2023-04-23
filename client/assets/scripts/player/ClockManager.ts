import { _decorator, Component, Label, Node } from 'cc';
import { MessageUtils } from '../net/MessageUtils';
import { Gloabal } from '../Global';
import { reqNoSend, reqCountdown, reqSuggest, reqSendCard } from '../request/request';
const { ccclass, property } = _decorator;

@ccclass('ClockManager')
export class ClockManager extends Component {

    @property
    private countdown = 10;

    private time = 10;

    updateTime(dt: number){
        this.time -= dt;
        this.updateLabel(this.time);
    }

    updateLabel(_time: number){
        const children = this.node.children
        for(let i = 0; i < children.length; i++){
            children[i].getComponent(Label).string = _time + "";
        }
    }

    init(){
        this.time = this.countdown;
        this.updateLabel(this.time);
    }

    
    
}


