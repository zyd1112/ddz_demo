import { GameManager } from "../framework/GameManager";

export abstract class MessageHander{

    abstract handler(message: any, gameManager: GameManager) : void;
}