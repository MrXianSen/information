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
 * 一开始的欢迎界面，2秒后进入登录界面并退出当前Activity
 ******************************************************/
public class Welcome extends Activity {

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //去除标题栏
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.wlecome);

        new Wait().execute();
    }

    /*
     * 让系统暂停2秒
     * 
     * 1.系统进入欢迎界面并暂停2秒
     * 2.跳转到登录界面
     * 3.退出当前Activity
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
