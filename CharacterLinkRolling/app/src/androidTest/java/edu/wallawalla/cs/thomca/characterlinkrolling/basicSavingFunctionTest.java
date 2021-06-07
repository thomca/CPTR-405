package edu.wallawalla.cs.thomca.characterlinkrolling;


import android.view.View;
import android.view.ViewGroup;
import android.view.ViewParent;

import androidx.test.espresso.DataInteraction;
import androidx.test.espresso.ViewInteraction;
import androidx.test.filters.LargeTest;
import androidx.test.rule.ActivityTestRule;
import androidx.test.runner.AndroidJUnit4;

import org.hamcrest.Description;
import org.hamcrest.Matcher;
import org.hamcrest.TypeSafeMatcher;
import org.hamcrest.core.IsInstanceOf;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import static androidx.test.espresso.Espresso.onData;
import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.action.ViewActions.closeSoftKeyboard;
import static androidx.test.espresso.action.ViewActions.pressImeActionButton;
import static androidx.test.espresso.action.ViewActions.replaceText;
import static androidx.test.espresso.assertion.ViewAssertions.matches;
import static androidx.test.espresso.contrib.RecyclerViewActions.actionOnItemAtPosition;
import static androidx.test.espresso.matcher.ViewMatchers.isDisplayed;
import static androidx.test.espresso.matcher.ViewMatchers.withClassName;
import static androidx.test.espresso.matcher.ViewMatchers.withContentDescription;
import static androidx.test.espresso.matcher.ViewMatchers.withId;
import static androidx.test.espresso.matcher.ViewMatchers.withParent;
import static androidx.test.espresso.matcher.ViewMatchers.withText;
import static org.hamcrest.Matchers.allOf;
import static org.hamcrest.Matchers.anything;
import static org.hamcrest.Matchers.is;

@LargeTest
@RunWith(AndroidJUnit4.class)
public class basicSavingFunctionTest {

    @Rule
    public ActivityTestRule<MainActivity> mActivityTestRule = new ActivityTestRule<>(MainActivity.class);

    @Test
    public void basicSavingFunctionTest() {
        ViewInteraction materialButton = onView(
                allOf(withId(R.id.setCharacterButton), withText("Update Character"),
                        childAtPosition(
                                childAtPosition(
                                        withId(R.id.main_fragment_container),
                                        0),
                                9),
                        isDisplayed()));
        materialButton.perform(click());

        ViewInteraction appCompatEditText = onView(
                allOf(withId(R.id.characterNameEditText),
                        childAtPosition(
                                childAtPosition(
                                        withId(android.R.id.content),
                                        0),
                                1),
                        isDisplayed()));
        appCompatEditText.perform(replaceText("Rin"), closeSoftKeyboard());

        ViewInteraction appCompatSpinner = onView(
                allOf(withId(R.id.classSpinner),
                        childAtPosition(
                                childAtPosition(
                                        withId(android.R.id.content),
                                        0),
                                3),
                        isDisplayed()));
        appCompatSpinner.perform(click());

        DataInteraction appCompatCheckedTextView = onData(anything())
                .inAdapterView(childAtPosition(
                        withClassName(is("android.widget.PopupWindow$PopupBackgroundView")),
                        0))
                .atPosition(3);
        appCompatCheckedTextView.perform(click());

        ViewInteraction switchCompat = onView(
                allOf(withId(R.id.sharingStatusClassSwitch),
                        childAtPosition(
                                childAtPosition(
                                        withId(android.R.id.content),
                                        0),
                                5),
                        isDisplayed()));
        switchCompat.perform(click());

        ViewInteraction materialButton2 = onView(
                allOf(withId(R.id.newCharacter), withText("New Character"),
                        childAtPosition(
                                childAtPosition(
                                        withId(android.R.id.content),
                                        0),
                                7),
                        isDisplayed()));
        materialButton2.perform(click());

        ViewInteraction button = onView(
                allOf(withId(R.id.saveActionButton), withText("SAVE ACTION"),
                        withParent(withParent(withId(R.id.main_fragment_container))),
                        isDisplayed()));
        button.check(matches(isDisplayed()));

        ViewInteraction actionMenuItemView = onView(
                allOf(withId(R.id.loadCharacterInMenu), withContentDescription("Update Character"),
                        childAtPosition(
                                childAtPosition(
                                        withId(R.id.my_toolbar),
                                        1),
                                0),
                        isDisplayed()));
        actionMenuItemView.perform(click());

        ViewInteraction textView = onView(
                allOf(withId(R.id.itemNameTab), withText("Rin"),
                        withParent(withParent(IsInstanceOf.<View>instanceOf(androidx.recyclerview.widget.RecyclerView.class))),
                        isDisplayed()));
        textView.check(matches(withText("Rin")));

        ViewInteraction recyclerView = onView(
                childAtPosition(
                        withId(R.id.custom),
                        0));
        recyclerView.perform(actionOnItemAtPosition(1, click()));

        ViewInteraction appCompatEditText2 = onView(
                allOf(withId(R.id.modifierField), withText("0"),
                        childAtPosition(
                                childAtPosition(
                                        withId(R.id.main_fragment_container),
                                        0),
                                6),
                        isDisplayed()));
        appCompatEditText2.perform(replaceText("12"));

        ViewInteraction appCompatEditText3 = onView(
                allOf(withId(R.id.modifierField), withText("12"),
                        childAtPosition(
                                childAtPosition(
                                        withId(R.id.main_fragment_container),
                                        0),
                                6),
                        isDisplayed()));
        appCompatEditText3.perform(closeSoftKeyboard());

        ViewInteraction appCompatEditText4 = onView(
                allOf(withId(R.id.modifierField), withText("12"),
                        childAtPosition(
                                childAtPosition(
                                        withId(R.id.main_fragment_container),
                                        0),
                                6),
                        isDisplayed()));
        appCompatEditText4.perform(pressImeActionButton());

        ViewInteraction materialButton3 = onView(
                allOf(withId(R.id.saveActionButton), withText("Save Action"),
                        childAtPosition(
                                childAtPosition(
                                        withId(R.id.main_fragment_container),
                                        0),
                                7),
                        isDisplayed()));
        materialButton3.perform(click());

        ViewInteraction editText = onView(
                allOf(withId(R.id.ActionModifierField), withText("12"),
                        withParent(withParent(withId(R.id.custom))),
                        isDisplayed()));
        editText.check(matches(withText("12")));

        ViewInteraction appCompatEditText5 = onView(
                allOf(withId(R.id.ActionNameField),
                        childAtPosition(
                                childAtPosition(
                                        withId(R.id.custom),
                                        0),
                                1),
                        isDisplayed()));
        appCompatEditText5.perform(replaceText("Test Action"), closeSoftKeyboard());

        ViewInteraction appCompatEditText6 = onView(
                allOf(withId(R.id.ActionNameField), withText("Test Action"),
                        childAtPosition(
                                childAtPosition(
                                        withId(R.id.custom),
                                        0),
                                1),
                        isDisplayed()));
        appCompatEditText6.perform(pressImeActionButton());

        ViewInteraction appCompatEditText7 = onView(
                allOf(withId(R.id.ActionModifierField), withText("12"),
                        childAtPosition(
                                childAtPosition(
                                        withId(R.id.custom),
                                        0),
                                7),
                        isDisplayed()));
        appCompatEditText7.perform(pressImeActionButton());

        ViewInteraction materialButton4 = onView(
                allOf(withId(R.id.ActionSaveActionButton), withText("Save Action"),
                        childAtPosition(
                                childAtPosition(
                                        withId(R.id.custom),
                                        0),
                                8),
                        isDisplayed()));
        materialButton4.perform(click());

        ViewInteraction actionMenuItemView2 = onView(
                allOf(withId(R.id.selectDiceInMenu), withContentDescription("Select dice"),
                        childAtPosition(
                                childAtPosition(
                                        withId(R.id.my_toolbar),
                                        1),
                                1),
                        isDisplayed()));
        actionMenuItemView2.perform(click());

        ViewInteraction textView2 = onView(
                allOf(withId(R.id.itemNameTab), withText("Test Action"),
                        withParent(withParent(IsInstanceOf.<View>instanceOf(androidx.recyclerview.widget.RecyclerView.class))),
                        isDisplayed()));
        textView2.check(matches(withText("Test Action")));
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
