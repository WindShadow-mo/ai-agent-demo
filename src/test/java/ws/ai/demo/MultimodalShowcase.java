package ws.ai.demo;

import org.junit.jupiter.api.Test;
import org.springframework.ai.content.Media;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import org.springframework.http.MediaType;

/**
 * 多模态对话showcase；
 * 多模态输入依赖模型{@link org.springframework.ai.chat.model.ChatModel}的处理，具体看底层解析封装了
 *
 * @author WindShadow
 * @version 2026-10-07
 */

@SpringBootTest
public class MultimodalShowcase extends BaseLLMShowcase {

    @Test
    void chatWithImgShow() {

        Resource resource = new ClassPathResource("tom.png");
        Media img = new Media(MediaType.IMAGE_PNG, resource);
        // 通过提示词描述器，链式传入多媒体资源。
        chatClient.prompt()
                .user(p -> p.text("图片中的人是谁？").media(img))
                .stream()
                .content()
                .doOnNext(System.out::print)
                .blockLast();
        // 因为从配置中获取的是公共模型，可通过option重新指定模型名称，以切换到支持多模态的模型
//        chatClient.prompt().options(new DefaultChatOptionsBuilder().model(""));
    }
}
