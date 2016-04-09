package com.albery.Activity;

import java.util.ArrayList;

import org.androidpn.client.ServiceManager;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.albery.Menu.ResideMenu;
import com.albery.Menu.ResideMenuItem;
import com.albery.adapter.MyAdapter;
import com.albery.entity.InformationEntity;
import com.albery.entity.PageBean;
import com.albery.entity.User;
import com.albery.information.R;
import com.albery.util.HttpUtil;
import com.albery.view.MyListView;
import com.albery.view.MyListView.ILoadListener;
import com.albery.view.MyListView.IRefalshListener;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup.LayoutParams;
import android.view.WindowManager;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.AdapterView.OnItemClickListener;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Typeface;
import android.graphics.drawable.BitmapDrawable;

/*************************************************
 * ��Activity
 *
 * @author �Ž���
 *************************************************/
public class MainActivity extends Activity implements ILoadListener,
        IRefalshListener, OnClickListener, OnItemClickListener {
    /*************************************************
     * ���ݶ�����
     **************************************************/
    //�򿪸������Ľ����������
    public static final int PERSONAL = 1;
    //�������Ľ�����Ϣ�޸ĵĽ����
    public static final int CHANGE = 0;
    // ���ڿؼ�
    private Button person;
    private TextView name;
    private ImageView gender;
    private TextView exit;
    private TextView change;
    private TextView info_id;
    Intent intent;
    //�ӵ�¼���洫�ݹ������û���Ϣ����������,�໬���ڵ�һЩ������Ϣ
    User user;
    // �˵����Ӳ˵����
    private ResideMenu resideMenu;
    private ResideMenuItem personal;
    private ResideMenuItem changePassed;
    int page = 1;
    // �Զ����Adapter
    MyAdapter adapter;
    // �����ж��Ƿ�����˸��������
    boolean isLoadData;
    // �Զ����ListView
    MyListView listView;
    // PageBean����������ListView���з�ҳ����
    PageBean pageBean;
    // ���ݼ��ϣ������洢�ӷ�������ȡ������
    ArrayList<InformationEntity> list = new ArrayList<InformationEntity>();

    /*************************************************
     * Activity���
     **************************************************/
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE); // ȥ��������
        setContentView(R.layout.main_activity);
        InitView();
        // ���ӷ�����������һ��������
        ServiceManager serviceManager = new ServiceManager(this);
        serviceManager.setNotificationIcon(R.drawable.notification);
        serviceManager.startService();
        // ��ȡ����������
        new Thread(getData).start();
    }

    /*************************************************
     * ��ʼ������
     **************************************************/
    private void InitView() {
        //���մӵ�¼���洫�ݹ���������
        intent = getIntent();
        Bundle data = intent.getExtras();
        user = (User) data.getSerializable("user");

        // ��ʼ���໬�˵�
        resideMenu = new ResideMenu(this);
        resideMenu.setBackground(R.drawable.menu_bg);
        resideMenu.setUserName(user.getName());
        if (user.getGender().equals("��"))
            resideMenu.setUserGander(R.drawable.male);
        else if (user.getGender().equals("Ů"))
            resideMenu.setUserGander(R.drawable.female);
        else
            resideMenu.setUserGander(R.drawable.gender);
        resideMenu.setUserHead(user.getHead());
        resideMenu.attachToActivity(this);
        resideMenu.setScaleValue(0.8f);

        // ��ʼ���໬�˵����Ӳ˵�
        personal = new ResideMenuItem(this,
                R.drawable.left_fragment_near_icon_normal, "��������");
        personal.setOnClickListener(this);
        resideMenu.addMenuItem1(personal, ResideMenu.DIRECTION_LEFT);
        changePassed = new ResideMenuItem(this,
                R.drawable.left_fragment_near_icon_normal, "�޸�����");
        changePassed.setOnClickListener(this);
        resideMenu.addMenuItem1(changePassed, ResideMenu.DIRECTION_LEFT);

        //��ʼ���˵����½ǵı���
        exit = new TextView(this);
        exit.setTypeface(Typeface.MONOSPACE, Typeface.ITALIC);
        exit.setText("�˳�");
        exit.setOnClickListener(this);
        resideMenu.addExit(exit);
        change = new TextView(this);
        change.setTypeface(Typeface.MONOSPACE, Typeface.ITALIC);
        change.setText("�л��û�");
        change.setOnClickListener(this);
        resideMenu.addChange(change);

        // ��ʼ�����ڿؼ�
        person = (Button) findViewById(R.id.person);
        person.setOnClickListener(this);
        name = (TextView) findViewById(R.id.name);
    }

    /*************************************************
     * ��ӵ���¼�
     **************************************************/
    @Override
    public void onClick(View v) {
        //����û���ť
        if (v == person) {
            //�����໬�˵�
            resideMenu.openMenu(0);
        }
        //�����������
        if (v == personal) {
            //�ѵ�ǰUser��Ϣ�ഫ�ݸ���������
            Intent intent = new Intent(this, Personal.class);
            Bundle data = new Bundle();
            data.putSerializable("user", user);
            intent.putExtras(data);
            /*
			 * ��¼�������Ķ��û���Ϣ���޸ģ������ı�໬�˵�
			 * ���û�ͷ���Ա�����
			 */
            startActivityForResult(intent, PERSONAL);
        }
        //����޸�����
        if (v == changePassed) {
            showDialog();
        }
        if (v == exit) {
            showExit();
        }
        if (v == change) {
            Intent intent = new Intent(this, Login.class);
            startActivity(intent);
            this.finish();
        }
    }

    /**
     * ֻ�������޸ĵ���ʾ�򣬵��û����������������ͬ
     * ʱ����ʾ�û������޸ĳɹ���������ʾ�û������޸�ʧ��
     */
    private String newPwd = "";
    private String oldPwd = "";

    public void showDialog() {
        final View dialog = getLayoutInflater().inflate(
                R.layout.change_password, null);
        new AlertDialog.Builder(this)
                .setTitle("�޸�����")
                .setView(dialog)
                .setPositiveButton("ȷ��",
                        new DialogInterface.OnClickListener() {

                            @Override
                            public void onClick(DialogInterface arg0,
                                                int arg1) {
                                newPwd = ((EditText) dialog.findViewById(R.id.newPassed)).getText().toString();
                                String againPassed = ((EditText) dialog.findViewById(R.id.againPassed)).getText().toString();
                                if (newPwd.equals("") || againPassed.equals("")) {
                                    Toast.makeText(MainActivity.this, "�������벻��Ϊ�գ��޸�ʧ�ܣ�", Toast.LENGTH_SHORT).show();
                                } else {
                                    if (newPwd.equals(againPassed)) {
                                        new Thread(modifyPwd).start();
                                    } else {
                                        Toast.makeText(MainActivity.this, "�����������벻��ͬ���޸�ʧ�ܣ�", Toast.LENGTH_SHORT).show();
                                    }
                                }
                            }
                        }).setNegativeButton("ȡ��", null).show();
    }

    /*************************************************
     * ����ʾ���ڵ���ʽ��ȷ���û��Ƿ��˳�ϵͳ
     **************************************************/
    public void showExit() {
        new AlertDialog.Builder(this)
                .setTitle("�˳�ϵͳ")
                .setMessage("ȷ��Ҫ�˳�ϵͳô��")
                        //���ȷ����ť�������˳�
                .setPositiveButton("ȷ��",
                        new DialogInterface.OnClickListener() {

                            @Override
                            public void onClick(DialogInterface arg0,
                                                int arg1) {
                                MainActivity.this.finish();
                            }
                        }).setNegativeButton("ȡ��", null).show();
    }

    /*************************************************
     * �����б���ĵ�������һ���б��������ת����Ϣ����ϸ����
     *************************************************/
    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        InformationEntity info = list.get(position - 1);
        Bundle data = new Bundle();
        data.putSerializable("info", info);
        Intent intent = new Intent(this, InfoDetail.class);
        intent.putExtras(data);
        startActivity(intent);
    }

    /*************************************************
     * ���û��ڸ����������޸����û���Ϣ�����ص��ý���ʱʱ����ǰ�໬
     * �˵�����Ӧ�ı䣬�������ò໬�˵����û�ͷ���������Ա�
     *************************************************/
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent intent) {

        if (requestCode == PERSONAL && resultCode == CHANGE) {
            //����User��Ϣ��
            Bundle data = intent.getExtras();
            user = (User) data.getSerializable("user");
            resideMenu.setUserHead(user.getHead());
            resideMenu.setUserName(user.getName());
            if (user.getGender().equals("��")) {
                resideMenu.setUserGander(R.drawable.male);
            } else {
                resideMenu.setUserGander(R.drawable.female);
            }
        }
    }

    /*************************************************
     * �½������̺߳�Handler
     *************************************************/
    Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            Bundle data = msg.getData();
            String val = data.getString("done");
            System.out.println(val);
            showListView(list);
        }
    };
    Handler modifyPwdHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            Bundle data = msg.getData();
            String val = data.getString("done");
            if (val.equals("1")) {
                modifyPwdDone();
            } else {
                Toast.makeText(getApplicationContext(), "�޸�ʧ��", 2000);
                return;
            }
        }
    };
    Runnable getData = new Runnable() {
        @Override
        public void run() {
            getData();
            Message msg = new Message();
            Bundle data = new Bundle();
            data.putString("done", "1");
            msg.setData(data);
            handler.sendMessage(msg);
        }
    };
    Runnable getReflashData = new Runnable() {
        @Override
        public void run() {
            getRefalshData();
            Message msg = new Message();
            Bundle data = new Bundle();
            data.putString("done", "1");
            msg.setData(data);
            handler.sendMessage(msg);
        }
    };
    Runnable modifyPwd = new Runnable() {
        @Override
        public void run() {
            Bundle data = new Bundle();
            Message msg = new Message();
            if (HttpUtil.modifyPwd(newPwd, user.getPassword(), user.getID()))
                data.putString("done", "1");
            else data.putString("done", "0");
            msg.setData(data);
            modifyPwdHandler.sendMessage(msg);
        }
    };

    /*************************************************
     * ��ʾListView
     *************************************************/
    private void showListView(ArrayList<InformationEntity> list) {
        // ��һ�μ���
        if (adapter == null) {
            listView = (MyListView) findViewById(R.id.listview);
            listView.setInterface(this, this);
            adapter = new MyAdapter(this, list);
            listView.setAdapter(adapter);
            listView.setOnItemClickListener(this);
        } else {
            adapter.onDataChange(list);
        }
    }

    /*************************************************
     * �������ݣ�����������ʱ����
     * �û����»�����Ļʱ����
     *************************************************/
    private void getData() {
        // ��ȡ����������������
        int tr = HttpUtil.getTotalRecord();
        pageBean = new PageBean();
        // ����pageBean���ܼ�¼��
        pageBean.setTotalRecord(tr);
        if (pageBean.getTotalPage() >= page) {
            pageBean.setCurrPage(page);
            // ��ȡ������������
            JSONArray jsonArray = HttpUtil.getInformationFromServer(pageBean
                    .getCurrPage());
            if (jsonArray != null && jsonArray.length() > 0) {
                isLoadData = true;
                for (int i = 0; i < jsonArray.length(); i++) {
                    try {
                        JSONObject jsonObject = jsonArray.getJSONObject(i);
                        InformationEntity info = new InformationEntity();
                        info.setId(jsonObject.getString("id"));
                        info.setTitle(jsonObject.getString("title"));
                        info.setDate(jsonObject.getString("date"));
                        info.setType(jsonObject.getString("type"));
                        info.setContent(jsonObject.getString("content"));
                        list.add(info);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            }
        }
        if (isLoadData) {
            page++;
        }
    }

    /*************************************************
     * ��ȡˢ������
     * ��������ʱ����
     *************************************************/
    public void getRefalshData() {
        // ��ҳ������Ϊ1
        page = 1;
        isLoadData = false;
        list = new ArrayList<InformationEntity>();
        // ��ȡ����������������
        int tr = HttpUtil.getTotalRecord();
        pageBean = new PageBean();
        // ����pageBean���ܼ�¼��
        pageBean.setTotalRecord(tr);
        if (pageBean.getTotalPage() >= page) {
            pageBean.setCurrPage(page);
            // ��ȡ������������
            JSONArray jsonArray = HttpUtil.getInformationFromServer(pageBean
                    .getCurrPage());
            if (jsonArray != null && jsonArray.length() > 0) {
                isLoadData = true;
                for (int i = 0; i < jsonArray.length(); i++) {
                    try {
                        JSONObject jsonObject = jsonArray.getJSONObject(i);
                        InformationEntity info = new InformationEntity();
                        info.setId(jsonObject.getString("id"));
                        info.setTitle(jsonObject.getString("title"));
                        info.setDate(jsonObject.getString("date"));
                        info.setType(jsonObject.getString("type"));
                        info.setContent(jsonObject.getString("content"));
                        list.add(info);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            }
        }
        if (isLoadData) {
            page++;
        }
    }

    private void modifyPwdDone() {
        user.setPassword(newPwd);
        Toast.makeText(MainActivity.this, "�޸�����ɹ�", Toast.LENGTH_SHORT).show();
    }

    /*************************************************
     * ʵ�ֽӿڷ���
     * onLoad:���ظ��������
     * onReflash:ˢ������
     *************************************************/
    @Override
    public void onLoad() {
        Handler loadHandler = new Handler();
        loadHandler.postDelayed(new Runnable() {
            @Override
            public void run() {
                if (pageBean.getTotalPage() >= page) {
                    // ��ȡ��������
                    new Thread(getData).start();
                }
                // ֪ͨlistview�������
                isLoadData = false;
                listView.loadComplete();
            }
        }, 2000);
    }

    @Override
    public void onReflash() {
        // ���Ȼ�ȡ��������
        Handler reflashHandler = new Handler();
        reflashHandler.postDelayed(new Runnable() {
            @Override
            public void run() {
                new Thread(getReflashData).start();
                // ������еĶ���Ч��
                listView.reflashComplete();
            }
        }, 2000);
    }
}
