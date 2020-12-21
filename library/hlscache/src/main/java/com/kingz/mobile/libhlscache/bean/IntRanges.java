package com.kingz.mobile.libhlscache.bean;

import com.kingz.mobile.libhlscache.utils.IntIntPair;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

/**
 * int 数据段，[x1, x2) [x3, x4)，连续将自动合并。
 * Created  2017/11/14.
 */
public class IntRanges {
    private List<IntIntPair> ranges = new ArrayList<>();

    public void addInt(int n) {
        ranges.add(new IntIntPair(n, n + 1));

        merge();
    }

    public IntIntPair contains(int num) {
        for (IntIntPair range : ranges) {
            if (range.first <= num && num < range.second) {
                return range;
            }
        }

        return null;
    }

    public List<IntIntPair> getRanges() {
        return ranges;
    }

    private void merge() {
        if (ranges.isEmpty() || ranges.size() == 1) {
            return;
        }

        Collections.sort(ranges, new Comparator<IntIntPair>() {
            @Override
            public int compare(IntIntPair intIntPair, IntIntPair t1) {
                return intIntPair.first - t1.first;
            }
        });


        List<IntIntPair> newRanges = new ArrayList<>();
        int start = ranges.get(0).first;
        int end = ranges.get(0).second;
        for (int i = 1; i < ranges.size(); i++) {
            IntIntPair pair = ranges.get(i);
            if (end >= pair.first) {
                end = pair.second;
            } else {
                newRanges.add(new IntIntPair(start, end));
                start = pair.first;
                end = pair.second;
            }
        }
        newRanges.add(new IntIntPair(start, end));
        ranges = newRanges;
    }

    @Override
    public String toString() {
        return ranges.toString();
    }
}
