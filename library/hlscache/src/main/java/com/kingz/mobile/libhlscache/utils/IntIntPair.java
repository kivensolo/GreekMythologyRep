package com.kingz.mobile.libhlscache.utils;

/**
 * 2017/11/14.
 */
public class IntIntPair {
    public int first;
    public int second;

    public IntIntPair(int first, int second) {
        this.first = first;
        this.second = second;
    }

    @Override
    public boolean equals(Object o) {
        if (!(o instanceof IntIntPair)) {
            return false;
        }
        IntIntPair p = (IntIntPair) o;
        return p.first == first && p.second == second;
    }

    @Override
    public int hashCode() {
        return first ^ second;
    }

    @Override
    public String toString() {
        return "[" + first + "," + second + ")";
    }
}
