package com.amin.baselib.ScreenHelper;


import android.view.View;
import android.view.ViewGroup;

/**
 * 屏幕支配器
 *
 * @author zcx
 */
public interface ScaleScreenHelper {

    /**
     * 设置视图最大宽高和最小宽高
     *
     * @param v  视图
     * @param nw 最小宽
     * @param nh 最小高
     * @param xw 最大宽
     * @param xh 最大高
     * @return
     */
    public View loadViewMinMax(View v, int nw, int nh, int xw, int xh);

    /**
     * 设置视图内边距
     *
     * @param v 视图
     * @param l 距左
     * @param t 距上
     * @param r 距右
     * @param b 距下
     * @return
     */
    public View loadViewPadding(View v, int l, int t, int r, int b);

    /**
     * 设置视图外边距
     *
     * @param v 视图
     * @param l 距左
     * @param t 距上
     * @param r 距右
     * @param b 距下
     * @return
     */
    public View loadViewMargin(View v, int l, int t, int r, int b);

    /**
     * 设置视图宽高字体
     *
     * @param v 视图
     * @param w 宽
     * @param h 高
     * @param s 字体大小
     * @return
     */
    public View loadViewWidthHeightSize(View v, int w, int h, int s);

    /**
     * 设置视图宽高
     *
     * @param v 视图
     * @param w 宽
     * @param h 高
     * @return
     */
    public View loadViewWidthHeight(View v, int w, int h);

    /**
     * 设置视图字体
     *
     * @param v 视图
     * @param s 字体大小
     * @return
     */
    public View loadViewSize(View v, int s);

    /**
     * 对ViewGroup进行适配<br>
     * <p>
     * TextView android:tag="s{10,10,10,10}[10,10,10,10](46,30,30)"<br>
     * android:layout_width=" wrap_content " <br>
     * android:layout_height=" wrap_content "  <br>
     * <p>
     * 适配标签有三种： <br>
     * <p>
     * 一、{10,10,10,10}   margin标签 {左，上，右，下} <br>
     * <p>
     * margin 标签不需要时可以不写，书写时要求不能缺少参数，内部参数顺序固定。  <br>
     * <p>
     * 二、[10,10,10,10]    padding标签[左，上，右，下] <br>
     * <p>
     * padding标签不需要时可以不写，书写时要求不能缺少参数，内部参数顺序固定。  <br>
     * <p>
     * 三、(46,30,30)   设置视图的（宽，高，字体）  <br>
     * <p>
     * 1，标签应用是如果该视图之需要适配宽高（46,30） <br>
     * 2，如果我们需要适配可以显示问题的视图并要求适配字体如TextView, CheckBox 等视图可以这么写（46,30,30） <br>
     * 3，如果 只需要适配字体的话 （30） <br>
     * 4，最后一种情况，比如我们会碰到一些视图，例如LinearLayout  由于布局要求他的宽或者高是充满全屏，又或者是自适应的时候，我们在填写参数的时候可以把相应的参数设置成0，这样的话适配工具不会对在但是进行计算，例如（46,0,30）<br>
     * <p>
     * 动画： <br>
     * <p>
     * s 小~大 <br>
     * t 渐出	 <br>
     * l 左~右 <br>
     * r 又~左 <br>
     * p 上~下 <br>
     * b 下~上 <br>
     *
     * @param v ViewGroup
     * @return
     */
    public View loadView(ViewGroup v);

    /**
     * 换算宽高
     *
     * @param v 测量的宽或高
     * @return
     */
    public int getWidthHeight(int v);

    /**
     * 换算字体
     *
     * @param v 测量的大小
     * @return
     */
    public float getSize(int v);

}

