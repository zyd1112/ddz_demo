import { Label, director, game, random } from "cc";
import { Gloabal } from "../../../Global";
import { CharacterType, Role } from "../../../constant/CharacterType";
import { GameManager } from "../../../framework/GameManager";
import { PlayerManager } from "../../../framework/PlayerManager";
import { Card, CardLoader } from "../../../player/CardLoader";
import { CardManager } from "../../../player/CardManager";
import { ClockManager } from "../../../player/ClockManager";
import { MessageHander } from "../../MessageHanderl";
import { updateClockPos } from "../../../api/button";
import { getNextPlayer, initBtn, initGarbage, initPlayer } from "../../../api/game";


 export interface PlayerInfo{
    uid: number;

    name: string;

    roomType: number;

    characterType: number;

    roomHost: boolean

    ready: boolean;

    enterTime: number;

    imageIndex: number;

    joyBeans: number;

    auto: boolean;

    cards: Card[];
}

interface ResPlayerCharacterMessage{
    opcode: number;
    cardsMap: {[uid: number]: Card[]};
    character: {[uid: number]: number};
    multiple: number;
    uid: number;
    status: boolean;
    success: boolean;
}
export class PlayerCharacterHandler extends MessageHander{
    
    handler(message: ResPlayerCharacterMessage, gameManager: GameManager): void {
        const playerNodes = gameManager.playerNodes;
        let nextId = getNextPlayer(playerNodes, message.uid).uid
        if(message.success){
            gameManager.scrambleBtn.active = false;
            for(let i = 0; i < playerNodes.length; i++){
                const playerManager = playerNodes[i].getComponent(PlayerManager)
                const player = playerManager.playerInfo;
                playerNodes[i].getChildByName("scramble").active = false;
                playerNodes[i].getChildByName("noScramble").active = false;
                player.characterType = message.character[playerManager.playerInfo.uid];
                if(player.characterType == CharacterType.LANDOWNER){
                    nextId = player.uid;
                    playerManager.initLandowner(gameManager.landowner);
                }
                if(playerManager.role == Role.SELF){
                    playerManager.sendBtn.active = player.characterType == CharacterType.LANDOWNER;
                }
                playerManager.initCards(message.cardsMap[playerManager.playerInfo.uid]);
            }
        }else{
            for(let i = 0; i < playerNodes.length; i++){
                const playerManager = playerNodes[i].getComponent(PlayerManager)
                const player = playerManager.playerInfo;
                if(playerManager.role == Role.SELF){
                    gameManager.scrambleBtn.active = nextId == Gloabal.uid;
                }
                if(player.uid == message.uid){
                    playerNodes[i].getChildByName("scramble").active = message.status;
                    playerNodes[i].getChildByName("noScramble").active = !message.status;
                }
            }
        }
        gameManager.multipleNode.getChildByName("value").getComponent(Label).string = message.multiple + " %"
        updateClockPos(nextId);
    }
    
}

interface ResPlayerCardMessage{
    opcode: number;
    cardsMap: {[uid: number]: Card[]};
    type: number;
    removeCards: Card[]
    uid: number;
    firstId: number;
}
export class PlayerCardHandler extends MessageHander{

    handler(message: ResPlayerCardMessage, gameManager: GameManager): void {
        console.log(message.cardsMap);
        const playerNodes = gameManager.playerNodes;
        gameManager.countDown.active = false;
        const nextId = getNextPlayer(playerNodes, message.uid).uid;
        gameManager.nextId = nextId;
        if(message.type == 0){
            for(let i = 0; i < playerNodes.length; i++){
                const playerManager = playerNodes[i].getComponent(PlayerManager)
                playerManager.mark.destroy();
                if(playerManager.role == Role.SELF){
                    playerManager.startBtn.destroy();
                    playerManager.readyBtn.destroy();
    
                    gameManager.scrambleBtn.active = playerManager.playerInfo.roomHost;
                }
                playerManager.initCards(message.cardsMap[playerManager.playerInfo.uid]);
            }
            updateClockPos(message.firstId);
        }else{
            const remove = message.removeCards;
            gameManager.gabage.removeAllChildren();
            if(remove.length > 0){
                initGarbage(gameManager, remove);
            }
            for(let i = 0; i < playerNodes.length; i++){
                const playerManager = playerNodes[i].getComponent(PlayerManager)
                if(playerManager.playerInfo.uid == message.uid){
                    playerManager.initCards(message.cardsMap[playerManager.playerInfo.uid]);
                    playerManager.cardNode.getComponent(CardManager).reset();
                }
            }
            for(let i = 0; i < playerNodes.length; i++){
                const playerManager = playerNodes[i].getComponent(PlayerManager)
                const player = playerManager.playerInfo;
                if(playerManager.role == Role.SELF){
                    playerManager.sendBtn.active = player.uid == nextId;
                    playerManager.button.active = player.uid == nextId && !(message.firstId == player.uid);
                }
            }
            updateClockPos(nextId);
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

export class PlayerEnterRoomHandler extends MessageHander{

    handler(message: ResRoomPlayerInfoMessage, gameManager: GameManager): void {
        for(let i = 0; i < message.playerInfos.length; i++){
            const playerInfo = message.playerInfos[i];
            for(let i = 0; i < gameManager.playerNodes.length; i++){
                const playerManager = gameManager.playerNodes[i].getComponent(PlayerManager)
                const player = playerManager.playerInfo;
                if(player.uid == playerInfo.uid && !playerInfo.auto){
                    break;
                }
                if(Gloabal.uid == playerInfo.uid && playerManager.role == Role.SELF){
                    initPlayer(gameManager.playerNodes[i], playerInfo, false)
                    break;
                }
                if(player.uid == 0 && playerManager.role != Role.SELF){
                    initPlayer(gameManager.playerNodes[i], playerInfo, false)
                    break;
                }
            }
        }
    }
    
}



interface ResRoomTimeHeartMessage{
    opcode: number;
    time: number;
}
export class CountDownHandler extends MessageHander{

    handler(message: ResRoomTimeHeartMessage, gameManager: GameManager): void {
        gameManager.clock.getComponent(ClockManager).updateTime(message.time)
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
                if(playerManager.playerInfo.roomHost){
                    continue;
                }
                if(playerManager.playerInfo.uid == playerInfo.uid){
                    playerManager.mark.active = playerInfo.ready;
                }
            }
        }
    }
}

interface ResRoomReadyTimeMessage{
    opcode: number;
    countDown: number;
    start: boolean;
}

export class RoomReadyCountDownHandler extends MessageHander{

    handler(message: ResRoomReadyTimeMessage, gameManager: GameManager): void {
        const countDown = gameManager.countDown;
        gameManager.countDown.active = message.start;
        
        const time = message.countDown
        countDown.getComponent(Label).string = time + "";
    }
}

interface ResPlayerLeaveRoomMessage{
    opcode: number;
    playerMap: {[uid: number]: PlayerInfo};
}


export class PlayerLeaveRoomHandler extends MessageHander{

    handler(message: ResPlayerLeaveRoomMessage, gameManager: GameManager): void {
        const playerNodes = gameManager.playerNodes;
        for(let i = 0; i < playerNodes.length; i++){
            const playerManager = playerNodes[i].getComponent(PlayerManager);
            const player = playerManager.playerInfo
            if(player.uid == 0){
                continue;
            }
            const playerInfo = message.playerMap[player.uid];
            if(playerInfo == undefined){
                playerManager.clear();
            }else{
                player.roomHost = playerInfo.roomHost;
                playerNodes[i].getChildByName("offline").active = playerInfo.auto;
                
                if(player.roomHost){
                    playerManager.initRoomHost(gameManager.roomHostImage);
                }
            }

        }
    }
}

interface ResGameOverRewardMessage{
    opcode: number;
    playerRewards: {[uid: number]: number};
}

export class GameOverRewardsHandler extends MessageHander{

    handler(message: ResGameOverRewardMessage, gameManager: GameManager): void {
        const playerNodes = gameManager.playerNodes;
        for(let i = 0; i < playerNodes.length; i++){
            const playerManager = playerNodes[i].getComponent(PlayerManager);
            const player = playerManager.playerInfo
            const node = gameManager.rewardTable.getChildByName("player_" + (i + 1));
            node.getChildByName("name").getComponent(Label).string = player.name;
            node.getChildByName("value").getComponent(Label).string = message.playerRewards[player.uid] + "";
            Gloabal.joyBeans = Math.max(0, Gloabal.joyBeans + message.playerRewards[Gloabal.uid]);
            if(playerManager.role == Role.SELF){
                playerManager.sendBtn.active = false;
                playerManager.button.active = false;
            }
            initBtn(playerManager);
        }
        gameManager.clock.active = false;
        gameManager.rewardTable.active = true;
        setTimeout(() => {
            for(let i = 0; i < playerNodes.length; i++){
                playerNodes[i].getComponent(PlayerManager).clearCards();
            }
        }, 3000)
    }
}

interface ResPlayerReconnectMessage{
    opcode: number;
    nextId: number;
    firstId: number;
    multiple: number;
    garbageList: Card[];
    playerInfos: PlayerInfo[];
}

export class PlayerReconnectHandler extends MessageHander{

    handler(message: ResPlayerReconnectMessage, gameManager: GameManager): void {
        initGarbage(gameManager, message.garbageList);
        gameManager.multipleNode.getChildByName("value").getComponent(Label).string = message.multiple + " %";
        for(let i = 0; i < message.playerInfos.length; i++){
            const playerInfo = message.playerInfos[i];
            for(let i = 0; i < gameManager.playerNodes.length; i++){
                const playerManager = gameManager.playerNodes[i].getComponent(PlayerManager)
                const player = playerManager.playerInfo;
                if(player.uid == playerInfo.uid && !playerInfo.auto){
                    gameManager.playerNodes[i].getChildByName("offline").active = false
                    break;
                }
                if(Gloabal.uid == playerInfo.uid && playerManager.role == Role.SELF){
                    initPlayer(gameManager.playerNodes[i], playerInfo, true)
                    playerManager.sendBtn.active = player.uid == message.nextId;
                    playerManager.button.active = player.uid == message.nextId && !(message.firstId == player.uid);
                    break;
                }
                if(player.uid == 0 && playerManager.role != Role.SELF){
                    initPlayer(gameManager.playerNodes[i], playerInfo, true)
                     break;
                }
            }
        }
        updateClockPos(message.nextId);
    }

}
