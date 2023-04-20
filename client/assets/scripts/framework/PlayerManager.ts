import { _decorator, Component, Node, Prefab } from 'cc';
import { PoolManager } from './PoolManager';
import { Card } from '../player/CardLoader';
import { CardManager } from '../player/CardManager';
import { Gloabal } from '../Global';
import { Role } from '../constant/CharacterType';
const { ccclass, property } = _decorator;

@ccclass('PlayerManager')
export class PlayerManager extends Component {
    @property(Node)
    public cardNode: Node = null;

    @property(Node)
    public clock: Node = null;

    @property(Node)
    public button: Node = null;

    @property(Node)
    public readyBtn: Node = null;
    @property(Node)
    public startBtn: Node = null;

    @property(Node)
    public mark: Node = null;

    @property
    public image_offsetX = 0;
    @property
    public image_offsetY = 0;

    @property
    public role = 1;

    public uid: number = 0;

    public characterType: number = 0;

    public roomHost = false;

    initImage(image: Prefab){
        PoolManager.getInstance().getNode(image, this.node).setPosition(this.image_offsetX, this.image_offsetY, 0);
    }

    clearImage(image: Node){
        PoolManager.getInstance().putNode(image);
    }

    initCards(cards: Card[]){
        this.cardNode.getComponent(CardManager).init(cards, this.role);
    }

    startClock(){
        this.clock.active = true;
    }

    closeClock(){
        this.clock.active = false;
    }
}


