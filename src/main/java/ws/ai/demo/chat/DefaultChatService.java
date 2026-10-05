package ws.ai.demo.chat;

import lombok.RequiredArgsConstructor;
import org.jspecify.annotations.Nullable;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.chat.client.advisor.MessageChatMemoryAdvisor;
import org.springframework.ai.chat.client.advisor.api.BaseChatMemoryAdvisor;
import org.springframework.ai.chat.memory.ChatMemory;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;
import ws.ai.demo.common.ConversationType;
import ws.ai.demo.pojo.vo.ConversationView;

import java.util.List;

/**
 * @author WindShadow
 * @version 2026-10-05
 */

@Service
public class DefaultChatService implements ChatService {

    private final ChatClient chatClient;
    private final BaseChatMemoryAdvisor chatMemoryAdvisor;
    private final ChatConversationRepository repository;

    public DefaultChatService(ChatClient.Builder builder, BaseChatMemoryAdvisor chatMemoryAdvisor, ChatConversationRepository repository) {
        this.chatClient = builder.build();
        this.chatMemoryAdvisor = chatMemoryAdvisor;
        this.repository = repository;
    }

    @Override
    public ChatClient.ChatClientRequestSpec open() {
        return doOpen(null);
    }

    @Override
    public ChatClient.ChatClientRequestSpec open(String conversationId) {

        Assert.hasText(conversationId, "conversationId must not be empty");
        return doOpen(conversationId);
    }

    @Override
    public List<String> listConversationId() {
        return repository.listConversationId(ConversationType.CHAT);
    }

    @Override
    public ConversationView getConversation(String conversationId) {
        return repository.getConversation(ConversationType.CHAT, conversationId);
    }

    private ChatClient.ChatClientRequestSpec doOpen(@Nullable String conversationId) {

        ChatClient.ChatClientRequestSpec spec = chatClient.prompt();
        if (conversationId != null) {
            // 增加会话ID设置增强，增加会话记忆增强
            spec = spec.advisors(advisorSpec -> advisorSpec.param(ChatMemory.CONVERSATION_ID, conversationId))
                    .advisors(chatMemoryAdvisor);
            repository.saveConversation(ConversationType.CHAT, conversationId);
        }
        return spec;
    }
}
