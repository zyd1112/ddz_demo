import { Sprite } from 'cc';
import { _decorator, Component } from 'cc';
const { ccclass } = _decorator;

export interface Card{
    cardValue: number;
    shape: number;

    content: string;

}

@ccclass('CardLoader')
export class CardLoader extends Component {
    private card: Card = null;
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

    public setCard(card: Card){
        this.card = card;
    }

    public getCard(){
        return this.card;
    }
}


