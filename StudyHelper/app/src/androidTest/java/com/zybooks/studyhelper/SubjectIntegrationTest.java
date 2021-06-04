package com.zybooks.studyhelper;

import android.app.Instrumentation;
import android.content.Context;
import android.content.Intent;

import androidx.test.core.app.ActivityScenario;
import androidx.test.espresso.intent.Intents;
import androidx.test.ext.junit.runners.AndroidJUnit4;
import androidx.test.platform.app.InstrumentationRegistry;
import androidx.test.runner.AndroidJUnitRunner;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import static android.app.Activity.RESULT_OK;
import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.Espresso.openActionBarOverflowOrOptionsMenu;
import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.assertion.ViewAssertions.matches;
import static androidx.test.espresso.intent.Intents.intending;
import static androidx.test.espresso.intent.matcher.IntentMatchers.hasComponent;
import static androidx.test.espresso.matcher.ViewMatchers.withId;
import static androidx.test.espresso.matcher.ViewMatchers.withText;

@RunWith(AndroidJUnit4.class)
public class SubjectIntegrationTest extends AndroidJUnitRunner {

    private Context mAppContext;
    private Subject mTestSubject;

    @Before
    public void createTestSubject() {
        mAppContext = InstrumentationRegistry.getInstrumentation().getTargetContext();
        StudyDatabase studyDb = StudyDatabase.getInstance(mAppContext);

        mTestSubject = new Subject("TEST SUBJECT");
        long newId = studyDb.subjectDao().insertSubject(mTestSubject);
        mTestSubject.setId(newId);
    }

    @After
    public void deleteTestSubject() {
        StudyDatabase studyDb = StudyDatabase.getInstance(mAppContext);
        studyDb.subjectDao().deleteSubject(mTestSubject);
    }

    @Test
    public void testAddQuestion() {

        Intents.init();

        // Create a test question
        String questionText = "TEST QUESTION";
        Question question = new Question();
        question.setSubjectId(mTestSubject.getId());
        question.setText(questionText);
        question.setAnswer("TEST ANSWER");

        // Add question to database
        StudyDatabase studyDb = StudyDatabase.getInstance(mAppContext);
        long newId = studyDb.questionDao().insertQuestion(question);
        question.setId(newId);

        // Create an ActivityResult stub that returns the question ID of the question added
        Intent data = new Intent();
        data.putExtra(QuestionEditActivity.EXTRA_QUESTION_ID, question.getId();
        Instrumentation.ActivityResult activityResult = new Instrumentation.ActivityResult(RESULT_OK, data);

        // Verify QuestionEditActivity tried to start and respond with stub result
        intending(hasComponent(QuestionEditActivity.class.getName())
                .respondWith(activityResult);

        // Start QuestionActivity with the test subject
        Intent intent = new Intent(mAppContext, QuestionActivity.class);
        intent.putExtra(QuestionActivity.EXTRA_SUBJECT_ID, mTestSubject.getId());
        ActivityScenario<QuestionActivity> activityScenario = ActivityScenario.launch(intent);

        // Open overflow menu
        openActionBarOverflowOrOptionsMenu(mAppContext);

        // Click on the Add menu item
        onView(withText("Add")).perform(click());

        // Verify the added question is displayed
        onView(withId(R.id.questionText)).check(matches(withText(questionText)));

        activityScenario.close();

        Intents.release();
    }
}