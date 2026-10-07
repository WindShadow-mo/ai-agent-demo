package ws.ai.demo.agent;

import org.junit.jupiter.api.Test;
import org.springaicommunity.agent.tools.FileSystemTools;
import org.springaicommunity.agent.tools.SkillsTool;
import org.springframework.ai.chat.messages.SystemMessage;
import org.springframework.ai.chat.prompt.Prompt;
import org.springframework.ai.tool.ToolCallback;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.core.io.FileSystemResource;
import ws.ai.demo.BaseLLMShowcase;

/**
 * @author WindShadow
 * @version 2026-10-07
 */
@SpringBootTest
public class SkillShowcase extends BaseLLMShowcase {

    private static final Prompt PROMPT = Prompt.builder()
            .messages(new SystemMessage("调用skill时，说明调用了哪个skill"))
            .build();

    @Test
    void skillCallShow() {

        // spring-ai-agent-utils 能读磁盘的skill文件，也能读jar包内的skill文件
        // 为了读取reference文件，必须加载file tool

        FileSystemTools fileSystemTools = FileSystemTools.builder()
                .build();

        ToolCallback skills = SkillsTool.builder()
                .addSkillsResource(new FileSystemResource("./work/skills/"))
                .build();

        chatClient.prompt(PROMPT)
                .system("不能访问本地文件系统时，告诉用户是啥原因")
                .user("寻找一下张三的手机号码是多少")
                .tools(skills, fileSystemTools)
                .stream()
                .content()
                .doOnNext(System.out::print)
                .blockLast();
    }
}
