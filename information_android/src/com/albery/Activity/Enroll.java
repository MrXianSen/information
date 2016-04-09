package com.albery.Activity;

import java.util.ArrayList;
import java.util.List;

import com.albery.entity.User;
import com.albery.information.R;
import com.albery.util.HttpUtil;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RadioGroup;
import android.widget.Toast;

public class Enroll extends Activity implements OnClickListener {

    //全局变量区
    //***************************************
    private ImageView head;
    private int choosed;    //记录选择的头像
    private EditText name;
    private EditText password;
    private EditText againPassed;
    private EditText school;
    private RadioGroup genderChoose;
    private String gender;    //记录选择的性别
    private Button enroll;
    private Button cancel;
    private Thread registerThread;
    //***************************************

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //去除标题栏
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.enroll);
        initView();
    }

    /**
     * 初始化窗口控件
     */
    private void initView() {
        head = (ImageView) findViewById(R.id.head);
        head.setOnClickListener(this);
        name = (EditText) findViewById(R.id.name);
        name.setOnClickListener(this);
        password = (EditText) findViewById(R.id.password);
        password.setOnClickListener(this);
        againPassed = (EditText) findViewById(R.id.againPassed);
        againPassed.setOnClickListener(this);
        school = (EditText) findViewById(R.id.school);
        school.setOnClickListener(this);
        enroll = (Button) findViewById(R.id.enroll);
        enroll.setOnClickListener(this);
        cancel = (Button) findViewById(R.id.cancel);
        cancel.setOnClickListener(this);
        choosed = R.drawable.head00;

        //性别初始化默认为“男”
        gender = "男";
        genderChoose = (RadioGroup) findViewById(R.id.gender);
        genderChoose.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup arg0, int arg1) {
                gender = arg1 == R.id.male ? "男" : "女";
            }
        });
    }

    /**
     * 判断两次输入的密码是否相同
     *
     * @return 密码相同返回true，否则返回false
     */
    private boolean isPassSame() {
        String uPassword = password.getText().toString();
        String uAgainPassed = againPassed.getText().toString();
        if (uPassword.equals(uAgainPassed))
            return true;
        else
            Toast.makeText(this, "两次输入的密码不相同！", Toast.LENGTH_SHORT).show();
        return false;
    }

    /**
     * 判断信息是否全部填写
     *
     * @return 有一项信息没填返回false, 否则返回true
     */
    private boolean isEmpty() {
        String uName = name.getText().toString();
        String uSchool = school.getText().toString();
        String uPassword = password.getText().toString();
        String uAgainPassed = againPassed.getText().toString();
        if (uName.equals("") || uPassword.equals("") || uAgainPassed.equals("") || uSchool.equals("")) {
            Toast.makeText(this, "每一项都不能为空！", Toast.LENGTH_SHORT).show();
            return false;
        }
        return true;
    }

    /**
     * 以提示窗口的形式让用户选择头像
     */
    private void chooseHead() {
        final View choose = getLayoutInflater().inflate(R.layout.choose_head, null);
        RadioGroup head_choose = (RadioGroup) choose.findViewById(R.id.head_choose);
        head_choose.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {

            //记录用户选择的头像
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                if (checkedId == R.id.head0001)
                    choosed = R.drawable.head01;
                else if (checkedId == R.id.head0002)
                    choosed = R.drawable.head02;
                else if (checkedId == R.id.head0003)
                    choosed = R.drawable.head03;
                else if (checkedId == R.id.head0004)
                    choosed = R.drawable.head04;
                else if (checkedId == R.id.head0005)
                    choosed = R.drawable.head05;
                else if (checkedId == R.id.head0006)
                    choosed = R.drawable.head06;
            }
        });
        new AlertDialog.Builder(this)
                .setTitle("选择头像")
                .setView(choose)
                        //点击确定后，更改界面头像
                .setPositiveButton("确定",
                        new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface arg0,
                                                int arg1) {
                                head.setImageResource(choosed);
                            }
                        }).setNegativeButton("取消", null).show();
    }

    @Override
    public void onClick(View v) {
        //点击头像
        if (v == head) {
            chooseHead();
        }
        //点击取消
        if (v == cancel) {
            Intent intent = new Intent(this, Login.class);
            startActivity(intent);
            this.finish();
        }
        //点击注册
        if (v == enroll) {
            if (isEmpty()) {
                if (isPassSame()) {
                    registerThread = new Thread(register);
                    registerThread.start();
                }
            }
        }
    }

    /**
     * 注册成功
     */
    private void registerDone() {
        Intent intent = new Intent(this, MainActivity.class);
        User user = new User(choosed, name.getText().toString(), password.getText().toString(), school.getText().toString(), gender);
        Bundle data = new Bundle();
        data.putSerializable("user", user);
        intent.putExtras(data);
        startActivity(intent);
        this.finish();
    }

    /**
     * 处理注册事件的Handler
     */
    Handler registerHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            Bundle data = msg.getData();
            String val = data.getString("done");
            if (val.equals("1")) {
                registerDone();
            } else if (val == "0") {
                Toast.makeText(getApplicationContext(), "注册失败", 2000).show();
                return;
            }
        }
    };
    /**
     * 注册线程
     */
    Runnable register = new Runnable() {
        @Override
        public void run() {
            //获取注册数据
            String username = name.getText().toString();
            String pwd = password.getText().toString();
            String gende = gender;
            String schl = school.getText().toString();
            String icon = String.valueOf(choosed);
            Message msg = new Message();
            Bundle data = new Bundle();
            if (HttpUtil.register(username, pwd, gende, schl, icon)) {

                data.putString("done", "1");
            } else {
                data.putString("done", "0");
            }
            msg.setData(data);
            registerHandler.sendMessage(msg);
        }
    };

}
