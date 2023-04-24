import { _decorator, Button, Component, Node, NodeEventType, Prefab, Scene, SceneAsset, SystemEvent } from 'cc';
import { GameClientNet } from '../net/GameClientNet';
import { MessageFactory } from '../proto/MessageFactory';
import { MessageUtils } from '../net/MessageUtils';
import { Gloabal } from '../Global';
const { ccclass, property } = _decorator;


@ccclass('GameManager')
export class GameManager extends Component {

    @property(Node)
    playerNodes: Node[] = [];

    @property(Node)
    playerSelf: Node = null;

    @property(Node)
    gabage: Node = null;

    @property(Node)
    clock: Node = null;

    @property(Prefab)
    headImages: Prefab[] = [];

    @property(Node)
    countDown: Node = null;

    @property(Prefab)
    roomHostImage: Prefab = null;

    @property(Prefab)
    landowner: Prefab = null;

    @property(Node)
    scrambleBtn: Node = null;

    @property(SceneAsset)
    gameScene: SceneAsset = null;

    @property(SceneAsset)
    homeScene: SceneAsset = null;

    public nextId: number = 0;

    public gameStart: boolean = false;

    public multiple: number = 15;

    start() {
        
        GameClientNet.startClient("10.40.4.208", 10001);
        GameClientNet.getConnection().onopen = () => {
            console.log("已连接")
        }
        GameClientNet.getConnection().onmessage = (event) => {
            let data = event.data;
            try{
                data = JSON.parse(data);
            }catch {
                let json = data.replace(/(\w+):/g, "\"$1\":");
                data = JSON.parse(json);
            }
            
            let handler = MessageFactory.getHandler(data.opcode)
            if(handler){
                handler.handler(data.protocol, this);
            }
        }

        GameClientNet.getConnection().onclose = () => {
            alert("服务器断开");
        }

    }

    
    
}


