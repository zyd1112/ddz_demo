import { GameClientNet } from "./GameClientNet";

export class MessageUtils{

    public static send(opcode: number, message: any){
        GameClientNet.getConnection().send(JSON.stringify({
            opcode: opcode,
            message: message,
        }));
    }
}