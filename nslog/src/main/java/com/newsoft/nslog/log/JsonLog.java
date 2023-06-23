package com.newsoft.nslog.log;

import android.util.Log;

import com.newsoft.nslog.NSLog;
import com.newsoft.nslog.NSLogUtil;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class JsonLog {

    public static void printJson(String tag, String msg, String headString) {

        String message;

        try {
            if (msg.startsWith("{")) {
                JSONObject jsonObject = new JSONObject(msg);
                message = jsonObject.toString(NSLog.JSON_INDENT);
            } else if (msg.startsWith("[")) {
                JSONArray jsonArray = new JSONArray(msg);
                message = jsonArray.toString(NSLog.JSON_INDENT);
            } else {
                message = msg;
            }
        } catch (JSONException e) {
            message = msg;
        }

        NSLogUtil.printLine(tag, true);
        message = headString + NSLog.LINE_SEPARATOR + message;
        String[] lines = message.split(NSLog.LINE_SEPARATOR);
        for (String line : lines) {
            Log.d(tag, "║ " + line);
        }
        NSLogUtil.printLine(tag, false);
    }
}
