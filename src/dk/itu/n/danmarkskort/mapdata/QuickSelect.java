package dk.itu.n.danmarkskort.mapdata;

import java.util.ArrayList;
import java.util.Random;

public class QuickSelect {

    // finds the Kth smallest item of the list.
    // In out case the median, aka the length/2-th item
    public static Comparable quickSelect(ArrayList<Comparable> list, int k) {
        shuffle(list);

        int lo = 0, hi = list.size() - 1;

        while(hi > lo) {
            int j = partition(list, lo, hi);
            if(j == k) return list.get(k);
            else if(j > k) hi = j - 1;
            else if(j < k ) lo = j + 1;
        }
        return list.get(k);
    }

    private static int partition(ArrayList<Comparable> list, int lo, int hi) {
        int i = lo, j = hi + 1;
        Comparable v = list.get(lo);
        while(true) {
            while(less(list.get(++i), v)) if(i == hi) break;
            while(less(v, list.get(--j))) if(j == lo) break;
            if(i >= j) break;
            swap(list, i, j);
        }
        swap(list, lo, j);
        return j;
    }

    private static void swap(ArrayList<Comparable> list, int a, int b) {
        Comparable temp = list.get(a);
        list.add(a, list.get(b));
        list.add(b, temp);
    }

    private static void shuffle(ArrayList<Comparable> list) {
        Random random = new Random();
        int size = list.size();
        for(int i = 0; i < size; i++) {
            int rn = random.nextInt() + size - 1;
            swap(list, i, rn);
        }
    }

    private static boolean less(Comparable a, Comparable b) {
        return a.compareTo(b) < 0;
    }
}
