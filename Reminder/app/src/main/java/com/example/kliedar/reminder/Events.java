package com.example.kliedar.reminder;

import android.util.Log;

import org.json.JSONException;
import org.json.JSONObject;

public class Events{
    int eventID, permission, approve, fin;
    String userID, title, content, startTime, endTime;
    String json;

    Events(){

    }

    Events(String s) throws JSONException {
        JSONObject jsonTmp = new JSONObject(s);
        eventID = jsonTmp.getInt("eventID");
        permission = jsonTmp.getInt("permission");
        approve = jsonTmp.getInt("approve");
        fin = jsonTmp.getInt("fin");
        userID = jsonTmp.getString("userID");
        title = jsonTmp.getString("title");
        content = jsonTmp.getString("content");
        startTime = jsonTmp.getString("startTime");
        endTime = jsonTmp.getString("endTime");
        json = s;
        Log.v("222", json);
    }

    Events(JSONObject jsonTmp) throws JSONException {
        eventID = jsonTmp.getInt("eventID");
        permission = jsonTmp.getInt("permission");
        approve = jsonTmp.getInt("approve");
        fin = jsonTmp.getInt("fin");
        userID = jsonTmp.getString("userID");
        title = jsonTmp.getString("title");
        content = jsonTmp.getString("content");
        startTime = jsonTmp.getString("startTime");
        endTime = jsonTmp.getString("endTime");
        json = jsonTmp.toString();
        Log.v("222", json);
    }

    String export(){
        String Sout = title;
        return Sout;
    }

    String get_json(){
        return json;
    }

    int get_key(){
//        return Integer.parseInt(startTime());
        return 0;
    }
    static String zero(){
        return "000";
    }
};

