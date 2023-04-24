import { Label, game, random } from "cc";
import { Gloabal } from "../../../Global";
import { CharacterType, Role } from "../../../constant/CharacterType";
import { GameManager } from "../../../framework/GameManager";
import { PlayerManager } from "../../../framework/PlayerManager";
import { Card, CardLoader } from "../../../player/CardLoader";
import { CardManager } from "../../../player/CardManager";
import { ClockManager } from "../../../player/ClockManager";
import { MessageHander } from "../../MessageHanderl";
import { updateClockPos } from "../../../api/button";
import { getNextPlayer } from "../../../api/game";


 export interface PlayerInfo{
    uid: number;

    roomType: number;

    characterType: number;

    roomHost: boolean

    ready: boolean;

    enterTime: number;
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
        gameManager.multiple = message.multiple;
        if(message.success){
            gameManager.scrambleBtn.active = false;
            for(let i = 0; i < playerNodes.length; i++){
                const playerManager = playerNodes[i].getComponent(PlayerManager)
                const player = playerManager.playerInfo;
                playerManager.scrambleText.active = false;
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
                const text = playerManager.scrambleText
                if(playerManager.role == Role.SELF){
                    gameManager.scrambleBtn.active = nextId == Gloabal.uid;
                }
                if(player.uid == message.uid){
                    text.getComponent(Label).string = message.status ? "抢" : "不抢";
                    text.active = true;
                }
            }
        }
        updateClockPos(gameManager, nextId);
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
        gameManager.clock.active = true;
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
            updateClockPos(gameManager, message.firstId);
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
            updateClockPos(gameManager, nextId);
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
                const headImage = gameManager.headImages[playerInfo.roomHost ? 0 : 1];
                const player = playerManager.playerInfo;
                if(player.uid == playerInfo.uid){
                    break;
                }
                
                if(Gloabal.uid == playerInfo.uid && playerManager.role == Role.SELF){
                    player.uid = playerInfo.uid;
                    player.roomHost = playerInfo.roomHost;
                    playerManager.initImage(headImage)
                    player.enterTime = playerInfo.enterTime;
                    if(player.roomHost){
                        playerManager.initRoomHost(gameManager.roomHostImage);
                    }
                    initBtn(playerManager);
                    break;
                }
                if(player.uid == 0 && playerManager.role != Role.SELF){
                    player.uid = playerInfo.uid;
                    playerManager.initImage(headImage)
                    player.roomHost = playerInfo.roomHost;
                    player.enterTime = playerInfo.enterTime;
                    if(player.roomHost){
                        playerManager.initRoomHost(gameManager.roomHostImage);
                    }else{
                        playerManager.mark.active = playerInfo.ready;
                    }
                    
                    break;
                }
            }
        }
    }
    
}

function initBtn(playerManager: PlayerManager){
    if(playerManager.startBtn != null){
        playerManager.startBtn.active = playerManager.playerInfo.roomHost;
    }
    if(playerManager.startBtn != null){
        playerManager.readyBtn.active = !playerManager.playerInfo.roomHost;
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
    uid: number;
    playerList: PlayerInfo[];
}


export class PlayerLeaveRoomHandler extends MessageHander{

    handler(message: ResPlayerLeaveRoomMessage, gameManager: GameManager): void {
        const playerNodes = gameManager.playerNodes;
        gameManager.clock.active = false;
        for(let i = 0; i < playerNodes.length; i++){
            const playerManager = playerNodes[i].getComponent(PlayerManager);
            const player = playerManager.playerInfo
            if(player.uid == message.uid){
                playerManager.clear();
            }
            for(let j = 0; j < message.playerList.length; j++){
                const playerInfo = message.playerList[j];
                if(playerInfo.uid == player.uid){
                    player.roomHost = playerInfo.roomHost;
                    if(playerManager.role == Role.SELF){
                        initBtn(playerManager);
                    }
                    if(player.roomHost){
                        playerManager.initRoomHost(gameManager.roomHostImage);
                    }else{
                        playerManager.mark.active = playerInfo.ready;
                    }
                }
            }
        }
    }
}
