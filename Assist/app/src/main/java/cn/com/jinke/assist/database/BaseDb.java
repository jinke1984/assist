package cn.com.jinke.assist.database;

import org.xutils.DbManager;
import org.xutils.x;

import java.util.List;

/**
 * @Author: lufengwen
 * @Date: 2016年3月30日 下午17:49:25
 * @Description: 数据库基类
 */
public abstract class BaseDb {
	private DbManager mDbUtils;
	private List<BaseTable> mTableList;

	public BaseDb(String dbName, int dbVersion) {
		this(dbName, dbVersion, null);
	}

	public BaseDb(String dbName, int dbVersion, DbManager.DbUpgradeListener listener) {
		DbManager.DaoConfig config = new DbManager.DaoConfig();
		config.setDbVersion(dbVersion);
		config.setDbName(dbName);
		config.setDbUpgradeListener(listener);
		mDbUtils = x.getDb(config);
		createTables();
	}
	
	public DbManager getDbUtils() {
		return mDbUtils;
	}

	protected abstract List<BaseTable> getTables();
	
	protected abstract void clearCache();

	protected void createTables() {
		mTableList = getTables();
		if (mTableList != null && !mTableList.isEmpty()) {
			for (BaseTable table : mTableList) {
				table.createTable();
			}
		}
	}
}
