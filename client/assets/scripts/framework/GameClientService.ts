import { _decorator, Component, Node, Prefab, resources, Scene, SceneAsset } from 'cc';
import { GameClientNet } from '../net/GameClientNet';
import { MessageFactory } from '../proto/MessageFactory';
import { GameManager } from './GameManager';
import { resourceLoad } from '../api/home';
const { ccclass } = _decorator;

@ccclass('GameClientService')
export class GameClientService extends Component {

    start() {
        resourceLoad();
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
                handler.handler(data.protocol, GameManager.getInstance());
            }
        }

        GameClientNet.getConnection().onclose = () => {
            alert("服务器断开");
        }
    }
}


