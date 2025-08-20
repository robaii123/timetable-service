package com.gamacore.rag_service.util;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.*;
import java.util.stream.DoubleStream;

public final class VectorUtils {
    private VectorUtils() {}

    private static String fmt(double d, int scale, boolean stripZeros) {
        if (Double.isNaN(d) || Double.isInfinite(d)) d = 0.0;
        BigDecimal bd = BigDecimal.valueOf(d);
        if (scale >= 0) bd = bd.setScale(scale, RoundingMode.HALF_UP);
        if (stripZeros) bd = bd.stripTrailingZeros();
        return bd.toPlainString();
    }

    private static String joinPrefixSuffix(
            int len, String sep, java.util.function.IntFunction<String> valFn) {
        StringBuilder sb = new StringBuilder(Math.max(16, len * 12) + 2);
        sb.append('[');
        for (int i = 0; i < len; i++) {
            if (i > 0) sb.append(sep);
            sb.append(valFn.apply(i));
        }
        sb.append(']');
        return sb.toString();
    }


    public static String toPgVectorLiteral(List<? extends Number> v) {
        return toPgVectorLiteral(v, 7, true, false);
    }

    public static String toPgVectorLiteral(float[] v) {
        return toPgVectorLiteral(v, 7, true, false);
    }

    public static String toPgVectorLiteral(double[] v) {
        return toPgVectorLiteral(v, 7, true, false);
    }

    public static String toPgVectorLiteral(
            List<? extends Number> v, int scale, boolean spaces, boolean stripTrailingZeros) {
        final String sep = spaces ? ", " : ",";
        return joinPrefixSuffix(
                v.size(), sep, i -> fmt(
                        Optional.ofNullable(v.get(i)).map(Number::doubleValue).orElse(0.0),
                        scale, stripTrailingZeros));
    }
    public static String toPgVectorLiteral(
            float[] v, int scale, boolean spaces, boolean stripTrailingZeros) {
        final String sep = spaces ? ", " : ",";
        return joinPrefixSuffix(v.length, sep, i -> fmt(v[i], scale, stripTrailingZeros));
    }

    public static String toPgVectorLiteral(
            double[] v, int scale, boolean spaces, boolean stripTrailingZeros) {
        final String sep = spaces ? ", " : ",";
        return joinPrefixSuffix(v.length, sep, i -> fmt(v[i], scale, stripTrailingZeros));
    }

    public static String toPgVectorLiteralNoSpaces(float[] v) {
        return toPgVectorLiteral(v, 7, false, false);
    }

    public static String toPgVectorLiteralNoSpaces(float[] v, int scale) {
        return toPgVectorLiteral(v, scale, false, false);
    }

    public static String toPgVectorLiteralCompact(float[] v) {
        return toPgVectorLiteral(v, 7, true, true);
    }

    public static String toPgVectorLiteralCompact(double[] v) {
        return toPgVectorLiteral(v, 7, true, true);
    }

    public static String toPgVectorLiteral(float[] v, int scale) {
        return toPgVectorLiteral(v, scale, true, false);
    }

    public static String toPgVectorLiteral(List<? extends Number> v, int scale) {
        return toPgVectorLiteral(v, scale, true, false);
    }


    public static void l2Normalize(float[] v) {
        double sum = 0.0;
        for (float x : v) sum += (double) x * x;
        double norm = Math.sqrt(sum);
        if (norm == 0.0) return;
        for (int i = 0; i < v.length; i++) v[i] = (float) (v[i] / norm);
    }

    public static double[] parsePgVectorLiteral(String literal) {
        String s = literal.trim();
        if (s.startsWith("[") && s.endsWith("]")) s = s.substring(1, s.length() - 1);
        if (s.isEmpty()) return new double[0];
        String[] parts = s.split("\\s*,\\s*");
        double[] out = new double[parts.length];
        for (int i = 0; i < parts.length; i++) {
            try {
                out[i] = Double.parseDouble(parts[i]);
            } catch (NumberFormatException e) {
                out[i] = 0.0;
            }
        }
        return out;
    }
}
