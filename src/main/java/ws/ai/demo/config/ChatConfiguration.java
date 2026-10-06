package ws.ai.demo.config;

import org.springframework.ai.chat.client.ChatClientBuilderCustomizer;
import org.springframework.ai.chat.client.advisor.SimpleLoggerAdvisor;
import org.springframework.ai.chat.model.ChatModel;
import org.springframework.ai.model.chat.client.autoconfigure.ChatClientAutoConfiguration;
import org.springframework.ai.model.chat.client.autoconfigure.ChatClientBuilderConfigurer;
import org.springframework.ai.model.chat.client.autoconfigure.ChatClientBuilderProperties;
import org.springframework.ai.model.openai.autoconfigure.OpenAiChatAutoConfiguration;
import org.springframework.ai.model.openai.autoconfigure.OpenAiChatProperties;
import org.springframework.ai.model.openai.autoconfigure.OpenAiCommonProperties;
import org.springframework.ai.model.tool.ToolCallingManager;
import org.springframework.beans.factory.ObjectProvider;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @author WindShadow
 * @version 2026-10-04
 */

@Configuration(proxyBeanMethods = false)
public class ChatConfiguration {

    /**
     * ChatClient.Builder 来自{@link ChatClientAutoConfiguration#chatClientBuilder(ChatClientBuilderProperties, ChatClientBuilderConfigurer, ChatModel, ObjectProvider, ObjectProvider, ObjectProvider, ObjectProvider)}；
     * ChatModel 来自{@link OpenAiChatAutoConfiguration#openAiChatModel(OpenAiCommonProperties, OpenAiChatProperties, ToolCallingManager, ObjectProvider, ObjectProvider, ObjectProvider, ObjectProvider)}
     *
     * @return
     */
    @Bean
    public static ChatClientBuilderCustomizer customizer() {

        // advisor增强是调用大模型前后做一下增强，类似AOP，是核心扩展之一
        return builder -> builder.defaultAdvisors(new SimpleLoggerAdvisor());  // advisor 增强，日志增强
    }
}
