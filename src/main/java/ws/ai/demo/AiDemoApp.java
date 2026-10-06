package ws.ai.demo;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.server.servlet.context.ServletComponentScan;

/**
 * @author WindShadow
 * @version 2026-10-04
 */
@ServletComponentScan
@SpringBootApplication
public class AiDemoApp {

    public static void main(String[] args) {
        SpringApplication.run(AiDemoApp.class, args);
    }
}
