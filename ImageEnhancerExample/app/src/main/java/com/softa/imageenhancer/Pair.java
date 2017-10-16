package com.softa.imageenhancer;

import java.util.Comparator;

public class Pair implements Comparator<Pair> ,Comparable<Pair>{
    public final int index;
    public double value;

    public Pair(int index, double value) {
        this.index = index;
        this.value = value;
    }

    @Override
    public int compareTo(Pair other) {
        return Double.valueOf(this.value).compareTo(other.value);
    }
    // Overriding the compare method, to sort by index instead
    public int compare(Pair p0, Pair p1) {
        return p0.index - p1.index;
    }
    //returns value instead
    public double getValue(){
        return value;
    }

    public void setValue(double k){
        //this necessary?
        this.value=k;
    }
}
