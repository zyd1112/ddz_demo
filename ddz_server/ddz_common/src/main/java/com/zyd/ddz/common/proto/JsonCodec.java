package com.zyd.ddz.common.proto;

import com.alibaba.fastjson.JSONObject;
import com.zyd.zgame.common.network.packet.*;
import com.zyd.zgame.core.codec.json.SimpleJsonCodec;
import io.netty.buffer.ByteBuf;
import org.springframework.stereotype.Component;

/**
 * @author zyd
 * @date 2023/4/15 11:42
 */
@Component(value = "jsonCodec")
public class JsonCodec extends SimpleJsonCodec {


    @Override
    public Message encodePacket(NetworkProtocol networkProtocol) {
        return new StringMessage(JSONObject.toJSONString(networkProtocol));
    }

    @Override
    public MessagePacket decodePacket(ByteBuf byteBuf) {
        DefaultMessagePacket packet = new DefaultMessagePacket();
        byte[] bytes = new byte[byteBuf.readableBytes()];
        byteBuf.readBytes(bytes);
        JSONObject jsonObject = JSONObject.parseObject(new String(bytes));
        packet.setId(jsonObject.getInteger("opcode"));
        packet.setLength(byteBuf.readableBytes());
        packet.setMessage(new StringMessage(jsonObject.getString("message")));

        return packet;
    }
}
