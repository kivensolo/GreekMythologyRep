package com.kingz;

import android.content.Context;
import android.support.test.InstrumentationRegistry;
import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;

import com.kingz.customdemo.R;
import com.kingz.utils.NetTools;
import com.kingz.view.animation.ShakeAnimation;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.action.ViewActions.closeSoftKeyboard;
import static android.support.test.espresso.action.ViewActions.typeText;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static android.support.test.espresso.matcher.ViewMatchers.withText;
import static junit.framework.TestCase.assertEquals;

/**
 * Instrumentation test, which will execute on an Android device.
 *
 * @see <a href="http://d.android.com/tools/testing">Testing documentation</a>
 * Espresso 测试
 */
@RunWith(AndroidJUnit4.class)
public class LoginUiInstrumentedTest {
    @Rule
    public ActivityTestRule<ShakeAnimation> mShakePageRule = new ActivityTestRule<>(ShakeAnimation.class);
    private Context mContext;
    public ShakeAnimation shakePage = null;
    private static final String STRING_TO_BE_TYPED = "KingZ";

    @Before
    public void setMainActivity() {
        mContext = InstrumentationRegistry.getTargetContext();
        this.shakePage = mShakePageRule.getActivity();
    }

    /**
     * UI Auto Test
     */
    @Test
    public void login(){
        // step-1: 通过id找到eidtText,输入KingZ，然后关闭键盘
        onView(withId(R.id.user_name_edit_text)).perform(typeText(STRING_TO_BE_TYPED), closeSoftKeyboard());

        // step-2: 通过文字找到button,并执行点击事件
        onView(withText(R.string.login)).perform(click());

        // step-3: 将eidtText上的文本同预期结果对比，如果一致则测试通过
        String expectedText = mContext.getString(R.string.user_error_tips);
        onView(withId(R.id.user_name_edit_text)).check(matches(withText(expectedText))); //line 3
    }

    /**
     * 顺序测试
     * @throws Exception
     */
    @Test
    public void test_sque()throws Exception {
        testPackageName();
        net_isConnect();
    }


    @Test
    public void testPackageName() throws Exception {
        assertEquals("com.kingz.customdemo", mContext.getPackageName());
    }


    @Test
    public void net_isConnect() throws Exception{
        assertEquals(true,NetTools.isConnect());
    }

}
