package no.runeflobakk.option;

import static org.apache.commons.collections15.PredicateUtils.notNullPredicate;

import java.util.Iterator;
import java.util.NoSuchElementException;

import no.runeflobakk.iter.ReadOnlyIterator;

import org.apache.commons.collections15.Predicate;
import org.apache.commons.collections15.Transformer;
import org.apache.commons.collections15.functors.NOPTransformer;

public abstract class Optional<V> implements Iterable<V> {


    public static <V> Optional<V> optional(V value) {
        return optional(notNullPredicate(), value);
    }

    public static <V> Optional<V> optional(Predicate<? super V> isPresent, V value) {
        if (isPresent.evaluate(value)) {
            return new Some<V, V>(value);
        } else {
            return None.getInstance();
        }
    }

    public static final class Some<V, O> extends Optional<O> {

        private final V value;
        private final Transformer<? super V, O> transformer;

        private Some(V value) {
            this(value, null);

        }

        @SuppressWarnings("unchecked")
        private Some(V value, Transformer<? super V, O> transformer) {
            this.value = value;
            if (transformer == null) {
                this.transformer = (Transformer<? super V, O>) NOPTransformer.getInstance();
            } else {
                this.transformer = transformer;
            }
        }

        @Override
        public Iterator<O> iterator() {
            return new ReadOnlyIterator<O>() {
                private boolean returned = false;
                @Override
                public boolean hasNext() {
                    return !returned;
                }

                @Override
                public O next() {
                    returned = true;
                    return transformer.transform(value);
                }
            };
        }

        @Override
        public boolean isSome() {
            return true;
        }

        @Override
        public O getOrElse(O fallback) {
            return get();
        }
    }

    public static final class None<V> extends Optional<V> {

        private static final None<?> instance = new None<Object>();

        @SuppressWarnings("unchecked")
        private static <V> None<V> getInstance() {
            return (None<V>) instance;
        }

        @Override
        public Iterator<V> iterator() {
            return new ReadOnlyIterator<V>() {

                @Override
                public boolean hasNext() {
                    return false;
                }

                @Override
                public V next() {
                    throw new NoSuchElementException();
                }
            };
        }

        @Override
        public boolean isSome() {
            return false;
        }

        @Override
        public V getOrElse(V fallback) {
            return fallback;
        }

    }

    public V get() {
        return iterator().next();
    }

    @SuppressWarnings("unchecked")
    public <M> Optional<M> map(Transformer<? super V, M> transformer) {
        if (this instanceof None) {
            return None.getInstance();
        } else {
            return new Some<V, M>(((Some<?, V>) this).get(), transformer);
        }
    }

    public abstract boolean isSome();

    public abstract V getOrElse(V fallback);
}
