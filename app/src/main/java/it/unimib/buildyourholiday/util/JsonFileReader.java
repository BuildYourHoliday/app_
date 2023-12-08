package it.unimib.buildyourholiday.util;

import android.content.Context;

import java.io.IOException;
import java.io.InputStream;

public class JsonFileReader {

    public String readJsonFromAssets(Context context, String filename) {
        String styleJson = "";

        try {
            InputStream is = context.getAssets().open(filename);
            int size = is.available();
            byte[] buffer = new byte[size];
            is.read(buffer);
            is.close();
            styleJson = new String(buffer, "UTF-8");
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        return styleJson;
    }
}
