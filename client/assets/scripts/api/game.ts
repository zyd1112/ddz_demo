import { GameManager } from "../framework/GameManager";
import { PlayerManager } from "../framework/PlayerManager";
import { Node } from 'cc';
import { PlayerInfo } from "../proto/messageHandler/room/GameRoomHandler";


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