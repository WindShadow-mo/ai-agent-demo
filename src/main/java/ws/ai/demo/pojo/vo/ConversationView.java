package ws.ai.demo.pojo.vo;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.util.List;
import java.util.Objects;
import java.util.function.Consumer;

/**
 * @author WindShadow
 * @version 2026-10-05
 */
@NoArgsConstructor
@Data
@ToString
public class ConversationView {

    private String conversationId;
    private List<MessageView> messages;

    public void visitMessages(Consumer<String> consumer) {

        Objects.requireNonNull(consumer);
        if (messages == null || messages.isEmpty()) return;
        for (MessageView messageView : messages) {
            consumer.accept(messageView.getRole() + " " + messageView.getContent());
        }
    }
}
