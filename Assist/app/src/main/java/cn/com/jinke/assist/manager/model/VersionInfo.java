package cn.com.jinke.assist.manager.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

/**
 * Created by jinke on 2016/12/15.
 */

public class VersionInfo implements Serializable {

    @Expose
    @SerializedName("version")
    private String version;

    @Expose
    @SerializedName("filepath")
    private String filepath;

    @Expose
    @SerializedName("filesize")
    private String filesize;

    @Expose
    @SerializedName("updatetime")
    private String updatetime;

    @Expose
    @SerializedName("updatecontent")
    private String updatecontent;

    public String getVersion() {
        return version;
    }

    public void setVersion(String version) {
        this.version = version;
    }

    public String getFilepath() {
        return filepath;
    }

    public void setFilepath(String filepath) {
        this.filepath = filepath;
    }

    public String getFilesize() {
        return filesize;
    }

    public void setFilesize(String filesize) {
        this.filesize = filesize;
    }

    public String getUpdatetime() {
        return updatetime;
    }

    public void setUpdatetime(String updatetime) {
        this.updatetime = updatetime;
    }

    public String getUpdatecontent() {
        return updatecontent;
    }

    public void setUpdatecontent(String updatecontent) {
        this.updatecontent = updatecontent;
    }
}
