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
 * 主Activity
 *
 * @author 张建国
 *************************************************/
public class MainActivity extends Activity implements ILoadListener,
        IRefalshListener, OnClickListener, OnItemClickListener {
    /*************************************************
     * 数据定义区
     **************************************************/
    //打开个人中心界面的请求码
    public static final int PERSONAL = 1;
    //个人中心界面信息修改的结果吗
    public static final int CHANGE = 0;
    // 窗口控件
    private Button person;
    private TextView name;
    private ImageView gender;
    private TextView exit;
    private TextView change;
    private TextView info_id;
    Intent intent;
    //从登录界面传递过来的用户信息类用于设置,侧滑窗口的一些基本信息
    User user;
    // 菜单与子菜单组件
    private ResideMenu resideMenu;
    private ResideMenuItem personal;
    private ResideMenuItem changePassed;
    int page = 1;
    // 自定义的Adapter
    MyAdapter adapter;
    // 用来判断是否加载了跟多的数据
    boolean isLoadData;
    // 自定义的ListView
    MyListView listView;
    // PageBean对象，用来对ListView进行分页处理
    PageBean pageBean;
    // 数据集合，用来存储从服务器获取的数据
    ArrayList<InformationEntity> list = new ArrayList<InformationEntity>();

    /*************************************************
     * Activity入口
     **************************************************/
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE); // 去除标题栏
        setContentView(R.layout.main_activity);
        InitView();
        // 连接服务器，穿件一个长连接
        ServiceManager serviceManager = new ServiceManager(this);
        serviceManager.setNotificationIcon(R.drawable.notification);
        serviceManager.startService();
        // 获取服务器数据
        new Thread(getData).start();
    }

    /*************************************************
     * 初始化界面
     **************************************************/
    private void InitView() {
        //接收从登录界面传递过来的数据
        intent = getIntent();
        Bundle data = intent.getExtras();
        user = (User) data.getSerializable("user");

        // 初始化侧滑菜单
        resideMenu = new ResideMenu(this);
        resideMenu.setBackground(R.drawable.menu_bg);
        resideMenu.setUserName(user.getName());
        if (user.getGender().equals("男"))
            resideMenu.setUserGander(R.drawable.male);
        else if (user.getGender().equals("女"))
            resideMenu.setUserGander(R.drawable.female);
        else
            resideMenu.setUserGander(R.drawable.gender);
        resideMenu.setUserHead(user.getHead());
        resideMenu.attachToActivity(this);
        resideMenu.setScaleValue(0.8f);

        // 初始化侧滑菜单的子菜单
        personal = new ResideMenuItem(this,
                R.drawable.left_fragment_near_icon_normal, "个人中心");
        personal.setOnClickListener(this);
        resideMenu.addMenuItem1(personal, ResideMenu.DIRECTION_LEFT);
        changePassed = new ResideMenuItem(this,
                R.drawable.left_fragment_near_icon_normal, "修改密码");
        changePassed.setOnClickListener(this);
        resideMenu.addMenuItem1(changePassed, ResideMenu.DIRECTION_LEFT);

        //初始化菜单左下角的本文
        exit = new TextView(this);
        exit.setTypeface(Typeface.MONOSPACE, Typeface.ITALIC);
        exit.setText("退出");
        exit.setOnClickListener(this);
        resideMenu.addExit(exit);
        change = new TextView(this);
        change.setTypeface(Typeface.MONOSPACE, Typeface.ITALIC);
        change.setText("切换用户");
        change.setOnClickListener(this);
        resideMenu.addChange(change);

        // 初始化窗口控件
        person = (Button) findViewById(R.id.person);
        person.setOnClickListener(this);
        name = (TextView) findViewById(R.id.name);
    }

    /*************************************************
     * 添加点击事件
     **************************************************/
    @Override
    public void onClick(View v) {
        //点击用户按钮
        if (v == person) {
            //弹出侧滑菜单
            resideMenu.openMenu(0);
        }
        //点击个人中心
        if (v == personal) {
            //把当前User信息类传递给个人中心
            Intent intent = new Intent(this, Personal.class);
            Bundle data = new Bundle();
            data.putSerializable("user", user);
            intent.putExtras(data);
            /*
			 * 记录个人中心对用户信息的修改，用来改变侧滑菜单
			 * 的用户头像，性别，姓名
			 */
            startActivityForResult(intent, PERSONAL);
        }
        //点击修改密码
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
     * 只是密码修改的提示框，当用户输入的两次密码相同
     * 时，提示用户密码修改成功，否则提示用户密码修改失败
     */
    private String newPwd = "";
    private String oldPwd = "";

    public void showDialog() {
        final View dialog = getLayoutInflater().inflate(
                R.layout.change_password, null);
        new AlertDialog.Builder(this)
                .setTitle("修改密码")
                .setView(dialog)
                .setPositiveButton("确定",
                        new DialogInterface.OnClickListener() {

                            @Override
                            public void onClick(DialogInterface arg0,
                                                int arg1) {
                                newPwd = ((EditText) dialog.findViewById(R.id.newPassed)).getText().toString();
                                String againPassed = ((EditText) dialog.findViewById(R.id.againPassed)).getText().toString();
                                if (newPwd.equals("") || againPassed.equals("")) {
                                    Toast.makeText(MainActivity.this, "输入密码不能为空！修改失败！", Toast.LENGTH_SHORT).show();
                                } else {
                                    if (newPwd.equals(againPassed)) {
                                        new Thread(modifyPwd).start();
                                    } else {
                                        Toast.makeText(MainActivity.this, "两次输入密码不相同！修改失败！", Toast.LENGTH_SHORT).show();
                                    }
                                }
                            }
                        }).setNegativeButton("取消", null).show();
    }

    /*************************************************
     * 以提示窗口的形式，确认用户是否退出系统
     **************************************************/
    public void showExit() {
        new AlertDialog.Builder(this)
                .setTitle("退出系统")
                .setMessage("确定要退出系统么？")
                        //点击确定按钮，程序退出
                .setPositiveButton("确定",
                        new DialogInterface.OnClickListener() {

                            @Override
                            public void onClick(DialogInterface arg0,
                                                int arg1) {
                                MainActivity.this.finish();
                            }
                        }).setNegativeButton("取消", null).show();
    }

    /*************************************************
     * 监听列表项的点击，点击一个列表项，程序跳转到信息的详细界面
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
     * 当用户在个人中心内修改了用户信息并返回到该界面时时，当前侧滑
     * 菜单会相应改变，重新设置侧滑菜单的用户头像，姓名，性别
     *************************************************/
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent intent) {

        if (requestCode == PERSONAL && resultCode == CHANGE) {
            //更新User信息类
            Bundle data = intent.getExtras();
            user = (User) data.getSerializable("user");
            resideMenu.setUserHead(user.getHead());
            resideMenu.setUserName(user.getName());
            if (user.getGender().equals("男")) {
                resideMenu.setUserGander(R.drawable.male);
            } else {
                resideMenu.setUserGander(R.drawable.female);
            }
        }
    }

    /*************************************************
     * 新建联网线程和Handler
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
                Toast.makeText(getApplicationContext(), "修改失败", 2000);
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
     * 显示ListView
     *************************************************/
    private void showListView(ArrayList<InformationEntity> list) {
        // 第一次加载
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
     * 加载数据，进入主界面时加载
     * 用户向下滑动屏幕时调用
     *************************************************/
    private void getData() {
        // 获取服务器中数据总数
        int tr = HttpUtil.getTotalRecord();
        pageBean = new PageBean();
        // 设置pageBean的总记录数
        pageBean.setTotalRecord(tr);
        if (pageBean.getTotalPage() >= page) {
            pageBean.setCurrPage(page);
            // 获取服务器的数据
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
     * 获取刷新数据
     * 界面下拉时调用
     *************************************************/
    public void getRefalshData() {
        // 将页数重置为1
        page = 1;
        isLoadData = false;
        list = new ArrayList<InformationEntity>();
        // 获取服务器中数据总数
        int tr = HttpUtil.getTotalRecord();
        pageBean = new PageBean();
        // 设置pageBean的总记录数
        pageBean.setTotalRecord(tr);
        if (pageBean.getTotalPage() >= page) {
            pageBean.setCurrPage(page);
            // 获取服务器的数据
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
        Toast.makeText(MainActivity.this, "修改密码成功", Toast.LENGTH_SHORT).show();
    }

    /*************************************************
     * 实现接口方法
     * onLoad:加载更多的数据
     * onReflash:刷新数据
     *************************************************/
    @Override
    public void onLoad() {
        Handler loadHandler = new Handler();
        loadHandler.postDelayed(new Runnable() {
            @Override
            public void run() {
                if (pageBean.getTotalPage() >= page) {
                    // 获取更多数据
                    new Thread(getData).start();
                }
                // 通知listview加载完毕
                isLoadData = false;
                listView.loadComplete();
            }
        }, 2000);
    }

    @Override
    public void onReflash() {
        // 首先获取最新数据
        Handler reflashHandler = new Handler();
        reflashHandler.postDelayed(new Runnable() {
            @Override
            public void run() {
                new Thread(getReflashData).start();
                // 清除所有的动画效果
                listView.reflashComplete();
            }
        }, 2000);
    }
}
