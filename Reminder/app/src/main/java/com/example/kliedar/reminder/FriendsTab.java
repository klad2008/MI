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

public class FriendsTab extends AppCompatActivity {

    String[] FriendsListString;
    int FriendsListLength;
    Intent intent_n;
    String Status, UserID;
    ListView FriendsListView;
    Button ReturnButton, AddFriendButton, DeleteFriendButton;
    TextView StatusText, uBIDText;

    String updateFriend(int tmp){
        String url = stdURL + "/updateFriends/";
        String content = "";
        content = content + "uBID=" + uBIDText.getText() + "&";
        content = content + "friendStatus=" + String.valueOf(tmp);
        return NetUtils.post(url, content);
    }

    void setAddFriendButton(){
        AddFriendButton.setOnClickListener(
            new View.OnClickListener(){
                @Override
                public void onClick(View view){
                    new Thread(new Runnable(){
                        @Override
                        public void run(){
                            String Tmp = updateFriend(1);
                            try {
                                JSONObject json = new JSONObject(Tmp);
                                int stat = Integer.parseInt(json.getString("errCode"));
                                switch (stat){
                                    case 0 :
                                        Status = "添加成功";
                                        break;
                                    case 1 :
                                        Status = "用户未找到";
                                        break;
                                    case 2 :
                                        Status = "好友已存在";
                                        break;
                                    case 3 :
                                        Status = "请先登录";
                                        break;
                                    default:
                                        Status = "程序异常终止";
                                }
                            } catch (JSONException e) {
                                Status = "json解析失败 : " + Tmp;
                                e.printStackTrace();
                            }
                            runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    StatusText.setText(Status);
                                }
                            });
                        }
                    }).start();
                }
            }
        );
    }

    void setDeleteFriendButton(){
        DeleteFriendButton.setOnClickListener(
            new View.OnClickListener(){
                @Override
                public void onClick(View view){
                    new Thread(new Runnable(){
                        @Override
                        public void run(){
                            String Tmp = updateFriend(0);
                            try {
                                JSONObject json = new JSONObject(Tmp);
                                int stat = Integer.parseInt(json.getString("errCode"));
                                switch (stat){
                                    case 0 :
                                        Status = "删除成功";
                                        break;
                                    case 1 :
                                        Status = "用户未找到";
                                        break;
                                    case 2 :
                                        Status = "好友不存在";
                                        break;
                                    case 3 :
                                        Status = "请先登录";
                                        break;
                                    default:
                                        Status = "程序异常终止";
                                }
                            } catch (JSONException e) {
                                Status = "json解析失败 : " + Tmp;
                                e.printStackTrace();
                            }
                            runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    StatusText.setText(Status);
                                }
                            });
                        }
                    }).start();
                }
            }
        );
    }

    void setReturnButton(){
        ReturnButton.setOnClickListener(
                new View.OnClickListener(){
                    @Override
                    public void onClick(View view){
                        finish();
                    }
                }
        );
    }

    String queryFriends(){
        String url = stdURL + "/queryFriends/";
        String content = "";
        content = content + "userID=" + UserID;
        return NetUtils.post(url, content);
    }

    void RefreshFriendsList(){
        new Thread(new Runnable() {
            @Override
            public void run() {
                String Tmp = queryFriends();
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

                    JSONArray json_friends = new JSONArray(json.getString("friends"));

                    FriendsListLength = json_friends.length();
                    FriendsListString = new String[FriendsListLength];

                    for (int i = 0; i < FriendsListLength; i++) {
                        JSONObject jsonTmp = (JSONObject) json_friends.getJSONObject(i);
                        FriendsListString[i] = jsonTmp.getString("uBID");
                    }

                } catch (JSONException e) {
                    Status = "json解析失败 : " + Tmp;

                    FriendsListLength = 0;
                    FriendsListString = new String[0];
                    e.printStackTrace();
                }
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        StatusText.setText(Status);
                        ArrayAdapter<String> adapter = new ArrayAdapter<String>(FriendsTab.this,android.R.layout.simple_list_item_1, FriendsListString);
                        FriendsListView.setAdapter(adapter);
                    }
                });
            }
        }).start();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.friendspage);

        intent_n = getIntent();
        UserID = intent_n.getStringExtra("userID");

        StatusText = (TextView) findViewById(R.id.status_text);
        uBIDText = (TextView) findViewById(R.id.uBID_text);
        FriendsListView = (ListView) findViewById(R.id.friends_list);

        AddFriendButton = (Button) findViewById(R.id.add_button);
        DeleteFriendButton = (Button) findViewById(R.id.delete_button);
        ReturnButton = (Button) findViewById(R.id.return_button);

        setAddFriendButton();
        setDeleteFriendButton();
        setReturnButton();

        RefreshFriendsList();
    }
}
