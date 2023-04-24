import { _decorator, Button, Component, Node, NodeEventType, Prefab, Sprite, SpriteAtlas, SpriteFrame } from 'cc';
import { PoolManager } from '../framework/PoolManager';
import { Card, CardLoader } from './CardLoader';
import { Role } from '../constant/CharacterType';
const { ccclass, property } = _decorator;

@ccclass('CardManager')
export class CardManager extends Component {
    @property(Prefab)
    public cardPrefab: Prefab = null;

    @property
    public offset_x = 70;
    @property
    public offset_y = 0;

    @property
    private pop = 5;

    public uid: number = 0;


    public init(cards: Card[], role: number){
        if(cards == null){
            return;
        }
        let tx = 0;
        let ty = 0;
        for(let i = 0; i < cards.length; i++){
            this.addCard(tx, ty, cards[i], role);
            tx += this.offset_x;
            ty += this.offset_y;
        }
    }
    
    public addCard(tx: number, ty: number, c: Card, role: number){
        const card = PoolManager.getInstance().getNode(this.cardPrefab, this.node);
        card.setPosition(tx, ty, 0);
        const cardLoader = card.getComponent(CardLoader);
        cardLoader.setCard(c);
        if(role == Role.SELF){
            cardLoader.load("card_" + c.cardValue + "_" + c.shape);
            this.addButton(card);
            if(c.send){
                this.popCard(card);
            }
        }else{
            cardLoader.load("card_55");
            c.send = false;
        }
        return card;
    }

    public addButton(card: Node) {
        const button = card.addComponent(Button);
        button.node.on(NodeEventType.TOUCH_END, () => {
            this.popCard(card);
        });
    }

    public popCard(card: Node){
        const pos = card.position;
        let y = pos.y != 0 ? 0 : pos.y + this.pop;
        card.setPosition(pos.x, y, pos.z);
        card.getComponent(CardLoader).getCard().send = y != 0;
    }

    public reset(){
        const children = this.node.children;
        for(let i = 0; i < children.length; i++){
            const cardLoader = children[i].getComponent(CardLoader)
            if(cardLoader.getCard().send){
                children[i].setPosition(children[i].position.x, 0, children[i].position.z);
                cardLoader.getCard().send = false;
            }
        }
        
    }
}

