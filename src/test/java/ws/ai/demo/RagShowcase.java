package ws.ai.demo;

import org.junit.jupiter.api.Test;
import org.springframework.ai.chat.client.advisor.vectorstore.QuestionAnswerAdvisor;
import org.springframework.ai.chat.messages.SystemMessage;
import org.springframework.ai.chat.messages.UserMessage;
import org.springframework.ai.chat.prompt.Prompt;
import org.springframework.ai.document.Document;
import org.springframework.ai.vectorstore.SearchRequest;
import org.springframework.ai.vectorstore.VectorStore;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.core.io.ClassPathResource;

import java.util.List;

/**
 * RAG的分段是非常重要的能力，分段的好坏很大程度上决定了模型的输出
 *
 * @author WindShadow
 * @version 2026-10-06
 */
@SpringBootTest
public class RagShowcase extends BaseLLMShowcase implements InitializingBean {

    private static final String PLAN_A = "Tom-planA.md";
    private static final String PLAN_B = "Tom-planB.md";

    private static final Prompt PROMPT = Prompt.builder()
            .messages(new SystemMessage("如果引用了用户提供的资料，则在回答之后，使用单独的章节（章节名为 ”引用“）说明引用的片段"))
            .messages(new UserMessage("根据已知行程，Tom在06:00可能在室内还是室外？"))
            .build();

    @Autowired
    private VectorStore vectorStore;

    @Override
    public void afterPropertiesSet() throws Exception {

        // 读取markdown文档(使用专门的读取器)，并分段
        List<Document> documentsA = VectorStoreShowcase.loadMarkdownDocument(PLAN_A, new ClassPathResource(PLAN_A));
        List<Document> documentsB = VectorStoreShowcase.loadMarkdownDocument(PLAN_B, new ClassPathResource(PLAN_B));
        vectorStore.add(documentsA);
        vectorStore.add(documentsB);
    }

    @Test
    void chatWithRagShow() {

        // LLM对话带RAG
//        VectorStoreChatMemoryAdvisor advisor = VectorStoreChatMemoryAdvisor.builder(vectorStore).build();

        // 默认检索请求
        SearchRequest defaultSearchRequest = SearchRequest.builder()
                .topK(2)
                .similarityThreshold(0.5d)
                .filterExpression("filename == '%s'".formatted(PLAN_A)) // 默认的检索过滤表达式
                .build();

        // （RAG）问答增强
        QuestionAnswerAdvisor qaAdvisor = QuestionAnswerAdvisor.builder(vectorStore) // 向量库
                .searchRequest(defaultSearchRequest) // 向量库检索参数
                .build();
        // 根据 planA，预期tom在室外
        chatClient.prompt(PROMPT)
                .advisors(qaAdvisor)
                .stream()
                .content()
                .doOnNext(System.out::print)
                .blockLast();
    }

    @Test
    void chatWithRagResetFilterExpressionShow() {

        chatClient.prompt(PROMPT)
                // 单次调用中，重新指定使用的相似度搜索的过滤表达式
                .advisors(a -> a.param(QuestionAnswerAdvisor.FILTER_EXPRESSION, "filename == '%s'".formatted(PLAN_B)))
                .advisors(QuestionAnswerAdvisor.builder(vectorStore).build())
                .stream()
                .content()
                .doOnNext(System.out::print)
                .blockLast();
        // 根据 planB，预期tom在室内
    }

    /**
     * 歧义的rag知识
     */
    @Test
    void chatWithRagWhenAmbiguousExpressionShow() {

        // 由于有两份计划，预期是歧义的，无法判定Tom在室内还是室外
        SearchRequest defaultSearchRequest = SearchRequest.builder()
                .topK(6)
                .similarityThreshold(0.5d)
                .build();
        chatClient.prompt(PROMPT)
                .advisors(QuestionAnswerAdvisor.builder(vectorStore)
                        .searchRequest(defaultSearchRequest)
                        .build())
                .stream()
                .content()
                .doOnNext(System.out::print)
                .blockLast();
    }
}
