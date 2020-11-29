package com.gtech.fishbangla.MODEL.TAG_PRODUCT;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class SEND_TAGID {

    @SerializedName("tagId")
    @Expose
    private String tagId;


    public SEND_TAGID(String tagId) {
        this.tagId = tagId;
    }

    public String getTagId() {
        return tagId;
    }

    public void setTagId(String tagId) {
        this.tagId = tagId;
    }

    @Override
    public String toString() {
        return "SEND_TAGID{" +
                "tagId='" + tagId + '\'' +
                '}';
    }
}