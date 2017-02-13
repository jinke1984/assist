package cn.com.jinke.assist.function.model;

import com.google.gson.annotations.SerializedName;

/**
 * Created by jinke on 2017/2/13.
 */

public class Zcfg {

    @SerializedName("infoid")
    private int infoid;

    @SerializedName("infoname")
    private String infoname;

    @SerializedName("author")
    private String author;

    @SerializedName("source")
    private String source;

    @SerializedName("body")
    private String body;

    @SerializedName("pubtime")
    private String pubtime;

    public int getInfoid() {
        return infoid;
    }

    public void setInfoid(int infoid) {
        this.infoid = infoid;
    }

    public String getInfoname() {
        return infoname;
    }

    public void setInfoname(String infoname) {
        this.infoname = infoname;
    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public String getSource() {
        return source;
    }

    public void setSource(String source) {
        this.source = source;
    }

    public String getBody() {
        return body;
    }

    public void setBody(String body) {
        this.body = body;
    }

    public String getPubtime() {
        return pubtime;
    }

    public void setPubtime(String pubtime) {
        this.pubtime = pubtime;
    }
}
