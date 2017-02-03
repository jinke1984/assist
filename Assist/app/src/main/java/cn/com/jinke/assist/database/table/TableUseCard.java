package cn.com.jinke.assist.database.table;

import org.xutils.DbManager;
import org.xutils.ex.DbException;

import java.util.List;
import java.util.concurrent.Callable;

import cn.com.jinke.assist.database.BaseTable;
import cn.com.jinke.assist.me.model.UserCard;

/**
 * Created by apple on 2017/1/19.
 */

public class TableUseCard extends BaseTable {

    public TableUseCard(DbManager dbUtils){
        super(dbUtils);
    }

    public void saveUserCard(final UserCard data) {
        submit(new Runnable() {
            @Override
            public void run() {
                try {
                    mDbUtils.saveOrUpdate(data);
                }
                catch (DbException e) {
                    e.printStackTrace();
                }
            }
        });
    }

    public UserCard getUserCard(final String userId) {
        return submit(new Callable<UserCard>() {
            @Override
            public UserCard call() throws Exception {
                UserCard result = mDbUtils.findById(UserCard.class, userId);
                return result;
            }
        });
    }

    public UserCard getUserCard(){
        return submit(new Callable<UserCard>() {
            @Override
            public UserCard call() throws Exception {
                List<UserCard> result = mDbUtils.findAll(UserCard.class);
                return result.get(0);
            }
        });
    }

    public void clearData() {
        submit(new Runnable() {
            @Override
            public void run() {
                try {
                    mDbUtils.delete(UserCard.class);
                }
                catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
    }
}
