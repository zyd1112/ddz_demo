import { _decorator, Button, Component, Node, NodeEventType, Prefab, Sprite, SpriteAtlas, SpriteFrame } from 'cc';
import { PoolManager } from '../framework/PoolManager';
import { Card, CardLoader } from './CardLoader';
import { CharacterType } from '../constant/CharacterType';
const { ccclass, property } = _decorator;

@ccclass('CardManager')
export class CardManager extends Component {
    @property(Prefab)
    public cardPrefab: Prefab = null;
    @property
    private x = 0;
    @property
    private y = 0;
    @property
    public offset_x = 70;
    @property
    public offset_y = 0;

    @property
    public role = 1;

    @property
    private move = 5;

    private tx = 0;
    private ty = 0;

    public init(cards: Card[]){
        if(cards == null){
            return;
        }
        this.tx = this.x;
        this.ty = this.y;
        for(let i = 0; i < cards.length; i++){
            this.addCard(this.tx, this.ty, cards[i]);
            this.tx += this.offset_x;
            this.ty += this.offset_y;
        }
    }
    
    public addCard(tx: number, ty: number, c: Card){
        const card = PoolManager.getInstance().getNode(this.cardPrefab, this.node);
        card.setPosition(tx, ty, 0);
        const cardLoader = card.getComponent(CardLoader);
        
        if(this.role == CharacterType.role.SELF){
            cardLoader.load("card_" + c.cardValue + "_" + c.shape);
            this.addButton(card);
        }else{
            cardLoader.load("card_55");
        }
        cardLoader.setCard(c);
        cardLoader.setIsSend(false);
        return card;
    }
    public refresh(){
        this.tx = this.x;
        this.ty = this.y;
        const children = this.node.children;
        for(let i = 0; i < children.length; i++){
            children[i].setPosition(this.tx, this.ty, 0);
            this.tx += this.offset_x;
            this.ty += this.offset_y;
        }
    }

    public addButton(card: Node) {
        const button = card.addComponent(Button);
        button.node.on(NodeEventType.TOUCH_END, () => {
            const pos = card.position;
            let y = pos.y != this.y ? this.y : pos.y + this.move;
            card.setPosition(pos.x, y, pos.z);
            card.getComponent(CardLoader).setIsSend(y != this.y);
        });
    }
}

