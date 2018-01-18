package com.example.kliedar.reminder;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Objects;

import static com.example.kliedar.reminder.staticPATH.stdURL;

public class UserTab extends AppCompatActivity {

    Intent intent_n;
    String Status, UserID;
    TextView StatusText, UserIDText;
    Button LogOutButton, ChangePasswordButton, FriendsButton;
    Button UserTabButton, NearbyTabButton, EventsTabButton;

    String logOut(){
        String url = stdURL + "/logOut/";
        String content = "";
        content = content + "userID=" + UserID;
        return NetUtils.post(url, content);
    }

    void setLogOutButton (){
        LogOutButton.setOnClickListener(
                new View.OnClickListener(){
                    @Override
                    public void onClick(View view){
                        new Thread(new Runnable() {
                            @Override
                            public void run() {
                                String Tmp = logOut();
                                try {
                                    JSONObject json = new JSONObject(Tmp);
                                    int stat = Integer.parseInt(json.getString("errCode"));
                                    switch (stat){
                                        case 0 :
                                            Status = "登出成功";
                                            break;
                                        case 1 :
                                            Status = "用户未登录";
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
                                if (Objects.equals(Status, "登出成功")){
                                    Intent intent = new Intent(UserTab.this, LogInTab.class);
                                    intent.putExtra("userID", UserID);
                                    startActivity(intent);
                                }
                            }
                        }).start();
                    }
                }
        );
    }

    void switchToChangePaswordTab(){
        ChangePasswordButton.setOnClickListener(
                new View.OnClickListener(){
                    @Override
                    public void onClick(View view){
                        Intent intent = new Intent();
                        intent.putExtra("userID", UserID);
                        intent.setClass(UserTab.this, ChangePasswordTab.class);
                        startActivity(intent);
                    }
                }
        );
    }

    void switchToFriendsTab(){
        FriendsButton.setOnClickListener(
                new View.OnClickListener(){
                    @Override
                    public void onClick(View view){
                        Intent intent = new Intent();
                        intent.putExtra("userID", UserID);
                        intent.setClass(UserTab.this, FriendsTab.class);
                        startActivity(intent);
                    }
                }
        );
    }

    void switchToUserTab(){
        UserTabButton.setOnClickListener(
                new View.OnClickListener(){
                    @Override
                    public void onClick(View view){
                        Intent intent = new Intent();
                        intent.putExtra("userID", UserID);
                        intent.setClass(UserTab.this, UserTab.class);
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
                        intent.setClass(UserTab.this, EventsTab.class);
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
                        intent.setClass(UserTab.this, NearbyTab.class);
                        startActivity(intent);
                    }
                }
        );
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.userpage);

        intent_n = getIntent();
        UserID = intent_n.getStringExtra("userID");

        StatusText = (TextView) findViewById(R.id.status_text);
        UserIDText = (TextView) findViewById(R.id.userID_text);

        LogOutButton = (Button) findViewById(R.id.logOut_button);
        ChangePasswordButton = (Button) findViewById(R.id.changePassword_button);
        FriendsButton = (Button) findViewById(R.id.friendsTab);

        UserTabButton = (Button) findViewById(R.id.UserTab);
        EventsTabButton = (Button) findViewById(R.id.EventsTab);
        NearbyTabButton = (Button) findViewById(R.id.NearbyTab);

        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                UserIDText.setText(UserID);
            }
        });

        setLogOutButton();

//        switchToUserTab();
        switchToEventsTab();
        switchToNearbyTab();
        switchToChangePaswordTab();
        switchToFriendsTab();
    }
}
