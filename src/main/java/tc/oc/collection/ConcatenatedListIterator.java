package tc.oc.collection;

import java.util.ListIterator;

import com.google.common.collect.UnmodifiableListIterator;

import static com.google.common.base.Preconditions.checkArgument;

public class ConcatenatedListIterator<T> extends UnmodifiableListIterator<T> {

    private final ListIterator<T> head, tail;

    public ConcatenatedListIterator(ListIterator<T> head, ListIterator<T> tail) {
        checkArgument(!(head.hasNext() && tail.hasPrevious()));
        this.head = head;
        this.tail = tail;
    }

    @Override
    public boolean hasNext() {
        return head.hasNext() || tail.hasNext();
    }

    @Override
    public T next() {
        return (head.hasNext() ? head : tail).next();
    }

    @Override
    public boolean hasPrevious() {
        return tail.hasPrevious() || head.hasPrevious();
    }

    @Override
    public T previous() {
        return (tail.hasPrevious() ? tail : head).previous();
    }

    @Override
    public int nextIndex() {
        return head.hasNext() ? head.nextIndex() : head.nextIndex() + tail.nextIndex();
    }

    @Override
    public int previousIndex() {
        return tail.hasPrevious() ? head.nextIndex() + tail.previousIndex() : head.previousIndex();
    }
}
