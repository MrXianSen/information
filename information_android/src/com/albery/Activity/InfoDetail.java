package com.albery.Activity;

import java.util.ArrayList;

import org.json.JSONArray;
import org.json.JSONObject;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import com.albery.adapter.CommentAdapter;
import com.albery.entity.Comment;
import com.albery.entity.InformationEntity;
import com.albery.entity.PageBean;
import com.albery.entity.User;
import com.albery.information.R;
import com.albery.util.HttpUtil;
import com.albery.view.MyListView;
import com.albery.view.MyListView.ILoadListener;
import com.albery.view.MyListView.IRefalshListener;

public class InfoDetail extends Activity implements ILoadListener,
        IRefalshListener, OnClickListener {
    /*************************************************
     * 变量区
     *************************************************/
    private TextView title;
    private TextView time;
    private TextView type;
    private TextView content;
    private EditText commentContent;
    private Button send;
    int page = 1;
    CommentAdapter adapter;
    MyListView listView;
    PageBean pageBean;
    ArrayList<Comment> list = new ArrayList<Comment>();
    private InformationEntity info;
    private User user;
    public Comment comment;

    /*************************************************
     * 初始化界面
     *************************************************/
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE); // 去除标题栏
        setContentView(R.layout.info_detail);
        InitView();
        new Thread(getComment).start();
    }

    private void InitView() {
        Intent intent = getIntent();
        Bundle data = intent.getExtras();
        info = (InformationEntity) data.getSerializable("info");
        user = (User) data.getSerializable("user");
        title = (TextView) findViewById(R.id.item_title);
        title.setText(info.getTitle());
        time = (TextView) findViewById(R.id.item_date);
        time.setText(info.getDate());
        type = (TextView) findViewById(R.id.item_type);
        type.setText(info.getType());
        content = (TextView) findViewById(R.id.item_content);
        content.setText(info.getContent());
        commentContent = (EditText) findViewById(R.id.comment_content);
        send = (Button) findViewById(R.id.send);
        send.setOnClickListener(this);
    }

    /*************************************************
     * 添加事件
     *************************************************/
    @Override
    public void onClick(View v) {
        if (v == send) {
            if (HttpUtil.loginUser == null) {
                Toast.makeText(getApplicationContext(), "请先登录", 2000).show();
                return;
            }
            commentContent = (EditText) findViewById(R.id.comment_content);
            String content = commentContent.getText().toString();
            comment = new Comment();
            comment.setInfoId(info.getId());
            comment.setUserID(HttpUtil.loginUser.getID());
            comment.setName(HttpUtil.loginUser.getName());
            comment.setContent(content);
            new Thread(saveComment).start();
            commentContent.setText("");
        }
    }

    /*************************************************
     * 评论列表
     *************************************************/
    public void showListView() {
        if (adapter == null) {
            listView = (MyListView) findViewById(R.id.commentList);
            listView.setInterface(this, this);
            adapter = new CommentAdapter(this, list);
            listView.setAdapter(adapter);
        } else {
            adapter.onDataChange(list);
        }
    }

    /*************************************************
     * 新建联网线程和Handler
     *************************************************/
    Handler commentListHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            Bundle data = msg.getData();
            String val = data.getString("done");
            if (val == "1") {
                showListView();
            }
        }
    };
    Handler saveCommentHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            Bundle data = msg.getData();
            String val = data.getString("done");
            if (val.equals("1")) {
                list = new ArrayList<Comment>();
                new Thread(getComment).start();
            } else {
                Toast.makeText(getApplicationContext(), "评论失败", 2000).show();
            }
        }
    };
    Runnable getComment = new Runnable() {
        @Override
        public void run() {
            getData();
            Bundle data = new Bundle();
            Message msg = new Message();
            data.putString("done", "1");
            msg.setData(data);
            commentListHandler.sendMessage(msg);
        }
    };

    Runnable saveComment = new Runnable() {
        @Override
        public void run() {
            int res = HttpUtil.saveComment(comment);
            Message msg = new Message();
            Bundle data = new Bundle();
            data.putString("done", String.valueOf(res));
            msg.setData(data);
            saveCommentHandler.sendMessage(msg);
        }
    };

    /*************************************************
     * 获取数据
     *************************************************/
    private void getData() {
        JSONArray jsonArray = HttpUtil.getCommentFromServer(info.getId());
        try {
            if (jsonArray != null && jsonArray.length() > 0
                    && !jsonArray.getJSONObject(0).has("code")) {
                for (int i = 0; i < jsonArray.length(); i++) {

                    JSONObject jsonObj = jsonArray.getJSONObject(i);
                    Comment comment = new Comment();
                    comment.setId(jsonObj.getString("id"));
                    comment.setContent(jsonObj.getString("content"));
                    comment.setInfoId(jsonObj.getString("infoId"));
                    comment.setName(jsonObj.getString("userName"));
                    list.add(comment);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /*************************************************
     * onReflash:刷新 onLoad:加载
     *************************************************/
    @Override
    public void onReflash() {
        // 首先获取最新数据
        Handler reflashHandler = new Handler();
        reflashHandler.postDelayed(new Runnable() {
            @Override
            public void run() {
                list = new ArrayList<Comment>();
                new Thread(getComment).start();
                // 清除所有的动画效果
                listView.reflashComplete();
            }
        }, 2000);
    }

    @Override
    public void onLoad() {
        Handler loadHandler = new Handler();
        loadHandler.postDelayed(new Runnable() {
            @Override
            public void run() {
                // 通知listview加载完毕
                list = new ArrayList<Comment>();
                new Thread(getComment).start();
                listView.loadComplete();
            }
        }, 2000);
    }
}
