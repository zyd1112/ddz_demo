import { GameManager } from "../framework/GameManager";
import { PlayerManager } from "../framework/PlayerManager";
import { Node, Prefab, director, resources } from 'cc';
import { PlayerInfo } from "../proto/messageHandler/room/GameRoomHandler";
import { Path } from "../constant/path";
import { Gloabal } from "../Global";
import { Role } from "../constant/CharacterType";
import { CardManager } from "../player/CardManager";
import { Card } from "../player/CardLoader";

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
    gameManager.playerNodes = [];
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
            case "rewardTable":
                gameManager.rewardTable = node;
                break;
            case "multiple":
                gameManager.multipleNode = node;
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

export function initBtn(playerManager: PlayerManager){
    if(playerManager.startBtn != null){
        playerManager.startBtn.active = playerManager.playerInfo.roomHost;
    }
    if(playerManager.startBtn != null){
        playerManager.readyBtn.active = !playerManager.playerInfo.roomHost;
    }
}

export function initPlayer(playerNode: Node, playerInfo: PlayerInfo, gameStart: boolean){
    const playerManager = playerNode.getComponent(PlayerManager)
    const player = playerManager.playerInfo;
    const gameManager = GameManager.getInstance();
    player.uid = playerInfo.uid;
    player.roomHost = playerInfo.roomHost;
    player.name = playerInfo.name;
    player.auto = playerInfo.auto
    playerManager.initImage(gameManager.headImages[playerInfo.imageIndex])
    player.enterTime = playerInfo.enterTime;
    player.joyBeans = playerInfo.joyBeans;
    if(player.roomHost){
        playerManager.initRoomHost(gameManager.roomHostImage);
    }else if(playerManager.role != Role.SELF && !gameStart){
        playerManager.mark.active = playerInfo.ready;
    }
    if(playerManager.role == Role.SELF && !gameStart){
        initBtn(playerManager);
    }
    playerManager.initCards(playerInfo.cards)
    playerNode.getChildByName("offline").active = playerManager.playerInfo.auto
}

export function initGarbage(gameManager: GameManager, cards: Card[]){
    const gabageCardManager = gameManager.gabage.getComponent(CardManager)
    let offset = gabageCardManager.offset_x;
    let lx = -(Math.floor(cards.length / 2) * offset);
    for(let i = 0; i < cards.length; i++){
        gabageCardManager.addCard(lx, 0, cards[i], Role.SELF);
        lx += offset;
    }
}