import { GameManager } from "../framework/GameManager";

export abstract class MessageHander{

    abstract handler(message: Message, gameManager: GameManager) : void;
}