import { _decorator, Component, Node, Prefab, Sprite, SpriteAtlas, SpriteFrame } from 'cc';
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

    private tx = 0;
    private ty = 0;
    start() {
        this.tx = this.x;
        this.ty = this.y;
        for(let i = 0; i < 17; i++){
            const card = PoolManager.getInstance().getNode(this.cardPrefab, this.node);
            this.tx += this.offset_x;
            this.ty += this.offset_y;
            card.setPosition(this.tx, this.ty, 0);
            if(this.role == CharacterType.role.SELF){
                card.getComponent(CardLoader).setCardName("card_" + (i + 1));
            }else{
                card.getComponent(CardLoader).setCardName("card_55");
                
            }
        }
    }

    update(deltaTime: number) {
        
    }
}


