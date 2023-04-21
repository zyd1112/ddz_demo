import { _decorator, Component, Label, Node } from 'cc';
import { MessageUtils } from '../net/MessageUtils';
import { Gloabal } from '../Global';
import { reqNoSend, reqCountdown, reqSuggest, reqSendCard } from '../request/request';
const { ccclass, property } = _decorator;

@ccclass('ClockManager')
export class ClockManager extends Component {

    private curTime = 0;

    @property
    private countdown = 10;

    private time = 10;

    onEnable(){
        this.time = this.countdown;
        this.schedule(reqCountdown, 1)
    }

    onDisable(){
        this.curTime = 0;
        this.time = this.countdown;
        this.updateLabel(this.time);
        this.unschedule(reqCountdown);
    }

    updateTime(time: number){
        
        const dt = this.curTime == 0 ? 1 : Math.round((time - this.curTime) / 1000);
        this.time -= dt;
        this.updateLabel(this.time);
        this.curTime = time;
        
        return this.time;
    }

    updateLabel(_time: number){
        const children = this.node.children
        for(let i = 0; i < children.length; i++){
            children[i].getComponent(Label).string = _time + "";
        }
    }

    
    
}


