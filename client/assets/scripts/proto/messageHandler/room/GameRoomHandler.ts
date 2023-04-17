import { Gloabal } from "../../../Global";
import { Card } from "../../../card/CardLoader";
import { CardManager } from "../../../card/CardManager";
import { GameManager } from "../../../framework/GameManager";
import { PoolManager } from "../../../framework/PoolManager";
import { MessageHander } from "../../MessageHanderl";

interface ResPlayerCardMessage{
    opcode: number;
    cardsMap: {[characterType: number]: Card[]};
    type: number;
    removeCards: Card[]
}
export class GameRoomHandler extends MessageHander{

    handler(message: ResPlayerCardMessage, gameManager: GameManager): void {
        console.log(message.cardsMap);
        let cards = message.cardsMap[Gloabal.charactorType]
        if(message.type == 0){
            gameManager.cardSlef.getComponent(CardManager).init(cards);
            // gameManager.cardSide_1.getComponent(CardManager).init(msg.cardsMap[2]);
            // gameManager.cardSide_2.getComponent(CardManager).init(msg.cardsMap[2]);
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