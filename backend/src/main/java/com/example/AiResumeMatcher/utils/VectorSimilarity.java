package com.example.AiResumeMatcher.utils;

/**
 * If you want static access without @Autowired (e.g. in tests or utils)
 * But normally you would just use CosineSimilarityCalculator
 */
public final class VectorSimilarity {

    private VectorSimilarity() {}

    // Only if you really want a static version (optional)
    public static double cosineStatic(double[] a, double[] b) {
        // You can copy the logic here if you want — but better to delegate
        // or just delete this method
        return 0.0; // placeholder
    }

    // Better: just leave empty or with 1–2 math helpers
    public static double dotProduct(double[] a, double[] b) {
        if (a.length != b.length) return 0.0;
        double sum = 0.0;
        for (int i = 0; i < a.length; i++) {
            sum += a[i] * b[i];
        }
        return sum;
    }
}