package ws.ai.demo.pojo.dto;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;
import org.springframework.ai.tool.annotation.ToolParam;

/**
 * @author WindShadow
 * @version 2026-10-05
 */

@NoArgsConstructor
@Data
@ToString
public class EmailQueryResult {

//    @ToolParam(description = "用户名")
    private String username;

//    @ToolParam(description = "邮箱地址")
    private String email;
}
