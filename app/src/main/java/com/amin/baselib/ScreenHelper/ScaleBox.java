package com.amin.baselib.ScreenHelper;


import android.view.View;
import android.widget.ImageView;

class ScaleBox {
 
	void onScale(int[] size, View view, ScaleScreenHelperFactory_Base factory_base) {

			//宽高字体大小设置
			if (size[0] != 0 || size[1] != 0 || size[2] != 0) {
				
				try {
					
					if (size[2] != 0) {

						if(size[0] == 0 && size[1] == 0){

							factory_base.loadViewSize(view, size[2]);

						}else {

							factory_base.loadViewWidthHeightSize(view, size[0], size[1],size[2]);

						}

					} else {

						factory_base.loadViewWidthHeight(view, size[0], size[1]);
						
					}
					
				} catch (Exception e) {}
				
			}

		if (size[3] != 0 || size[4] != 0 || size[5] != 0 || size[6] != 0) {

			try {

				factory_base.loadViewMargin(view, size[3], size[4], size[5], size[6]);

			} catch (Exception e) {}

		}
			
			if (size[7] != 0 || size[8] != 0 || size[9] != 0 || size[10] != 0) {
				
				try {

					factory_base.loadViewPadding(view, size[7], size[8], size[9], size[10]);
				
				} catch (Exception e) {}
				
			}
			
			if (size[11] != 0 || size[12] != 0 || size[13] != 0 || size[14] != 0) {
				
				try {

					factory_base.loadViewMinMax(view,size[11],size[12],size[13],size[14]);
				
				} catch (Exception e) {}
				
			}
			
			if (view instanceof ImageView) {

				view.setTag(null);
				
			}
			

		
	}
	
	private int getInt(String string) {
		
		return Integer.parseInt(string);
		
	}

	private String[] getValues(String tag, String start, String end){

		if(tag.contains(start) && tag.contains(end)){

			return tag.substring(tag.indexOf(start) + 1, tag.indexOf(end)).split(",");

		}

		return null;

	}
	
}