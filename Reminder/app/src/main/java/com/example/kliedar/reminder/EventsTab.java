package com.example.kliedar.reminder;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.Arrays;
import java.util.Comparator;

import static com.example.kliedar.reminder.staticPATH.stdURL;

public class EventsTab extends AppCompatActivity {

    Events[] EventsList;
    String[] EventsListString;
    int EventsListLength;
    Intent intent_n;
    String Status, UserID;
    ListView EventsListView;
    TextView StatusText;
    Button CreateEventTabButton, SearchCommentTabButton;
    Button UserTabButton, NearbyTabButton, EventsTabButton;

    void switchToUserTab(){
        UserTabButton.setOnClickListener(
                new View.OnClickListener(){
                    @Override
                    public void onClick(View view){
                        Intent intent = new Intent();
                        intent.putExtra("userID", UserID);
                        intent.setClass(EventsTab.this, UserTab.class);
                        startActivity(intent);
                    }
                }
        );
    }

    void switchToEventsTab(){
        EventsTabButton.setOnClickListener(
                new View.OnClickListener(){
                    @Override
                    public void onClick(View view){
                        Intent intent = new Intent();
                        intent.putExtra("userID", UserID);
                        intent.setClass(EventsTab.this, EventsTab.class);
                        startActivity(intent);
                    }
                }
        );
    }

    void switchToNearbyTab(){
        NearbyTabButton.setOnClickListener(
                new View.OnClickListener(){
                    @Override
                    public void onClick(View view){
                        Intent intent = new Intent();
                        intent.putExtra("userID", UserID);
                        intent.setClass(EventsTab.this, NearbyTab.class);
                        startActivity(intent);
                    }
                }
        );
    }

    void switchToCreateEventTab(){
        CreateEventTabButton.setOnClickListener(
                new View.OnClickListener(){
                    @Override
                    public void onClick(View view){
                        Intent intent = new Intent();
                        intent.putExtra("userID", UserID);
                        intent.setClass(EventsTab.this, CreateEventTab.class);
                        startActivity(intent);
                    }
                }
        );
    }

    void switchToSearchCommentTab(){
        SearchCommentTabButton.setOnClickListener(
                new View.OnClickListener(){
                    @Override
                    public void onClick(View view){
                        Intent intent = new Intent();
                        intent.putExtra("userID", UserID);
                        intent.setClass(EventsTab.this, SearchCommentTab.class);
                        startActivity(intent);
                    }
                }
        );
    }

    String queryEvents(){
        String url = stdURL + "/queryEvents/";
        String content = "";
        content = content + "UserID=" + UserID + "&";
        content = content + "fin=" + "0";
        return NetUtils.post(url, content);
    }

    void RefreshEventsList(){
        new Thread(new Runnable() {
            @Override
            public void run() {
                String Tmp = queryEvents();
                try {
                    JSONObject json = new JSONObject(Tmp);
                    int stat = Integer.parseInt(json.getString("errCode"));
                    switch (stat){
                        case 0 :
                            Status = "事件添加成功";
                            break;
                        default:
                            Status = "程序异常终止";
                    }
                    JSONArray json_events = new JSONArray(json.getString("events"));

                    EventsListLength = json_events.length();
                    EventsList = new Events[EventsListLength];
                    for (int i = 0; i < EventsListLength; i++) {
                        EventsList[i] = new Events(json_events.getJSONObject(i));
                    }
                    Arrays.sort(EventsList, new Comparator<Events>() {
                        @Override
                        public int compare(Events o1, Events o2) {
                            return o1.get_key() - o2.get_key();
                        }
                    });

                    EventsListString = new String[EventsListLength];
                    for (int i = 0; i < EventsListLength; i++) {
                        EventsListString[i] = EventsList[i].export();
                    }

                } catch (JSONException e) {
                    Status = "json解析失败 : " + Tmp;

                    EventsListLength = 0;
                    EventsListString = new String[0];
                    e.printStackTrace();
                }
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        StatusText.setText(Status);
                        ArrayAdapter<String> adapter = new ArrayAdapter<String>(EventsTab.this,android.R.layout.simple_list_item_1, EventsListString);
                        EventsListView.setAdapter(adapter);
                        EventsListView.setOnItemClickListener(new AdapterView.OnItemClickListener(){
                            @Override
                            public void onItemClick(AdapterView<?> adapter, View v, int position, long id){
                                if (id == -1) return;
                                Intent intent = new Intent();
                                intent.putExtra("userID", UserID);
                                intent.putExtra("event", EventsList[(int) id].get_json());
                                intent.setClass(EventsTab.this,ChangeEventTab.class);
                                startActivity(intent);
                            }
                        });

                    }
                });
            }
        }).start();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.eventspage);

        intent_n = getIntent();
        UserID = intent_n.getStringExtra("userID");

        StatusText = (TextView) findViewById(R.id.status_text);
        EventsListView = (ListView) findViewById(R.id.events_list);

        CreateEventTabButton = (Button) findViewById(R.id.create_events_button);
        SearchCommentTabButton = (Button) findViewById(R.id.search_comment_button);

        UserTabButton = (Button) findViewById(R.id.UserTab);
        EventsTabButton = (Button) findViewById(R.id.EventsTab);
        NearbyTabButton = (Button) findViewById(R.id.NearbyTab);

        switchToUserTab();
//        switchToEventsTab();
        switchToNearbyTab();
        switchToCreateEventTab();
        switchToSearchCommentTab();

        RefreshEventsList();
    }

}
