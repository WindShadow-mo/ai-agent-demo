package ws.ai.demo.tool;

import lombok.extern.slf4j.Slf4j;
import org.springframework.ai.tool.annotation.Tool;
import org.springframework.ai.tool.annotation.ToolParam;
import org.springframework.stereotype.Component;
import org.springframework.util.Assert;
import ws.ai.demo.pojo.dto.EmailQueryResult;
import ws.ai.demo.pojo.dto.EmailReq;

/**
 * 通过注解声明工具及其入参；
 * 毫无疑问的，一般的tool肯定是浅封装，而不是在其中耦合各种操作逻辑，如sql操作，远程调用，只整个能力而不实际操作
 *
 * @author WindShadow
 * @version 2026-10-05
 */
@Slf4j
@Component
public class EmailTool {

    private static final String DOMAIN = "@ws.com";

    /**
     * tool的返回值结果，会被spring ai进行json序列化，作为tool的实际响应，再次给到LLM
     *
     * @param username
     * @return
     */
    @Tool(description = "查询用户的邮箱")
    public EmailQueryResult findEmail(@ToolParam(description = "用户名或用户姓名") String username) {

        // 禁止查管理员的邮箱；作为error场景
        if (username.equals("admin")) {
            throw new IllegalArgumentException("Reject find email of admin");
        }

        EmailQueryResult result = new EmailQueryResult();
        result.setUsername(username);
        result.setEmail(username + DOMAIN);
        return result;
    }

    @Tool(description = "发送邮件")
    public boolean sendEmail(@ToolParam(description = "邮件发送请求") EmailReq req) {

        String email = req.getEmail();
        Assert.isTrue(email.endsWith(DOMAIN), "The email address is invalid");
        log.info("【{}】发送邮件到：{}，内容：{}", req.getSendWay().getValue(), email, req.getEmailContent());
        return true;
    }
}
