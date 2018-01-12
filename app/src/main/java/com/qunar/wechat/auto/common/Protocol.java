package com.qunar.wechat.auto.common;

import android.util.Log;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

/**
 * Created by lihaibin.li on 2017/11/21.
 */

public class Protocol {
    private static final String TAG = Protocol.class.getSimpleName();
    public static String parseStream(InputStream stream) throws IOException {
        try {
            BufferedReader br = new BufferedReader(new InputStreamReader(stream, "utf-8"));
            StringBuilder sb = new StringBuilder();
            String line = null;
            while ((line = br.readLine()) != null) {
                sb.append(line);
            }

            br.close();
            return sb.toString();
        } catch (IOException e) {
            Log.e(TAG, "error" + e.getLocalizedMessage());
        } finally {
            if (stream != null) {
                stream.close();
            }
        }

        return "";
    }
}
