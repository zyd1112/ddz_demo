package com.zyd.ddz.service;

import com.zyd.ddz.entity.Card;

import java.util.List;
import java.util.Map;

/**
 * @author zyd
 * @date 2023/4/7 9:25
 */
public interface DdzService {

    /**
     * 请求发牌
     * @return map 1-3 : 手牌
     *             4 : 底牌
     */
    Map<Integer, List<Card>> reqSendCard();

    /**
     * 请求提示
     * @param own 该玩家手牌
     * @param target 目标的牌组
     * @return 结果集
     */
    List<List<Card>> reqPrompt(List<Card> own, List<Card> target);
}
