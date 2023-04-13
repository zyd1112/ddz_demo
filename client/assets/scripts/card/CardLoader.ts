import { sp, SpriteFrame } from 'cc';
import { Sprite } from 'cc';
import { _decorator, Component, Node, resources, SpriteAtlas } from 'cc';
const { ccclass, property } = _decorator;

@ccclass('CardLoader')
export class CardLoader extends Component {
    
    private cardName = "";

    start() {
        const sprite = this.getComponent(Sprite);
        const cards = sprite.spriteAtlas.getSpriteFrame(this.cardName);
        sprite.spriteFrame = cards;
    }

    public setCardName(name : string){
        this.cardName = name;
    }
}


