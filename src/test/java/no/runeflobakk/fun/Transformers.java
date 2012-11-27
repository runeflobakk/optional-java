package no.runeflobakk.fun;

import org.apache.commons.collections15.Transformer;
import org.apache.commons.collections15.functors.NOPTransformer;

public final class Transformers {

    public static Transformer<String, String> toLowerCase() {
        return new Transformer<String, String>() {
            @Override
            public String transform(String s) {
                return s.toLowerCase();
            }
        };
    }

    public static Transformer<String, String> trimmed() {
        return new Transformer<String, String>() {
            @Override
            public String transform(String s) {
                return s.trim();
            }
        };
    }

    public static <IN, OUT> Chain<IN, IN, OUT> first(Transformer<IN, OUT> first) {
        return new Chain<IN, IN, OUT>(NOPTransformer.<IN>getInstance(), first);
    }

    public static class Chain<IN, INTERMEDIATE, OUT> implements Transformer<IN, OUT> {

        private Transformer<IN, INTERMEDIATE> first;
        private Transformer<? super INTERMEDIATE, OUT> second;

        private Chain(Transformer<IN, INTERMEDIATE> first, Transformer<? super INTERMEDIATE, OUT> second) {
            this.first = first;
            this.second = second;
        }

        @Override
        public OUT transform(IN input) {
            return second.transform(first.transform(input));
        }

        public <NEWOUT> Chain<IN, OUT, NEWOUT> then(Transformer<? super OUT, NEWOUT> next) {
            return new Chain<IN, OUT, NEWOUT>(this, next);
        }
    }

    private Transformers() {}


}
