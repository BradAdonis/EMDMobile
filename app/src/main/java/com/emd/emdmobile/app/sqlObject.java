package com.emd.emdmobile.app;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by Bradl_000 on 2014-07-30.
 */
public class sqlObject {
    private long id;
    private String unid;
    private Date moddate;
    private String type;
    private String keywords;
    private String json;

    public long getID(){
        return id;
    }

    public void setID(long id){
        this.id = id;
    }

    public String getUnid(){
        return unid;
    }

    public void setUnid(String unid){
        this.unid = unid;
    }

    public String getModDate(){
        if(moddate != null) {
            DateFormat df = new SimpleDateFormat("yyyy-MM-dd");
            String date = df.format(moddate);
            return date;
        }
        else
        {
            return "1900-01-01";
        }
    }

    public void setModDate(Date moddate){
        this.moddate = moddate;
    }

    public String getType(){
        return type;
    }

    public void setType(String type){
        this.type = type;
    }

    public String getKeywords(){
        return keywords;
    }

    public void setKeywords(String keywords){
        this.keywords = keywords;
    }

    public String getJson(){
        return json;
    }

    public void setJson(String json){
        this.json = json;
    }
}
