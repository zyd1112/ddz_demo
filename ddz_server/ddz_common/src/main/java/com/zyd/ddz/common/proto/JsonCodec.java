package com.zyd.ddz.common.proto;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.zyd.zgame.common.constant.CodecType;
import com.zyd.zgame.common.network.packet.*;
import com.zyd.zgame.common.utils.CodecUtils;
import com.zyd.zgame.core.codec.json.SimpleJsonCodec;
import io.netty.buffer.ByteBuf;
import lombok.Getter;
import lombok.Setter;
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
        Request request = JSON.parseObject(new String(bytes), Request.class);
        packet.setId(request.getOpcode());
        packet.setLength(byteBuf.readableBytes());
        packet.setMessage(new SimpleMessage(request.getMessage().getBytes()));

        return packet;
    }

    @Setter
    @Getter
    static class Request{
        int opcode;
        String message;
    }
}
