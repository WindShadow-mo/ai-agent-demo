package ws.ai.demo.chat;

import lombok.RequiredArgsConstructor;
import org.springframework.ai.chat.memory.ChatMemory;
import org.springframework.ai.chat.messages.Message;
import org.springframework.ai.chat.messages.MessageType;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;
import ws.ai.demo.common.ConversationType;
import ws.ai.demo.pojo.vo.ConversationView;
import ws.ai.demo.pojo.vo.MessageView;

import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @author WindShadow
 * @version 2026-10-05
 */
@Service
@RequiredArgsConstructor
public class InMemeryChatConversationRepository implements ChatConversationRepository {

    private final Map<ConversationType, Set<String>> repository = new ConcurrentHashMap<>();
    private final ChatMemory chatMemory;

    @Override
    public void saveConversation(ConversationType type, String conversationId) {

        Assert.notNull(type, "type must not be empty");
        Assert.hasText(conversationId, "conversationId must not be empty");
        repository.computeIfAbsent(type, k -> ConcurrentHashMap.newKeySet()).add(conversationId);
    }

    @Override
    public ConversationView getConversation(ConversationType type, String conversationId) {

        Assert.notNull(type, "type must not be empty");
        Assert.hasText(conversationId, "conversationId must not be empty");
        boolean has = repository.getOrDefault(type, Set.of()).contains(conversationId);
        Assert.isTrue(has, "conversation with id " + conversationId + " not found");
        List<Message> messages = chatMemory.get(conversationId);
        ConversationView view = new ConversationView();
        view.setConversationId(conversationId);
        view.setMessages(messages.stream()
                .filter(msg -> msg.getMessageType() == MessageType.USER || msg.getMessageType() == MessageType.ASSISTANT)
                .map(msg -> {

                    MessageView mv = new MessageView();
                    mv.setRole(msg.getMessageType().getValue());
                    mv.setContent(msg.getText());
                    return mv;
                })
                .toList());
        return view;
    }

    @Override
    public List<String> listConversationId(ConversationType type) {

        if (repository.containsKey(type)) {
            return repository.get(type).stream().toList();
        }
        return List.of();
    }
}
