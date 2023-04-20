import { Gloabal } from "../../../Global";
import { CharacterType, Role } from "../../../constant/CharacterType";
import { GameManager } from "../../../framework/GameManager";
import { PlayerManager } from "../../../framework/PlayerManager";
import { Card, CardLoader } from "../../../player/CardLoader";
import { CardManager } from "../../../player/CardManager";
import { ClockManager } from "../../../player/ClockManager";
import { MessageHander } from "../../MessageHanderl";

interface ResPlayerCardMessage{
    opcode: number;
    cardsMap: {[characterType: number]: Card[]};
    type: number;
    removeCards: Card[]
    uid: number;
    nextId: number;
}
export class PlayerCardHandler extends MessageHander{

    handler(message: ResPlayerCardMessage, gameManager: GameManager): void {
        console.log(message.cardsMap);
        const playerNodes = gameManager.playerNodes;
        if(message.type == 0){
            for(let i = 0; i < playerNodes.length; i++){
                const playerManager = playerNodes[i].getComponent(PlayerManager)
                playerManager.mark.destroy();
                if(playerManager.role == Role.SELF){
                    playerManager.startBtn.destroy();
                    playerManager.readyBtn.destroy();
                }
                playerManager.initCards(message.cardsMap[playerManager.uid]);
            }
        }else{
            const gabageCardManager = gameManager.gabage.getComponent(CardManager)
            const remove = message.removeCards;
            gameManager.gabage.removeAllChildren();
            if(remove.length > 0){
                let offset = gabageCardManager.offset_x;
                let lx = -(Math.floor(remove.length / 2) * offset);
                for(let i = 0; i < remove.length; i++){
                    gabageCardManager.addCard(lx, 0, remove[i], Role.SELF);
                    lx += offset;
                }
            }
            for(let i = 0; i < playerNodes.length; i++){
                const playerManager = playerNodes[i].getComponent(PlayerManager)
                if(playerManager.uid == message.uid){
                    playerManager.cardNode.removeAllChildren();
                    playerManager.initCards(message.cardsMap[playerManager.characterType]);
                }
            } 
        }
        for(let i = 0; i < playerNodes.length; i++){
            const playerManager = playerNodes[i].getComponent(PlayerManager)
            if(playerManager.role == Role.SELF){
                playerManager.button.active = playerManager.uid == message.nextId;
            }
            if(playerManager.uid == message.nextId){
                playerManager.startClock();
            }
            if(playerManager.uid == message.uid){
                playerManager.closeClock();
            }
        }
    }
    
}


interface ResPlayerSuggestMessage{
    opcode: number;
    availableCards: Card[];
}
export class PlayerSuggestHandler extends MessageHander{
    
    handler(message: ResPlayerSuggestMessage, gameManager: GameManager): void {
        const cards = message.availableCards;
        let cardSlef = null;
        for(let i = 0; i < gameManager.playerNodes.length; i++){
            const playerManager = gameManager.playerNodes[i].getComponent(PlayerManager);
            if(playerManager.role == Role.SELF){
                cardSlef = playerManager.cardNode;
            }
        }
        const cardManager = cardSlef.getComponent(CardManager);
        cardManager.reset()
        for(let i = 0; i < cards.length; i++){
            console.log(cards[i]);
            const children = cardSlef.children
            for(let j = 0; j < children.length; j ++){
                const cardLoader = children[j].getComponent(CardLoader)
                if(cards[i].content == cardLoader.getCard().content){
                    cardManager.popCard(children[j]);
                }
            }
        }
    }
}

interface ResRoomPlayerInfoMessage{
    opcode: number;
    playerInfos: PlayerInfo[]
}

interface PlayerInfo{
    uid: number;

    roomType: number;

    characterType: number;

    roomHost: boolean

    ready: boolean;
}
export class PlayerEnterRoomHandler extends MessageHander{

    handler(message: ResRoomPlayerInfoMessage, gameManager: GameManager): void {
        for(let i = 0; i < message.playerInfos.length; i++){
            const playerInfo = message.playerInfos[i];
            for(let i = 0; i < gameManager.playerNodes.length; i++){
                const playerManager = gameManager.playerNodes[i].getComponent(PlayerManager)
                if(playerManager.uid == playerInfo.uid){
                    break;
                }
                if(Gloabal.uid == playerInfo.uid && playerManager.role == Role.SELF){
                    playerManager.uid = playerInfo.uid;
                    playerManager.roomHost = playerInfo.roomHost;
                    playerManager.initImage(gameManager.images[playerInfo.characterType - 1])
                    this.initBtn(playerManager);
                    break;
                }
                if(playerManager.uid == 0 && playerManager.role != Role.SELF){
                    playerManager.uid = playerInfo.uid;
                    playerManager.initImage(gameManager.images[playerInfo.characterType - 1])
                    break;
                }
            }
        }
    }
    initBtn(playerManager: PlayerManager){
        if(playerManager.roomHost){
            playerManager.startBtn.active = true;
        }else{
            playerManager.readyBtn.active = true;
        }
    }
}

interface ResRoomTimeMessage{
    time: number;
}
export class CountDownHandler extends MessageHander{

    handler(message: ResRoomTimeMessage, gameManager: GameManager): void {
        const playerNodes = gameManager.playerNodes;
        for(let i = 0; i < playerNodes.length; i++){
            const playerManager = playerNodes[i].getComponent(PlayerManager);
            if(playerManager.clock.active){
                playerManager.clock.getComponent(ClockManager).updateTime(message.time, playerManager.uid)
            }
        }
    }
}

interface ResPlayerReadyMessage{
    opcode: number;
    playerDtoList: PlayerInfo[]
}
export class PlayerReadyHandler extends MessageHander{

    handler(message: ResPlayerReadyMessage, gameManager: GameManager): void {
        const playerNodes = gameManager.playerNodes;
        for(let i = 0; i < message.playerDtoList.length; i++){
            const playerInfo = message.playerDtoList[i];
            for(let i = 0; i < playerNodes.length; i++){
                const playerManager = playerNodes[i].getComponent(PlayerManager);
                if(playerManager.roomHost){
                    continue;
                }
                if(playerManager.uid == playerInfo.uid){
                    playerManager.mark.active = playerInfo.ready;
                }
            }
        }
    }
}