package com.zyd.ddz.server.controller;

import com.zyd.ddz.common.message.room.dto.PlayerDto;
import com.zyd.ddz.common.message.room.request.ReqScrambleMessage;
import com.zyd.ddz.common.message.room.request.ReqSendCardsMessage;
import com.zyd.ddz.common.service.RoomService;
import com.zyd.ddz.common.service.UserService;
import com.zyd.zgame.common.annotation.Controller;
import com.zyd.zgame.common.annotation.Exposed;
import com.zyd.zgame.common.constant.ThreadModuleGroup;
import com.zyd.zgame.common.network.SessionManager;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;



/**
 * @author zyd
 * @date 2023/4/12 10:39
 */
@Controller(threadGroup = ThreadModuleGroup.COMMON_THREAD)
@Slf4j
public class GameController {

    @Autowired
    RoomService roomService;
    @Autowired
    UserService userService;

    @Exposed(id = 10)
    public void enterRoom(PlayerDto playerDto){
        long uid = playerDto.getUid();
        if (!SessionManager.isOnline(uid)){
            log.info("[{}] 用户没有在线", uid);
            return;
        }

        roomService.enterRoom(uid, playerDto.getRoomType());
    }

    @Exposed(id = 12)
    public void sendCard(ReqSendCardsMessage message){
        long uid = message.getUid();
        if (!SessionManager.isOnline(uid)){
            log.info("[{}] 用户没有在线", uid);
            return;
        }

        roomService.sendCard(message.getUid(), message.getRoomType(), message.getCardList());
    }

    @Exposed(id = 13)
    public void suggest(PlayerDto playerDto){
        long uid = playerDto.getUid();
        if (!SessionManager.isOnline(uid)){
            log.info("[{}] 用户没有在线", uid);
            return;
        }


        roomService.suggest(playerDto.getUid(), playerDto.getRoomType());

    }

    @Exposed(id = 14)
    public void reqCountdown(PlayerDto playerDto){
        roomService.reqCountdown(playerDto.getUid(), playerDto.getRoomType());
    }

    @Exposed(id = 15)
    public void noSend(PlayerDto playerDto){
        long uid = playerDto.getUid();
        if (!SessionManager.isOnline(uid)){
            log.info("[{}] 用户没有在线", uid);
            return;
        }

        roomService.noSend(playerDto.getUid(), playerDto.getRoomType());
    }

    @Exposed(id = 16)
    public void ready(PlayerDto playerDto){
        long uid = playerDto.getUid();
        if (!SessionManager.isOnline(uid)){
            log.info("[{}] 用户没有在线", uid);
            return;
        }

        roomService.playerReady(uid, playerDto.getRoomType());
    }

    @Exposed(id = 17)
    public void start(PlayerDto playerDto){
        long uid = playerDto.getUid();
        if (!SessionManager.isOnline(uid)){
            log.info("[{}] 用户没有在线", uid);
            return;
        }

        roomService.playerStart(uid, playerDto.getRoomType());
    }

    @Exposed(id = 18)
    public void scramble(ReqScrambleMessage message){
        long uid = message.getPlayerDto().getUid();
        if (!SessionManager.isOnline(uid)){
            log.info("[{}] 用户没有在线", uid);
            return;
        }

        roomService.scramble(uid, message.getPlayerDto().getRoomType(), message.isStatus());
    }

    @Exposed(id = 19)
    public void reqLeaveRoom(PlayerDto playerDto){
        long uid = playerDto.getUid();
        if (!SessionManager.isOnline(uid)){
            log.info("[{}] 用户没有在线", uid);
            return;
        }

        roomService.playerLeave(uid, playerDto.getRoomType());
    }
}
