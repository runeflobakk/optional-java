package no.runeflobakk.fun;

import static org.apache.commons.collections15.PredicateUtils.notPredicate;
import static org.apache.commons.lang3.StringUtils.isBlank;

import org.apache.commons.collections15.Predicate;

public final class Predicates {

    public static final Predicate<String> blank() {
        return new Predicate<String>() {
            @Override
            public boolean evaluate(String string) {
                return isBlank(string);
            }
        };
    }

    public static final <T> Predicate<T> not(Predicate<T> predicate) {
        return notPredicate(predicate);
    }

    private Predicates() {}
}
