package ws.ai.demo.pojo.enums;

import lombok.Getter;

/**
 * @author WindShadow
 * @version 2026-10-05
 */

public enum EmailSendWay {

    SYNC("同步"),

    ASYNC("异步");

    public static final String TOOL_PARM_DESC = "枚举描述：SYNC 为同步，ASYNC 为异步";

    @Getter
    private final String value;

    EmailSendWay(String value) {
        this.value = value;
    }
}
