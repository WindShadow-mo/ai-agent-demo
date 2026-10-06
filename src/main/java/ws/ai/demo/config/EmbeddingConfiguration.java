package ws.ai.demo.config;

import org.springframework.ai.model.openai.autoconfigure.OpenAiCommonProperties;
import org.springframework.ai.model.openai.autoconfigure.OpenAiEmbeddingAutoConfiguration;
import org.springframework.ai.model.openai.autoconfigure.OpenAiEmbeddingProperties;
import org.springframework.beans.factory.ObjectProvider;
import org.springframework.context.annotation.Configuration;

/**
 * EmbeddingModel 来自{@link OpenAiEmbeddingAutoConfiguration#openAiEmbeddingModel(OpenAiCommonProperties, OpenAiEmbeddingProperties, ObjectProvider, ObjectProvider, ObjectProvider, ObjectProvider)}；
 * 且韦配置 spring.ai.openai.embedding.xx时(配置类{@link OpenAiEmbeddingProperties})，会使用openai包内置默认的模型：{@link com.openai.models.embeddings.EmbeddingModel}
 *
 * @author WindShadow
 * @version 2026-10-04
 */

@Configuration(proxyBeanMethods = false)
public class EmbeddingConfiguration {

}
