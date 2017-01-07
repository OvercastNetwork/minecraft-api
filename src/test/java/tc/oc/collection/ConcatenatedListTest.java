package tc.oc.collection;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;

import org.junit.Test;

import static org.junit.Assert.*;
import static tc.oc.test.Assert.*;

public class ConcatenatedListTest {

    private <T> void assertList(List<T> expected, List<T> actual) {
        assertList(expected, actual, true);
    }

    private <T> void assertList(List<T> expected, List<T> actual, boolean sublists) {
        // Comparison
        assertEquals(expected, actual);
        assertEquals(expected.hashCode(), actual.hashCode());

        // Queries
        assertEquals(expected.isEmpty(), actual.isEmpty());
        assertEquals(expected.size(), actual.size());
        assertTrue(actual.containsAll(expected));

        // Indexed
        for(int i = 0; i < expected.size(); i++) {
            final T e = expected.get(i);
            assertEquals(e, actual.get(i));
            assertTrue(actual.contains(e));
            assertEquals(expected.indexOf(e), actual.indexOf(e));
            assertEquals(expected.lastIndexOf(e), actual.lastIndexOf(e));
        }

        // Iterator / ListIterator
        final Iterator<T> iter = actual.iterator();
        final ListIterator<T> liter = actual.listIterator();
        int i = 0;

        assertFalse(liter.hasPrevious());
        assertEquals(-1, liter.previousIndex());

        for(; i < expected.size(); i++) {
            final T e = actual.get(i);

            assertTrue(liter.hasNext());
            assertSame(e, iter.next());

            assertTrue(liter.hasNext());
            assertEquals(i, liter.nextIndex());
            assertSame(e, liter.next());
            assertTrue(liter.hasPrevious());
            assertEquals(i, liter.previousIndex());
        }

        assertFalse(iter.hasNext());

        assertFalse(liter.hasNext());
        assertEquals(actual.size(), liter.nextIndex());

        for(i--; i >= 0; i--) {
            assertTrue(liter.hasPrevious());
            assertEquals(i, liter.previousIndex());
            assertSame(liter.previous(), actual.get(i));
            assertTrue(liter.hasNext());
            assertEquals(i, liter.nextIndex());
        }

        assertFalse(liter.hasPrevious());
        assertEquals(-1, liter.previousIndex());

        // Stream
        final Iterator<T> iter1 = actual.iterator();
        actual.forEach(e -> assertSame(iter1.next(), e));

        final Iterator<T> iter2 = actual.iterator();
        actual.stream().forEach(e -> assertSame(iter2.next(), e));

        if(sublists) {
            for(int from = 0; from <= actual.size(); from++) {
                for(int to = from; to <= actual.size(); to++) {
                    final List<T> ref = new ArrayList<>(to - from);
                    for(int j = from; j < to; j++) {
                        ref.add(actual.get(j));
                    }
                    assertList(ref, actual.subList(from, to), false);
                }
            }
        }
    }

    @Test
    public void empty() throws Throwable {
        assertList(Collections.emptyList(), ConcatenatedList.of());
    }

    @Test
    public void singleton() throws Throwable {
        final List<Integer> list = Arrays.asList(1, 2, 3);
        assertList(list, ConcatenatedList.of(list));
    }

    @Test
    public void twoLists() throws Throwable {
        assertList(Arrays.asList(1, 2, 3, 4, 5),
                   ConcatenatedList.of(Arrays.asList(1, 2, 3), Arrays.asList(4, 5)));
    }

    @Test
    public void manyLists() throws Throwable {
        assertList(Arrays.asList(1, 2, 3, 4, 5, 6, 7, 8, 9),
                   ConcatenatedList.of(Arrays.asList(),
                                       Arrays.asList(1, 2, 3),
                                       Arrays.asList(4, 5),
                                       Arrays.asList(),
                                       Arrays.asList(6, 7, 8),
                                       Arrays.asList(9),
                                       Arrays.asList()));
    }
}
