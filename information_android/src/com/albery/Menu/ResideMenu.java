/************************************************************************************
 * 菜单界面
 * <p>
 * 该文件实现菜单的界面，该菜单可通过点击主页面的“个人”按钮来调出，也能通过右滑主页
 * 面菜单来调出，菜单实现了用户的一些个人信息（如果用户没有登录，可通过点击头像来进入登
 * 界面录），菜单实现了几个子菜单，我的相册，我的动态，我的消息，我的社区4个子菜单项
 * 该文件借鉴了他人代码，具体的一些接口会有相应注释，其他的地方不说明
 * <p>
 * 功能：实现菜单界面
 * <p>
 * 开发者：黄静峰
 * <p>
 * 版本：1.0.0
 * <p>
 * 开发时间：2015/4/26
 ************************************************************************************/

package com.albery.Menu;

import java.util.ArrayList;
import java.util.List;

import android.animation.Animator;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.app.Activity;
import android.content.Context;
import android.content.res.Configuration;
import android.graphics.Rect;
import android.os.Build;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.GestureDetector;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.animation.AnimationUtils;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import com.albery.information.R;
import com.nineoldandroids.view.ViewHelper;

@TargetApi(Build.VERSION_CODES.HONEYCOMB)
public class ResideMenu extends FrameLayout {

    public static final int DIRECTION_LEFT = 0;
    public static final int DIRECTION_RIGHT = 1;
    private static final int PRESSED_MOVE_HORIZANTAL = 2;
    private static final int PRESSED_DOWN = 3;
    private static final int PRESSED_DONE = 4;
    private static final int PRESSED_MOVE_VERTICAL = 5;

    private ImageView imageViewShadow;
    private ImageView imageViewBackground;
    private ImageView userHead;
    private TextView userName;
    private ImageView userGander;
    private TextView exit;
    private LinearLayout exitLayout;
    private LinearLayout changeLayout;
    private LinearLayout layoutLeftMenu1;
    private ScrollView scrollViewLeftMenu;
    private ScrollView scrollViewMenu;
    /** the activity that view attach to */
    private Activity activity;
    /** the decorview of the activity */
    private ViewGroup viewDecor;
    /** the viewgroup of the activity */
    private TouchDisableView viewActivity;
    /** the flag of menu open status */
    private boolean isOpened;
    private GestureDetector gestureDetector;
    private float shadowAdjustScaleX;
    private float shadowAdjustScaleY;
    /** the view which don't want to intercept touch event */
    private List<View> ignoredViews;
    private List<ResideMenuItem> leftMenuItems;
    private DisplayMetrics displayMetrics = new DisplayMetrics();
    private OnMenuListener menuListener;
    private float lastRawX;
    private boolean isInIgnoredView = false;
    private int scaleDirection = DIRECTION_LEFT;
    private int pressedState = PRESSED_DOWN;
    private List<Integer> disabledSwipeDirection = new ArrayList<Integer>();
    // valid scale factor is between 0.0f and 1.0f.
    private float mScaleValue = 0.1f;

    public ResideMenu(Context context) {
        super(context);
        //初始化view
        initViews(context);
    }

    //加载了一个residemenu的布局
    private void initViews(Context context) {
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        inflater.inflate(R.layout.residemenu, this);
        scrollViewLeftMenu = (ScrollView) findViewById(R.id.sv_left_menu);
        imageViewShadow = (ImageView) findViewById(R.id.iv_shadow);
        layoutLeftMenu1 = (LinearLayout) findViewById(R.id.layout_left_menu1);
        exitLayout = (LinearLayout) findViewById(R.id.exitLayout);
        changeLayout = (LinearLayout) findViewById(R.id.changeLayout);
        imageViewBackground = (ImageView) findViewById(R.id.iv_background);
        userHead = (ImageView) findViewById(R.id.menu_user_head);
        userName = (TextView) findViewById(R.id.name);
        userGander = (ImageView) findViewById(R.id.gender);

    }

    /**
     * use the method to set up the activity which residemenu need to show;
     *
     * @param activity
     */
    public void attachToActivity(Activity activity) {
        //初始化参数
        initValue(activity);
        //正对横竖屏缩放比例进行调整
        setShadowAdjustScaleXByOrientation();
        //添加当前view
        viewDecor.addView(this, 0);
        //设置view边距
        setViewPadding();
    }

    //初始化控件
    private void initValue(Activity activity) {
        this.activity = activity;
        leftMenuItems = new ArrayList<ResideMenuItem>();
        ignoredViews = new ArrayList<View>();
        viewDecor = (ViewGroup) activity.getWindow().getDecorView();
        viewActivity = new TouchDisableView(this.activity);

        View mContent = viewDecor.getChildAt(0);
        viewDecor.removeViewAt(0);
        viewActivity.setContent(mContent);
        addView(viewActivity);
    }

    private void setShadowAdjustScaleXByOrientation() {
        int orientation = getResources().getConfiguration().orientation;
        if (orientation == Configuration.ORIENTATION_LANDSCAPE) {
            shadowAdjustScaleX = 0.034f;
            shadowAdjustScaleY = 0.12f;
        } else if (orientation == Configuration.ORIENTATION_PORTRAIT) {
            shadowAdjustScaleX = 0.06f;
            shadowAdjustScaleY = 0.07f;
        }
    }

    //设置菜单的背景
    public void setBackground(int imageResrouce) {
        imageViewBackground.setImageResource(imageResrouce);
    }

    //设置菜单的用户头像
    public void setUserHead(int imageResrouce) {
        userHead.setImageResource(imageResrouce);
    }

    //设置菜单的名户名
    public void setUserName(String name) {
        userName.setText(name);
    }

    //设置菜单的用户性别
    public void setUserGander(int imageResrouce) {
        userGander.setImageResource(imageResrouce);
    }

    //添加子菜单项到子菜单1栏里边
    public void addMenuItem1(ResideMenuItem menuItem, int direction) {
        if (direction == DIRECTION_LEFT) {
            this.leftMenuItems.add(menuItem);
            layoutLeftMenu1.addView(menuItem);
        }
    }

    public void addExit(TextView exit) {
        exitLayout.addView(exit);
    }

    public void addChange(TextView change) {
        changeLayout.addView(change);
    }

    /**
     * we need the call the method before the menu show, because the padding of
     * activity can't get at the moment of onCreateView();
     */
    private void setViewPadding() {
        this.setPadding(viewActivity.getPaddingLeft(), viewActivity.getPaddingTop(), viewActivity.getPaddingRight(), viewActivity.getPaddingBottom());
    }

    /*******************************************
     *打开菜单栏
     *
     *功能：打开菜单
     *
     *参数分析：
     *direction-----打开哪个菜单（原来还有右菜单的，这里去掉）
     *
     ********************************************/
    public void openMenu(int direction) {

        //设置移动的位置
        setScaleDirection(direction);

        //设置菜单状态
        isOpened = true;
        //设置主页缩放的大小
        AnimatorSet scaleDown_activity = buildScaleDownAnimation(viewActivity, mScaleValue, mScaleValue);
        AnimatorSet scaleDown_shadow = buildScaleDownAnimation(imageViewShadow, mScaleValue + shadowAdjustScaleX, mScaleValue + shadowAdjustScaleY);
        //设置菜单透明度变化
        AnimatorSet alpha_menu = buildMenuAnimation(scrollViewMenu, 1.0f);
        scaleDown_shadow.addListener(animationListener);
        scaleDown_activity.playTogether(scaleDown_shadow);
        scaleDown_activity.playTogether(alpha_menu);
        scaleDown_activity.start();
    }

    /*******************************************
     *关闭菜单栏
     *
     *功能：关闭菜单
     *
     ********************************************/
    @SuppressLint("NewApi")
    public void closeMenu() {

        //设置菜单状态
        isOpened = false;
        //设置主页缩放的大小
        AnimatorSet scaleUp_activity = buildScaleUpAnimation(viewActivity, 1.0f, 1.0f);
        AnimatorSet scaleUp_shadow = buildScaleUpAnimation(imageViewShadow, 1.0f, 1.0f);
        //设置菜单透明度变化
        AnimatorSet alpha_menu = buildMenuAnimation(scrollViewMenu, 0.0f);
        scaleUp_activity.addListener(animationListener);
        scaleUp_activity.playTogether(scaleUp_shadow);
        scaleUp_activity.playTogether(alpha_menu);
        scaleUp_activity.start();
    }

    private boolean isInDisableDirection(int direction) {
        return disabledSwipeDirection.contains(direction);
    }

    //设置主页滑动的位置
    private void setScaleDirection(int direction) {

        int screenWidth = getScreenWidth();
        float pivotX;
        float pivotY = getScreenHeight() * 0.5f;

        if (direction == DIRECTION_LEFT) {
            scrollViewMenu = scrollViewLeftMenu;
            pivotX = screenWidth * 4.0f;
        } else {
//			scrollViewMenu = scrollViewRightMenu;
            pivotX = screenWidth * -0.5f;
        }

        ViewHelper.setPivotX(viewActivity, pivotX);
        ViewHelper.setPivotY(viewActivity, pivotY);
        ViewHelper.setPivotX(imageViewShadow, pivotX);
        ViewHelper.setPivotY(imageViewShadow, pivotY);
        scaleDirection = direction;
    }

    /**
     * return the flag of menu status;
     *
     * @return
     */
    public boolean isOpened() {
        return isOpened;
    }

    private OnClickListener viewActivityOnClickListener = new OnClickListener() {
        @Override
        public void onClick(View view) {
            if (isOpened())
                closeMenu();
        }
    };

    private Animator.AnimatorListener animationListener = new Animator.AnimatorListener() {
        @Override
        public void onAnimationStart(Animator animation) {
            if (isOpened()) {
                scrollViewMenu.setVisibility(VISIBLE);
                if (menuListener != null)
                    menuListener.openMenu();
            }
        }

        @Override
        public void onAnimationEnd(Animator animation) {
            // reset the view;
            if (isOpened()) {
                viewActivity.setTouchDisable(true);
                viewActivity.setOnClickListener(viewActivityOnClickListener);
            } else {
                viewActivity.setTouchDisable(false);
                viewActivity.setOnClickListener(null);
                scrollViewMenu.setVisibility(GONE);
                if (menuListener != null)
                    menuListener.closeMenu();
            }
        }

        @Override
        public void onAnimationCancel(Animator animation) {

        }

        @Override
        public void onAnimationRepeat(Animator animation) {

        }
    };

    /**
     * a helper method to build scale down animation;
     *
     * @param target
     * @param targetScaleX
     * @param targetScaleY
     * @return
     */
    private AnimatorSet buildScaleDownAnimation(View target, float targetScaleX, float targetScaleY) {

        AnimatorSet scaleDown = new AnimatorSet();
        scaleDown.playTogether(ObjectAnimator.ofFloat(target, "scaleX", targetScaleX), ObjectAnimator.ofFloat(target, "scaleY", targetScaleY));

        scaleDown.setInterpolator(AnimationUtils.loadInterpolator(activity, android.R.anim.decelerate_interpolator));
        scaleDown.setDuration(500);
        return scaleDown;
    }

    /**
     * a helper method to build scale up animation;
     *
     * @param target
     * @param targetScaleX
     * @param targetScaleY
     * @return
     */
    private AnimatorSet buildScaleUpAnimation(View target, float targetScaleX, float targetScaleY) {

        AnimatorSet scaleUp = new AnimatorSet();
        scaleUp.playTogether(ObjectAnimator.ofFloat(target, "scaleX", targetScaleX), ObjectAnimator.ofFloat(target, "scaleY", targetScaleY));

        scaleUp.setDuration(500);
        return scaleUp;
    }

    private AnimatorSet buildMenuAnimation(View target, float alpha) {

        AnimatorSet alphaAnimation = new AnimatorSet();
        alphaAnimation.playTogether(ObjectAnimator.ofFloat(target, "alpha", alpha));

        alphaAnimation.setDuration(500);
        return alphaAnimation;
    }


    /**
     * if the motion evnent was relative to the view which in ignored view
     * list,return true;
     *
     * @param ev
     * @return
     */
    private boolean isInIgnoredView(MotionEvent ev) {
        Rect rect = new Rect();
        for (View v : ignoredViews) {
            v.getGlobalVisibleRect(rect);
            if (rect.contains((int) ev.getX(), (int) ev.getY()))
                return true;
        }
        return false;
    }

    private void setScaleDirectionByRawX(float currentRawX) {
        if (currentRawX > lastRawX)
            setScaleDirection(DIRECTION_LEFT);
        // else
        // setScaleDirection(DIRECTION_RIGHT);
    }

    private float getTargetScale(float currentRawX) {
        float scaleFloatX = ((currentRawX - lastRawX) / getScreenWidth()) * 0.75f;
        scaleFloatX = scaleDirection == DIRECTION_RIGHT ? -scaleFloatX : scaleFloatX;

        float targetScale = ViewHelper.getScaleX(viewActivity) - scaleFloatX;
        targetScale = targetScale > 1.0f ? 1.0f : targetScale;
        targetScale = targetScale < 0.5f ? 0.5f : targetScale;
        return targetScale;
    }

    private float lastActionDownX, lastActionDownY;

    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {
        float currentActivityScaleX = ViewHelper.getScaleX(viewActivity);
        if (currentActivityScaleX == 1.0f)
            setScaleDirectionByRawX(ev.getRawX());

        switch (ev.getAction()) {
            case MotionEvent.ACTION_DOWN:
                lastActionDownX = ev.getX();
                lastActionDownY = ev.getY();
                isInIgnoredView = isInIgnoredView(ev) && !isOpened();
                pressedState = PRESSED_DOWN;
                break;
            case MotionEvent.ACTION_MOVE:
                if (isInIgnoredView || isInDisableDirection(scaleDirection))
                    break;

                if (pressedState != PRESSED_DOWN && pressedState != PRESSED_MOVE_HORIZANTAL)
                    break;

                int xOffset = (int) (ev.getX() - lastActionDownX);
                int yOffset = (int) (ev.getY() - lastActionDownY);

                if (pressedState == PRESSED_DOWN) {
                    Log.i("info", "PRESSED_DOWN");
                    if (yOffset > 25 || yOffset < -25) {
                        pressedState = PRESSED_MOVE_VERTICAL;
                        break;
                    }
                    if (xOffset < -50 || xOffset > 50) {
                        pressedState = PRESSED_MOVE_HORIZANTAL;
                        ev.setAction(MotionEvent.ACTION_CANCEL);
                    }
                } else if (pressedState == PRESSED_MOVE_HORIZANTAL) {
                    Log.i("info", "PRESSED_MOVE_HORIZANTAL");
                    if (currentActivityScaleX < 0.95)
                        scrollViewMenu.setVisibility(VISIBLE);
                    float targetScale = getTargetScale(ev.getRawX());
                    ViewHelper.setScaleX(viewActivity, targetScale);
                    ViewHelper.setScaleY(viewActivity, targetScale);
                    ViewHelper.setScaleX(imageViewShadow, targetScale + shadowAdjustScaleX);
                    ViewHelper.setScaleY(imageViewShadow, targetScale + shadowAdjustScaleY);
                    ViewHelper.setAlpha(scrollViewMenu, (1 - targetScale) * 2.0f);

                    lastRawX = ev.getRawX();
                    return true;
                }
                break;
            case MotionEvent.ACTION_UP:
                if (isInIgnoredView)
                    break;
                if (pressedState != PRESSED_MOVE_HORIZANTAL)
                    break;

                pressedState = PRESSED_DONE;
                if (isOpened()) {
                    if (currentActivityScaleX > 0.56f)
                        closeMenu();
                    else
                        openMenu(scaleDirection);
                } else {
                    if (currentActivityScaleX < 0.94f) {
                        openMenu(scaleDirection);
                    } else {
                        closeMenu();
                    }
                }
                break;

        }
        lastRawX = ev.getRawX();
        return super.dispatchTouchEvent(ev);
    }

    public int getScreenHeight() {
        activity.getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
        return displayMetrics.heightPixels;
    }

    public int getScreenWidth() {
        activity.getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
        return displayMetrics.widthPixels;
    }

    public void setScaleValue(float scaleValue) {
        this.mScaleValue = scaleValue;
    }

    public interface OnMenuListener {

        /**
         * the method will call on the finished time of opening menu's
         * animation.
         */
        public void openMenu();

        /**
         * the method will call on the finished time of closing menu's animation
         * .
         */
        public void closeMenu();
    }

}
