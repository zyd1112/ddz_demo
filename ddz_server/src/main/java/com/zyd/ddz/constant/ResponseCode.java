package com.zyd.ddz.constant;

import lombok.Getter;

/**
 * @author zyd
 * @date 2023/4/25 13:16
 */
@Getter
public enum ResponseCode {

    SUCCESS(200, "成功"),
    FAIL(500, "失败");

    int code;

    String content;

    ResponseCode(int code, String content){
        this.code = code;
        this.content = content;
    }
}
