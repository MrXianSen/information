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

    //ȫ�ֱ�����
    //***************************************
    private ImageView head;
    private int choosed;    //��¼ѡ���ͷ��
    private EditText name;
    private EditText password;
    private EditText againPassed;
    private EditText school;
    private RadioGroup genderChoose;
    private String gender;    //��¼ѡ����Ա�
    private Button enroll;
    private Button cancel;
    private Thread registerThread;
    //***************************************

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //ȥ��������
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.enroll);
        initView();
    }

    /**
     * ��ʼ�����ڿؼ�
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

        //�Ա��ʼ��Ĭ��Ϊ���С�
        gender = "��";
        genderChoose = (RadioGroup) findViewById(R.id.gender);
        genderChoose.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup arg0, int arg1) {
                gender = arg1 == R.id.male ? "��" : "Ů";
            }
        });
    }

    /**
     * �ж���������������Ƿ���ͬ
     *
     * @return ������ͬ����true�����򷵻�false
     */
    private boolean isPassSame() {
        String uPassword = password.getText().toString();
        String uAgainPassed = againPassed.getText().toString();
        if (uPassword.equals(uAgainPassed))
            return true;
        else
            Toast.makeText(this, "������������벻��ͬ��", Toast.LENGTH_SHORT).show();
        return false;
    }

    /**
     * �ж���Ϣ�Ƿ�ȫ����д
     *
     * @return ��һ����Ϣû���false, ���򷵻�true
     */
    private boolean isEmpty() {
        String uName = name.getText().toString();
        String uSchool = school.getText().toString();
        String uPassword = password.getText().toString();
        String uAgainPassed = againPassed.getText().toString();
        if (uName.equals("") || uPassword.equals("") || uAgainPassed.equals("") || uSchool.equals("")) {
            Toast.makeText(this, "ÿһ�����Ϊ�գ�", Toast.LENGTH_SHORT).show();
            return false;
        }
        return true;
    }

    /**
     * ����ʾ���ڵ���ʽ���û�ѡ��ͷ��
     */
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
                        //���ȷ���󣬸��Ľ���ͷ��
                .setPositiveButton("ȷ��",
                        new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface arg0,
                                                int arg1) {
                                head.setImageResource(choosed);
                            }
                        }).setNegativeButton("ȡ��", null).show();
    }

    @Override
    public void onClick(View v) {
        //���ͷ��
        if (v == head) {
            chooseHead();
        }
        //���ȡ��
        if (v == cancel) {
            Intent intent = new Intent(this, Login.class);
            startActivity(intent);
            this.finish();
        }
        //���ע��
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
     * ע��ɹ�
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
     * ����ע���¼���Handler
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
                Toast.makeText(getApplicationContext(), "ע��ʧ��", 2000).show();
                return;
            }
        }
    };
    /**
     * ע���߳�
     */
    Runnable register = new Runnable() {
        @Override
        public void run() {
            //��ȡע������
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
