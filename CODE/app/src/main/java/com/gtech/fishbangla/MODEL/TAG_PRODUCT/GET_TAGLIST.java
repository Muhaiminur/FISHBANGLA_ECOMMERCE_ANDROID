package com.gtech.fishbangla.MODEL.TAG_PRODUCT;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.gtech.fishbangla.MODEL.PRODUCT.Get_Product;

import java.util.List;

public class GET_TAGLIST {

    @SerializedName("tagData")
    @Expose
    private List<Get_Product> tagData = null;
    @SerializedName("tagId")
    @Expose
    private Integer tagId;
    @SerializedName("tagName")
    @Expose
    private String tagName;

    public List<Get_Product> getTagData() {
        return tagData;
    }

    public void setTagData(List<Get_Product> tagData) {
        this.tagData = tagData;
    }

    public Integer getTagId() {
        return tagId;
    }

    public void setTagId(Integer tagId) {
        this.tagId = tagId;
    }

    public String getTagName() {
        return tagName;
    }

    public void setTagName(String tagName) {
        this.tagName = tagName;
    }

}