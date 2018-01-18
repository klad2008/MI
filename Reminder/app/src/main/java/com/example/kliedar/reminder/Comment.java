package com.example.kliedar.reminder;

import android.util.Log;

import org.json.JSONException;
import org.json.JSONObject;

public class Comment {
    int eventID;
    String userID, title, content;
    String json;

    Comment(){

    }

    Comment(String s) throws JSONException {
        JSONObject jsonTmp = new JSONObject(s);
        eventID = jsonTmp.getInt("eventID");
        userID = jsonTmp.getString("userID");
        title = jsonTmp.getString("title");
        content = jsonTmp.getString("content");
        json = s;
        Log.v("222", json);
    }

    Comment(JSONObject jsonTmp) throws JSONException {
        eventID = jsonTmp.getInt("eventID");
        userID = jsonTmp.getString("userID");
        title = jsonTmp.getString("title");
        content = jsonTmp.getString("content");
        json = jsonTmp.toString();
        Log.v("222", json);
    }

    String export(){
        return title + " " + userID + ":" + content;
    }

    String get_json(){
        return json;
    }

    int get_key(){
        return 0;
    }
    static String zero(){
        return "000";
    }
};

