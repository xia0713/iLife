package com.szgs.bbs.common.util;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;

/**
 * @Description SharedPrance工具类
 * @author db
 * 
 */
@SuppressWarnings("static-access")
public class SharedPranceUtils {
	/**
	 * 使用SharedPreferences保存String类型
	 */
	public static void SaveStringDate(Context context, String str, String key) {
		SharedPreferences preferences = context.getSharedPreferences("Lggs",
				context.MODE_PRIVATE);
		Editor editor = preferences.edit();
		editor.putString(key, str);
		editor.commit();
	}

	/**
	 * 删除数据
	 */
	public static void DeleteDate(Context context, String key) {
		SharedPreferences preferences = context.getSharedPreferences("Lggs",
				context.MODE_PRIVATE);
		Editor editor = preferences.edit();
		editor.remove(key);
		editor.commit();
	}

	/**
	 * 取String类型数据
	 * 
	 * @param key
	 * @return String
	 */
	public static String GetStringDate(String key, Context context) {
		SharedPreferences preferences = context.getSharedPreferences("Lggs",
				context.MODE_PRIVATE);
		String str = preferences.getString(key, "");
		return str;
		// }
	}

	/**
	 * 保存boolean值
	 */
	public static void SaveBolDate(Context context, Boolean str, String key) {
		SharedPreferences preferences = context.getSharedPreferences("Lggs",
				context.MODE_PRIVATE);
		Editor editor = preferences.edit();
		editor.putBoolean(key, str);
		editor.commit();
	}

	/**
	 * 取boolean值
	 * 
	 * @param key
	 * @return
	 */
	public static Boolean GetBolDate(String key, Context context, Boolean deflut) {
		SharedPreferences preferences = context.getSharedPreferences("Lggs",
				context.MODE_PRIVATE);
		boolean str = preferences.getBoolean(key, deflut);
		return str;
		// }

	}

	/**
	 * 存int型
	 */
	public static void SaveIntDate(Context context, int str, String key) {
		SharedPreferences preferences = context.getSharedPreferences("Lggs",
				context.MODE_PRIVATE);
		Editor editor = preferences.edit();
		editor.putInt(key, str);
		editor.commit();
	}

	/**
	 * 取int数据
	 * 
	 * @param key
	 * @return
	 */
	public static int GetIntDate(String key, Context context) {
		SharedPreferences preferences = context.getSharedPreferences("Lggs",
				context.MODE_PRIVATE);
		int str = preferences.getInt(key, 404);
		return str;
	}
}
