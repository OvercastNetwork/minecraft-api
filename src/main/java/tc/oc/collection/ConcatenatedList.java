package tc.oc.collection;

import java.util.AbstractList;
import java.util.Collection;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;
import java.util.function.Consumer;
import java.util.stream.Stream;

public class ConcatenatedList<T> extends AbstractList<T> {

    public static <T> List<T> of(List<T>... lists) {
        return of(0, lists);
    }

    private static <T> List<T> of(int offset, List<T>... lists) {
        switch(lists.length - offset) {
            case 0: return Collections.emptyList();
            case 1: return lists[offset];
            default: return new ConcatenatedList<>(lists[offset], of(offset + 1, lists));
        }
    }

    public static <T> List<T> of(Collection<? extends List<T>> lists) {
        switch(lists.size()) {
            case 0: return Collections.emptyList();
            case 1: return lists.iterator().next();
            default: return of(lists.iterator());
        }
    }

    public static <T> List<T> of(Iterable<? extends List<T>> lists) {
        return lists instanceof Collection ? of((Collection<? extends List<T>>) lists)
                                           : of(lists.iterator());
    }

    public static <T> List<T> of(Iterator<? extends List<T>> lists) {
        List<T> whole = Collections.emptyList();
        if(lists.hasNext()) {
            whole = lists.next();
            if(lists.hasNext()) {
                whole = new ConcatenatedList<>(whole, of(lists));
            }
        }
        return whole;
    }

    private final List<T> head, tail;

    private ConcatenatedList(List<T> head, List<T> tail) {
        this.head = head;
        this.tail = tail;
    }

    @Override
    public T get(int index) {
        final int cut = head.size();
        return index < cut ? head.get(index) : tail.get(index - cut);
    }

    @Override
    public int size() {
        return head.size() + tail.size();
    }

    @Override
    public boolean isEmpty() {
        return head.isEmpty() && tail.isEmpty();
    }

    @Override
    public int indexOf(Object o) {
        int index = head.indexOf(o);
        if(index >= 0) return index;
        index = tail.indexOf(o);
        return index >= 0 ? head.size() + index : -1;
    }

    @Override
    public int lastIndexOf(Object o) {
        int index = tail.lastIndexOf(o);
        if(index >= 0) return head.size() + index;
        return head.lastIndexOf(o);
    }

    @Override
    public Iterator<T> iterator() {
        return listIterator();
    }

    @Override
    public ListIterator<T> listIterator() {
        return new ConcatenatedListIterator<>(head.listIterator(), tail.listIterator());
    }

    @Override
    public ListIterator<T> listIterator(int index) {
        final int cut = head.size();
        return new ConcatenatedListIterator<>(
            head.listIterator(Math.min(index, cut)),
            tail.listIterator(Math.max(index - cut, 0))
        );
    }

    @Override
    public List<T> subList(int fromIndex, int toIndex) {
        if(fromIndex == toIndex) {
            return Collections.emptyList();
        }

        final int cut = head.size();

        if(fromIndex < cut && toIndex < cut) {
            return head.subList(fromIndex, toIndex);
        }

        if(fromIndex >= cut && toIndex >= cut) {
            return tail.subList(fromIndex - cut, toIndex - cut);
        }

        return new ConcatenatedList<>(
            head.subList(fromIndex, cut),
            tail.subList(0, toIndex - cut)
        );
    }

    @Override
    public boolean contains(Object o) {
        return head.contains(o) || tail.contains(o);
    }

    @Override
    public Stream<T> stream() {
        return Stream.of(head, tail)
                     .flatMap(List::stream);
    }

    @Override
    public void forEach(Consumer<? super T> action) {
        head.forEach(action);
        tail.forEach(action);
    }
}
