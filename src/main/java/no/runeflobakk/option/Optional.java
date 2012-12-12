package no.runeflobakk.option;

import static org.apache.commons.collections15.PredicateUtils.notNullPredicate;

import java.util.Iterator;
import java.util.NoSuchElementException;

import no.runeflobakk.iter.ReadOnlyIterator;

import org.apache.commons.collections15.Predicate;
import org.apache.commons.collections15.Transformer;

/**
 * An <code>Optional</code> wraps a value that is either defined or
 * undefined. Usually, undefined means <code>null</code>, and defined means
 * an object instance, but the distinction between defined and undefined can
 * be customized with a {@link Predicate}.
 *
 * @param <V> The type of the wrapped object.
 *
 * @see #optional(Object)
 * @see #optional(Predicate, Object)
 */
public abstract class Optional<V> implements Iterable<V> {

    /**
     * Wrap an object or <code>null</code> in an <code>Optional</code>.
     *
     * @param value The object to wrap. May be <code>null</code>.
     * @param <V>   The type of the wrapped object.
     *
     * @return {@link Some} if <code>value</code> is an object,
     *         {@link None} if <code>value</code> is <code>null</code>
     *
     * @see Optional
     * @see Some
     * @see None
     */
    public static <V> Optional<V> optional(V value) {
        return optional(notNullPredicate(), value);
    }


    /**
     * Wrap an object in an <code>Optional</code>, using a predicate to
     * determine if the value should be treated as defined.
     *
     * @param <V>       The type of the wrapped object.
     * @param isPresent A {@link Predicate} function returning true if
     *                  the value is considered defined or 'present'. This
     *                  function must be able to handle <code>null</code>.
     *
     * @param value     The object to wrap. May be <code>null</code>.
     *
     *
     * @return          {@link Some} if <code>value</code> is defined,
     *                  {@link None} otherwise.
     *
     * @see Optional
     * @see Some
     * @see None
     */
    public static <V> Optional<V> optional(Predicate<? super V> isPresent, V value) {
        if (isPresent.evaluate(value)) {
            return new Some<V>(value);
        } else {
            return None.getInstance();
        }
    }




    /**
     * Wrapper for a 'defined' value. You never under normal
     * circumstances refer to this type.
     *
     * @param <V> The type of the wrapped object.
     * @param <O> The type of the obtained object from this wrapper.
     */
    public static final class Some<V> extends Optional<V> {

        private final V value;

        private Some(V value) {
            this.value = value;
        }

        @Override
        public final Iterator<V> iterator() {
            return new ReadOnlyIterator<V>() {
                private boolean returned = false;
                @Override
                public boolean hasNext() {
                    return !returned;
                }

                @Override
                public V next() {
                    returned = true;
                    return value;
                }
            };
        }

        @Override
        public final boolean isSome() {
            return true;
        }

        @Override
        public final V getOrElse(V fallback) {
            return get();
        }

        @Override
        public <O> Optional<O> map(Transformer<? super V, O> transformer) {
            O mapped = transformer.transform(this.value);
            return optional(mapped);
        }

        @Override
        public <O> Optional<O> map(Predicate<? super O> isPresent, Transformer<? super V, O> transformer) {
            O mapped = transformer.transform(this.value);
            return optional(isPresent, mapped);
        }
    }

    /**
     * Wrapper for an 'undefined' value. You never under normal
     * circumstances refer to this type.
     *
     * @param <V> The type of the undefined value.
     */
    public static final class None<V> extends Optional<V> {

        private static final None<?> instance = new None<Object>();

        @SuppressWarnings("unchecked")
        private static <V> None<V> getInstance() {
            return (None<V>) instance;
        }

        @Override
        public final Iterator<V> iterator() {
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
        public final boolean isSome() {
            return false;
        }

        @Override
        public final V getOrElse(V fallback) {
            return fallback;
        }

        @Override
        public <O> Optional<O> map(Transformer<? super V, O> transformer) {
            return None.getInstance();
        }

        @Override
        public <O> Optional<O> map(Predicate<? super O> isPresent, Transformer<? super V, O> transformer) {
            return None.getInstance();
        }

    }



    /**
     * Obtain the value contained in the <code>Optional</code>. To use
     * this method one <em>must</em> first determine that the
     * <code>Optional</code> does in fact hold a value.
     *
     * To obtain the wrapped value, it is in general preferable to favor
     * <em>iterating</em> or using {@link #getOrElse(V)}, which will never
     * throw an exception.
     *
     * @throws NoSuchElementException if this method is called on an
     *         <code>Optional</code> without a defined value.
     *
     * @see #isSome()
     */
    public final V get() {
        return iterator().next();
    }



    /**
     * Map this <code>Optional</code> to another type of <code>Optional</code>.
     *
     * @param transformer A {@link Transformer} function which transforms
     *                    (or maps) the wrapped value to another value. This
     *                    function will only ever be called if the
     *                    <code>Optional</code> holds a defined value.
     *
     * @return A new <code>Optional</code> from which the new value can be
     *         obtained, if it is defined.
     */
    public abstract <O> Optional<O> map(Transformer<? super V, O> transformer);


    /**
     * Map this <code>Optional</code> to another type of <code>Optional</code>.
     *
     * @see #map(Transformer)
     * @see #optional(Predicate, Object)
     */
    public abstract <O> Optional<O> map(Predicate<? super O> isPresent, Transformer<? super V, O> transformer);


    /**
     * @return <code>true</code> if the <code>Optional</code> holds a
     *         defined value, or <code>false</code> otherwise, which usually
     *         means that the wrapped value is <code>null</code>.
     */
    public abstract boolean isSome();


    /**
     *
     * @param fallback A value to return if called on a {@link None}.
     * @return
     */
    public abstract V getOrElse(V fallback);

}
