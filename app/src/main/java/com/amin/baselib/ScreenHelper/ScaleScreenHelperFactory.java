package com.amin.baselib.ScreenHelper;

import android.content.Context;


/**
 *
 * 屏幕适配构造器
 *
 * @author zcx
 *
 */
public final class ScaleScreenHelperFactory {
	
	private static ScaleScreenHelper i;

	private ScaleScreenHelperFactory() {}

	/**
	 * 获取实例
	 *
	 * @return
	 */
	public static ScaleScreenHelper getInstance() {
		
		return i;
		
	}
 
	public static void create(Context a, int s, float c) {
		
		i = new ScaleScreenHelperFactory_Base(a, s, c);
		
	}

	public static void create(Context a, int s) {

		i = new ScaleScreenHelperFactory_Base(a, s);

	}

}