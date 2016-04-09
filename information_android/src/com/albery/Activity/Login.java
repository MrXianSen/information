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
 * ϵͳ��¼���棬�����ο����ģʽ��ע�������ת
 *********************************************************/
public class Login extends Activity implements OnClickListener {

    //�����ڲ���ȫ�ֱ�����
    //**************************************
    private Button cencel;
    private Button login;
    private EditText userName;
    private EditText password;
    private TextView enroll;
    private TextView guest;

    /*��¼�û�ѡ���û���ģʽ���ο�ģʽ����������¼ģʽ�����ᱻ��
    Ϊ�������ݵ�MainActivity*/
    private User user;
    //**************************************

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //ȥ��������
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.login);
        initView();
    }

    /*
     * ��ʼ������
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
     * ����ռ�������
     */
    @Override
    public void onClick(View v) {

        //ȡ��
        if (v == cencel) {
            this.finish();
        }
        //��¼
        if (v == login) {
            //�ж��������ݺϷ���
            if (islegal()) {
                new Thread(loginThread).start();
            }
        }
        //ע��
        if (v == enroll) {
            Intent intent = new Intent(this, Enroll.class);
            startActivity(intent);
            this.finish();
        }
        //�ο͵�¼
        if (v == guest) {
            user = new User(R.drawable.head00, "guest", "��");
            Intent intent = new Intent(this, MainActivity.class);
            Bundle bundle = new Bundle();
            bundle.putSerializable("user", user);
            intent.putExtras(bundle);
            startActivity(intent);
            this.finish();
        }
    }

    /*
     * �ж��û����������Ƿ�Ϸ�
     *
     * @return	�û��������ݺϷ��򷵻�true
     * 			�û��������ݷǷ��򷵻�false
     */
    private boolean islegal() {
        String name = userName.getText().toString();
        String passed = password.getText().toString();
        if (name.equals("") || passed.equals("")) {
            Toast.makeText(this, "��������ݲ���Ϊ�գ�", Toast.LENGTH_SHORT).show();
            return false;
        }
        return true;
    }

    /*************************************************
     * ��¼���
     *************************************************/
    private void loginDone() {
        //�ѵ�ǰ�û���Ϣ���ݵ�MainActivity
        Intent intent = new Intent(this, MainActivity.class);
        user = HttpUtil.loginUser;
        intent.putExtra("user", user);
        startActivity(intent);
        this.finish();
    }

    /*************************************************
     * �����¼�¼���Handler
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
                Toast.makeText(getApplicationContext(), "��¼ʧ��", 2000).show();
                return;
            }
        }
    };
    /*************************************************
     * ��¼�߳�
     *************************************************/
    Runnable loginThread = new Runnable() {
        @Override
        public void run() {
            //��ȡ�û�����
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
