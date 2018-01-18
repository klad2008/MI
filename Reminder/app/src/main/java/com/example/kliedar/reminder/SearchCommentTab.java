package com.example.kliedar.reminder;

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

public class SearchCommentTab extends AppCompatActivity {

    Comment[] CommentsList;
    String[] CommentsListString;
    int CommentsListLength;
    Intent intent_n;
    String Status, UserID;
    ListView CommentsListView;
    TextView StatusText;
    Button ReturnButton;

    String queryComments(){
        String url = stdURL + "/queryComments/";
        String content = "";
        content = content + "UserID=" + UserID + "&";
        return NetUtils.post(url, content);
    }

    void RefreshCommentsList(){
        new Thread(new Runnable() {
            @Override
            public void run() {
                String Tmp = queryComments();
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
                    JSONArray json_comments = new JSONArray(json.getString("comments"));

                    CommentsListLength = json_comments.length();
                    CommentsList = new Comment[CommentsListLength];
                    for (int i = 0; i < CommentsListLength; i++) {
                        CommentsList[i] = new Comment(json_comments.getJSONObject(i));
                    }
                    Arrays.sort(CommentsList, new Comparator<Comment>() {
                        @Override
                        public int compare(Comment o1, Comment o2) {
                            return o1.get_key() - o2.get_key();
                        }
                    });

                    CommentsListString = new String[CommentsListLength];
                    for (int i = 0; i < CommentsListLength; i++) {
                        CommentsListString[i] = CommentsList[i].export();
                    }

                } catch (JSONException e) {
                    Status = "json解析失败 : " + Tmp;

                    CommentsListLength = 0;
                    CommentsListString = new String[0];
                    e.printStackTrace();
                }
                Log.v("111", CommentsListString[0]);
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        StatusText.setText(Status);
                        ArrayAdapter<String> adapter = new ArrayAdapter<String>(SearchCommentTab.this,android.R.layout.simple_list_item_1, CommentsListString);
                        CommentsListView.setAdapter(adapter);
                        CommentsListView.setOnItemClickListener(new AdapterView.OnItemClickListener(){
                            @Override
                            public void onItemClick(AdapterView<?> adapter, View v, int position, long id){
                                if (id == -1) return;
                                StatusText.setText(String.valueOf(id));
                            }
                        });

                    }
                });
            }
        }).start();
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
        setContentView(R.layout.searchcommentpage);

        intent_n = getIntent();
        UserID = intent_n.getStringExtra("userID");

        StatusText = (TextView) findViewById(R.id.status_text);
        CommentsListView = (ListView) findViewById(R.id.comments_list);

        ReturnButton = (Button) findViewById(R.id.return_button);

        RefreshCommentsList();
        setReturnButton();
    }

}
