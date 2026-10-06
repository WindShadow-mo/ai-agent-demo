package ws.ai.demo;

import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.springframework.ai.chat.client.advisor.api.BaseChatMemoryAdvisor;
import org.springframework.ai.chat.memory.ChatMemory;
import org.springframework.ai.chat.messages.Message;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import ws.ai.demo.chat.ChatService;
import ws.ai.demo.chat.ConversationIdGen;
import ws.ai.demo.common.AppPromptCosntants;
import ws.ai.demo.pojo.vo.ConversationView;

/**
 * @author WindShadow
 * @version 2026-10-04
 */
@Slf4j
@SpringBootTest
public class ChatShowcase extends BaseLLMShowcase {

    @Autowired
    private ConversationIdGen conversationIdGen;

    @Autowired
    private BaseChatMemoryAdvisor chatMemoryAdvisor;

    @Autowired
    private ChatService chatService;

    // ~ 普通会话调用
    // ==================================

    /**
     * 提示词，核心为{@linkplain Message message}，
     * message分为4大类型{@link org.springframework.ai.chat.messages.MessageType}：
     * 系统提示词：SystemMessage、用户提示词：UserMessage，工具响应提示词：ToolResponseMessage、大模型回答：AssistantMessage
     *
     */
    @Test
    void chatSyncShow() {

        String content = chatClient.prompt()
                // 系统提示词
                .system(AppPromptCosntants.YUN_YUN)
                // 用户提示词
                .user("你是谁？")
                .call()
                .content();
        log.info("content:{}", content);
    }

    @Test
    void chatAsyncShow() {

        chatClient.prompt()
                .system(AppPromptCosntants.YUN_YUN)
                .user("你是谁？")
                .stream()
                .content()
                .doOnNext(System.out::print)
                .blockLast();
    }

    // ~ 会话记忆 核心：ChatMemory
    // ==================================

    @Test
    void chatMemoryShow() {

        // 会话记忆，结合ChatMemory + 会话ID，通过advisor增强，给大模型发送提示词时，自动把历史提示词和回答都带上了
        final String chatId = conversationIdGen.nextConversationId();
        String content1 = chatClient.prompt()
                .system("回答必须简洁，不需要太发散")
                .user("12个苹果平均分给3个人，每个人能分到几个？")
                // 在增强上下文中，指定会话ID
                .advisors(advisorSpec -> advisorSpec.param(ChatMemory.CONVERSATION_ID, chatId))
                .advisors(chatMemoryAdvisor)
                .call()
                .content();
        System.out.println(content1);

        String content2 = chatClient.prompt()
                .user("如果分给4个人呢？")
                // 在增强上下文中，指定会话ID
                .advisors(advisorSpec -> advisorSpec.param(ChatMemory.CONVERSATION_ID, chatId))
                .advisors(chatMemoryAdvisor)
                .call()
                .content();

        System.out.println(content2);
    }

    @Test
    void chatHistoryShow() {

        // 结合其它持久化机制，通过维护会话id，可实现历史会话记录等功能
        final String chatId = conversationIdGen.nextConversationId();
        chatService.open(chatId)
                .system("回答必须简洁，不需要太发散")
                .user("12个苹果平均分给3个人，每个人能分到几个？")
                // 在增强上下文中，指定会话ID
                .call()
                .content();

        ConversationView view = chatService.getConversation(chatId);
        view.visitMessages(System.out::println);
    }
}
