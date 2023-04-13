package com.zyd.ddz.room;

import com.zyd.ddz.entity.Card;
import com.zyd.ddz.entity.Player;
import com.zyd.ddz.entity.Room;
import com.zyd.ddz.utils.GameLogicUtils;

import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;


/**
 * @author zyd
 * @date 2023/4/7 17:06
 */
public class ClassicAbstractRoomManager extends AbstractRoomManager {


    @Override
    public int getSize() {
        return 3;
    }

    @Override
    public void onHeart(Room room, int dt) {
        if (!room.isStart()){
            return;
        }
        //托管机器人
        Player curPlayer = room.getCurPlayer();
        if(curPlayer != null){
            List<Player> sortPlayers = room.getPlayers().values()
                    .stream()
                    .sorted(Comparator.comparing(player -> player.getCharacter().ordinal()))
                    .collect(Collectors.toList());
            int curIndex = sortPlayers.indexOf(curPlayer);
            int next = curIndex == sortPlayers.size() - 1 ? 0 : curIndex + 1;
            Player nextPlayer = sortPlayers.get(next);
            if(nextPlayer.isAuto()){
                List<List<Card>> availableCards = GameLogicUtils.getAvailableCards(nextPlayer.getCardList(), curPlayer.getSendCard());
                if(!availableCards.isEmpty()){
                    List<Card> cards = availableCards.get(0);
                    nextPlayer.setSendCard(cards);
                    nextPlayer.getCardList().removeIf(cards::contains);
                }
                room.setCurPlayer(nextPlayer);
            }
        }
    }

}
