package com.zyd.ddz.common.proto;

import com.alibaba.fastjson.JSON;
import io.netty.buffer.ByteBuf;
import lombok.Getter;
import lombok.Setter;
import xyz.noark.core.lang.ByteArray;
import xyz.noark.core.lang.ImmutableByteArray;
import xyz.noark.core.lang.StringByteArray;
import xyz.noark.core.network.NetworkPacket;
import xyz.noark.core.network.NetworkProtocol;
import xyz.noark.network.codec.ByteBufWrapper;
import xyz.noark.network.codec.DefaultNetworkPacket;
import xyz.noark.network.codec.json.SimpleJsonCodec;

/**
 * @author zyd
 * @date 2023/4/15 11:42
 */
public class JsonCodec extends SimpleJsonCodec {


    @Override
    public ByteArray encodePacket(NetworkProtocol networkProtocol) {
        return new StringByteArray(JSON.toJSONString(networkProtocol));
    }

    @Override
    public NetworkPacket decodePacket(ByteBuf byteBuf) {
        DefaultNetworkPacket packet = new DefaultNetworkPacket();
        ByteBufWrapper byteBufWrapper = new ByteBufWrapper(byteBuf);
        Request request = JSON.parseObject(new String(byteBufWrapper.array()), Request.class);
        packet.setLength(byteBuf.readableBytes());
        packet.setOpcode(request.getOpcode());
        packet.setBytes(new ImmutableByteArray(request.getMessage().getBytes()));

        return packet;
    }

    @Setter
    @Getter
    static class Request{
        int opcode;
        String message;
    }
}
