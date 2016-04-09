package com.albery.Activity;

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
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.albery.entity.User;
import com.albery.information.R;
import com.albery.util.HttpUtil;

public class Edit extends Activity implements OnClickListener {

    //全局变量区
    //********************************
    private ImageView head;
    //记录用户选择的头像
    private int choosed;
    private EditText name;
    private RadioGroup gender;
    private RadioButton male;
    private RadioButton female;
    //记录用户改变的信息的性别
    private String gen;
    private EditText school;
    private EditText sign;
    private Button save;
    private Button back;

    private Intent intent;
    //记录传递过来的User信息类
    private User user;
    //********************************

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // 去除标题栏
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.edit);
        initView();
    }

    /**
     * 初始化窗口控件
     */
    private void initView() {

        //记录User信息类
        intent = getIntent();
        Bundle data = intent.getExtras();
        user = (User) data.getSerializable("user");

        head = (ImageView) findViewById(R.id.head);
        head.setImageResource(user.getHead());
        head.setOnClickListener(this);
        choosed = user.getHead();
        name = (EditText) findViewById(R.id.name);
        name.setText(user.getName());
        gender = (RadioGroup) findViewById(R.id.gender);
        gender.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {

            @Override
            public void onCheckedChanged(RadioGroup arg0, int arg1) {
                if (arg1 == R.id.male)
                    gen = "男";
                else
                    gen = "女";
            }
        });
        male = (RadioButton) findViewById(R.id.male);
        female = (RadioButton) findViewById(R.id.female);
        gen = user.getGender();
        if (gen.equals("男"))
            male.setChecked(true);
        else if (gen.equals("女"))
            female.setChecked(true);
        school = (EditText) findViewById(R.id.school);
        school.setText(user.getSchool());
        sign = (EditText) findViewById(R.id.sign);
        sign.setText(user.getSign());
        save = (Button) findViewById(R.id.save);
        save.setOnClickListener(this);
        back = (Button) findViewById(R.id.back);
        back.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        //点击头像
        if (v == head) {
            //头像选择
            chooseHead();
        }
        if (v == save) {
            user.setID(HttpUtil.loginUser.getID());
            user.setHead(choosed);
            user.setName(name.getText().toString());
            user.setGender(gen);
            user.setSchool(school.getText().toString());
            user.setSign(sign.getText().toString());
            new Thread(modifyInfo).start();
        }
        if (v == back) {
            this.setResult(1);
            this.finish();
        }
    }

    //用户选择头像提示框
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
                .setPositiveButton("确定",
                        new DialogInterface.OnClickListener() {

                            @Override
                            public void onClick(DialogInterface arg0,
                                                int arg1) {
                                head.setImageResource(choosed);
                            }
                        }).setNegativeButton("取消", null).show();
    }

    private void modifyDone() {
        Bundle data = new Bundle();
        data.putSerializable("user", user);
        intent.putExtras(data);
        this.setResult(0, intent);
        this.finish();
    }

    Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            Bundle data = msg.getData();
            String val = data.getString("done");
            if (val == "1") {
                modifyDone();
            } else {
                Toast.makeText(getApplicationContext(), "修改失败", 2000);
                return;
            }
        }
    };
    Handler modifyPwdHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
        }

    };
    Runnable modifyInfo = new Runnable() {
        @Override
        public void run() {
            Bundle data = new Bundle();
            Message msg = new Message();
            if (HttpUtil.modifyUserInfo(user)) {
                data.putString("done", "1");
            } else {
                data.putString("done", "0");
            }
            msg.setData(data);
            handler.sendMessage(msg);
        }
    };
    Runnable modifyPwd = new Runnable() {
        @Override
        public void run() {

        }
    };
}
