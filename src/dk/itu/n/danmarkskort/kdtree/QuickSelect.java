package dk.itu.n.danmarkskort.kdtree;

import java.util.Random;

import dk.itu.n.danmarkskort.newmodels.ParsedItem;

public class QuickSelect {

    // finds the Kth smallest item of the list.
    // In out case the median, aka the length/2-th item
    public static ParsedItem quickSelect(ParsedItem[] array, int k, boolean sortByLon) {
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

    private static int partition(ParsedItem[] array, int lo, int hi, boolean sortByLon) {
        int i = lo, j = hi + 1;
        ParsedItem v = array[lo];
        while(true) {
            while(less(array[++i], v, sortByLon)) if(i == hi) break;
            while(less(v, array[--j], sortByLon)) if(j == lo) break;
            if(i >= j) break;
            swap(array, i, j);
        }
        swap(array, lo, j);
        return j;
    }

    private static void swap(ParsedItem[] array, int a, int b) {
        ParsedItem temp = array[a];
        array[a] = array[b];
        array[b] = temp;
    }

    private static void shuffle(ParsedItem[] array) {
        Random random = new Random();
        for(int i = 0; i < array.length; i++) {
            int rn = random.nextInt(array.length);
            swap(array, i, rn);
        }
    }

    private static boolean less(ParsedItem a, ParsedItem b, boolean sortByLon) {
        if(sortByLon) return a.compareLon(b) < 1;
        return a.compareLat(b) < 1;
    }
}
