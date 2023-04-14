import { CardManager } from "../../../card/CardManager";
import { GameManager } from "../../../framework/GameManager";
import { MessageHander } from "../../MessageHanderl";
import { ResPlayerCardMessage } from "../../message/room/ResPlayerCardMessage";

export class GameRoomHandler extends MessageHander{

    handler(message: Message, gameManager: GameManager): void {
        let msg = message as ResPlayerCardMessage;
        console.log(msg.cardList);
        gameManager.cardSlef.getComponent(CardManager).init(msg.cardList);
        gameManager.cardSide_1.getComponent(CardManager).init(msg.cardList);
        gameManager.cardSide_2.getComponent(CardManager).init(msg.cardList);
    }
    
}