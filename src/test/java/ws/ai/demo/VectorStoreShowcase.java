package ws.ai.demo;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.springframework.ai.document.Document;
import org.springframework.ai.document.DocumentReader;
import org.springframework.ai.reader.markdown.MarkdownDocumentReader;
import org.springframework.ai.reader.markdown.config.MarkdownDocumentReaderConfig;
import org.springframework.ai.vectorstore.SearchRequest;
import org.springframework.ai.vectorstore.VectorStore;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import org.springframework.util.CollectionUtils;

import java.util.List;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

/**
 * 向量数据库showcase；
 * 核心操作：文档提取、分段、入库、相似度检索
 *
 * @author WindShadow
 * @version 2026-10-06
 */

@SpringBootTest
public class VectorStoreShowcase {

    private static final String FILENAME = "跨领域学习资料.md";
    private static Resource MD_RESOURCE;

    private final Set<String> docIds = ConcurrentHashMap.newKeySet();

    @Autowired
    private VectorStore vectorStore;

    @BeforeAll
    static void loadMd() {
        MD_RESOURCE = new ClassPathResource(FILENAME);
    }

    @AfterEach
    void clean() {
        vectorStore.delete(docIds.stream().toList());
    }

    private void addDocuments(List<Document> documents) {

        documents.forEach(document -> docIds.add(document.getId()));
        vectorStore.add(documents);
    }

    @Test
    void eltShow() {

        // 读取markdown文档(使用专门的读取器)，并分段
        MarkdownDocumentReaderConfig config = MarkdownDocumentReaderConfig.builder()
                .withHorizontalRuleCreateDocument(true)
                .withIncludeCodeBlock(false)
                .withIncludeBlockquote(false)
                .withAdditionalMetadata("filename", FILENAME)
                .build();
        DocumentReader reader = new MarkdownDocumentReader(MD_RESOURCE, config);
        List<Document> documents = reader.read();
        // 讲分段之后的文档，加入向量库
        addDocuments(documents);

        // 相似度搜索，取top1，匹配度到达0.5才行
        List<Document> results = vectorStore.similaritySearch(SearchRequest.builder()
                .query("软件开发中的哈希操作，可以去重，做唯一映射，高效读取")
                .topK(1)
                .similarityThreshold(0.5)
                .filterExpression("filename == '%s'".formatted(FILENAME)) // 表达式过滤，通过元数据过滤
                .build());
        visitSearchResult(results);

        // 无关内容搜索
        visitSearchResult(vectorStore.similaritySearch(SearchRequest.builder()
                .query("美国总统特朗普表示明天猛加关税")
                .topK(1)
                .similarityThreshold(0.5)
                .build()));

        // 元数据未匹配搜索
        visitSearchResult(vectorStore.similaritySearch(SearchRequest.builder()
                .query("软件开发中的哈希操作，可以去重，做唯一映射，高效读取")
                .topK(1)
                .similarityThreshold(0.5)
                .filterExpression("filename == '%s'".formatted("test-" + FILENAME))
                .build()));
    }

    private static void visitSearchResult(List<Document> results) {

        if (CollectionUtils.isEmpty(results)) {
            System.out.println("没搜索到内容");
        } else {

            for (Document document : results) {

                System.out.println("ID: " + document.getId());
                System.out.println("得分: " + document.getScore());
                System.out.println("内容: \n" + document.getText());
            }
        }
        System.out.println("-----------------");
    }
}
