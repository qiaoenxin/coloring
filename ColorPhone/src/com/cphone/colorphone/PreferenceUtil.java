package com.cphone.colorphone;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;

public class PreferenceUtil {
	
	public static final  String PERFER_KEY_COLOR = "PERFER_KEY_COLOR";
	public static final  String PERFER_KEY_BOX = "PERFER_KEY_BOX";
	public static final  String PERFER_KEY_COLOR_ADV = "PERFER_KEY_COLOR_ADV";
	public static final  String PERFER_KEY_TOP_X_Y = "PERFER_KEY_TOP_X_Y";
	
	
	public static String getStringFromSharedPreferences(Context ct, String key) {
		String str = "";
		SharedPreferences sp = ct.getSharedPreferences("color_phone", Context.MODE_PRIVATE);
		str = sp.getString(key, "").toString();
		return str;
	}
	

	public static void putStringFromSharedPreferences(Context ct, String key, String value) {
		SharedPreferences sp = ct.getSharedPreferences("color_phone", Context.MODE_PRIVATE);
		Editor ed = sp.edit();
		ed.putString(key, value);
		ed.commit();
	}
}
