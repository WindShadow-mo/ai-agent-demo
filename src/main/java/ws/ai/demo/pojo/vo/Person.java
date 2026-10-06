package ws.ai.demo.pojo.vo;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

/**
 * @author WindShadow
 * @version 2026-10-06
 */

@NoArgsConstructor
@Data
@ToString
public class Person {

    private String name;
    private Integer age;
    private String email;
}
