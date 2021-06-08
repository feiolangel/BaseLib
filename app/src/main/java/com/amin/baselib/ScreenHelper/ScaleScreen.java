package com.amin.baselib.ScreenHelper;


import android.content.Context;

abstract class ScaleScreen {

    int SCREEN_WIDTH, SCREEN_HIGHT;

    public ScaleScreen(Context context) {

        int[] size = UtilScreen.screenSize(context);

        SCREEN_WIDTH = size[0];

        SCREEN_HIGHT = size[1];

    }

}


