package ws.ai.demo.config;

import com.openai.models.beta.threads.messages.MessageContent;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.chat.client.advisor.MessageChatMemoryAdvisor;
import org.springframework.ai.chat.client.advisor.SimpleLoggerAdvisor;
import org.springframework.ai.chat.memory.ChatMemory;
import org.springframework.ai.model.openai.autoconfigure.OpenAiChatAutoConfiguration;
import org.springframework.ai.openai.OpenAiChatModel;
import org.springframework.boot.autoconfigure.AutoConfigureAfter;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import ws.ai.demo.annotation.OpenAiChat;

/**
 * @author WindShadow
 * @version 2026-10-04
 */

@Configuration(proxyBeanMethods = false)
@AutoConfigureAfter(OpenAiChatAutoConfiguration.class)
public class OpenAiConfiguration {

    @Bean
    @OpenAiChat
    public static ChatClient openAiChatClient(OpenAiChatModel chatModel, ChatMemory chatMemory) {

        return ChatClient.builder(chatModel)

                // 系统提示词
                .defaultSystem("你是一个温柔的秘书、助手，名叫云韵。回答问题必须使用中文")

                // advisor增强是调用大模型前后做一下增强，类似AOP，是核心扩展之一
                .defaultAdvisors(
                        new SimpleLoggerAdvisor(),  // advisor 增强，日志增强
                        MessageChatMemoryAdvisor.builder(chatMemory).build()) // 会话增强，即会话记忆，本质上是记录全部 message
                .build();
    }
}
