package com.albery.Activity;

import com.albery.information.R;

import android.os.AsyncTask;
import android.os.Bundle;
import android.app.Activity;
import android.content.Intent;
import android.view.Menu;
import android.view.Window;

/******************************************************
 * Welcome.java
 * <p>
 * һ��ʼ�Ļ�ӭ���棬2�������¼���沢�˳���ǰActivity
 ******************************************************/
public class Welcome extends Activity {

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //ȥ��������
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.wlecome);

        new Wait().execute();
    }

    /*
     * ��ϵͳ��ͣ2��
     * 
     * 1.ϵͳ���뻶ӭ���沢��ͣ2��
     * 2.��ת����¼����
     * 3.�˳���ǰActivity
     */
    private class Wait extends AsyncTask<Void, Void, Void> {

        @Override
        protected Void doInBackground(Void... arg0) {
            try {
                Thread.sleep(2000);
                Intent intent = new Intent(Welcome.this, Login.class);
                startActivity(intent);
                Welcome.this.finish();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            return null;
        }
    }
}
