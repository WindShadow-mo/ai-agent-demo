package ws.ai.demo.chat;

import ws.ai.demo.common.ConversationType;
import ws.ai.demo.pojo.vo.ConversationView;

import java.util.List;

/**
 * @author WindShadow
 * @version 2026-10-05
 */
public interface ChatConversationRepository {

    /**
     * 保存历史会话
     *
     * @param type
     * @param conversationId
     */
    void saveConversation(ConversationType type, String conversationId);

    /**
     * 获取历史会话内容
     *
     * @param type
     * @param conversationId
     * @return
     */
    ConversationView getConversation(ConversationType type, String conversationId);

    List<String> listConversationId(ConversationType type);
}
