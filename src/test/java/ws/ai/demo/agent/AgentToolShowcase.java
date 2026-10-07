package ws.ai.demo.agent;

import org.junit.jupiter.api.Test;
import org.springaicommunity.agent.tools.FileSystemTools;
import org.springaicommunity.agent.tools.ShellTools;
import org.springframework.ai.chat.messages.SystemMessage;
import org.springframework.ai.chat.prompt.Prompt;
import org.springframework.ai.chat.prompt.PromptTemplate;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.util.StreamUtils;
import ws.ai.demo.BaseLLMShowcase;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Map;

/**
 * agent常用的一些tool的showcase，基于springaicommunity.agent.tools。
 * SpringAI官方没有提供这些tool，它的定位目前来说，是倾向于统一LLM的调用的底座，如advisor机制等。
 * 其他扩展目前是没有到，而SpringAI社区（SpringAI Community）由SpringAI团队部分负责人来领导，孵化AI更上层的能力或框架。
 *
 * @author WindShadow
 * @version 2026-10-07
 */
@SpringBootTest
public class AgentToolShowcase extends BaseLLMShowcase {

    private static final Prompt PROMPT = Prompt.builder()
            .messages(new SystemMessage("调用工具时，说明你的操作过程"))
            .build();

    private static Path tmpFile;

    @Test
    void shellToolCallShow() {

        ShellTools shellTools = ShellTools.builder()
                .build();
        chatClient.prompt(PROMPT)
                .user("在终端执行命令，输出指定内容，内容为“你中了500万”")
                .tools(shellTools)
                .stream()
                .content()
                .doOnNext(System.out::print)
                .blockLast();
    }

    @Test
    void fileToolCallShow() throws IOException {

        FileSystemTools fileSystemTools = FileSystemTools.builder()
                .build();

        // 创建临时文件
        Path tmpFile = Files.createTempFile("ai-tmp", ".txt");

        // 构建提示词读取改文件
        String prompt = PromptTemplate.builder()
                .template("读取该文件: {file_path}，并输出其内容")
                .variables(Map.of("file_path", tmpFile.toString()))
                .build()
                .render();
        try {
            // 临时文件写入时间戳
            StreamUtils.copy("时间戳：" + System.currentTimeMillis(), StandardCharsets.UTF_8, Files.newOutputStream(tmpFile));

            chatClient.prompt(PROMPT)
                    .user(prompt)
                    .tools(fileSystemTools)
                    .stream()
                    .content()
                    .doOnNext(System.out::print)
                    .blockLast();
        } finally {
            Files.deleteIfExists(tmpFile);
        }
    }
}
