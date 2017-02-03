package cn.com.jinke.assist.database;

import org.xutils.DbManager;

import java.util.ArrayList;
import java.util.List;

import cn.com.jinke.assist.database.table.TableUseCard;

/**
 * @Author: jinke
 * @Date: 2016年3月30日 下午18:03:25
 * @Description: 公共数据库，存储与用户无关的数据
 */
public class CommonDb extends BaseDb {
	private TableUseCard tableUseCard;

	public CommonDb(String dbName, int dbVersion, DbManager.DbUpgradeListener listener) {
		super(dbName, dbVersion, listener);
	}

	@Override
	protected List<BaseTable> getTables() {
		List<BaseTable> list = new ArrayList<>();

		tableUseCard = new TableUseCard(getDbUtils());
		list.add(tableUseCard);

		return list;
	}

	public TableUseCard getTableUserCard() {
		return tableUseCard;
	}

	@Override
	protected void clearCache() {

	}

}
