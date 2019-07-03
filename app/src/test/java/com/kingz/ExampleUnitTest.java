package com.kingz;

import com.kingz.utils.NetTools;

import org.junit.Test;

import static org.junit.Assert.assertEquals;

/**
 * Example local unit test, which will execute on the development machine (host).
 *
 * @see <a href="http://d.android.com/tools/testing">Testing documentation</a>
 */
public class ExampleUnitTest {
    @Test
    public void addition_isCorrect() throws Exception {
        int sum = 2 + 2;
		System.out.println("结果为：" + sum);
		assertEquals(4, sum);
    }
}