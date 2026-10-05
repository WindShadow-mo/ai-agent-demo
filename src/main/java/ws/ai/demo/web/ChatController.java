package ws.ai.demo.web;

import jakarta.validation.constraints.NotBlank;
import lombok.RequiredArgsConstructor;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;
import ws.ai.demo.chat.ChatService;

/**
 * @author WindShadow
 * @version 2026-10-04
 */
@Validated
@RestController
@RequestMapping("/api/v1")
@RequiredArgsConstructor
public class ChatController {

    private static final String CHAT_MEDIA_TYPE = org.springframework.http.MediaType.TEXT_HTML_VALUE + ";charset=UTF-8";

    private final ChatService chatService;

    @GetMapping(value = "/chat", produces = CHAT_MEDIA_TYPE) // 必须指定produces
    public Flux<String> chat(@RequestParam("prompt") @NotBlank String prompt) {

        return chatService.open()
                .user(prompt)
                .stream()
                .content();
    }
}
