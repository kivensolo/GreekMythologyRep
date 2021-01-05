package com.kingz.mobile.libhlscache.bean;


import com.kingz.mobile.libhlscache.utils.Pair;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

/**
 * int 数据段，[x1, x2) [x3, x4)，连续将自动合并。
 * Created  2017/11/14.
 */
public class IntRanges {
    private List<Pair<Integer,Integer>> ranges = new ArrayList<>();

    public void addInt(int n) {
        ranges.add(Pair.create(n, n + 1));
        merge();
    }

    public Pair contains(int num) {
        for (Pair<Integer,Integer> range : ranges) {
            if (range.first <= num && num < range.second) {
                return range;
            }
        }

        return null;
    }

    public List<Pair<Integer,Integer>> getRanges() {
        return ranges;
    }

    private void merge() {
        if (ranges.isEmpty() || ranges.size() == 1) {
            return;
        }

        Collections.sort(ranges, new Comparator<Pair<Integer,Integer>>() {
            @Override
            public int compare(Pair<Integer,Integer> intIntPair, Pair<Integer,Integer> t1) {
                return intIntPair.first - t1.first;
            }
        });


        List<Pair<Integer,Integer>> newRanges = new ArrayList<>();
        int start = ranges.get(0).first;
        int end = ranges.get(0).second;
        for (int i = 1; i < ranges.size(); i++) {
            Pair<Integer,Integer> pair = ranges.get(i);
            if (end >= pair.first) {
                end = pair.second;
            } else {
                newRanges.add(Pair.create(start, end));
                start = pair.first;
                end = pair.second;
            }
        }
        newRanges.add(Pair.create(start, end));
        ranges = newRanges;
    }

    @Override
    public String toString() {
        return ranges.toString();
    }
}
