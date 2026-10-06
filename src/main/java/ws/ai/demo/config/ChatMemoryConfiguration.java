package ws.ai.demo.config;

import org.springframework.ai.chat.client.advisor.MessageChatMemoryAdvisor;
import org.springframework.ai.chat.client.advisor.api.BaseChatMemoryAdvisor;
import org.springframework.ai.chat.memory.ChatMemory;
import org.springframework.ai.chat.memory.ChatMemoryRepository;
import org.springframework.ai.chat.memory.InMemoryChatMemoryRepository;
import org.springframework.ai.chat.memory.MessageWindowChatMemory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * 会话记忆配置
 *
 * @author WindShadow
 * @version 2026-10-05
 */

@Configuration(proxyBeanMethods = false)
public class ChatMemoryConfiguration {

    /**
     * chat消息存储层
     *
     * @return
     */
    @Bean
    static ChatMemoryRepository chatMemoryRepository() {
        return new InMemoryChatMemoryRepository();
    }

    /**
     * chat会话记忆
     *
     * @param repository
     * @return
     */
    @Bean
    public static ChatMemory chatMemory(ChatMemoryRepository repository) {
        // 维护一个滑动窗口，窗口内消息数量不超过指定的最大数量。
        // 当消息数量超过最大值时，较旧的消息将被移除，但SystemMessage实例始终保留。默认窗口大小为 20 条消息。
        return MessageWindowChatMemory.builder()
                .chatMemoryRepository(repository)
                .maxMessages(100)
                .build();
    }

    /**
     * chat会话记忆增强
     *
     * @param chatMemory
     * @return
     */
    @Bean
    public static BaseChatMemoryAdvisor chatMemoryAdvisor(ChatMemory chatMemory) {

        // 会话增强，即会话记忆，本质上是记录全部 message
        return MessageChatMemoryAdvisor.builder(chatMemory).build();
    }
}
