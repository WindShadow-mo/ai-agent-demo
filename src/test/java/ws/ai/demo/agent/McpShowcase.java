package ws.ai.demo.agent;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.springframework.ai.chat.messages.SystemMessage;
import org.springframework.ai.chat.prompt.Prompt;
import org.springframework.ai.mcp.AsyncMcpToolCallbackProvider;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.test.context.ActiveProfiles;
import ws.ai.demo.AiDemoApp;
import ws.ai.demo.BaseLLMShowcase;

/**
 * <pre>
 * 原始 Chat + Tool Calling 的交互大致是：
 * 1: 用户向 Chat 应用/Agent 提问；
 * 2: 应用把对话历史 + 可用工具的 JSON Schema 一起发给 LLM；
 * 3: LLM 决定调用某个工具，返回结构化 tool_call（工具名 + 参数）；
 * 4: 宿主应用在本地执行函数/API；
 * 5: 应用把执行结果作为 tool 消息回传；
 * 6: LLM 基于结果继续生成回答。
 * 这个模式能工作，但问题在于：工具定义、执行、鉴权、上下文注入都耦合在宿主应用里。
 * 每接一个模型、每换一个框架、每新增一个工具或数据源，都要重复适配(孤岛效应)。
 * 于是形成 M 个 AI 应用 × N 个工具/数据源 的集成爆炸，工具难以跨应用、跨模型复用，权限、安全、生命周期也没有统一标准。
 * MCP（Model Context Protocol）产生的原因，就是把这层“模型如何连接外部工具与上下文”标准化。
 * 它把外部能力抽象成 MCP Server，由支持 MCP 的 Host/Client 通过统一协议发现和调用，Server 可暴露 tools、resources、prompts 等。
 * 模型仍然做 tool call 决策，但工具如何被发现、描述、调用、返回、授权，由 MCP 统一。
 *
 * 因此，MCP 的核心动机是：把原始的 chat + tool 调用从“每个应用各自定制”变成“标准化插拔接口”，让集成从 M×N 降为 M+N，类似 AI 世界的 USB-C。
 * </pre>
 *
 * @author WindShadow
 * @version 2026-10-07
 */
@ActiveProfiles({"mcp", "mcp-server"})
@SpringBootTest
public class McpShowcase extends BaseLLMShowcase {

    private static final Prompt PROMPT = Prompt.builder()
            .messages(new SystemMessage("调用工具时，说明你的操作过程"))
            .messages(new SystemMessage("调用mcp时，说明调用了哪个mcp")) // 这个规则不会触发
            .build();

    @Autowired
    private AsyncMcpToolCallbackProvider mcpProvider;

    @BeforeAll
    static void runMcpServer() {

        ConfigurableApplicationContext server = SpringApplication.run(AiDemoApp.class, "--spring.profiles.active=mcp-server");
        Runtime.getRuntime().addShutdownHook(new Thread(() -> {

            System.out.println("------ 关闭mcp server ------");
            server.close();
        }));
    }

    @Test
    void mcpCallShow() {

        chatClient.prompt(PROMPT)
                .user("查一下张三的手机号码是多少")
                .tools(mcpProvider)
                .stream()
                .content()
                .doOnNext(System.out::print)
                .blockLast();
    }

    @Test
    void mcpAskShow() {

        // 直接问，这里模型不知道自己有哪些mcp的，应该是需要tool去给它查询
        chatClient.prompt(PROMPT)
                .user("当前有哪些mcp?")
                .tools(mcpProvider)
                .stream()
                .content()
                .doOnNext(System.out::print)
                .blockLast();
    }
}
