package com.example.kliedar.reminder;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import org.json.JSONException;
import org.json.JSONObject;

import static com.example.kliedar.reminder.staticPATH.stdURL;

public class ChangePasswordTab extends AppCompatActivity {

    Intent intent_n;
    int status;
    String Status, UserID, EventJson;
    Button ReturnButton, UpdatePasswordButton;
    TextView StatusText, UserIDText, PasswordText;

    String updatePassword(){
        String url = stdURL + "/updatePassword/";
        String content = "";
        content = content + "userID=" + UserIDText.getText() + "&";
        content = content + "password=" + PasswordText.getText();
        return NetUtils.post(url, content);
    }

    void setUpdatePasswordButton (){
        UpdatePasswordButton.setOnClickListener(
                new View.OnClickListener(){
                    @Override
                    public void onClick(View view){
                        new Thread(new Runnable() {
                            @Override
                            public void run() {
                                String Tmp = updatePassword();
                                try {
                                    JSONObject json = new JSONObject(Tmp);
                                    int stat = Integer.parseInt(json.getString("errCode"));
                                    switch (stat){
                                        case 0 :
                                            Status = "密码修改成功";
                                            break;
                                        case 1 :
                                            Status = "用户不存在";
                                            break;
                                        case 2 :
                                            Status = "不能修改他人账号的密码";
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
        setContentView(R.layout.changepasswordpage);

        intent_n = getIntent();
        status = intent_n.getIntExtra("status", 0);

        StatusText = (TextView) findViewById(R.id.status_text);
        UserIDText = (TextView) findViewById(R.id.userID_text);
        PasswordText = (TextView) findViewById(R.id.password_text);

        UpdatePasswordButton = (Button) findViewById(R.id.updatePassword_button);
        ReturnButton = (Button) findViewById(R.id.return_button);

        if (status == 0) {
            UserID = intent_n.getStringExtra("userID");
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    UserIDText.setText(UserID);
                }
            });

            setUpdatePasswordButton();
            setReturnButton();
        }
        else{

        }
    }
}
