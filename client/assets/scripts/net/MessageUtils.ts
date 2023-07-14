import { GameClientNet } from "./GameClientNet";

export class MessageUtils{

    public static send(opcode: number, message: any){
        if(GameClientNet.getConnection().readyState == 1){
            GameClientNet.getConnection().send(JSON.stringify({
                opcode: opcode,
                message: message,
            }));
        }
    }
}