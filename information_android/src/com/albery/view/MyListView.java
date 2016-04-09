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
 * �Զ����ListView��
 *
 * @author Administrator
 */
public class MyListView extends ListView implements OnScrollListener {

    View footer;
    View header;
    int totalItemCount; // ������
    int lastVisiableItem;
    int firstVisiableItem;// ��ǰ�ɼ��ĵ�һ��item��λ��
    boolean isLoading;
    ILoadListener iLoadListener;
    IRefalshListener iRefalshListener;
    boolean isRemark; // ��ǰ����ListView����ˣ����������µ�
    int startY; // ����ʱ��yֵ
    int headerHeight;
    int scrollState; // ListView��ǰ����״̬

    int state; // ��ǰ״̬
    final int NONE = 0; // ����װ��
    final int PULL = 1; // ����״̬
    final int RELEASE = 2; // �ͷ�״̬
    final int REFLASHING = 3;// ����ˢ��

    // ���췽��
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
     * ��ʼ��view������������ӵ�listview�ײ�
     *
     * @param context
     */
    public void initView(Context context) {
        LayoutInflater inflater = LayoutInflater.from(context);
        footer = inflater.inflate(R.layout.footer_layout, null);
        footer.findViewById(R.id.load_layout).setVisibility(View.GONE);

        header = inflater.inflate(R.layout.header_layout, null);
        measureView(header);
        // ��ȡheader�ĸ߶�
        headerHeight = header.getMeasuredHeight();
        topPadding(-headerHeight);
        this.addFooterView(footer);
        this.addHeaderView(header);
        // ��ӹ��������¼�
        this.setOnScrollListener(this);
    }

    /**
     * ֪ͨ�򲼾�ռ�õĿ��
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
     * ����header���ֵĿ���
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
     * ����ListView״̬�ı仯
     */
    @Override
    public void onScrollStateChanged(AbsListView view, int scrollState) {

		/*
         * ����Ļֹͣ������ʱ��scrollState��ֵΪ0 ���û��ִ�����Ļ��ʱ��scrollState��ֵΪ1
		 * ����Ļ������ʱ��scrollState��ֵΪ2
		 */
        this.scrollState = scrollState;

        if (totalItemCount == lastVisiableItem
                && scrollState == SCROLL_STATE_IDLE) {
            if (!isLoading) {
                isLoading = true;
                footer.findViewById(R.id.load_layout).setVisibility(
                        View.VISIBLE);
                // �ӿڻص�
                iLoadListener.onLoad();
            }
        }
    }

    /**
     * ����û������¼�
     */
    @Override
    public boolean onTouchEvent(MotionEvent ev) {
        switch (ev.getAction()) {
            case MotionEvent.ACTION_DOWN:
                if (this.firstVisiableItem == 0) {
                    isRemark = true;
                    // ��ȡ��һ��viewʱ��yλ��
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
                    // ������������
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
     * �ж��ƶ������еĲ���
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
                if (space > 0) { // ��������״̬
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
     * ����״̬�ı�view
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
            case NONE: // ����״̬
                arrow.clearAnimation();
                topPadding(-headerHeight);
                break;
            case PULL: // ����״̬
                arrow.setVisibility(View.VISIBLE);
                progress.setVisibility(View.GONE);
                tip.setText("��������ˢ��...");
                arrow.clearAnimation();
                arrow.setAnimation(animBack);
                break;
            case RELEASE: // �ͷ�״̬
                arrow.setVisibility(View.VISIBLE);
                progress.setVisibility(View.GONE);
                tip.setText("�ɿ�����ˢ��...");
                arrow.clearAnimation();
                arrow.setAnimation(anim);
                break;
            case REFLASHING: // ����ˢ��״̬
                topPadding(20);
                arrow.clearAnimation();
                arrow.setVisibility(View.GONE);
                progress.setVisibility(View.VISIBLE);
                tip.setText("����ˢ��...");
                break;
        }
    }

    /**
     * ˢ����� ���ݼ������֮�����
     */
    public void reflashComplete() {
        state = NONE;
        isRemark = false;
        reflashViewByState();
    }

    /**
     * ������� ��������
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
     * �ص��ӿ�
     *
     * @author Administrator
     */
    // ���ظ��������
    public interface ILoadListener {
        public void onLoad();
    }

    // ˢ������
    public interface IRefalshListener {
        public void onReflash();
    }

}
