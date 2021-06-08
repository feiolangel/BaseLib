package com.amin.baselib.ScreenHelper;

import android.content.Context;
import android.util.DisplayMetrics;
import android.view.WindowManager;

/**
 * 屏幕尺寸工具
 *
 * @author Administrator
 *
 */
public final class UtilScreen {

	private UtilScreen() {}

	/**
	 * 获取屏幕尺寸
	 *
	 * @param context
	 * @return
	 */
	public static int[] screenSize(Context context) {

		DisplayMetrics displayMetrics = new DisplayMetrics();

		WindowManager windowManager = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
		
		windowManager.getDefaultDisplay().getMetrics(displayMetrics); 
		
		return new int[]{displayMetrics.widthPixels, displayMetrics.heightPixels}; 

	}

}
