package ws.ai.demo.config;

import org.springframework.ai.chat.client.advisor.MessageChatMemoryAdvisor;
import org.springframework.ai.chat.client.advisor.api.BaseChatMemoryAdvisor;
import org.springframework.ai.chat.memory.ChatMemory;
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

    @Bean
    public static ChatMemory chatMemory() {
        return MessageWindowChatMemory.builder().build();
    }

    @Bean
    public static BaseChatMemoryAdvisor chatMemoryAdvisor(ChatMemory chatMemory) {

        // 会话增强，即会话记忆，本质上是记录全部 message
        return MessageChatMemoryAdvisor.builder(chatMemory).build();
    }
}
