package com.example.kliedar.reminder;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
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

public class NearbyTab extends AppCompatActivity {

    Intent intent_n;
    String Status, UserID;
    TextView StatusText, UserIDText;
    Button LogOutButton, ChangePasswordButton;
    Button UserTabButton, NearbyTabButton, EventsTabButton;
    int NearbyListLength;
    Events[] NearbyList;
    String[] NearbyListString;
    ListView NearbyListView;


    void switchToUserTab(){
        UserTabButton.setOnClickListener(
                new View.OnClickListener(){
                    @Override
                    public void onClick(View view){
                        Intent intent = new Intent();
                        intent.putExtra("userID", UserID);
                        intent.setClass(NearbyTab.this, UserTab.class);
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
                        intent.setClass(NearbyTab.this, EventsTab.class);
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
                        intent.setClass(NearbyTab.this, NearbyTab.class);
                        startActivity(intent);
                    }
                }
        );
    }

    String queryNearby(){
        String url = stdURL + "/queryNearby/";
        String content = "";
        content = content + "UserID=" + UserID + "&";
        return NetUtils.post(url, content);
    }

    void RefreshNearbyList(){
        new Thread(new Runnable() {
            @Override
            public void run() {
                String Tmp = queryNearby();
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
                    JSONArray json_events = new JSONArray(json.getString("nearby"));

                    NearbyListLength = json_events.length();
                    NearbyList = new Events[NearbyListLength];
                    for (int i = 0; i < NearbyListLength; i++) {
                        NearbyList[i] = new Events(json_events.getJSONObject(i));
                    }
                    Arrays.sort(NearbyList, new Comparator<Events>() {
                        @Override
                        public int compare(Events o1, Events o2) {
                            return o1.get_key() - o2.get_key();
                        }
                    });

                    NearbyListString = new String[NearbyListLength];
                    for (int i = 0; i < NearbyListLength; i++) {
                        NearbyListString[i] = NearbyList[i].export();
                    }

                } catch (JSONException e) {
                    Status = "json解析失败 : " + Tmp;

                    NearbyListLength = 0;
                    NearbyListString = new String[0];
                    e.printStackTrace();
                }
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        StatusText.setText(Status);
                        ArrayAdapter<String> adapter = new ArrayAdapter<String>(NearbyTab.this,android.R.layout.simple_list_item_1, NearbyListString);
                        NearbyListView.setAdapter(adapter);
                        NearbyListView.setOnItemClickListener(new AdapterView.OnItemClickListener(){
                            @Override
                            public void onItemClick(AdapterView<?> adapter, View v, int position, long id){
                                if (id == -1) return;
                                Intent intent = new Intent();
                                intent.putExtra("userID", UserID);
                                intent.putExtra("event", NearbyList[(int) id].get_json());
                                intent.setClass(NearbyTab.this, CommentEventTab.class);
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
        setContentView(R.layout.nearbypage);

        intent_n = getIntent();
        UserID = intent_n.getStringExtra("userID");

        StatusText = (TextView) findViewById(R.id.status_text);
        NearbyListView = (ListView) findViewById(R.id.nearby_list);

        UserTabButton = (Button) findViewById(R.id.UserTab);
        EventsTabButton = (Button) findViewById(R.id.EventsTab);
        NearbyTabButton = (Button) findViewById(R.id.NearbyTab);

        switchToUserTab();
        switchToEventsTab();
//        switchToNearbyTab();

        RefreshNearbyList();
    }

}
