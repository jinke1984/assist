package cn.com.jinke.assist.database;

import org.xutils.DbManager;

import java.util.ArrayList;
import java.util.List;

/**
 * Author: lufengwen
 * Date: 2016-03-30 17:32
 * Description: 用户数据表，存储于用户相关的数据
 */
public class UserDb extends BaseDb {

	//private TableMessage mTableMessage;

	public UserDb(String dbName, int dbVersion, DbManager.DbUpgradeListener listener) {
		super(dbName, dbVersion, listener);
	}

	@Override
	protected List<BaseTable> getTables() {
		List<BaseTable> list = new ArrayList<>();

//		mTableMessage = new TableMessage(getDbUtils());
//		list.add(mTableMessage);

		return list;
	}

//	public TableMessage getTableMessage() {
//		return mTableMessage;
//	}

	@Override
	protected void clearCache() {

	}
}
