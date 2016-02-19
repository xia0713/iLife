package com.szgs.bbs.common.util;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;

/**
 * 保存 用户信息
 * 
 * @author db
 * 
 */
@SuppressWarnings("static-access")
public class CacheUtils {
	public static final String USERID = "UserId";
	public static final String ISREMEMBERPWD = "isrempwd";
	public static final String USERNAME = "username";
	public static final String PASSWORD = "password";
	public static final String USERMSG = "lggsuser";
	public static final String TELPHONE = "telphone";
	public static final String AVATAR = "avatar";
	
	public static final String AUTHORIZATION = "author";

	/**
	 * 保存用户ID
	 * 
	 * @param context
	 * @param userId
	 */
	public static void saveUserID(Context context, long userId) {
		SharedPreferences preferences = context.getSharedPreferences(USERMSG,
				context.MODE_PRIVATE);
		Editor editor = preferences.edit();
		editor.putLong(USERID, userId);
		editor.commit();
	}

	/**
	 * 获取用户ID
	 * 
	 * @param context
	 * @return
	 */
	public static long getUserId(Context context) {
		SharedPreferences preferences = context.getSharedPreferences(USERMSG,
				context.MODE_PRIVATE);
		long str = preferences.getLong(USERID, -1);
		return str;
	}

	/**
	 * 保存是否保存密码
	 * 
	 * @param context
	 * @param b
	 */
	public static void saveIsRememberPwd(Context context, boolean b) {
		SharedPreferences preferences = context.getSharedPreferences(USERMSG,
				context.MODE_PRIVATE);
		Editor editor = preferences.edit();
		editor.putBoolean(ISREMEMBERPWD, b);
		editor.commit();
	}

	/**
	 * 获取是否保存密码
	 * 
	 * @param context
	 * @return
	 */
	public static boolean getIsRememberPwd(Context context) {
		SharedPreferences preferences = context.getSharedPreferences(USERMSG,
				context.MODE_PRIVATE);
		boolean b = preferences.getBoolean(ISREMEMBERPWD, false);
		return b;
	}

	/**
	 * 保存电话号码
	 * 
	 * @param context
	 * @param username
	 */
	public static void saveTelphone(Context context, String phone) {
		SharedPreferences preferences = context.getSharedPreferences(USERMSG,
				context.MODE_PRIVATE);
		Editor editor = preferences.edit();
		editor.putString(TELPHONE, phone);
		editor.commit();
	}

	/**
	 * 获取电话号码
	 * 
	 * @param context
	 * @return
	 */
	public static String getTelphone(Context context) {
		SharedPreferences preferences = context.getSharedPreferences(USERMSG,
				context.MODE_PRIVATE);
		String str = preferences.getString(TELPHONE, "");
		return str;
	}

	/**
	 * 保存用户名
	 * 
	 * @param context
	 * @param username
	 */
	public static void saveUserName(Context context, String username) {
		SharedPreferences preferences = context.getSharedPreferences(USERMSG,
				context.MODE_PRIVATE);
		Editor editor = preferences.edit();
		editor.putString(USERNAME, username);
		editor.commit();
	}

	/**
	 * 保存用户密码
	 * 
	 * @param context
	 * @param password
	 */
	public static void savePassWord(Context context, String password) {
		SharedPreferences preferences = context.getSharedPreferences(USERMSG,
				context.MODE_PRIVATE);
		Editor editor = preferences.edit();
		editor.putString(PASSWORD, password);
		editor.commit();
	}

	/**
	 * 获取用户密码
	 * 
	 * @param context
	 * @return
	 */
	public static String getPassWord(Context context) {
		SharedPreferences preferences = context.getSharedPreferences(USERMSG,
				context.MODE_PRIVATE);
		String str = preferences.getString(PASSWORD, "");
		return str;
	}

	/**
	 * 获取用户名
	 * 
	 * @param context
	 * @return
	 */
	public static String getUserName(Context context) {
		SharedPreferences preferences = context.getSharedPreferences(USERMSG,
				context.MODE_PRIVATE);
		String str = preferences.getString(USERNAME, "");
		return str;
	}

	/**
	 * 清楚用户数据 保存userid 因为考虑到必须大多数数接口必须传userid
	 * 
	 * @param context
	 */
	public static void cleanUserMsg(Context context) {
		SharedPreferences preferences = context.getSharedPreferences(USERMSG,
				context.MODE_PRIVATE);
		Editor editor = preferences.edit();
		long userId = preferences.getLong(USERID, 404);
		editor.clear();
//		editor.putLong(USERID, userId);
		editor.commit();
	}

	/**
	 * 保存头像地址
	 * 
	 * @param context
	 * @param username
	 */
	public static void saveAvatar(Context context, String avatar) {
		SharedPreferences preferences = context.getSharedPreferences(USERMSG,
				context.MODE_PRIVATE);
		Editor editor = preferences.edit();
		editor.putString(AVATAR, avatar);
		editor.commit();
	}

	/**
	 * 获取头像地址
	 * 
	 * @param context
	 * @return
	 */
	public static String getAvatar(Context context) {
		SharedPreferences preferences = context.getSharedPreferences(USERMSG,
				context.MODE_PRIVATE);
		String str = preferences.getString(AVATAR, "");
		return str;
	}
	/**
	 * 保存鉴权令牌
	 * 
	 * @param context
	 * @param username
	 */
	public static void saveAuthor(Context context, String author) {
		SharedPreferences preferences = context.getSharedPreferences(USERMSG,
				context.MODE_PRIVATE);
		Editor editor = preferences.edit();
		editor.putString(AUTHORIZATION, author);
		editor.commit();
	}

	/**
	 * 获取电话号码
	 * 
	 * @param context
	 * @return
	 */
	public static  String getAuthor(Context context) {
		SharedPreferences preferences = context.getSharedPreferences(USERMSG,
				context.MODE_PRIVATE);
		String str = preferences.getString(AUTHORIZATION, "");
		return str;
	}
}
