package com.zybooks.to_dolist;

import android.content.Context;

import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.TemporaryFolder;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.PrintWriter;
import java.nio.file.Files;
import java.nio.file.Paths;

import static org.junit.Assert.assertArrayEquals;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class ExampleUnitTest {

    @Mock
    Context mMockContext;

    @Rule
    public TemporaryFolder mTempFolder = new TemporaryFolder();

    public final String[] TODOS = { "mow lawn", "feed dog" };

    private File createTempFile() throws IOException {

        // Create temp file that holds the todos
        File tempFile = mTempFolder.newFile("tempfile");
        PrintWriter writer = new PrintWriter(tempFile);
        for (String item : TODOS) {
            writer.println(item);
        }
        writer.close();
        return tempFile;
    }

    @Test
    public void testSaveToFile() throws Exception {

        // Create a file for the todos in the temporary folder
        File writeFile = mTempFolder.newFile(ToDoList.FILENAME);

        // Provide alternative behavior when openFileOutput() is called
        when(mMockContext.openFileOutput(ToDoList.FILENAME, Context.MODE_PRIVATE))
                .thenReturn(new FileOutputStream(writeFile));

        // Add the todos to the list and save the file
        ToDoList list = new ToDoList(mMockContext);
        for (String item : TODOS) {
            list.addItem(item);
        }
        list.saveToFile();

        // Create temp file with same todos
        File tempFile = createTempFile();

        // Verify files are identical
        byte[] expectedBytes = Files.readAllBytes(Paths.get(tempFile.getAbsolutePath()));
        byte[] actualBytes = Files.readAllBytes(Paths.get(writeFile.getAbsolutePath()));
        assertArrayEquals(expectedBytes, actualBytes);
    }

}
