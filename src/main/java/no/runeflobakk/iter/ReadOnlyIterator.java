package no.runeflobakk.iter;

import java.util.Iterator;

public abstract class ReadOnlyIterator<T> implements Iterator<T> {

    /**
     * Throws {@link UnsupportedOperationException}.
     */
    @Override
    public final void remove() {
        throw new UnsupportedOperationException(getClass().getName() + " does not support remove() operation");
    };
}
