package ws.ai.demo.pojo.dto;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;
import org.springframework.ai.tool.annotation.ToolParam;
import ws.ai.demo.pojo.enums.EmailSendWay;

/**
 * @author WindShadow
 * @version 2026-10-05
 */

@NoArgsConstructor
@Data
@ToString
public class EmailReq {

    @ToolParam(description = "邮箱地址")
    private String email;
    @ToolParam(description = "邮件内容")
    private String emailContent;

//    @ToolParam(description = "邮件发送方式", required = false)
    // 枚举值不能用ToolParam注解标记，自行增加枚举描述会好一些
    @ToolParam(description = "邮件发送方式" + "；" + EmailSendWay.TOOL_PARM_DESC, required = false)
    private EmailSendWay sendWay = EmailSendWay.SYNC;
}
