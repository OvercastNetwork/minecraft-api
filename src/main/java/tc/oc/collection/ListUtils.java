package tc.oc.collection;

import java.util.List;
import java.util.function.Predicate;

public class ListUtils {

    public static <T> int indexOf(List<T> list, Predicate<T> filter) {
        final int size = list.size();
        for(int i = 0; i < size; i++) {
            if(filter.test(list.get(i))) return i;
        }
        return -1;
    }

    public static <T> int lastIndexOf(List<T> list, Predicate<T> filter) {
        for(int i = list.size() - 1; i >= 0; i--) {
            if(filter.test(list.get(i))) return i;
        }
        return -1;
    }
}
