package ws.ai.demo.pojo.vo;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

/**
 * @author WindShadow
 * @version 2026-10-05
 */

@NoArgsConstructor
@Data
@ToString
public class MessageView {

    private String role;
    private String content;
}
