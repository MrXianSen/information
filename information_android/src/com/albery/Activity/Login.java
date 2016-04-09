package com.albery.Activity;

import java.util.HashMap;
import java.util.Map;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.view.Window;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import com.albery.entity.User;
import com.albery.information.R;
import com.albery.util.HttpUtil;

/*********************************************************
 * Login.java
 * <p>
 * 系统登录界面，包含游客浏览模式和注册界面跳转
 *********************************************************/
public class Login extends Activity implements OnClickListener {

    //该类内部的全局变量区
    //**************************************
    private Button cencel;
    private Button login;
    private EditText userName;
    private EditText password;
    private TextView enroll;
    private TextView guest;

    /*记录用户选择用户的模式（游客模式或者正常登录模式），会被作
    为参数传递到MainActivity*/
    private User user;
    //**************************************

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //去除标题栏
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.login);
        initView();
    }

    /*
     * 初始化变量
     */
    private void initView() {
        cencel = (Button) findViewById(R.id.login_cannel);
        cencel.setOnClickListener(this);
        login = (Button) findViewById(R.id.login_login);
        login.setOnClickListener(this);
        userName = (EditText) findViewById(R.id.page1_login_user_edit);
        password = (EditText) findViewById(R.id.page1_login_passwd_edit);
        enroll = (TextView) findViewById(R.id.enroll);
        enroll.setOnClickListener(this);
        guest = (TextView) findViewById(R.id.guest);
        guest.setOnClickListener(this);
    }

    /*
     * 界面空间点击监听
     */
    @Override
    public void onClick(View v) {

        //取消
        if (v == cencel) {
            this.finish();
        }
        //登录
        if (v == login) {
            //判断输入内容合法性
            if (islegal()) {
                new Thread(loginThread).start();
            }
        }
        //注册
        if (v == enroll) {
            Intent intent = new Intent(this, Enroll.class);
            startActivity(intent);
            this.finish();
        }
        //游客登录
        if (v == guest) {
            user = new User(R.drawable.head00, "guest", "男");
            Intent intent = new Intent(this, MainActivity.class);
            Bundle bundle = new Bundle();
            bundle.putSerializable("user", user);
            intent.putExtras(bundle);
            startActivity(intent);
            this.finish();
        }
    }

    /*
     * 判断用户输入内容是否合法
     *
     * @return	用户输入内容合法则返回true
     * 			用户输入内容非法则返回false
     */
    private boolean islegal() {
        String name = userName.getText().toString();
        String passed = password.getText().toString();
        if (name.equals("") || passed.equals("")) {
            Toast.makeText(this, "输入的内容不能为空！", Toast.LENGTH_SHORT).show();
            return false;
        }
        return true;
    }

    /*************************************************
     * 登录完成
     *************************************************/
    private void loginDone() {
        //把当前用户信息传递到MainActivity
        Intent intent = new Intent(this, MainActivity.class);
        user = HttpUtil.loginUser;
        intent.putExtra("user", user);
        startActivity(intent);
        this.finish();
    }

    /*************************************************
     * 处理登录事件的Handler
     *************************************************/
    Handler loginHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            Bundle data = msg.getData();
            String val = data.getString("done");
            if (val.equals("1")) {
                loginDone();
            } else {
                Toast.makeText(getApplicationContext(), "登录失败", 2000).show();
                return;
            }
        }
    };
    /*************************************************
     * 登录线程
     *************************************************/
    Runnable loginThread = new Runnable() {
        @Override
        public void run() {
            //获取用户数据
            String username = userName.getText().toString();
            String pwd = password.getText().toString();
            Message msg = new Message();
            Bundle data = new Bundle();
            if (HttpUtil.login(username, pwd)) {
                data.putString("done", "1");
            } else {
                data.putString("done", "0");
            }
            msg.setData(data);
            loginHandler.sendMessage(msg);
        }
    };
}
