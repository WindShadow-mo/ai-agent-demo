package ws.ai.demo;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import ws.ai.demo.tool.EmailTool;

/**
 * 业界一般称作：FunctionCalling
 *
 * @author WindShadow
 * @version 2026-10-05
 */
@SpringBootTest
public class ToolCallShowcase extends BaseLLMShowcase {

    @Autowired
    private EmailTool emailTool;

    @Test
    void toolCallShow() {

        chatClient.prompt()
                .system("调用工具时，说明你的操作过程")
                .user("异步发送邮件给张三，内容为“你中了500万”")
                .tools(emailTool)
                .stream()
                .content()
                .doOnNext(System.out::print)
                .blockLast();
    }

    @Test
    void toolCallErrShow() {

        // 调用EmailTool#findEmail会报错，spring ai会讲异常的message，作为tool的输出，再提交给LLM
        chatClient.prompt()
                .system("调用工具时，说明你的操作过程")
                .user("发送邮件给admin，内容为“你中了500万”")
                .tools(emailTool)
                .stream()
                .content()
                .doOnNext(System.out::print)
                .blockLast();
        // 意外场景: 模型比较low的时候，直接调用EmailTool#sendEmail，以 "admin"作为邮箱地址发送
        // 意外场景: 模型比较low的时候，因为找不到admin的邮箱，会找 administrator 的邮箱，然后发送，即业务操作偏离实际预期
    }
}
