package cn.com.jinke.assist.share;

import android.app.Activity;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;

import cn.com.jinke.assist.booter.ProjectApplication;


/**
 * @author zhaojian
 * @time 下午5:25:55
 * @date 2014-10-28
 * @class_name ShareUtils.java
 */
public class ShareUtils {

	private static ShareUtils instance;
	static final String FAILED = "{\"result\":\"-1\"}";
	static final String SUCESS = "{\"result\":\"0\"}";

	public enum ETYPE {
		INT(Integer.class), LONG(Long.class), FLOAT(Float.class), BOOL(
				Boolean.class), STRING(String.class);
		Class<?> clazz;

		ETYPE(Class<?> c) {
			clazz = c;
		}

		public Class<?> value() {
			return clazz;
		}
	}

	public enum ESHARE {
		SYS("share_sys");
		
		private String value;

		ESHARE(String v) {
			this.value = v;
		}

		public String Consturstor() {
			return this.value;
		}
	}

	public static ShareUtils getInstance() {
		if (instance == null) {
			synchronized (ShareUtils.class) {
				if (instance == null) {
					instance = new ShareUtils();
				}
			}
		}
		return instance;
	}

	public String remove(ESHARE share, String key) {
		SharedPreferences sharedPreferences = ProjectApplication.getContext().getSharedPreferences(share.Consturstor(),
						Activity.MODE_PRIVATE);
		Editor editor = sharedPreferences.edit();
		editor.remove(key);
		editor.commit();
		return SUCESS;
	}

	/**
	 * 如果value = null，则默认为删除该字段
	 * 
	 * @param share
	 * @param key
	 * @param value
	 * @return
	 */
	public String commit(ESHARE share, String key, Object value) {

		if (value == null) {
			return remove(share, key);
		}

		SharedPreferences sharedPreferences = ProjectApplication.getContext().getSharedPreferences(share.Consturstor(),
						Activity.MODE_PRIVATE);
		Editor editor = sharedPreferences.edit();
		if (value instanceof Boolean) {
			editor.putBoolean(key, (Boolean) value);
		} else if (value instanceof Integer) {
			editor.putInt(key, (Integer) value);
		} else if (value instanceof Float) {
			editor.putFloat(key, (Float) value);
		} else if (value instanceof Long) {
			editor.putLong(key, (Long) value);
		} else if (value instanceof String) {
			editor.putString(key, (String) value);
		}
		editor.commit();
		return SUCESS;
	}

	@SuppressWarnings("unchecked")
	public <T> T get(ESHARE share, String key, ETYPE clazz) {

		SharedPreferences sharedPreferences = ProjectApplication.getContext().getSharedPreferences(share.Consturstor(),
						Activity.MODE_PRIVATE);
		if (clazz.value() == Boolean.class) {
			return (T) clazz.value().cast(
					sharedPreferences.getBoolean(key, true));
		} else if (clazz.value() == Integer.class) {
			return (T) clazz.value().cast(sharedPreferences.getInt(key, 0));
		} else if (clazz.value() == Long.class) {
			return (T) clazz.value().cast(sharedPreferences.getLong(key, 0));
		} else if (clazz.value() == Float.class) {
			return (T) clazz.value().cast(sharedPreferences.getFloat(key, 0));
		} else if (clazz.value() == String.class) {
			return (T) sharedPreferences.getString(key, "");
		} else {
			return null;
		}
	}
}
