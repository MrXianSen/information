package com.albery.view;

import com.albery.information.R;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.RotateAnimation;
import android.widget.AbsListView;
import android.widget.AbsListView.OnScrollListener;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;

/**
 * 自定义的ListView类
 *
 * @author Administrator
 */
public class MyListView extends ListView implements OnScrollListener {

    View footer;
    View header;
    int totalItemCount; // 总数量
    int lastVisiableItem;
    int firstVisiableItem;// 当前可见的第一个item的位置
    boolean isLoading;
    ILoadListener iLoadListener;
    IRefalshListener iRefalshListener;
    boolean isRemark; // 当前是在ListView的最顶端，并且是向下的
    int startY; // 摁下时的y值
    int headerHeight;
    int scrollState; // ListView当前滚动状态

    int state; // 当前状态
    final int NONE = 0; // 正常装填
    final int PULL = 1; // 下拉状态
    final int RELEASE = 2; // 释放状态
    final int REFLASHING = 3;// 正在刷新

    // 构造方法
    public MyListView(Context context) {
        super(context);
        initView(context);
    }

    public MyListView(Context context, AttributeSet attrs) {
        super(context, attrs);
        initView(context);
    }

    public MyListView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        initView(context);
    }

    /**
     * 初始化view，将加载条添加到listview底部
     *
     * @param context
     */
    public void initView(Context context) {
        LayoutInflater inflater = LayoutInflater.from(context);
        footer = inflater.inflate(R.layout.footer_layout, null);
        footer.findViewById(R.id.load_layout).setVisibility(View.GONE);

        header = inflater.inflate(R.layout.header_layout, null);
        measureView(header);
        // 获取header的高度
        headerHeight = header.getMeasuredHeight();
        topPadding(-headerHeight);
        this.addFooterView(footer);
        this.addHeaderView(header);
        // 添加滚动监听事件
        this.setOnScrollListener(this);
    }

    /**
     * 通知夫布局占用的宽高
     *
     * @param view
     */
    private void measureView(View view) {
        ViewGroup.LayoutParams p = view.getLayoutParams();
        if (p == null) {
            p = new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,
                    ViewGroup.LayoutParams.WRAP_CONTENT);
        }
        int width = ViewGroup.getChildMeasureSpec(0, 0, p.width);
        int height;
        int tempHeight = p.height;
        if (tempHeight > 0) {
            height = MeasureSpec.makeMeasureSpec(tempHeight,
                    MeasureSpec.EXACTLY);
        } else {
            height = MeasureSpec.makeMeasureSpec(0, MeasureSpec.UNSPECIFIED);
        }
        view.measure(width, height);
    }

    /**
     * 设置header布局的宽，高
     *
     * @param topPadding
     */
    private void topPadding(int topPadding) {
        header.setPadding(header.getPaddingLeft(), topPadding,
                header.getPaddingRight(), header.getPaddingBottom());
        header.invalidate();
    }

    @Override
    public void onScroll(AbsListView view, int firstVisiableItem,
                         int visiableItemCount, int totalItemCount) {
        this.lastVisiableItem = firstVisiableItem + visiableItemCount;
        this.firstVisiableItem = firstVisiableItem;
        this.totalItemCount = totalItemCount;
    }

    /***
     * 监听ListView状态的变化
     */
    @Override
    public void onScrollStateChanged(AbsListView view, int scrollState) {

		/*
         * 当屏幕停止滚动的时候scrollState的值为0 当用户手触碰屏幕的时候scrollState的值为1
		 * 当屏幕滑动的时候scrollState的值为2
		 */
        this.scrollState = scrollState;

        if (totalItemCount == lastVisiableItem
                && scrollState == SCROLL_STATE_IDLE) {
            if (!isLoading) {
                isLoading = true;
                footer.findViewById(R.id.load_layout).setVisibility(
                        View.VISIBLE);
                // 接口回调
                iLoadListener.onLoad();
            }
        }
    }

    /**
     * 添加用户触屏事件
     */
    @Override
    public boolean onTouchEvent(MotionEvent ev) {
        switch (ev.getAction()) {
            case MotionEvent.ACTION_DOWN:
                if (this.firstVisiableItem == 0) {
                    isRemark = true;
                    // 获取第一个view时的y位置
                    startY = (int) ev.getY();
                }
                break;
            case MotionEvent.ACTION_MOVE:
                onMove(ev);
                break;
            case MotionEvent.ACTION_UP:
                if (state == RELEASE) {
                    state = REFLASHING;
                    reflashViewByState();
                    // 加载最新数据
                    iRefalshListener.onReflash();
                } else if (state == PULL) {
                    state = NONE;
                    isRemark = false;
                    reflashViewByState();
                }
                break;
        }
        return super.onTouchEvent(ev);
    }

    /***
     * 判断移动过程中的操作
     *
     * @param ev
     */
    private void onMove(MotionEvent ev) {
        if (!isRemark) {
            return;
        }

        int tempY = (int) ev.getY();
        int space = tempY - startY;
        int topPadding = space - headerHeight;
        switch (state) {
            case NONE:
                if (space > 0) { // 处于下拉状态
                    state = PULL;
                    reflashViewByState();
                }
                break;
            case PULL:
                topPadding(topPadding);
                if (space > headerHeight + 30
                        && scrollState == SCROLL_STATE_TOUCH_SCROLL) {
                    state = RELEASE;
                    reflashViewByState();
                }
                break;
            case RELEASE:
                topPadding(topPadding);
                if (space < headerHeight + 30) {
                    state = PULL;
                    reflashViewByState();
                } else if (space <= 0) {
                    state = NONE;
                    isRemark = false;
                    reflashViewByState();
                }
                break;
            case REFLASHING:
                break;
        }
    }

    /**
     * 根据状态改变view
     */
    private void reflashViewByState() {
        TextView tip = (TextView) header.findViewById(R.id.tip);
        ImageView arrow = (ImageView) header.findViewById(R.id.arraw);
        ProgressBar progress = (ProgressBar) header
                .findViewById(R.id.progress_bar);
        RotateAnimation anim = new RotateAnimation(0, 180,
                Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF,
                0.5f);
        anim.setDuration(500);
        anim.setFillAfter(true);
        RotateAnimation animBack = new RotateAnimation(180, 0,
                Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF,
                0.5f);
        animBack.setDuration(500);
        animBack.setFillAfter(true);

        switch (state) {
            case NONE: // 正常状态
                arrow.clearAnimation();
                topPadding(-headerHeight);
                break;
            case PULL: // 下拉状态
                arrow.setVisibility(View.VISIBLE);
                progress.setVisibility(View.GONE);
                tip.setText("下拉可以刷新...");
                arrow.clearAnimation();
                arrow.setAnimation(animBack);
                break;
            case RELEASE: // 释放状态
                arrow.setVisibility(View.VISIBLE);
                progress.setVisibility(View.GONE);
                tip.setText("松开可以刷新...");
                arrow.clearAnimation();
                arrow.setAnimation(anim);
                break;
            case REFLASHING: // 正在刷新状态
                topPadding(20);
                arrow.clearAnimation();
                arrow.setVisibility(View.GONE);
                progress.setVisibility(View.VISIBLE);
                tip.setText("正在刷新...");
                break;
        }
    }

    /**
     * 刷新完毕 数据加载完毕之后调用
     */
    public void reflashComplete() {
        state = NONE;
        isRemark = false;
        reflashViewByState();
    }

    /**
     * 加载完毕 隐藏起来
     *
     * @author Administrator
     */
    public void loadComplete() {
        isLoading = false;
        footer.findViewById(R.id.load_layout).setVisibility(View.GONE);
    }

    public void setInterface(ILoadListener iLoadListener,
                             IRefalshListener iRefalshListener) {
        this.iLoadListener = iLoadListener;
        this.iRefalshListener = iRefalshListener;
    }

    /**
     * 回调接口
     *
     * @author Administrator
     */
    // 加载更多的数据
    public interface ILoadListener {
        public void onLoad();
    }

    // 刷新数据
    public interface IRefalshListener {
        public void onReflash();
    }

}
