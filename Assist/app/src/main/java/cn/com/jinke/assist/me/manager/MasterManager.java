package cn.com.jinke.assist.me.manager;

import cn.com.jinke.assist.database.DbManager;
import cn.com.jinke.assist.me.model.UserCard;

/**
 * Created by jinke on 2017/1/19.
 */

public class MasterManager {

    private UserCard mUserCard;

    private static MasterManager instance;

    private MasterManager(){

    }

    public static MasterManager getInstance(){
        if (instance == null) {
            synchronized (LoginManager.class) {
                if (instance == null) {
                    instance = new MasterManager();
                }
            }
        }
        return instance;
    }

    public UserCard getUserCard() {
        return mUserCard;
    }

    public void setUserCard(UserCard mUserCard) {
        this.mUserCard = mUserCard;
    }

    public void init(UserCard aUserCard){
        if(aUserCard != null){
            setUserCard(aUserCard);
            DbManager.getCommonDb().getTableUserCard().saveUserCard(aUserCard);
        }
    }

    public void init(){
        UserCard userCard = DbManager.getCommonDb().getTableUserCard().getUserCard();
        if(userCard != null){
            setUserCard(userCard);
        }
    }

    /**
     * 退出的方法
     */
    public void logout(){
        if(getUserCard() != null){
            setUserCard(null);
            DbManager.getCommonDb().getTableUserCard().clearData();
        }
    }
}
