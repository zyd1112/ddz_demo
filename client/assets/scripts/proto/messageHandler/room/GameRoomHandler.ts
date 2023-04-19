import { Gloabal } from "../../../Global";
import { Card, CardLoader } from "../../../card/CardLoader";
import { CardManager } from "../../../card/CardManager";
import { CharacterType, Role } from "../../../constant/CharacterType";
import { GameManager } from "../../../framework/GameManager";
import { MessageHander } from "../../MessageHanderl";

interface ResPlayerCardMessage{
    opcode: number;
    cardsMap: {[characterType: number]: Card[]};
    type: number;
    removeCards: Card[]
    uid: number;
}
export class PlayerCardHandler extends MessageHander{

    handler(message: ResPlayerCardMessage, gameManager: GameManager): void {
        console.log(message.cardsMap);
        const cardNodes = gameManager.cardNodes;
        if(message.type == 0){
            for(let i = 0; i < cardNodes.length; i++){
                const cardManager = cardNodes[i].getComponent(CardManager)
                cardManager.init(message.cardsMap[Gloabal.charactorType[cardManager.uid]]);
            }
        }else{
            const gabageCardManager = gameManager.gabage.getComponent(CardManager)
            const remove = message.removeCards;
            gameManager.gabage.removeAllChildren();
            if(remove.length > 0){
                let offset = gabageCardManager.offset_x;
                let lx = -(Math.floor(remove.length / 2) * offset);
                for(let i = 0; i < remove.length; i++){
                    gabageCardManager.addCard(lx, 0, remove[i]);
                    lx += offset;
                }
            }
            for(let i = 0; i < cardNodes.length; i++){
                const cardManager = cardNodes[i].getComponent(CardManager)
                if(cardManager.uid == message.uid){
                    cardNodes[i].removeAllChildren();
                    cardManager.init(message.cardsMap[Gloabal.charactorType[cardManager.uid]]);
                }
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
        for(let i = 0; i < gameManager.cardNodes.length; i++){
            if(gameManager.cardNodes[i].getComponent(CardManager).role == Role.SELF){
                cardSlef = gameManager.cardNodes[i];
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
}
export class PlayerCharacterHandler extends MessageHander{

    handler(message: ResRoomPlayerInfoMessage, gameManager: GameManager): void {
        for(let i = 0; i < message.playerInfos.length; i++){
            const playerInfo = message.playerInfos[i];
            Gloabal.charactorType[playerInfo.uid] = playerInfo.characterType;
            for(let i = 0; i < gameManager.cardNodes.length; i++){
                const cardManager = gameManager.cardNodes[i].getComponent(CardManager)
                if(cardManager.uid == playerInfo.uid){
                    break;
                }
                if(Gloabal.uid == playerInfo.uid && cardManager.role == Role.SELF){
                    cardManager.uid = playerInfo.uid;
                    break;
                }
                if(cardManager.uid == 0 && cardManager.role != Role.SELF){
                    cardManager.uid = playerInfo.uid;
                    break;
                }
            }
        }
        
    }
}