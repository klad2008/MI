package com.example.kliedar.reminder;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Objects;

import static com.example.kliedar.reminder.staticPATH.stdURL;

public class ChangeEventTab extends AppCompatActivity {

    int status;
    Events eventTmp;
    Intent intent_n;
    String Status, UserID, EventJsonString;
    TextView StatusText, TitleText, ContentText, StartTime, EndTime;
    RadioGroup ShareGroup;
    RadioButton ShareButton, ShareNotButton;
    Button CommitButton, ReturnButton, DeleteButton;

    String commitEvents(){
        String url = stdURL + "/changeEvent/";
        String content = "";
        content = content + "EventID=" + eventTmp.eventID + "&";
        content = content + "UserID=" + UserID + "&";
        content = content + "Title=" + TitleText.getText() + "&";
        content = content + "Content=" + ContentText.getText() + "&";
        content = content + "StartTime=" + StartTime.getText() + "&";
        content = content + "EndTime=" + EndTime.getText() + "&";
        String ShareStatus = "";
        for (int i = 0; i < ShareGroup.getChildCount(); i++) {
            RadioButton rd = (RadioButton) ShareGroup.getChildAt(i);
            if (rd.isChecked())
                ShareStatus = (String) rd.getText();
        }
        content = content + "Share=" + ShareStatus;
        return NetUtils.post(url, content);
    }

    void setCommitButton (){
        CommitButton.setOnClickListener(
                new View.OnClickListener(){
                    @Override
                    public void onClick(View view){
                        new Thread(new Runnable() {
                            @Override
                            public void run() {
                                String Tmp = commitEvents();
                                try {
                                    JSONObject json = new JSONObject(Tmp);
                                    int stat = Integer.parseInt(json.getString("errCode"));
                                    switch (stat){
                                        case 0 :
                                            Status = "事件修改成功";
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

    String deleteEvents(){
        String url = stdURL + "/deleteEvent/";
        String content = "";
        content = content + "EventID=" + eventTmp.eventID;
        Log.v("111", "111");
        return NetUtils.post(url, content);
    }

    void setDeleteButton (){
        DeleteButton.setOnClickListener(
                new View.OnClickListener(){
                    @Override
                    public void onClick(View view){
                        new Thread(new Runnable() {
                            @Override
                            public void run() {
                                String Tmp = deleteEvents();
                                try {
                                    JSONObject json = new JSONObject(Tmp);
                                    int stat = Integer.parseInt(json.getString("errCode"));
                                    switch (stat){
                                        case 0 :
                                            Status = "事件修改成功";
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

    void setReturnButton (){
        ReturnButton.setOnClickListener(
                new View.OnClickListener(){
                    @Override
                    public void onClick(View view){
                        finish();
                    }
                }
        );
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.changeeventpage);

        intent_n = getIntent();
        UserID = intent_n.getStringExtra("userID");

        StatusText = (TextView) findViewById(R.id.status_text);
        TitleText = (TextView) findViewById(R.id.title_text);
        ContentText = (TextView) findViewById(R.id.content_text);
        StartTime = (TextView) findViewById(R.id.start_time);
        EndTime = (TextView) findViewById(R.id.end_time);
        ShareGroup = (RadioGroup) findViewById(R.id.share_status);
        ShareButton = (RadioButton) findViewById(R.id.share_button);
        ShareNotButton = (RadioButton) findViewById(R.id.share_not_button);

        CommitButton = (Button) findViewById(R.id.commit_button);
        ReturnButton = (Button) findViewById(R.id.return_button);
        DeleteButton = (Button) findViewById(R.id.delete_button);

        EventJsonString = intent_n.getStringExtra("event");
        try {
            eventTmp = new Events(EventJsonString);
            TitleText.setText(eventTmp.title);
            ContentText.setText(eventTmp.content);
            StartTime.setText(eventTmp.startTime);
            EndTime.setText(eventTmp.endTime);
            int permission = eventTmp.permission;
            if ((permission & 1) == 0){
                ShareNotButton.setChecked(false);
                ShareButton.setChecked(true);
            }
            else{
                ShareButton.setChecked(false);
                ShareNotButton.setChecked(true);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        setCommitButton();
        setReturnButton();
        setDeleteButton();
    }

}
