package ws.ai.demo.chat;

import org.springframework.stereotype.Service;

/**
 * @author WindShadow
 * @version 2026-10-05
 */
@Service
public class DefaultConversationIdGen implements ConversationIdGen {

    @Override
    public String nextConversationId() {
        return "" + System.currentTimeMillis();
    }
}
