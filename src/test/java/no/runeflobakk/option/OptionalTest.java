package no.runeflobakk.option;

import static no.runeflobakk.fun.Predicates.blank;
import static no.runeflobakk.fun.Predicates.not;
import static no.runeflobakk.fun.Transformers.toLowerCase;
import static no.runeflobakk.fun.Transformers.toNull;
import static no.runeflobakk.fun.Transformers.trimmed;
import static no.runeflobakk.option.Optional.optional;
import static org.apache.commons.collections15.PredicateUtils.truePredicate;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.sameInstance;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import java.util.NoSuchElementException;

import no.runeflobakk.option.Optional.None;
import no.runeflobakk.option.Optional.Some;

import org.junit.Test;


public class OptionalTest {

    @Test
    public void optionalNullIsNone() {
        assertTrue(optional(null) instanceof None);
    }

    @Test
    public void optionalValueIsSome() {
        assertTrue(optional("value") instanceof Some);
    }

    @Test
    public void iteratesOneTimeOverSome() {
        int iterations = 0;
        for (String value : optional("value")) {
            assertThat(value, is("value"));
            iterations++;
        }
        assertThat(iterations, is(1));
    }

    @Test
    @SuppressWarnings("unused")
    public void doesNotIterateOverNone() {
        for (Object value : optional(null)) {
            fail("should not iterate over None");
        }
    }

    @Test(expected = NoSuchElementException.class)
    public void throwsExceptionIfGettingAValueFromNone() {
        optional(null).get();
    }

    @Test
    public void getTheValue() {
        assertThat(optional("value").get(), is("value"));
    }

    @Test
    public void mapTheValue() {
        assertThat(
                optional("    VALUE    ")
                .map(toLowerCase()).map(trimmed()).get(), is("value"));
    }

    @Test
    public void severalMappingsOfSomeValue() {
        assertThat(optional(" X ").map(toLowerCase()).map(trimmed()).get(), is("x"));
    }

    @Test
    public void severalMappingOfNone() {
        assertThat(optional((String) null).map(toLowerCase()).map(trimmed()).getOrElse("y"), is("y"));
    }

    @Test
    public void itIsPossibleToMapANone() {
        String nonExistingString = null;
        for (String none : optional(nonExistingString).map(toLowerCase())) {
            fail("it is possible to map a None, but it should not iterate. Got value " + none);
        }
    }

    @Test(expected = NoSuchElementException.class)
    public void throwsExceptionWhenGettingAMappedNone() {
        String nonExistingString = null;
        optional(nonExistingString).map(toLowerCase()).get();
    }

    @Test
    public void canAskIfValueIsPresent() {
        assertTrue(optional("value").isSome());
        assertFalse(optional(null).isSome());
    }

    @Test
    public void canDecideSomeOrNoneBasedOnPredicate() {
        assertThat(optional(not(blank()), "").isSome(), is(false));
        assertThat(optional(not(blank()), null).isSome(), is(false));
        assertTrue(optional(not(blank()), "abc").isSome());
    }

    @Test
    public void getOrElseOnNoneReturnsElse() {
        assertThat(optional((String) null).getOrElse("else"), is("else"));
    }

    @Test
    public void getOrElseOnSomeReturnsTheOptionalValue() {
        assertThat(optional("some").getOrElse("else"), is("some"));
    }

    @Test(expected = UnsupportedOperationException.class)
    public void removingFromAnOptionalIteratorIsAbsurd() {
        optional("x").iterator().remove();
    }

    @Test
    public void mapperFunctionReturningNullCreatesNone() {
        Optional<?> none = optional("whatever").map(toNull());
        assertFalse(none.isSome());
    }

    @Test
    public void mappingToNoneWithFailingPredicate() {
        Optional<?> none = optional("   ").map(not(blank()), trimmed());
        assertFalse(none.isSome());
    }

    @Test
    public void mappingNoneAlwaysGiveSameNone() {
        Optional<String> none = optional((String) null);
        Optional<String> mappedNone1 = none.map(toLowerCase());
        Optional<String> mappedNone2 = none.map(truePredicate(), toLowerCase());

        assertThat(none, sameInstance(mappedNone1));
        assertThat(none, sameInstance(mappedNone2));
    }


}
