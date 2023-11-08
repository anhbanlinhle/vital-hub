package com.example.vital_hub;


import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.action.ViewActions.scrollTo;
import static androidx.test.espresso.matcher.ViewMatchers.isDisplayed;
import static androidx.test.espresso.matcher.ViewMatchers.isNotSelected;
import static androidx.test.espresso.matcher.ViewMatchers.withClassName;
import static androidx.test.espresso.matcher.ViewMatchers.withId;
import static androidx.test.espresso.matcher.ViewMatchers.withText;
import static org.hamcrest.Matchers.allOf;
import static org.hamcrest.Matchers.is;

import static java.lang.Thread.sleep;

import android.view.View;
import android.view.ViewGroup;
import android.view.ViewParent;

import androidx.test.espresso.Espresso;
import androidx.test.espresso.ViewAction;
import androidx.test.espresso.ViewInteraction;
import androidx.test.espresso.action.ViewActions;
import androidx.test.espresso.matcher.ViewMatchers;
import androidx.test.ext.junit.rules.ActivityScenarioRule;
import androidx.test.ext.junit.runners.AndroidJUnit4;
import androidx.test.filters.LargeTest;

import org.hamcrest.Description;
import org.hamcrest.Matcher;
import org.hamcrest.TypeSafeMatcher;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

@LargeTest
@RunWith(AndroidJUnit4.class)
public class HomepageTest {

    @Rule
    public ActivityScenarioRule<SplashScreen> mActivityScenarioRule =
            new ActivityScenarioRule<>(SplashScreen.class);

    @Test
    public void homepageTest() throws InterruptedException {
        sleep(8000);

        ViewInteraction materialButton = onView(
                allOf(withId(R.id.btnLogin), withText("Login with Google"),
                        childAtPosition(
                                childAtPosition(
                                        withId(android.R.id.content),
                                        0),
                                3),
                        isDisplayed()));
        materialButton.perform(click());

        sleep(10000);

        ViewInteraction appCompatImageButton = onView(
                allOf(withId(R.id.cmt_button),
                        childAtPosition(
                                childAtPosition(
                                        withClassName(is("android.widget.LinearLayout")),
                                        3),
                                1),
                        isDisplayed()));
        appCompatImageButton.perform(click());

        sleep(5000);

        for (int i = 0; i < 4; i++) {
            Espresso.onView(ViewMatchers.withId(R.id.comment_recycler)).perform(ViewActions.swipeUp());
            sleep(3000);
        }

        sleep(5000);

        ViewInteraction appCompatImageButton2 = onView(
                allOf(withId(R.id.back_button),
                        childAtPosition(
                                childAtPosition(
                                        withClassName(is("android.widget.LinearLayout")),
                                        0),
                                0),
                        isDisplayed()));
        appCompatImageButton2.perform(click());

        sleep(5000);

        for (int i = 0; i < 4; i++) {
            Espresso.onView(ViewMatchers.withId(R.id.home_page_recycler)).perform(ViewActions.swipeUp());
            sleep(3000);
        }
    }

    private static Matcher<View> childAtPosition(
            final Matcher<View> parentMatcher, final int position) {

        return new TypeSafeMatcher<View>() {
            @Override
            public void describeTo(Description description) {
                description.appendText("Child at position " + position + " in parent ");
                parentMatcher.describeTo(description);
            }

            @Override
            public boolean matchesSafely(View view) {
                ViewParent parent = view.getParent();
                return parent instanceof ViewGroup && parentMatcher.matches(parent)
                        && view.equals(((ViewGroup) parent).getChildAt(position));
            }
        };
    }
}
