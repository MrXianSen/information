package com.albery.Activity;

import com.albery.entity.User;
import com.albery.information.R;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

public class Personal extends Activity implements OnClickListener {

    //全局变量区
    //***********************************
    public static final int EDIT = 0;
    public static final int CHANGE = 0;

    private ImageView head;
    private TextView name;
    private TextView gender;
    private TextView school;
    private TextView sign;
    private TextView edit;
    private Button change;
    private Button exit;

    private Intent intent;
    //记录从MainActivity传递过来的User信息类
    private User user;
    //***********************************

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // 去除标题栏
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.personal);
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
        name = (TextView) findViewById(R.id.name);
        name.setText(user.getName());
        gender = (TextView) findViewById(R.id.gender);
        gender.setText(user.getGender());
        school = (TextView) findViewById(R.id.school);
        school.setText(user.getSchool());
        sign = (TextView) findViewById(R.id.sign);
        sign.setText(user.getSign());
        edit = (TextView) findViewById(R.id.edit);
        edit.setOnClickListener(this);
        exit = (Button) findViewById(R.id.exit);
        exit.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        //点击编辑
        if (v == edit) {
            Intent intent = new Intent(this, Edit.class);
            Bundle data = new Bundle();
            data.putSerializable("user", user);
            intent.putExtras(data);
            startActivityForResult(intent, 0);
        }
        //点击退出
        if (v == exit) {
            Bundle data = new Bundle();
            data.putSerializable("user", user);
            intent.putExtras(data);
            this.setResult(0, intent);
            this.finish();
        }
    }

    /**
     * 当用户在编辑界面内修改了用户信息并返回到该界面时时，当前
     * 个人中心会做出相应，更新用户的个人信息
     */
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent intent) {

        if (requestCode == EDIT && resultCode == CHANGE) {
            Bundle data = intent.getExtras();
            user = (User) data.getSerializable("user");
            head.setImageResource(user.getHead());
            name.setText(user.getName());
            gender.setText(user.getGender());
            school.setText(user.getSchool());
            sign.setText(user.getSign());
        }
    }
}
