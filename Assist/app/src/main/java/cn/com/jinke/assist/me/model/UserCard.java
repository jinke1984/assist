package cn.com.jinke.assist.me.model;

import com.google.gson.annotations.SerializedName;

import org.xutils.db.annotation.Column;
import org.xutils.db.annotation.Table;

import java.io.Serializable;

import cn.com.jinke.assist.utils.JsonConstans;

/**
 * Created by jinke on 2017/1/18.
 */

@Table(name = "t_usercard")
public class UserCard implements Serializable, JsonConstans{

    @SerializedName(USERID)
    @Column(name = "user_id", isId = true, autoGen = false)
    private String userid;

    @SerializedName(USERNAME)
    @Column(name = "username")
    private String username;

    @SerializedName(COMMUNITYID)
    @Column(name = "communityid")
    private int communityid;

    @SerializedName(COMMUNITYNAME)
    @Column(name = "communityname")
    private String communityname;

    @SerializedName(ROLE)
    @Column(name = "role")
    private String role;

    public String getUserid() {
        return userid;
    }

    public void setUserid(String userid) {
        this.userid = userid;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public int getCommunityid() {
        return communityid;
    }

    public void setCommunityid(int communityid) {
        this.communityid = communityid;
    }

    public String getCommunityname() {
        return communityname;
    }

    public void setCommunityname(String communityname) {
        this.communityname = communityname;
    }

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }
}
