package com.example.lostandfound;

import android.view.View;

import com.google.firebase.database.DatabaseReference;

public class PostFeed {

    private String userId, itemDesc, cInfo, value, Status, Res_name, pid    ;

//    public PostFeed() {
//        this.userId = userId;
//        this.itemDesc = itemDesc;
//        this.value = value;
//        this.cInfo = cInfo;
////        String userId, String itemDesc, String value, String cInfo
//
//    }

    public PostFeed() {
    }

    public String getUserId(){ return userId;}
    public void setUserId(String userId){ this.userId = userId;}
    public String getItemDesc() {


        return itemDesc;
    }

    public void setItemDesc(String itemDesc) {
        this.itemDesc = itemDesc;
    }


    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }


    public String getcInfo() {
        return cInfo;
    }

    public void setcInfo(String cInfo) {
        this.cInfo = cInfo;
    }

    public String getStatus() {return Status; }

    public void setStatus(String Status) {this.Status = Status;}


    public String getRes_name() {return Res_name; }

    public void setRes_name(String Res_name) {this.Res_name = Res_name;}

    public String getpid() {return pid; }

    public void setpid(String pid) {this.pid = pid;}



}
