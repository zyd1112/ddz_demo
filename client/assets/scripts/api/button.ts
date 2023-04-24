import { Role } from "../constant/CharacterType";
import { GameManager } from "../framework/GameManager";
import { PlayerManager } from "../framework/PlayerManager";
import { ClockManager } from "../player/ClockManager";

export function updateClockPos(gameManager: GameManager,nextId: number){
    const playerNodes = gameManager.playerNodes;
    for(let i = 0; i < playerNodes.length; i++){
        const playerManager = playerNodes[i].getComponent(PlayerManager)
        const player = playerManager.playerInfo;
        if(player.uid == nextId){
            const pos = playerNodes[i].position;
            gameManager.clock.getComponent(ClockManager).init();
            gameManager.clock.setPosition(pos.x + playerManager.clock_offsetX, pos.y + playerManager.clock_offsetY, pos.z);
        }
    }
}