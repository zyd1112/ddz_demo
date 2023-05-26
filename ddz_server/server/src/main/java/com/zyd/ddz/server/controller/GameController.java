package com.zyd.ddz.server.controller;

import com.zyd.ddz.common.message.room.dto.PlayerDto;
import com.zyd.ddz.common.message.room.request.ReqScrambleMessage;
import com.zyd.ddz.common.message.room.request.ReqSendCardsMessage;
import com.zyd.ddz.common.service.RoomService;
import com.zyd.ddz.common.service.UserService;
import xyz.noark.core.annotation.Autowired;
import xyz.noark.core.annotation.Controller;
import xyz.noark.core.annotation.controller.ExecThreadGroup;
import xyz.noark.core.annotation.controller.PacketMapping;
import xyz.noark.core.network.Session;
import xyz.noark.core.network.SessionManager;

import static xyz.noark.log.LogHelper.logger;

/**
 * @author zyd
 * @date 2023/4/12 10:39
 */
@Controller(threadGroup = ExecThreadGroup.ModuleThreadGroup)
public class GameController {

    @Autowired
    RoomService roomService;
    @Autowired
    UserService userService;

    @PacketMapping(opcode = 10, state = Session.State.CONNECTED)
    public void enterRoom(PlayerDto playerDto){
        long uid = playerDto.getUid();
        if (!SessionManager.isOnline(uid)){
            logger.info("[{}] 用户没有在线", uid);
            return;
        }

        roomService.enterRoom(uid, playerDto.getRoomType());
    }

    @PacketMapping(opcode = 12, state = Session.State.CONNECTED)
    public void sendCard(ReqSendCardsMessage message){
        long uid = message.getUid();
        if (!SessionManager.isOnline(uid)){
            logger.info("[{}] 用户没有在线", uid);
            return;
        }

        roomService.sendCard(message.getUid(), message.getRoomType(), message.getCardList());
    }

    @PacketMapping(opcode = 13, state = Session.State.CONNECTED)
    public void suggest(PlayerDto playerDto){
        long uid = playerDto.getUid();
        if (!SessionManager.isOnline(uid)){
            logger.info("[{}] 用户没有在线", uid);
            return;
        }


        roomService.suggest(playerDto.getUid(), playerDto.getRoomType());

    }

    @PacketMapping(opcode = 14, state = Session.State.CONNECTED, printLog = false)
    public void reqCountdown(PlayerDto playerDto){
        roomService.reqCountdown(playerDto.getUid(), playerDto.getRoomType());
    }

    @PacketMapping(opcode = 15, state = Session.State.CONNECTED)
    public void noSend(PlayerDto playerDto){
        long uid = playerDto.getUid();
        if (!SessionManager.isOnline(uid)){
            logger.info("[{}] 用户没有在线", uid);
            return;
        }

        roomService.noSend(playerDto.getUid(), playerDto.getRoomType());
    }

    @PacketMapping(opcode = 16, state = Session.State.CONNECTED)
    public void ready(PlayerDto playerDto){
        long uid = playerDto.getUid();
        if (!SessionManager.isOnline(uid)){
            logger.info("[{}] 用户没有在线", uid);
            return;
        }

        roomService.playerReady(uid, playerDto.getRoomType());
    }

    @PacketMapping(opcode = 17, state = Session.State.CONNECTED)
    public void start(PlayerDto playerDto){
        long uid = playerDto.getUid();
        if (!SessionManager.isOnline(uid)){
            logger.info("[{}] 用户没有在线", uid);
            return;
        }

        roomService.playerStart(uid, playerDto.getRoomType());
    }

    @PacketMapping(opcode = 18, state = Session.State.CONNECTED)
    public void scramble(ReqScrambleMessage message){
        long uid = message.getPlayerDto().getUid();
        if (!SessionManager.isOnline(uid)){
            logger.info("[{}] 用户没有在线", uid);
            return;
        }

        roomService.scramble(uid, message.getPlayerDto().getRoomType(), message.isStatus());
    }

    @PacketMapping(opcode = 19, state = Session.State.CONNECTED)
    public void reqLeaveRoom(PlayerDto playerDto){
        long uid = playerDto.getUid();
        if (!SessionManager.isOnline(uid)){
            logger.info("[{}] 用户没有在线", uid);
            return;
        }

        roomService.playerLeave(uid, playerDto.getRoomType());
    }
}
