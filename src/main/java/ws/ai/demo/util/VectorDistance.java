package ws.ai.demo.util;

import java.util.Objects;

/**
 * 向量距离/相似度工具类。
 * 所有向量均以 float[] 表示，且要求两个向量维度一致。
 */
public class VectorDistance {

    /**
     * 参数校验
     */
    private static void validate(float[] a, float[] b) {

        Objects.requireNonNull(a, "向量 a 不能为 null");
        Objects.requireNonNull(b, "向量 b 不能为 null");
        if (a.length != b.length) {
            throw new IllegalArgumentException(
                    "两个向量维度不一致: " + a.length + " vs " + b.length);
        }
        if (a.length == 0) {
            throw new IllegalArgumentException("向量不能为空");
        }
    }

    /**
     * 欧氏距离（L2 距离）
     * d = sqrt( Σ (a_i - b_i)^2 )
     *
     * @return 距离，范围 [0, +∞)，值越小越相似
     */
    public static double euclideanDistance(float[] a, float[] b) {

        validate(a, b);
        double sum = 0.0;
        for (int i = 0; i < a.length; i++) {
            // 先转 double 再相减，避免 float 精度损失
            double diff = (double) a[i] - b[i];
            sum += diff * diff;
        }
        return Math.sqrt(sum);
    }

    /**
     * 便于把欧氏距离映射为 (0, 1] 的相似度（可选使用）
     * sim = 1 / (1 + d)
     */
    public static double euclideanSimilarity(float[] a, float[] b) {
        return 1.0 / (1.0 + euclideanDistance(a, b));
    }

    /**
     * 余弦相似度
     * cos = (A · B) / (||A|| * ||B||)
     *
     * @return 相似度，范围 [-1, 1]，越接近 1 越相似
     * 若任一向量为零向量，返回 0（相似度无定义）
     */
    public static double cosineSimilarity(float[] a, float[] b) {

        validate(a, b);

        double dot = 0.0;   // 点积
        double normA = 0.0; // A 的模长平方
        double normB = 0.0; // B 的模长平方

        for (int i = 0; i < a.length; i++) {
            double x = a[i];
            double y = b[i];
            dot += x * y;
            normA += x * x;
            normB += y * y;
        }

        if (normA == 0.0 || normB == 0.0) {
            return 0.0; // 零向量无法定义夹角
        }

        double cos = dot / (Math.sqrt(normA) * Math.sqrt(normB));
        // 修正浮点误差，把结果夹到 [-1, 1]
        return Math.max(-1.0, Math.min(1.0, cos));
    }

    /**
     * 余弦距离
     * d = 1 - cos(A, B)
     *
     * @return 距离，范围 [0, 2]，值越小越相似
     */
    public static double cosineDistance(float[] a, float[] b) {
        return 1.0 - cosineSimilarity(a, b);
    }
}