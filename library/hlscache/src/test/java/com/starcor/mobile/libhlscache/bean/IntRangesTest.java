package com.starcor.mobile.libhlscache.bean;

import com.kingz.mobile.libhlscache.bean.IntRanges;
import com.kingz.mobile.libhlscache.utils.IntIntPair;

import org.junit.Test;

import java.util.Arrays;

import static org.junit.Assert.assertEquals;

/**
 * Created  2017/11/14.
 */
public class IntRangesTest {
    @Test
    public void addInt() throws Exception {
        IntRanges intRanges = new IntRanges();

        intRanges.addInt(1);
        intRanges.addInt(1);
        assertEquals(Arrays.asList(new IntIntPair(1, 2)), intRanges.getRanges());

        intRanges.addInt(1);
        assertEquals(Arrays.asList(new IntIntPair(1, 2)), intRanges.getRanges());

        intRanges.addInt(2);
        assertEquals(Arrays.asList(new IntIntPair(1, 3)), intRanges.getRanges());

        intRanges.addInt(3);
        assertEquals(Arrays.asList(new IntIntPair(1, 4)), intRanges.getRanges());

        intRanges.addInt(15);
        intRanges.addInt(16);
        intRanges.addInt(17);
        assertEquals(Arrays.asList(new IntIntPair(1, 4), new IntIntPair(15, 18)), intRanges.getRanges());

        intRanges.addInt(5);
        intRanges.addInt(6);
        intRanges.addInt(6);
        intRanges.addInt(7);
        assertEquals(Arrays.asList(new IntIntPair(1, 4), new IntIntPair(5, 8), new IntIntPair(15, 18)), intRanges.getRanges());

        intRanges.addInt(4);
        assertEquals(Arrays.asList(new IntIntPair(1, 8), new IntIntPair(15, 18)), intRanges.getRanges());

        intRanges.addInt(8);
        intRanges.addInt(9);
        intRanges.addInt(10);
        intRanges.addInt(11);
        intRanges.addInt(12);
        intRanges.addInt(12);
        intRanges.addInt(13);
        intRanges.addInt(14);
        assertEquals(Arrays.asList(new IntIntPair(1, 18)), intRanges.getRanges());
    }

}