import { Button, sp, SpriteFrame } from 'cc';
import { Sprite } from 'cc';
import { _decorator, Component, Node, resources, SpriteAtlas } from 'cc';
const { ccclass, property } = _decorator;

@ccclass('CardLoader')
export class CardLoader extends Component {
    
    private send = false;

    load(path: string) {
        const sprite = this.getComponent(Sprite);
        const cards = sprite.spriteAtlas.getSpriteFrame(path);
        sprite.spriteFrame = cards;
    }


    public setIsSend(send: boolean){
        this.send = send;
    }

    public isSend() : boolean{
        return this.send;
    }
}


