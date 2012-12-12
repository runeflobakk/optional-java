package no.runeflobakk.fun;

import org.apache.commons.collections15.Transformer;

public final class Transformers {

    public static Transformer<String, String> toLowerCase() {
        return new Transformer<String, String>() {
            @Override public String transform(String s) { return s.toLowerCase(); }
        };
    }

    public static Transformer<String, String> trimmed() {
        return new Transformer<String, String>() {
            @Override public String transform(String s) { return s.trim(); }
        };
    }

    public static <T> Transformer<T, T> toNull() {
        return new Transformer<T, T>() {
            @Override public T transform(T input) { return null; }
        };
    }

    private Transformers() {} static { new Transformers(); }

}
