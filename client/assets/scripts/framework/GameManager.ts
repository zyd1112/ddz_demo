import { _decorator, Button, Component, director, Node, NodeEventType, Prefab, Scene, SceneAsset, SystemEvent } from 'cc';
import { GameClientNet } from '../net/GameClientNet';
import { MessageFactory } from '../proto/MessageFactory';
import { MessageUtils } from '../net/MessageUtils';
import { Gloabal } from '../Global';
const { ccclass, property } = _decorator;


@ccclass('GameManager')
export class GameManager extends Component {

    playerNodes: Node[] = [];

    gabage: Node = null;

    clock: Node = null;

    headImages: Prefab[] = [];

    countDown: Node = null;

    roomHostImage: Prefab = null;

    landowner: Prefab = null;

    scrambleBtn: Node = null;

    rewardTable: Node = null;

    public nextId: number = 0;

    public gameStart: boolean = false;

    public multiple: number = 15;

    private static INSTANCE: GameManager = new GameManager();

    private GameManager(){}

    public static getInstance(): GameManager{
        return this.INSTANCE
    }
    
    
}


