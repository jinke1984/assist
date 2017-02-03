package cn.com.jinke.assist.manager.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

/**
 * Created by jinke on 2016/12/15.
 */

public class VersionInfo implements Serializable {

    @Expose
    @SerializedName("id")
    private String id;

    @Expose
    @SerializedName("createUserId")
    private String createUserId;

    @Expose
    @SerializedName("createUserName")
    private String createUserName;

    @Expose
    @SerializedName("createTime")
    private String createTime;

    @Expose
    @SerializedName("updateUserId")
    private String updateUserId;

    @Expose
    @SerializedName("updateUserName")
    private String updateUserName;

    @Expose
    @SerializedName("updateTime")
    private String updateTime;

    @Expose
    @SerializedName("optLock")
    private int optLock;

    @Expose
    @SerializedName("dataState")
    private int dataState;

    @Expose
    @SerializedName("appName")
    private String appName;

    @Expose
    @SerializedName("version")
    private String version;

    @Expose
    @SerializedName("memo")
    private String memo;

    @Expose
    @SerializedName("path")
    private String path;

    @Expose
    @SerializedName("fileName")
    private String fileName;

    @Expose
    @SerializedName("downloadPath")
    private String downloadPath;

    private long packageSize;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getCreateUserId() {
        return createUserId;
    }

    public void setCreateUserId(String createUserId) {
        this.createUserId = createUserId;
    }

    public String getCreateUserName() {
        return createUserName;
    }

    public void setCreateUserName(String createUserName) {
        this.createUserName = createUserName;
    }

    public String getCreateTime() {
        return createTime;
    }

    public void setCreateTime(String createTime) {
        this.createTime = createTime;
    }

    public String getUpdateUserId() {
        return updateUserId;
    }

    public void setUpdateUserId(String updateUserId) {
        this.updateUserId = updateUserId;
    }

    public String getUpdateUserName() {
        return updateUserName;
    }

    public void setUpdateUserName(String updateUserName) {
        this.updateUserName = updateUserName;
    }

    public String getUpdateTime() {
        return updateTime;
    }

    public void setUpdateTime(String updateTime) {
        this.updateTime = updateTime;
    }

    public int getOptLock() {
        return optLock;
    }

    public void setOptLock(int optLock) {
        this.optLock = optLock;
    }

    public int getDataState() {
        return dataState;
    }

    public void setDataState(int dataState) {
        this.dataState = dataState;
    }

    public String getAppName() {
        return appName;
    }

    public void setAppName(String appName) {
        this.appName = appName;
    }

    public String getVersion() {
        return version;
    }

    public void setVersion(String version) {
        this.version = version;
    }

    public String getMemo() {
        return memo;
    }

    public void setMemo(String memo) {
        this.memo = memo;
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public String getFileName() {
        return fileName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    public String getDownloadPath() {
        return downloadPath;
    }

    public void setDownloadPath(String downloadPath) {
        this.downloadPath = downloadPath;
    }

    public long getPackageSize() {
        return packageSize;
    }

    public void setPackageSize(long packageSize) {
        this.packageSize = packageSize;
    }
}
