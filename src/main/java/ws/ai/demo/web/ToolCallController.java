package ws.ai.demo.web;

import jakarta.validation.constraints.NotBlank;
import lombok.extern.slf4j.Slf4j;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Flux;
import ws.ai.demo.common.WebContants;
import ws.ai.demo.tool.EmailTool;

/**
 * @author WindShadow
 * @version 2026-10-04
 */
@Slf4j
@Validated
@RestController
@RequestMapping("/api/v1")
public class ToolCallController {

    private final ChatClient chatClient;
    private final EmailTool emailTool;

    public ToolCallController(ChatClient.Builder builder, EmailTool emailTool) {
        this.chatClient = builder.build();
        this.emailTool = emailTool;
    }

    @GetMapping(value = "/tool/send-email", produces = WebContants.CHAT_MEDIA_TYPE) // 必须指定produces
    public Flux<String> sendEmail(@RequestParam("username") @NotBlank String username,
                                  @RequestParam("emailContent") @NotBlank String emailContent) {

        return chatClient.prompt()
                .system("调用工具时，说明你的操作过程，无法完成操作时说明原因")
                .user("发送邮件给%s，内容为“%s”".formatted(username, emailContent))
                .tools(emailTool)
                .stream()
                .content();
    }
}
