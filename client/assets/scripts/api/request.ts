import { Gloabal } from "../Global"
import { MessageUtils } from "../net/MessageUtils"
import { Card } from "../player/CardLoader"

export function reqGetMailCode(mail: string){
    let message = {
        opcode: 8,
        mail: mail,
    }
    MessageUtils.send(message.opcode, message);

}

export function reqMailLogin(mail: string, code: string){
    MessageUtils.send(9, {
        mail: mail,
        code: code,
    });
}

export function reqVisitorLogin(){
    MessageUtils.send(11, {});
}

export function reqEnterRoom(){
    let msg = {
        opcode: 10,
        roomType: 1,
        uid: Gloabal.uid
    };
    Gloabal.roomType = msg.roomType
    MessageUtils.send(msg.opcode, msg);
}

export function reqSendCard(cards: Card[]){
    let message = {
        opcode: 12,
        uid: Gloabal.uid,
        roomType: Gloabal.roomType,
        cardList: cards
    }
    MessageUtils.send(message.opcode, message)
}

export function reqSuggest(uid: number){
    let message = {
        opcode: 13,
        uid: uid,
        roomType: Gloabal.roomType,
    }
    MessageUtils.send(message.opcode, message)
}

export function reqCountdown(){
    MessageUtils.send(14, {
        uid: Gloabal.uid,
        roomType: Gloabal.roomType
    });        
}

export function reqNoSend(uid: number){
    let message = {
        opcode: 15,
        uid: uid,
        roomType: Gloabal.roomType,
    }
    MessageUtils.send(message.opcode, message)
}

export function reqReady(){
    let message = {
        opcode: 16,
        uid: Gloabal.uid,
        roomType: Gloabal.roomType,
        
    }
    MessageUtils.send(message.opcode, message)
}

export function reqStart(){
    let message = {
        opcode: 17,
        uid: Gloabal.uid,
        roomType: Gloabal.roomType,
    }

    MessageUtils.send(message.opcode, message)
}

export function reqScramble(status: boolean){
    let message = {
        opcode: 18,
        status: status,
        playerDto: {
            uid: Gloabal.uid,
            roomType: Gloabal.roomType
        }
    }
    MessageUtils.send(message.opcode, message);
}

export function reqLeaveRoom(){
    let message = {
        opcode: 19,
        uid: Gloabal.uid,
        roomType: Gloabal.roomType,
    }
    MessageUtils.send(message.opcode, message);

}

