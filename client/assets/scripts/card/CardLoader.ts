import { Button, sp, SpriteFrame } from 'cc';
import { Sprite } from 'cc';
import { _decorator, Component, Node, resources, SpriteAtlas } from 'cc';
const { ccclass, property } = _decorator;

@ccclass('CardLoader')
export class CardLoader extends Component {
    
    private id = 0;
    private send = false;

    start() {
        const sprite = this.getComponent(Sprite);
        const cards = sprite.spriteAtlas.getSpriteFrame("card_" + this.id);
        sprite.spriteFrame = cards;
    }

    public setId(id: number){
        this.id = id;
    }
    
    public getId() : number {
        return this.id;
    }


    public setIsSend(send: boolean){
        this.send = send;
    }

    public isSend() : boolean{
        return this.send;
    }
}


