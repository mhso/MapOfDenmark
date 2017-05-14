package dk.itu.n.danmarkskort.kdtree;

import java.util.Random;

/**
 * All methods in this class, except the less() method, are from the book Algorithms, by Robert Sedgewick and Kevin Wayne
 * (4th edition)
 *
 * It's an implementation of the QuickSelect algorithm, which is able to find the k'th largest element in an array, and
 * in the process order the array such that all items with a lower index than k i smaller than the k'th item, and every
 * item with a higher index is not smaller.
 *
 * Worst-case performance is O(n^2), but since we start out by shuffling the array, we reach the best-case/average-case
 * of O(n).
 *
 */

public class QuickSelect {

    /**
     * Takes an input array, and finds the index for the k'th largest element, either according to longitude or latitude.
     * At the same time, the array is being reordered, such that all elements at indices lower than k, are smaller that the
     * element at position k, and all with higher index values are not smaller.
     *
     * @param array array of KDComparable objects, to be looked at, and potentially have their index positions changed.
     * @param k The index position we look for.
     * @param sortByLon If true we compare elements by their longitude values, if false: latitude values.
     * @return the k-index element in the rearranged array.
     */
    static KDComparable quickSelect(KDComparable[] array, int k, boolean sortByLon) {
        // shuffling decreases the chances of hitting the worst-case scenario
        shuffle(array);

        int lo = 0, hi = array.length - 1;

        while(hi > lo) {
            int j = partition(array, lo, hi, sortByLon);
            if(j == k) return array[k];
            else if(j > k) hi = j - 1;
            else if(j < k ) lo = j + 1;
        }
        return array[k];
    }

    /**
     * From Algorithms, by Robert Sedgewick and Kevin Wayne, 4th edition, page 291:
     *
     * "This code partitions on the item v in a[lo]. The main loop exits when the scan indices i and j cross.
     * Within the loop, we increment i while a[i] is less than v and decrement j while a[j] is greater than
     * v, then do an exchange to maintain the invariant property that no entries to the left of i are greater
     * than v and no entries to the right of j are smaller than v. Once the indices meet, we complete the
     * partitioning by exchanging a[lo] with a[j] (thus leaving the partitioning value in a[j] )."
     *
     * @param array array being looked at, and on which we potentially manipulate the elements' index positions.
     * @param lo the lowest index position we compare with.
     * @param hi the highest index position we compare with.
     * @param sortByLon determines wether we compare elements according to longitude (true) or latitude (false).
     * @return j, the index position where v has been swapped to.
     */
    private static int partition(KDComparable[] array, int lo, int hi, boolean sortByLon) {
        int i = lo, j = hi + 1;
        KDComparable v = array[lo];
        while(true) {
            while(less(array[++i], v, sortByLon)) if(i == hi) break;
            while(less(v, array[--j], sortByLon)) if(j == lo) break;
            if(i >= j) break;
            swap(array, i, j);
        }
        swap(array, lo, j);
        return j;
    }

    /**
     * Swaps the position of two elements in an array of KDComparable objects.
     *
     * @param array The array the elements belong to.
     * @param a Index position of the first element.
     * @param b Index position of the second element.
     */
    private static void swap(KDComparable[] array, int a, int b) {
        KDComparable temp = array[a];
        array[a] = array[b];
        array[b] = temp;
    }

    /**
     * Shuffles an array, by looping through all index positions, and swapping the element there with another, at
     * a random index position.
     *
     * It's guaranteed that every index position gets a random index position.
     *
     * @param array Array of KDComparable objects.
     */
    private static void shuffle(KDComparable[] array) {
        Random random = new Random();
        for(int i = 0; i < array.length; i++) {
            int rn = random.nextInt(array.length);
            swap(array, i, rn);
        }
    }

    /**
     * Compares two KDComparable objects, and determines if the first objects firstNode (coordinate) is smaller than
     * the second's.
     *
     * @param a The first object.
     * @param b The second object.
     * @param sortByLon Determines whether comparison happens according to longitude (true) or latitude (false) values.
     * @return True if a's value is less than b's, else returns false.
     */
    private static boolean less(KDComparable a, KDComparable b, boolean sortByLon) {
        double valueA, valueB;
        
        if(sortByLon) {
            valueA = a.getFirstNode().x;
            valueB = b.getFirstNode().x;
        } else {
            valueA = a.getFirstNode().y;
            valueB = b.getFirstNode().y;
        }
        return valueA < valueB;
    }
}
