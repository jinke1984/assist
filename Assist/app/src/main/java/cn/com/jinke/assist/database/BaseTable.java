package cn.com.jinke.assist.database;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.text.TextUtils;

import org.xutils.DbManager;

import java.util.concurrent.Callable;
import java.util.concurrent.Future;

import cn.com.jinke.assist.thread.Dispatcher;
import cn.com.jinke.assist.thread.SingleThreadPool;


/**
 * @Author: lufengwen
 * @Date: 2016年3月30日 下午18:03:25
 * @Description: 数据表基类
 */
public abstract class BaseTable {

	protected final String TAG = getClassName();
	private String mClassName;
	protected DbManager mDbUtils;
	protected SQLiteDatabase mSQLiteDatabase;

	public BaseTable(DbManager dbUtils) {
		this.mDbUtils = dbUtils;
		this.mSQLiteDatabase = mDbUtils.getDatabase();
	}

	protected String getClassName() {
		if (TextUtils.isEmpty(mClassName)) {
			mClassName = getClass().getSimpleName();
		}
		return mClassName;
	}

	protected void setSQLiteDatabase(SQLiteDatabase db) {
		mSQLiteDatabase = db;
	}

	public Cursor rawQuery(String sql) {
		return mSQLiteDatabase.rawQuery(sql, null);
	}

	protected <T> T submit(Callable<T> callable) {
		try {
			Future<T> futrue = SingleThreadPool.getThreadPool().submit(callable);
			return futrue.get();
		}
		catch (Exception e) {
			//log(e.toString());
			e.printStackTrace();
		}
		return null;
	}

	protected boolean submit(Runnable runnable) {
		boolean result = false;
		try {
			Dispatcher.runOnSingleThread(runnable);
			result = true;
		}
		catch (Exception e) {
			e.printStackTrace();
			//log(e.toString());
		}
		return result;
	}

	protected void createTable() {

	}

//	public void log(String msg) {
//		AppLogger.d(TAG, msg, false);
//	}
}
