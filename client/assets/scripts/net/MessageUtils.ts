import { GameClientNet } from "./GameClientNet";

export class MessageUtils{

    public static send(opcode: number, message: Message){
        GameClientNet.getConnection().send(JSON.stringify({
            opcode: opcode,
            message: message,
        }));
    }
}