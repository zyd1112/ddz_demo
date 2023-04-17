import { Gloabal } from "../../../Global";
import { GameManager } from "../../../framework/GameManager";
import { MessageHander } from "../../MessageHanderl";

interface ResUserMessage{
    id: number;

    username: string;

    passwords: number;

    nickname: number;

    joyBeans: number;
}
export class UserLoginHandler extends MessageHander{
    
    
    handler(message: ResUserMessage, gameManager: GameManager): void {
        console.log(message);
        Gloabal.uid = (message as ResUserMessage).id;
    }
    
}