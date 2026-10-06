package ws.ai.demo.web;

import jakarta.validation.constraints.NotBlank;
import lombok.RequiredArgsConstructor;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;
import ws.ai.demo.chat.ChatService;
import ws.ai.demo.common.WebContants;

/**
 * @author WindShadow
 * @version 2026-10-04
 */
@Validated
@RestController
@RequestMapping("/api/v1")
@RequiredArgsConstructor
public class ChatController {

    private final ChatService chatService;

    @GetMapping(value = "/chat", produces = WebContants.CHAT_MEDIA_TYPE) // 必须指定produces
    public Flux<String> chat(@RequestParam("prompt") @NotBlank String prompt) {

        return chatService.open()
                .user(prompt)
                .stream()
                .content();
    }
}
