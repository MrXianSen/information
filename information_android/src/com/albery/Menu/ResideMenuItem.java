/************************************************************************************
 * �Ӳ˵�
 * <p>
 * ���ļ���Ҫʵ���Ӳ˵�
 * <p>
 * ���ܣ�ʵ���Ӳ˵�
 * <p>
 * �����ߣ��ƾ���
 * <p>
 * �汾��1.0.0
 * <p>
 * ����ʱ�䣺2015/4/26
 ************************************************************************************/

package com.albery.Menu;

import com.albery.information.R;
import android.content.Context;
import android.view.LayoutInflater;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

public class ResideMenuItem extends LinearLayout {

    //�Ӳ˵��ؼ�
    private ImageView iv_icon;        //�Ӳ˵���ͼ��
    private TextView tv_title;        //�Ӳ˵�������

    private int icon;
    private String title;

    /*******************************************
     *�Ӳ˵��Ĺ��캯��
     *
     *���ܣ�����һ���Ӳ˵�
     *
     *����������
     *context-----һ��context
     *icon--------�Ӳ˵�ͼ��
     *title-------�Ӳ˵�����
     ********************************************/
    public ResideMenuItem(Context context, int icon, String title) {
        super(context);
        initViews(context);                    //��ʼ��
        iv_icon.setImageResource(icon);        //�����Ӳ˵�ͼ��
        tv_title.setText(title);            //�����Ӳ˵�����
        this.icon = icon;
        this.title = title;
    }

    //��ʼ�����пؼ�
    private void initViews(Context context) {
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        inflater.inflate(R.layout.residemenu_item, this);
        iv_icon = (ImageView) findViewById(R.id.iv_icon);
        tv_title = (TextView) findViewById(R.id.tv_title);
    }

    //�����Ӳ˵�ͼ��
    public void setIcon(int icon) {
        iv_icon.setImageResource(icon);
    }

    //�����Ӳ˵�������
    public void setTitle(String title) {
        tv_title.setText(title);
    }

    public int getIcon() {
        return icon;
    }

    public String getTitle() {
        return title;
    }
}
