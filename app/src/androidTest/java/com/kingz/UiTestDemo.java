package com.kingz;

import android.support.test.runner.AndroidJUnit4;

import com.kingz.customdemo.R;

import org.junit.Test;
import org.junit.runner.RunWith;


import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.action.ViewActions.typeText;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.matcher.ViewMatchers.isDisplayed;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static android.support.test.espresso.matcher.ViewMatchers.withText;

@RunWith(AndroidJUnit4.class)
public class UiTestDemo {

    /**
     * UI Auto Test
     */
    @Test
    public void greeterSaysHello() {
        onView(withId(R.id.account_info_id)).perform(typeText("Steve"));
        onView(withId(R.id.alertdialog)).perform(click());
        onView(withText("Hello Steve!")).check(matches(isDisplayed()));
    }
}
