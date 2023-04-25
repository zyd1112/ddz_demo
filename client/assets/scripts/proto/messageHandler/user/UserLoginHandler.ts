import { Gloabal } from "../../../Global";
import { userInit } from "../../../api/home";
import { ResponseCode } from "../../../constant/ResponseCode";
import { GameManager } from "../../../framework/GameManager";
import { MessageHander } from "../../MessageHanderl";

interface ResUserMessage{
    opcode: number;
    code: number;
    user: {
        id: number;

        username: string;

        passwords: number;

        nickname: number;

        joyBeans: number;

        imageIndex: number;
    }
}
export class UserLoginHandler extends MessageHander{
    
    
    handler(message: ResUserMessage, gameManager: GameManager): void {
        console.log(message);
        if(message.code == ResponseCode.SUCCESS){
            Gloabal.uid = message.user.id;
            Gloabal.username = message.user.username;
            Gloabal.joyBeans = message.user.joyBeans;
            Gloabal.image = gameManager.headImages[message.user.imageIndex];
            userInit();
        }else{
            alert("登录失败")
        }
    }
    
}