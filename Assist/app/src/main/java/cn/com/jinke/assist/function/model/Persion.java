package cn.com.jinke.assist.function.model;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

/**
 * Created by jinke on 2017/3/13.
 */

public class Persion implements Serializable {

    @SerializedName("entityid")
    private int entityid;

    @SerializedName("name")
    private String name;

    @SerializedName("gender")
    private String gender;

    @SerializedName("idcard")
    private String idcard;

    @SerializedName("birthday")
    private String birthday;

    @SerializedName("address")
    private String address;

    @SerializedName("homephone")
    private String homephone;

    public int getEntityid() {
        return entityid;
    }

    public void setEntityid(int entityid) {
        this.entityid = entityid;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public String getIdcard() {
        return idcard;
    }

    public void setIdcard(String idcard) {
        this.idcard = idcard;
    }

    public String getBirthday() {
        return birthday;
    }

    public void setBirthday(String birthday) {
        this.birthday = birthday;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getPhone() {
        return homephone;
    }

    public void setPhone(String phone) {
        this.homephone = phone;
    }
}
