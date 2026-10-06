package ws.ai.demo;

import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.ai.embedding.EmbeddingModel;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import ws.ai.demo.util.VectorDistance;

import java.util.function.BiConsumer;

/**
 * 向量模型showcase
 *
 * @author WindShadow
 * @version 2026-10-06
 */

@Slf4j
@SpringBootTest
public class EmbeddingShowcase {

    @Autowired
    private EmbeddingModel embeddingModel;

    /**
     * 将文本向量化
     */
    @Test
    void embedShowcase() {

        float[] embedded1 = embeddingModel.embed("Spring AI");
        float[] embedded2 = embeddingModel.embed("Spring AI");
        Assertions.assertArrayEquals(embedded1, embedded2);
    }

    /**
     * 文本相似度匹配，取决与模型能力
     */
    @Test
    void matchShowcase() {

        BiConsumer<String, String> matcher = (input, target) -> {

            float[] inputEmbedded = embeddingModel.embed(input);
            float[] targetEmbedded = embeddingModel.embed(target);
            System.out.printf("%s & %s 欧式相似度：%f  余弦相似度：%f%n", input, target,
                    VectorDistance.euclideanSimilarity(inputEmbedded, targetEmbedded), VectorDistance.cosineSimilarity(inputEmbedded, targetEmbedded));
        };

        // 明显“香蕉”更匹配，相似度更高
        matcher.accept("猫", "水果");
        matcher.accept("香蕉", "水果");
        matcher.accept("车", "水果");

        System.out.println("--------------------");

        // 明显“宝马”更匹配，相似度更高
        matcher.accept("飞机", "车");
        matcher.accept("猫", "车");
        matcher.accept("宝马", "车");
    }
}
