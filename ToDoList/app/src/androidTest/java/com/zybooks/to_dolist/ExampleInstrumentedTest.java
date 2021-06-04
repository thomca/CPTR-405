package com.zybooks.to_dolist;

import android.content.Context;
import androidx.test.platform.app.InstrumentationRegistry;
import androidx.test.ext.junit.runners.AndroidJUnit4;
import org.junit.Test;
import org.junit.runner.RunWith;
import java.io.File;
import static org.junit.Assert.*;

@RunWith(AndroidJUnit4.class)
public class ExampleInstrumentedTest {

    @Test
    public void testFileSaved() throws Exception {
        // Get Context for the application being instrumented
        Context appContext = InstrumentationRegistry.getInstrumentation().getTargetContext();

        // Delete the file in case it exists
        File file = new File(appContext.getFilesDir(), ToDoList.FILENAME);
        file.delete();
        assertFalse(file.exists());

        // Save the file
        ToDoList list = new ToDoList(appContext);
        list.saveToFile();

        // Make sure the file now exists
        assertTrue(file.exists());
    }
}