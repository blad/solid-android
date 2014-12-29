package com.btellez.solidandroid.test.model;

import android.test.InstrumentationTestCase;

import com.btellez.solidandroid.model.Icon;
import com.btellez.solidandroid.model.IconParser;

import java.io.IOException;
import java.io.InputStream;
import java.util.List;

public class DeserializeTest extends InstrumentationTestCase {

    public void testSearchResultDeserialization() throws Exception {
        String jsonString = readFileFromAssets("search_results.json");
        IconParser.GsonIconParser parser = new IconParser.GsonIconParser();
        List<Icon> icons = parser.fromJson(jsonString, null);
        assertEquals(72, icons.size());
    }

    private String readFileFromAssets(String fileName) {
        try {
            InputStream is = getInstrumentation().getContext().getAssets().open(fileName);
            int size = is.available();
            byte[] buffer = new byte[size];
            is.read(buffer);
            is.close();
            return new String(buffer);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
