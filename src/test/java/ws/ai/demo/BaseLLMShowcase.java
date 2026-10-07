package ws.ai.demo;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * @author WindShadow
 * @version 2026-10-05
 */
public abstract class BaseLLMShowcase {

    @Autowired
    protected ChatClient.Builder builder;

    protected ChatClient chatClient;

    @BeforeEach
    void init() {
        chatClient = builder.build();
    }

    @AfterEach
    void nextLine() {
        System.out.println();
    }
}
