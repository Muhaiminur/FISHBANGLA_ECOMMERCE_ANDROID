package com.gtech.fishbangla.MODEL.Config;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.gtech.fishbangla.MODEL.FEEDBACK.Feedback;

import java.util.List;

public class Config_Data {

    @SerializedName("LANGUAGE")
    @Expose
    private String lANGUAGE;
    @SerializedName("FISH_ADDRESS")
    @Expose
    private String fISHADDRESS;
    @SerializedName("SERVICE_CHARGE")
    @Expose
    private String sERVICECHARGE;
    @SerializedName("VAT")
    @Expose
    private String vAT;
    @SerializedName("VERSION_CODE")
    @Expose
    private String vERSIONCODE;
    @SerializedName("MESSAGE_BODY")
    @Expose
    private String mESSAGEBODY;
    @SerializedName("REFERRER")
    @Expose
    private String rEFERRER;
    @SerializedName("MESSAGE_SHOW")
    @Expose
    private String mESSAGESHOW;
    @SerializedName("feedback")
    @Expose
    private List<Object> feedback = null;
    @SerializedName("DELIVERY_CHARGE")
    @Expose
    private String dELIVERYCHARGE;
    @SerializedName("unreadNotification")
    @Expose
    private String unreadNotification;
    @SerializedName("UPDATE_TYPE")
    @Expose
    private String uPDATETYPE;
    @SerializedName("FROZEN")
    @Expose
    private String fROZEN;

    public String getLANGUAGE() {
        return lANGUAGE;
    }

    public void setLANGUAGE(String lANGUAGE) {
        this.lANGUAGE = lANGUAGE;
    }

    public String getFISHADDRESS() {
        return fISHADDRESS;
    }

    public void setFISHADDRESS(String fISHADDRESS) {
        this.fISHADDRESS = fISHADDRESS;
    }

    public String getSERVICECHARGE() {
        return sERVICECHARGE;
    }

    public void setSERVICECHARGE(String sERVICECHARGE) {
        this.sERVICECHARGE = sERVICECHARGE;
    }

    public String getVAT() {
        return vAT;
    }

    public void setVAT(String vAT) {
        this.vAT = vAT;
    }

    public String getVERSIONCODE() {
        return vERSIONCODE;
    }

    public void setVERSIONCODE(String vERSIONCODE) {
        this.vERSIONCODE = vERSIONCODE;
    }

    public String getMESSAGEBODY() {
        return mESSAGEBODY;
    }

    public void setMESSAGEBODY(String mESSAGEBODY) {
        this.mESSAGEBODY = mESSAGEBODY;
    }

    public String getREFERRER() {
        return rEFERRER;
    }

    public void setREFERRER(String rEFERRER) {
        this.rEFERRER = rEFERRER;
    }

    public String getMESSAGESHOW() {
        return mESSAGESHOW;
    }

    public void setMESSAGESHOW(String mESSAGESHOW) {
        this.mESSAGESHOW = mESSAGESHOW;
    }

    public List<Object> getFeedback() {
        return feedback;
    }

    public void setFeedback(List<Object> feedback) {
        this.feedback = feedback;
    }

    public String getDELIVERYCHARGE() {
        return dELIVERYCHARGE;
    }

    public void setDELIVERYCHARGE(String dELIVERYCHARGE) {
        this.dELIVERYCHARGE = dELIVERYCHARGE;
    }

    public String getUnreadNotification() {
        return unreadNotification;
    }

    public void setUnreadNotification(String unreadNotification) {
        this.unreadNotification = unreadNotification;
    }

    public String getUPDATETYPE() {
        return uPDATETYPE;
    }

    public void setUPDATETYPE(String uPDATETYPE) {
        this.uPDATETYPE = uPDATETYPE;
    }

    public String getFROZEN() {
        return fROZEN;
    }

    public void setFROZEN(String fROZEN) {
        this.fROZEN = fROZEN;
    }

    @Override
    public String toString() {
        return "Config_Data{" +
                "lANGUAGE='" + lANGUAGE + '\'' +
                ", fISHADDRESS='" + fISHADDRESS + '\'' +
                ", sERVICECHARGE='" + sERVICECHARGE + '\'' +
                ", vAT='" + vAT + '\'' +
                ", vERSIONCODE='" + vERSIONCODE + '\'' +
                ", mESSAGEBODY='" + mESSAGEBODY + '\'' +
                ", rEFERRER='" + rEFERRER + '\'' +
                ", mESSAGESHOW='" + mESSAGESHOW + '\'' +
                ", feedback=" + feedback +
                ", dELIVERYCHARGE='" + dELIVERYCHARGE + '\'' +
                ", unreadNotification='" + unreadNotification + '\'' +
                ", uPDATETYPE='" + uPDATETYPE + '\'' +
                ", fROZEN='" + fROZEN + '\'' +
                '}';
    }
}

/*{

    @SerializedName("LANGUAGE")
    @Expose
    private String lANGUAGE;
    @SerializedName("feedback")
    @Expose
    private List<Feedback> feedback = null;
    @SerializedName("DELIVERY_CHARGE")
    @Expose
    private String dELIVERYCHARGE;
    @SerializedName("FISH_ADDRESS")
    @Expose
    private String fISHADDRESS;
    @SerializedName("SERVICE_CHARGE")
    @Expose
    private String sERVICECHARGE;
    @SerializedName("UPDATE_TYPE")
    @Expose
    private String uPDATETYPE;
    @SerializedName("VAT")
    @Expose
    private String vAT;
    @SerializedName("VERSION_CODE")
    @Expose
    private String vERSIONCODE;
    @SerializedName("MESSAGE_BODY")
    @Expose
    private String mESSAGEBODY;
    @SerializedName("REFERRER")
    @Expose
    private String rEFERRER;
    @SerializedName("MESSAGE_SHOW")
    @Expose
    private String mESSAGESHOW;
    @SerializedName("FROZEN")
    @Expose
    private String fROZEN;

    public String getLANGUAGE() {
        return lANGUAGE;
    }

    public void setLANGUAGE(String lANGUAGE) {
        this.lANGUAGE = lANGUAGE;
    }

    public List<Feedback> getFeedback() {
        return feedback;
    }

    public void setFeedback(List<Feedback> feedback) {
        this.feedback = feedback;
    }

    public String getDELIVERYCHARGE() {
        return dELIVERYCHARGE;
    }

    public void setDELIVERYCHARGE(String dELIVERYCHARGE) {
        this.dELIVERYCHARGE = dELIVERYCHARGE;
    }

    public String getFISHADDRESS() {
        return fISHADDRESS;
    }

    public void setFISHADDRESS(String fISHADDRESS) {
        this.fISHADDRESS = fISHADDRESS;
    }

    public String getSERVICECHARGE() {
        return sERVICECHARGE;
    }

    public void setSERVICECHARGE(String sERVICECHARGE) {
        this.sERVICECHARGE = sERVICECHARGE;
    }

    public String getUPDATETYPE() {
        return uPDATETYPE;
    }

    public void setUPDATETYPE(String uPDATETYPE) {
        this.uPDATETYPE = uPDATETYPE;
    }

    public String getVAT() {
        return vAT;
    }

    public void setVAT(String vAT) {
        this.vAT = vAT;
    }

    public String getVERSIONCODE() {
        return vERSIONCODE;
    }

    public void setVERSIONCODE(String vERSIONCODE) {
        this.vERSIONCODE = vERSIONCODE;
    }

    public String getMESSAGEBODY() {
        return mESSAGEBODY;
    }

    public void setMESSAGEBODY(String mESSAGEBODY) {
        this.mESSAGEBODY = mESSAGEBODY;
    }

    public String getREFERRER() {
        return rEFERRER;
    }

    public void setREFERRER(String rEFERRER) {
        this.rEFERRER = rEFERRER;
    }

    public String getMESSAGESHOW() {
        return mESSAGESHOW;
    }

    public void setMESSAGESHOW(String mESSAGESHOW) {
        this.mESSAGESHOW = mESSAGESHOW;
    }

    public String getFROZEN() {
        return fROZEN;
    }

    public void setFROZEN(String fROZEN) {
        this.fROZEN = fROZEN;
    }


    @Override
    public String toString() {
        return "Config_Data{" +
                "lANGUAGE='" + lANGUAGE + '\'' +
                ", feedback=" + feedback +
                ", dELIVERYCHARGE='" + dELIVERYCHARGE + '\'' +
                ", fISHADDRESS='" + fISHADDRESS + '\'' +
                ", sERVICECHARGE='" + sERVICECHARGE + '\'' +
                ", uPDATETYPE='" + uPDATETYPE + '\'' +
                ", vAT='" + vAT + '\'' +
                ", vERSIONCODE='" + vERSIONCODE + '\'' +
                ", mESSAGEBODY='" + mESSAGEBODY + '\'' +
                ", rEFERRER='" + rEFERRER + '\'' +
                ", mESSAGESHOW='" + mESSAGESHOW + '\'' +
                ", fROZEN='" + fROZEN + '\'' +
                '}';
    }
}*/