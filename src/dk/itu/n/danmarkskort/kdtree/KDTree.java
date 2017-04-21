package dk.itu.n.danmarkskort.kdtree;

import dk.itu.n.danmarkskort.models.Region;

import java.io.Serializable;
import java.util.Iterator;
import java.util.List;

public abstract class KDTree<T extends KDComparable> implements Serializable, Iterable<T> {

	private static final long serialVersionUID = 5138300688014828078L;

    abstract List<KDComparable[]> getItems(Region reg);

    abstract List<KDComparable[]> getItems(Region reg, boolean sortByLon);

    abstract List<KDComparable[]> getAllItems();

    public Iterator<T> iterator() { return new KDTreeIterator(); }

    public Iterator<T> iterator(Region reg) { return new KDTreeIterator(reg); }

    static KDComparable[] listToArray(List list) {
        KDComparable[] arr = new KDComparable[list.size()];
        for(int i = 0; i < arr.length; i++) arr[i] = (KDComparable) list.get(i);
        return arr;
    }

    private class KDTreeIterator implements Iterator<T> {
        List<KDComparable[]> arrList;
        int i = 0;
        int j = 0;

        KDTreeIterator() { arrList = getAllItems(); }

        KDTreeIterator(Region reg) { arrList = getItems(reg); }

        /**
         * Returns {@code true} if the iteration has more elements.
         * (In other words, returns {@code true} if {@link #next} would
         * return an element rather than throwing an exception.)
         *
         * @return {@code true} if the iteration has more elements
         */
        @Override
        public boolean hasNext() { return i < arrList.size(); }

        /**
         * Returns the next element in the iteration.
         *
         * @return the next element in the iteration
         * @throws NoSuchElementException if the iteration has no more elements
         */
        @SuppressWarnings("unchecked")
        @Override
        public T next() {
            T next = (T) arrList.get(i)[j++];
            if(j > arrList.get(i).length - 1) {
                j = 0;
                i++;
            }
            return next;
        }
    }
}