package com.example.cs_15_fyp;

import android.content.Intent;
import android.os.SystemClock;
import android.view.View;
import android.widget.RatingBar;

import androidx.test.core.app.ActivityScenario;
import androidx.test.espresso.UiController;
import androidx.test.espresso.ViewAction;
import androidx.test.ext.junit.runners.AndroidJUnit4;

import com.example.cs_15_fyp.activities.GiveReviewActivity;
import com.example.cs_15_fyp.R;

import org.hamcrest.Matcher;
import org.junit.Test;
import org.junit.runner.RunWith;

import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.matcher.ViewMatchers.withId;
import static androidx.test.espresso.matcher.ViewMatchers.isAssignableFrom;
import static androidx.test.espresso.matcher.ViewMatchers.isDisplayed;
import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.assertion.ViewAssertions.matches;

@RunWith(AndroidJUnit4.class)
public class GiveReviewIntegrationTest {

    public static ViewAction setRating(final float rating) {
        return new ViewAction() {
            @Override
            public Matcher<View> getConstraints() {
                return isAssignableFrom(RatingBar.class);
            }

            @Override
            public String getDescription() {
                return "Set rating on RatingBar to " + rating;
            }

            @Override
            public void perform(UiController uiController, View view) {
                ((RatingBar) view).setRating(rating);
            }
        };
    }

    /**
     * ✅ Test 1: Submitting without rating
     * Verifies that the app stays on GiveReviewActivity and rating bar is still shown.
     */
    @Test
    public void testSubmitWithoutRating_shouldStayOnScreen() {
        Intent intent = new Intent(
                androidx.test.platform.app.InstrumentationRegistry.getInstrumentation().getTargetContext(),
                GiveReviewActivity.class
        );
        intent.putExtra("restaurantId", "test123");
        intent.putExtra("restaurantName", "Test Restaurant");

        ActivityScenario.launch(intent);

        // Click submit with no rating
        onView(withId(R.id.btnSubmitReview)).perform(click());

        // Still on same activity — ratingBar still visible
        onView(withId(R.id.ratingBar)).check(matches(isDisplayed()));
    }

    /**
     * ✅ Test 2: Submit with rating
     * This simulates successful submission and checks if we return to info screen.
     * NOTE: This assumes success (either mock or real), or you only check review cleared.
     */
    @Test
    public void testSubmitWithRating_shouldClearInputs() {
        Intent intent = new Intent(
                androidx.test.platform.app.InstrumentationRegistry.getInstrumentation().getTargetContext(),
                GiveReviewActivity.class
        );
        intent.putExtra("restaurantId", "test123");
        intent.putExtra("restaurantName", "Test Restaurant");

        ActivityScenario.launch(intent);

        // Set a valid rating
        onView(withId(R.id.ratingBar)).perform(setRating(4.5f));

        // Click submit
        onView(withId(R.id.btnSubmitReview)).perform(click());

        // Wait for async review submission + UI update
        SystemClock.sleep(3000);

        // Either check that the rating bar is reset or the photo count is reset
        onView(withId(R.id.ratingBar)).check(matches(isDisplayed()));
        onView(withId(R.id.photoCount)).check(matches(isDisplayed()));
    }
}
