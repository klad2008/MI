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

public class CommentEventTab extends AppCompatActivity {

    int status;
    Events eventTmp;
    Intent intent_n;
    String Status, UserID, EventJsonString;
    TextView StatusText, TitleText, ContentText, StartTime, EndTime, CommentText;
    Button commentButton, ReturnButton, DeleteButton;

    String commentEvents(){
        String url = stdURL + "/commentEvent/";
        String content = "";
        content = content + "EventID=" + eventTmp.eventID + "&";
        content = content + "UserID=" + UserID + "&";
        content = content + "Content=" + CommentText.getText() + "&";
        return NetUtils.post(url, content);
    }

    void setcommentButton (){
        commentButton.setOnClickListener(
                new View.OnClickListener(){
                    @Override
                    public void onClick(View view){
                        new Thread(new Runnable() {
                            @Override
                            public void run() {
                                String Tmp = commentEvents();
                                try {
                                    JSONObject json = new JSONObject(Tmp);
                                    int stat = Integer.parseInt(json.getString("errCode"));
                                    switch (stat){
                                        case 0 :
                                            Status = "事件评论成功";
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
        setContentView(R.layout.commenteventpage);

        intent_n = getIntent();
        UserID = intent_n.getStringExtra("userID");

        StatusText = (TextView) findViewById(R.id.status_text);
        TitleText = (TextView) findViewById(R.id.title_text);
        ContentText = (TextView) findViewById(R.id.content_text);
        CommentText = (TextView) findViewById(R.id.comment_text);
        StartTime = (TextView) findViewById(R.id.start_time);
        EndTime = (TextView) findViewById(R.id.end_time);

        commentButton = (Button) findViewById(R.id.comment_button);
        ReturnButton = (Button) findViewById(R.id.return_button);
        DeleteButton = (Button) findViewById(R.id.delete_button);

        EventJsonString = intent_n.getStringExtra("event");
        try {
            eventTmp = new Events(EventJsonString);
            TitleText.setText(eventTmp.title);
            ContentText.setText(eventTmp.content);
            StartTime.setText(eventTmp.startTime);
            EndTime.setText(eventTmp.endTime);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        setcommentButton();
        setReturnButton();
    }

}
