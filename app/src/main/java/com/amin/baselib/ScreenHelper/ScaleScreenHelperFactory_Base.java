package com.amin.baselib.ScreenHelper;


import android.content.Context;
import android.os.Build;
import android.util.TypedValue;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewGroup.MarginLayoutParams;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.TextView;

import java.lang.reflect.Field;
import java.lang.reflect.Method;


final class ScaleScreenHelperFactory_Base extends ScaleScreen implements ScaleScreenHelper {

    private int scaleWidth;

    private float scaleSize;

    ScaleScreenHelperFactory_Base(Context context, int scaleWidth) {

        this(context, scaleWidth, 1f);

    }

    ScaleScreenHelperFactory_Base(Context context, int scaleWidth, float scaleSize) {

        super(context);

        this.scaleWidth = scaleWidth;

        this.scaleSize = scaleSize;

    }

    @Override
    public View loadViewMinMax(View view, int minWidth, int minHeight, int maxWidth, int maxHeight) {

        try {

            if (minWidth != 0) {

                view.getClass().getMethod("setMinWidth", int.class).invoke(view, getWidthHeight(minWidth));

            }

            if (minHeight != 0) {

                view.getClass().getMethod("setMinHeight", int.class).invoke(view, getWidthHeight(minHeight));

            }

            if (maxWidth != 0) {

                view.getClass().getMethod("setMaxWidth", int.class).invoke(view, getWidthHeight(maxWidth));

            }

            if (maxHeight != 0) {

                view.getClass().getMethod("setMaxHeight", int.class).invoke(view, getWidthHeight(maxHeight));

            }

        } catch (Exception e) {
        }

        return view;

    }

    @Override
    public View loadViewPadding(View view, int left, int top, int right, int bottom) {

        try {

            view.setPadding(getWidthHeight(left), getWidthHeight(top), getWidthHeight(right), getWidthHeight(bottom));

        } catch (Exception e) {
        }

        return view;

    }

    @Override
    public View loadViewMargin(View view, int left, int top, int right, int bottom) {

        try {

            MarginLayoutParams marginLayoutParams = (MarginLayoutParams) view.getLayoutParams();

            marginLayoutParams.leftMargin = getWidthHeight(left);

            marginLayoutParams.topMargin = getWidthHeight(top);

            marginLayoutParams.rightMargin = getWidthHeight(right);

            marginLayoutParams.bottomMargin = getWidthHeight(bottom);

        } catch (Exception e) {
        }

        return view;

    }

    @Override
    public View loadViewWidthHeightSize(View view, int width, int height, int size) {

        return loadViewSize(loadViewWidthHeight(view, width, height), size);

    }

    @Override
    public View loadViewWidthHeight(View view, int width, int height) {

        try {

            MarginLayoutParams marginLayoutParams = (MarginLayoutParams) view.getLayoutParams();

            int paramsWidth = getWidthHeight(width), paramsHeight = getWidthHeight(height);

            if (paramsWidth != 0) {

                marginLayoutParams.width = paramsWidth;

            }

            if (paramsHeight != 0) {

                marginLayoutParams.height = paramsHeight;

            }

        } catch (Exception e) {
        }

        return view;

    }

    @Override
    public View loadViewSize(View view, int size) {

        if (view instanceof TextView) {

            ((TextView) view).setTextSize(TypedValue.COMPLEX_UNIT_PX, getSize(size));

        } else if (view instanceof EditText) {

            ((EditText) view).setTextSize(TypedValue.COMPLEX_UNIT_PX, getSize(size));

        } else if (view instanceof Button) {

            ((Button) view).setTextSize(TypedValue.COMPLEX_UNIT_PX, getSize(size));

        } else if (view instanceof CheckBox) {

            ((CheckBox) view).setTextSize(TypedValue.COMPLEX_UNIT_PX, getSize(size));

        } else if (view instanceof RadioButton) {

            ((RadioButton) view).setTextSize(TypedValue.COMPLEX_UNIT_PX, getSize(size));

        }

        return view;

    }

    private View loadView(ViewGroup viewGroup, ScaleBox scaleBox) {

        scaleBox.onScale(getSize(viewGroup), viewGroup, this);

        for (int i = 0; i < viewGroup.getChildCount(); i++) {

            View view = viewGroup.getChildAt(i);

            if (view instanceof ViewGroup) {

                loadView((ViewGroup) view, scaleBox);

            } else {

                scaleBox.onScale(getSize(view), view, this);

            }

        }

//        if (viewGroup.getChildCount() != 0) {
//
//            for (int i = 0; i < viewGroup.getChildCount(); i++) {
//
//                View view = viewGroup.getChildAt(i);
//
//                if (view instanceof ViewGroup) {
//
//                    loadView((ViewGroup) view, scaleBox);
//
//                }else {
//
//                    scaleBox.onScale(getSize(view), view, this);
//
//                }
//
//            }
//
//        } else {
//
//            scaleBox.onScale(getSize(viewGroup), viewGroup, this);
//
//        }

        return viewGroup;

    }

    @Override
    public View loadView(ViewGroup viewGroup) {

        return loadView(viewGroup, new ScaleBox());

    }

    @Override
    public int getWidthHeight(int value) {

        return (int) scaleValue(SCREEN_WIDTH, scaleWidth, value);

    }

    @Override
    public float getSize(int value) {

        return (scaleValue(SCREEN_WIDTH, scaleWidth, value) * scaleSize);

    }

    private float scaleValue(float value1, float value2, float value3) {

        return (value2 > value3) ? (value1 / (value2 / value3)) : (value1 * (value3 / value2));

    }


    private int[] getSize(View view) {

        int[] size = new int[15];

        ViewGroup.LayoutParams params = view.getLayoutParams();

        if(params != null) {

            size[0] = params.width > 0 ? params.width : 0;//match-parent,warp-parent,0dp+weight布局不做处理

            size[1] = params.height > 0 ? params.height : 0;

            if (view instanceof TextView) {

                size[2] = (int) ((TextView) view).getTextSize();

            } else {

                size[2] = 0;

            }

            size[3] = ((MarginLayoutParams) params).leftMargin;
            size[4] = ((MarginLayoutParams) params).topMargin;
            size[5] = ((MarginLayoutParams) params).rightMargin;
            size[6] = ((MarginLayoutParams) params).bottomMargin;

            size[7] = view.getPaddingLeft();
            size[8] = view.getPaddingTop();
            size[9] = view.getPaddingRight();
            size[10] = view.getPaddingBottom();

            int minWidth,maxWidth,minHeight,maxHeght;
            minWidth = getMinWidth(view);
            maxWidth = getMaxWidth(view);
            minHeight = getMinHeight(view);
            maxHeght = getMaxHeight(view);

            size[11] = minWidth>0&&minWidth<720 ? minWidth : 0;
            size[12] = minHeight>0&&minHeight<1280 ? minHeight : 0;
            size[13] = maxWidth>0&&maxWidth<720 ? maxWidth : 0;
            size[14] = maxHeght>0&&maxHeght<1280 ? maxHeght : 0;

        }

            return size;

    }

    public static int getMinWidth(View view) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN)
            return view.getMinimumWidth();
        try {
            Field minWidth = view.getClass().getField("mMinWidth");
            minWidth.setAccessible(true);
            return (int) minWidth.get(view);
        } catch (Exception ignore) {
        }
        return 0;
    }

    public static int getMaxWidth(View view) {
        try {
            Method setMaxWidthMethod = view.getClass().getMethod("getMaxWidth");
            return (int) setMaxWidthMethod.invoke(view);
        } catch (Exception ignore) {
        }
        return 0;
    }

    public static int getMinHeight(View view) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
            return view.getMinimumHeight();
        } else {
            try {
                Field minHeight = view.getClass().getField("mMinHeight");
                minHeight.setAccessible(true);
                return (int) minHeight.get(view);
            } catch (Exception e) {
            }
        }

        return 0;
    }

    public static int getMaxHeight(View view) {
        try {
            Method setMaxWidthMethod = view.getClass().getMethod("getMaxHeight");
            return (int) setMaxWidthMethod.invoke(view);
        } catch (Exception ignore) {
        }
        return 0;
    }
}