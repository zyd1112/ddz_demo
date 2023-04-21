import { Gloabal } from "../Global"
import { MessageUtils } from "../net/MessageUtils"
import { Card } from "../player/CardLoader"

export function reqSendCard(cards: Card[]){
    let message = {
        opcode: 12,
        uid: Gloabal.uid,
        roomType: Gloabal.roomType,
        cardList: cards
    }
    MessageUtils.send(message.opcode, message)
}

export function reqSuggest(send: boolean, uid: number){
    let message = {
        opcode: 13,
        uid: uid,
        roomType: Gloabal.roomType,
        send: send,
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