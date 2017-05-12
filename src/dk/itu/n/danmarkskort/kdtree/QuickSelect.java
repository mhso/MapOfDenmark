package dk.itu.n.danmarkskort.kdtree;

import java.util.Random;

public class QuickSelect {

    // finds the Kth smallest item of the list.
    // In out case the median, aka the length/2-th item
    static KDComparable quickSelect(KDComparable[] array, int k, boolean sortByLon) {
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

    private static void swap(KDComparable[] array, int a, int b) {
        KDComparable temp = array[a];
        array[a] = array[b];
        array[b] = temp;
    }

    private static void shuffle(KDComparable[] array) {
        Random random = new Random();
        for(int i = 0; i < array.length; i++) {
            int rn = random.nextInt(array.length);
            swap(array, i, rn);
        }
    }

    /*
    * Returns true if 'a' is less than 'b', otherwise returns false
     */
    private static boolean less(KDComparable a, KDComparable b, boolean sortByLon) {
        double valueA, valueB;
        
        if(sortByLon) {
            valueA = a.getFirstNode().getX();
            valueB = b.getFirstNode().getX();
        } else {
            valueA = a.getFirstNode().getY();
            valueB = b.getFirstNode().getY();
        }
        return valueA < valueB;
    }
}
