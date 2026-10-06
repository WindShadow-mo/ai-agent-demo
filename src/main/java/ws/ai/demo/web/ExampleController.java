package ws.ai.demo.web;

import lombok.extern.slf4j.Slf4j;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Flux;
import ws.ai.demo.common.WebContants;

/**
 * @author WindShadow
 * @version 2026-10-05
 */

@Slf4j
@Validated
@RestController
@RequestMapping("/api/v1")
public class ExampleController {

    @GetMapping(value = "/flux", produces = WebContants.CHAT_MEDIA_TYPE) // 必须指定produces
    public Flux<String> sendEmail() {

        return Flux.just("A", "B", "C");
    }
}
