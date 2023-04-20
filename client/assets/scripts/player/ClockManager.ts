import { _decorator, Component, Label, Node } from 'cc';
import { MessageUtils } from '../net/MessageUtils';
import { Gloabal } from '../Global';
const { ccclass, property } = _decorator;

@ccclass('ClockManager')
export class ClockManager extends Component {

    private curTime = 0;

    @property
    private countdown = 10;

    private time = 0;

    onEnable(){
        this.time = this.countdown;
        this.schedule(this.reqCountdown, 1)
    }

    onDisable(){
        this.curTime = 0;
        this.time = this.countdown;
        this.updateLabel(this.time);
        this.unschedule(this.reqCountdown);
    }

    updateTime(time: number, uid: number){
        if(this.time <= 0){
            let message = {
                opcode: 15,
                uid: uid,
                roomType: Gloabal.roomType,
            }
            MessageUtils.send(message.opcode, message)
        }
        const dt = this.curTime == 0 ? 1 : Math.round((time - this.curTime) / 1000);
        this.time -= dt;
        this.updateLabel(this.time);
        this.curTime = time;
        
    }

    updateLabel(time: number){
        const children = this.node.children
        for(let i = 0; i < children.length; i++){
            children[i].getComponent(Label).string = this.time + "";
        }
    }

    reqCountdown(){
        MessageUtils.send(14, {
            uid: Gloabal.uid,
            roomType: Gloabal.roomType
        });        
    }
    
}


