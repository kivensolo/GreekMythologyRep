package com.kingz.mobile.libhlscache.utils;

/**
 * Created 2017/11/14.
 */
public class StringIntPair {
    public String str;
    public int i;

    public StringIntPair(String str, int i) {
        this.str = str;
        this.i = i;
    }

    @Override
    public boolean equals(Object o) {
        if (!(o instanceof StringIntPair)) {
            return false;
        }
        StringIntPair p = (StringIntPair) o;
        return objectsEqual(p.str, str) && p.i == i;
    }

    private static boolean objectsEqual(Object a, Object b) {
        return a == b || (a != null && a.equals(b));
    }

    @Override
    public int hashCode() {
        return str.hashCode() ^ i;
    }

    @Override
    public String toString() {
        return "[" + str + "," + i + ")";
    }
}
