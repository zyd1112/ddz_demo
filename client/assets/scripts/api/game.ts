import { GameManager } from "../framework/GameManager";
import { PlayerManager } from "../framework/PlayerManager";
import { Node, Prefab, director, resources } from 'cc';
import { PlayerInfo } from "../proto/messageHandler/room/GameRoomHandler";
import { Path } from "../constant/path";

export function resource_load(url:string,type:any,callback?: { (value: any): void; (value: any): void; (arg0: any): void; }): Promise<any> {
    return new Promise((resolve, reject) => {
        resources.load(url, type,(err, res:any) => {
            if (err) {
                reject(err);
            } else {
                if(callback){
                    callback(res);
                }
                resolve(res);
            }
        });
    })
}

export function resourceInit(gameManager: GameManager){
    const canvas = director.getScene().getChildByName("Canvas")
    console.log(canvas);
    for(let i = 0; i < canvas.children.length; i++){
        const node = canvas.children[i];
        switch(node.name){
            case "clock": 
                gameManager.clock = node;
                break;
            case "countDown":
                gameManager.countDown = node;
                break;
            case "scrambleBtn":
                gameManager.scrambleBtn = node;
                break;
            case "card":
                for(let i = 0; i < node.children.length; i++){
                    const cardNode = node.children[i];
                    if(cardNode.name == "gabage"){
                        gameManager.gabage = cardNode;
                    }else{
                        gameManager.playerNodes.push(cardNode);
                    }
                }
                break;
        }
    }
    
}

export function getNextPlayer(playerNodes: Node[], uid: number): PlayerInfo{
    playerNodes.sort((a, b) => {
        return a.getComponent(PlayerManager).playerInfo.enterTime - b.getComponent(PlayerManager).playerInfo.enterTime;
    })
    let index = 0;
    for(let i = 0; i < playerNodes.length; i++){
        const player = playerNodes[i].getComponent(PlayerManager).playerInfo;
        if(uid == player.uid){
            index = i;
            break;
        }
    }
    return index + 1 < playerNodes.length ? playerNodes[index + 1].getComponent(PlayerManager).playerInfo : playerNodes[0].getComponent(PlayerManager).playerInfo;
}