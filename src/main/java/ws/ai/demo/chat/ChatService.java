package ws.ai.demo.chat;

import org.springframework.ai.chat.client.ChatClient;
import ws.ai.demo.pojo.vo.ConversationView;

import java.util.List;

/**
 * 维护会话id，可实现历史会话记录
 *
 * @author WindShadow
 * @version 2026-10-05
 */
public interface ChatService {

    ChatClient.ChatClientRequestSpec open();

    ChatClient.ChatClientRequestSpec open(String conversationId);

    List<String> listConversationId();

    ConversationView getConversation(String conversationId);
}
