import { _decorator, Component, Node, Prefab } from 'cc';
import { PoolManager } from './PoolManager';
import { Card } from '../player/CardLoader';
import { CardManager } from '../player/CardManager';
import { Gloabal } from '../Global';
import { Role } from '../constant/CharacterType';
import { PlayerInfo } from '../proto/messageHandler/room/GameRoomHandler';
const { ccclass, property } = _decorator;

@ccclass('PlayerManager')
export class PlayerManager extends Component {
    @property(Node)
    public cardNode: Node = null;
    @property(Node)
    public sendBtn: Node = null;
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
    public clock_offsetX = 0;
    @property
    public clock_offsetY = 0;

    @property
    public roomHost_offsetX = 0;
    @property
    public roomHost_offsetY = 0;

    @property
    public landowner_offsetX = 0;
    @property
    public landowner_offsetY = 0;

    
    @property
    public role = 1;

    public playerInfo: PlayerInfo = {
        uid: 0,

        name: "",

        roomType: 0,

        characterType: 0,

        roomHost: false,

        ready: false,

        enterTime: 0,
        
        imageIndex: 0,

        joyBeans: 0,

        auto: false,

        cards: [],
    }

    public image: Node = null;
    public roomHostImage: Node = null;
    public landowner: Node = null;

    initImage(image: Prefab){
        const i = PoolManager.getInstance().getNode(image, this.node);
        i.setPosition(this.image_offsetX, this.image_offsetY, 0);
        this.image = i;
    }

    initRoomHost(roomHostImage: Prefab){
        const image = PoolManager.getInstance().getNode(roomHostImage, this.node);
        image.setPosition(this.roomHost_offsetX, this.roomHost_offsetY, 0);
        this.roomHostImage = image;
    }

    clearImage(image: Node){
        PoolManager.getInstance().putNode(image);
    }

    initCards(cards: Card[]){
        this.cardNode.removeAllChildren()
        this.cardNode.getComponent(CardManager).init(cards, this.role);
    }

    initLandowner(landowner: Prefab){
        const image = PoolManager.getInstance().getNode(landowner, this.node);
        image.setPosition(this.landowner_offsetX, this.landowner_offsetY, 0);
        this.landowner = image;
    }

    clear(){
        this.playerInfo = {
            uid: 0,

            name: "",
    
            roomType: 0,
    
            characterType: 0,
    
            roomHost: false,
    
            ready: false,
    
            enterTime: 0,
            
            imageIndex: 0,
    
            joyBeans: 0,

            auto: false,

            cards: [],
        };
        this.mark.active = false;
        if(this.readyBtn != null){
            this.readyBtn.active = false;
        }
        if(this.startBtn != null){
            this.startBtn.active = false;
        }
        if(this.sendBtn != null){
            this.sendBtn.active = false;
        }
        if(this.button != null){
            this.button.active = false;
        }
        this.clearImage(this.image);
        if(this.roomHostImage != null){
            this.clearImage(this.roomHostImage)
        }
    }
}


