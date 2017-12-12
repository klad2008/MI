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

public class LogInTab extends AppCompatActivity {

    String Status;
    Button LogInButton, RegisteButton, UserTabButton;
    TextView StatusText, UserIDText, PasswordText;

    String logIn(){
        String url = stdURL + "/logIn/";
        String content = "";
        content = content + "userID=" + UserIDText.getText() + "&";
        content = content + "password=" + PasswordText.getText();
        return NetUtils.post(url, content);
    }

    void setLogInButton(){
        LogInButton.setOnClickListener(
                new View.OnClickListener(){
                    @Override
                    public void onClick(View view){
                        new Thread(new Runnable(){
                            @Override
                            public void run(){
                                String Tmp = logIn();
                                try {
                                    JSONObject json = new JSONObject(Tmp);
                                    int stat = Integer.parseInt(json.getString("errCode"));
                                    switch (stat){
                                        case 0 :
                                            Status = "登录成功";
                                            break;
                                        case 1 :
                                            Status = "用户名不存在";
                                            break;
                                        case 2 :
                                            Status = "密码不正确";
                                            break;
                                        case 3 :
                                            Status = "用户已登录";
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
                                if (Objects.equals(Status, "登录成功")){
                                    Intent intent;
                                    intent = new Intent();
                                    intent.putExtra("userID", UserIDText.getText().toString());
                                    intent.setClass(LogInTab.this, UserTab.class);
                                    startActivity(intent);
                                }
                            }
                        }).start();
                    }
                }
        );
    }

    String registe(){
        String url = stdURL + "/registe/";
        String content = "";
        content = content + "userID=" + UserIDText.getText() + "&";
        content = content + "password=" + PasswordText.getText();
        return NetUtils.post(url, content);
    }

    void setRegisteButton (){
        RegisteButton.setOnClickListener(
                new View.OnClickListener(){
                    @Override
                    public void onClick(View view){
                        new Thread(new Runnable() {
                            @Override
                            public void run() {
                                String Tmp = registe();
                                try {
                                    JSONObject json = new JSONObject(Tmp);
                                    int stat = Integer.parseInt(json.getString("errCode"));
                                    switch (stat){
                                        case 0 :
                                            Status = "注册成功";
                                            break;
                                        case 1 :
                                            Status = "用户名已存在";
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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.loginpage);

        StatusText = (TextView) findViewById(R.id.status_text);
        UserIDText = (TextView) findViewById(R.id.userID_text);
        PasswordText = (TextView) findViewById(R.id.password_text);

        UserTabButton = (Button) findViewById(R.id.userTab);
        LogInButton = (Button) findViewById(R.id.logIn_button);
        RegisteButton = (Button) findViewById(R.id.registe_button);

        setLogInButton();
        setRegisteButton();
    }
}
