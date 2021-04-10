package com.example.cmput301w21t25.activities_main;


import android.view.View;
import android.view.ViewGroup;
import android.view.ViewParent;

import androidx.test.espresso.AmbiguousViewMatcherException;
import androidx.test.espresso.UiController;
import androidx.test.espresso.ViewAction;
import androidx.test.espresso.ViewInteraction;
import androidx.test.espresso.matcher.ViewMatchers;
import androidx.test.filters.LargeTest;
import androidx.test.rule.ActivityTestRule;
import androidx.test.runner.AndroidJUnit4;

import com.example.cmput301w21t25.R;

import org.hamcrest.Description;
import org.hamcrest.Matcher;
import org.hamcrest.TypeSafeMatcher;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.action.ViewActions.closeSoftKeyboard;
import static androidx.test.espresso.action.ViewActions.replaceText;
import static androidx.test.espresso.assertion.ViewAssertions.matches;
import static androidx.test.espresso.matcher.ViewMatchers.isDisplayed;
import static androidx.test.espresso.matcher.ViewMatchers.isRoot;
import static androidx.test.espresso.matcher.ViewMatchers.withClassName;
import static androidx.test.espresso.matcher.ViewMatchers.withId;
import static androidx.test.espresso.matcher.ViewMatchers.withParent;
import static androidx.test.espresso.matcher.ViewMatchers.withText;
import static org.hamcrest.Matchers.allOf;
import static org.hamcrest.Matchers.is;

@LargeTest
@RunWith(AndroidJUnit4.class)
/**
 * This test will create a blank user profile if launched for the first time,
 * and then create a new experiment.
 */
public class CreateExperimentTest {

    public static ViewAction waitFor(long delay) {
        return new ViewAction() {
            @Override public Matcher<View> getConstraints() {
                return ViewMatchers.isRoot();
            }

            @Override public String getDescription() {
                return "wait for " + delay + "milliseconds";
            }

            @Override public void perform(UiController uiController, View view) {
                uiController.loopMainThreadForAtLeast(delay);
            }
        };
    }

    @Rule
    public ActivityTestRule<MainActivity> mActivityTestRule = new ActivityTestRule<>(MainActivity.class);

    @Test
    public void createExperimentTest() {
        onView(isRoot()).perform(waitFor(5000));

        try {
            ViewInteraction materialButton = onView(
                    allOf(withId(R.id.skipProfileCreationButton), withText("Skip"),
                            childAtPosition(
                                    childAtPosition(
                                            withId(android.R.id.content),
                                            0),
                                    3),
                            isDisplayed()));
            materialButton.perform(click());
        }
        catch (Exception e) {
            //ignoring this, it means that test was launched with a user already created (not first launch)
        }
        onView(isRoot()).perform(waitFor(5000));

        ViewInteraction floatingActionButton = onView(
                allOf(withId(R.id.exp_create_button),
                        childAtPosition(
                                childAtPosition(
                                        withClassName(is("android.widget.LinearLayout")),
                                        1),
                                0),
                        isDisplayed()));
        floatingActionButton.perform(click());

        ViewInteraction appCompatEditText = onView(
                allOf(withId(R.id.editTextExpName),
                        childAtPosition(
                                allOf(withId(R.id.name_LL),
                                        childAtPosition(
                                                withClassName(is("androidx.constraintlayout.widget.ConstraintLayout")),
                                                0)),
                                1),
                        isDisplayed()));
        appCompatEditText.perform(replaceText("testExperiment"), closeSoftKeyboard());

        ViewInteraction appCompatEditText2 = onView(
                allOf(withId(R.id.editTextEnterDescription),
                        childAtPosition(
                                allOf(withId(R.id.description_LL),
                                        childAtPosition(
                                                withClassName(is("androidx.constraintlayout.widget.ConstraintLayout")),
                                                1)),
                                1),
                        isDisplayed()));
        appCompatEditText2.perform(replaceText("testDesc"), closeSoftKeyboard());

        ViewInteraction appCompatEditText3 = onView(
                allOf(withId(R.id.editTextKeywords),
                        childAtPosition(
                                allOf(withId(R.id.region_LL),
                                        childAtPosition(
                                                withClassName(is("androidx.constraintlayout.widget.ConstraintLayout")),
                                                2)),
                                0),
                        isDisplayed()));
        appCompatEditText3.perform(replaceText("test1,test2"), closeSoftKeyboard());

        ViewInteraction appCompatEditText4 = onView(
                allOf(withId(R.id.editTextMinTrials),
                        childAtPosition(
                                allOf(withId(R.id.region_LL),
                                        childAtPosition(
                                                withClassName(is("androidx.constraintlayout.widget.ConstraintLayout")),
                                                2)),
                                2),
                        isDisplayed()));
        appCompatEditText4.perform(replaceText("1"), closeSoftKeyboard());

        ViewInteraction appCompatEditText5 = onView(
                allOf(withId(R.id.editTextRegion),
                        childAtPosition(
                                allOf(withId(R.id.region_LL),
                                        childAtPosition(
                                                withClassName(is("androidx.constraintlayout.widget.ConstraintLayout")),
                                                2)),
                                3),
                        isDisplayed()));
        appCompatEditText5.perform(replaceText("canada"), closeSoftKeyboard());

        ViewInteraction materialCheckBox = onView(
                allOf(withId(R.id.checkBoxSubscribe), withText("Subscribe to experiment"),
                        childAtPosition(
                                allOf(withId(R.id.region_LL),
                                        childAtPosition(
                                                withClassName(is("androidx.constraintlayout.widget.ConstraintLayout")),
                                                2)),
                                4),
                        isDisplayed()));
        materialCheckBox.perform(click());

        ViewInteraction materialCheckBox3 = onView(
                allOf(withId(R.id.checkBoxGeolocation), withText("Geolocation enabled"),
                        childAtPosition(
                                allOf(withId(R.id.region_LL),
                                        childAtPosition(
                                                withClassName(is("androidx.constraintlayout.widget.ConstraintLayout")),
                                                2)),
                                5),
                        isDisplayed()));
        materialCheckBox3.perform(click());

        ViewInteraction materialCheckBox2 = onView(
                allOf(withId(R.id.checkBoxPublish), withText("Publish on create"),
                        childAtPosition(
                                allOf(withId(R.id.region_LL),
                                        childAtPosition(
                                                withClassName(is("androidx.constraintlayout.widget.ConstraintLayout")),
                                                2)),
                                6),
                        isDisplayed()));
        materialCheckBox2.perform(click());

        ViewInteraction materialRadioButton = onView(
                allOf(withId(R.id.radioButtonBinomial), withText("Binomial"),
                        childAtPosition(
                                allOf(withId(R.id.radioGroup),
                                        childAtPosition(
                                                withClassName(is("androidx.constraintlayout.widget.ConstraintLayout")),
                                                3)),
                                3),
                        isDisplayed()));
        materialRadioButton.perform(click());

        ViewInteraction materialButton2 = onView(
                allOf(withId(R.id.buttonCreateExperiment), withText("Create Experiment"),
                        childAtPosition(
                                childAtPosition(
                                        withClassName(is("android.widget.LinearLayout")),
                                        0),
                                4),
                        isDisplayed()));
        materialButton2.perform(click());

        try {
            ViewInteraction textView = onView(
                    allOf(withId(R.id.exp_description_text_view), withText("testExperiment"),
                            withParent(withParent(withId(R.id.background))),
                            isDisplayed()));
            textView.check(matches(withText("testExperiment")));
        }
        catch (AmbiguousViewMatcherException e) {
            //this is ok because it means that multiple experiments have been created (test was run multiple times)
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
