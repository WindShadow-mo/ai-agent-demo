package ws.ai.demo.mcp;

import jakarta.validation.constraints.NotBlank;
import lombok.extern.slf4j.Slf4j;
import org.springframework.ai.mcp.annotation.McpTool;
import org.springframework.ai.mcp.annotation.McpToolParam;
import org.springframework.stereotype.Component;
import org.springframework.validation.annotation.Validated;

/**
 * mcp server端的一个mcp工具
 */
@Slf4j
@Validated
@Component
public class PhoneTools {

    @McpTool(name = "findPhoneNumber", description = "查找某人的电话号码")
    public long findPhoneNumber(@McpToolParam(description = "用户名或姓名") @NotBlank String username) {

        long number = System.currentTimeMillis();
        log.info("{} 的号码： {}", username, number);
        return number;
    }
}