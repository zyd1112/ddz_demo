import { GameManager } from "../../../framework/GameManager";
import { MessageHander } from "../../MessageHanderl";
import { ResUserMessage } from "../../message/user/ResUserMessage";

export class UserLoginHandler extends MessageHander{
    
    
    handler(message: Message, gameManager: GameManager): void {
        console.log(message);
        gameManager.uid = (message as ResUserMessage).id;
        
    }
    
}