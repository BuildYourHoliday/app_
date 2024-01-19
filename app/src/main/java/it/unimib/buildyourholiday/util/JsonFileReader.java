package it.unimib.buildyourholiday.util;

import android.content.Context;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.OutputStreamWriter;

public class JsonFileReader {

    public static String readJsonFromAssets(Context context, String filename) {
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

    public static boolean saveJsonIntoAssets(Context context, String filename, String json) {
        try {
            OutputStream outputStream = context.getAssets().openFd(filename).createOutputStream();
            OutputStreamWriter writer = new OutputStreamWriter(outputStream);
            writer.write(json);
            writer.flush();
            writer.close();
            return true;
        } catch (IOException e) {
            e.printStackTrace();
        }

        return false;
    }
}
