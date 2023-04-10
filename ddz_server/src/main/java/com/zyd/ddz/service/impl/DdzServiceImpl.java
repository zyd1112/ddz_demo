package com.zyd.ddz.service.impl;

import com.zyd.ddz.constant.CardContext;
import com.zyd.ddz.entity.Card;
import com.zyd.ddz.entity.CardGroupType;
import com.zyd.ddz.factory.CardFactory;
import com.zyd.ddz.service.DdzService;
import com.zyd.ddz.utils.GameLogicUtils;
import xyz.noark.core.annotation.Service;

import java.util.*;
import java.util.concurrent.ThreadLocalRandom;

/**
 * @author zyd
 * @date 2023/4/7 9:21
 */
@Service
public class DdzServiceImpl implements DdzService {

    @Override
    public List<List<Card>> reqPrompt(List<Card> own, List<Card> target){
        return GameLogicUtils.getAvailableCards(own, target);
    }

    @Override
    public Map<Integer, List<Card>> reqSendCard() {
        return GameLogicUtils.sendCard();
    }

}
