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

    //ȫ�ֱ�����
    //********************************
    private ImageView head;
    //��¼�û�ѡ���ͷ��
    private int choosed;
    private EditText name;
    private RadioGroup gender;
    private RadioButton male;
    private RadioButton female;
    //��¼�û��ı����Ϣ���Ա�
    private String gen;
    private EditText school;
    private EditText sign;
    private Button save;
    private Button back;

    private Intent intent;
    //��¼���ݹ�����User��Ϣ��
    private User user;
    //********************************

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // ȥ��������
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.edit);
        initView();
    }

    /**
     * ��ʼ�����ڿؼ�
     */
    private void initView() {

        //��¼User��Ϣ��
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
                    gen = "��";
                else
                    gen = "Ů";
            }
        });
        male = (RadioButton) findViewById(R.id.male);
        female = (RadioButton) findViewById(R.id.female);
        gen = user.getGender();
        if (gen.equals("��"))
            male.setChecked(true);
        else if (gen.equals("Ů"))
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
        //���ͷ��
        if (v == head) {
            //ͷ��ѡ��
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

    //�û�ѡ��ͷ����ʾ��
    private void chooseHead() {
        final View choose = getLayoutInflater().inflate(R.layout.choose_head, null);
        RadioGroup head_choose = (RadioGroup) choose.findViewById(R.id.head_choose);
        head_choose.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {

            //��¼�û�ѡ���ͷ��
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
                .setTitle("ѡ��ͷ��")
                .setView(choose)
                .setPositiveButton("ȷ��",
                        new DialogInterface.OnClickListener() {

                            @Override
                            public void onClick(DialogInterface arg0,
                                                int arg1) {
                                head.setImageResource(choosed);
                            }
                        }).setNegativeButton("ȡ��", null).show();
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
                Toast.makeText(getApplicationContext(), "�޸�ʧ��", 2000);
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
