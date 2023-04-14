export class ReqEnterRoomMessage implements Message{
    opcode = 10;

    uid: bigint;

    roomType: number;

}