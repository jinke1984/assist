package cn.com.jinke.assist.function.model;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

/**
 * Created by apple on 2017/1/20.
 */

public class Gzfw implements Serializable{

    @SerializedName("entityid")
    private int entityid;

    @SerializedName("personalprofileid")
    private int personalprofileid;

    @SerializedName("jobcircs")
    private String jobcircs;

    @SerializedName("phoneno")
    private String phoneno;

    @SerializedName("srvcircs")
    private String srvcircs;

    @SerializedName("helpcircs")
    private String helpcircs;

    @SerializedName("staff")
    private String staff;

    @SerializedName("examinetime")
    private String examinetime;

    @SerializedName("userid")
    private int userid;

    @SerializedName("username")
    private String username;

    @SerializedName("lastupdate")
    private String lastupdate;

    public int getEntityid() {
        return entityid;
    }

    public void setEntityid(int entityid) {
        this.entityid = entityid;
    }

    public String getJobcircs() {
        return jobcircs;
    }

    public void setJobcircs(String jobcircs) {
        this.jobcircs = jobcircs;
    }

    public String getPhoneno() {
        return phoneno;
    }

    public void setPhoneno(String phoneno) {
        this.phoneno = phoneno;
    }

    public String getSrvcircs() {
        return srvcircs;
    }

    public void setSrvcircs(String srvcircs) {
        this.srvcircs = srvcircs;
    }

    public String getHelpcircs() {
        return helpcircs;
    }

    public void setHelpcircs(String helpcircs) {
        this.helpcircs = helpcircs;
    }

    public String getStaff() {
        return staff;
    }

    public void setStaff(String staff) {
        this.staff = staff;
    }

    public String getExaminetime() {
        return examinetime;
    }

    public void setExaminetime(String examinetime) {
        this.examinetime = examinetime;
    }

    public int getUserid() {
        return userid;
    }

    public void setUserid(int userid) {
        this.userid = userid;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getLastupdate() {
        return lastupdate;
    }

    public void setLastupdate(String lastupdate) {
        this.lastupdate = lastupdate;
    }

    public int getPersonalprofileid() {
        return personalprofileid;
    }

    public void setPersonalprofileid(int personalprofileid) {
        this.personalprofileid = personalprofileid;
    }
}
