import { _decorator, Button, Component, Node, NodeEventType, Prefab, Sprite, SpriteAtlas, SpriteFrame } from 'cc';
import { PoolManager } from '../framework/PoolManager';
import { CardLoader } from './CardLoader';
import { CharacterType } from '../constant/CharacterType';
const { ccclass, property } = _decorator;

@ccclass('CardManager')
export class CardManager extends Component {
    @property(Prefab)
    private cardPrefab: Prefab = null;
    @property
    private x = 0;
    @property
    private y = 0;
    @property
    private offset_x = 70;
    @property
    private offset_y = 0;

    @property
    private role = 1;

    @property
    private move = 5;

    private tx = 0;
    private ty = 0;
    start() {
        this.tx = this.x;
        this.ty = this.y;
        for(let i = 0; i < 17; i++){
            const card = this.addCard(this.tx, this.ty, i + 1);
            this.tx = card.position.x;
            this.ty = card.position.y;
        }
    }
    public addCard(tx: number, ty: number, id: number){
        const card = PoolManager.getInstance().getNode(this.cardPrefab, this.node);
        tx += this.offset_x;
        ty += this.offset_y;
        card.setPosition(tx, ty, 0);
        const cardLoader = card.getComponent(CardLoader);
        if(this.role == CharacterType.role.SELF){
            cardLoader.setId(id);
            this.addButton(card);
        }else{
            cardLoader.setId(55);
        }
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

