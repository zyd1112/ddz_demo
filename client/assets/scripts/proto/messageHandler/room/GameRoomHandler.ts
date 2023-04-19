import { Gloabal } from "../../../Global";
import { Card, CardLoader } from "../../../card/CardLoader";
import { CardManager } from "../../../card/CardManager";
import { GameManager } from "../../../framework/GameManager";
import { MessageHander } from "../../MessageHanderl";

interface ResPlayerCardMessage{
    opcode: number;
    cardsMap: {[characterType: number]: Card[]};
    type: number;
    removeCards: Card[]
}
export class PlayerCardHandler extends MessageHander{

    handler(message: ResPlayerCardMessage, gameManager: GameManager): void {
        console.log(message.cardsMap);
        let cards = message.cardsMap[Gloabal.charactorType[Gloabal.uid]]
        if(message.type == 0){
            gameManager.cardSlef.getComponent(CardManager).init(cards);
            gameManager.cardSide_1.getComponent(CardManager).init(cards);
            gameManager.cardSide_2.getComponent(CardManager).init(cards);
        }else{
            const cardManager = gameManager.cardSlef.getComponent(CardManager);
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
            gameManager.cardSlef.removeAllChildren();
            cardManager.init(cards);
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
        const cardManager = gameManager.cardSlef.getComponent(CardManager);
        cardManager.reset()
        for(let i = 0; i < cards.length; i++){
            console.log(cards[i]);
            const children = gameManager.cardSlef.children
            for(let j = 0; j < children.length; j ++){
                const cardLoader = children[j].getComponent(CardLoader)
                if(cards[i].content == cardLoader.getCard().content){
                    cardManager.popCard(children[j]);
                }
            }
        }
    }
}

interface ResPlayerCharacterMessage{
    opcode: number;
    uid: number;
    characterType: number;
}

export class PlayerCharacterHandler extends MessageHander{

    handler(message: ResPlayerCharacterMessage, gameManager: GameManager): void {
        Gloabal.charactorType[message.uid] = message.characterType;
    }
}