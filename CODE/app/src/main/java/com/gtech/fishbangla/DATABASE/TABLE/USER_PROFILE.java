package com.gtech.fishbangla.DATABASE.TABLE;


import androidx.annotation.NonNull;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "user_profile")
public class USER_PROFILE {
    @PrimaryKey
    @NonNull
    @ColumnInfo(name = "userid")
    String userId;

    @NonNull
    @ColumnInfo(name = "username")
    String userFullName;

    @NonNull
    @ColumnInfo(name = "userphone")
    String userPhoneNo;

    @NonNull
    @ColumnInfo(name = "userimage")
    String userImage;

    @NonNull
    @ColumnInfo(name = "useremail")
    String userEmail;

    @NonNull
    @ColumnInfo(name = "userratting")
    String userRatting;

    public USER_PROFILE() {
    }

    @NonNull
    public String getUserId() {
        return userId;
    }

    public void setUserId(@NonNull String userId) {
        this.userId = userId;
    }

    @NonNull
    public String getUserFullName() {
        return userFullName;
    }

    public void setUserFullName(@NonNull String userFullName) {
        this.userFullName = userFullName;
    }

    @NonNull
    public String getUserPhoneNo() {
        return userPhoneNo;
    }

    public void setUserPhoneNo(@NonNull String userPhoneNo) {
        this.userPhoneNo = userPhoneNo;
    }

    @NonNull
    public String getUserImage() {
        return userImage;
    }

    public void setUserImage(@NonNull String userImage) {
        this.userImage = userImage;
    }

    @NonNull
    public String getUserEmail() {
        return userEmail;
    }

    public void setUserEmail(@NonNull String userEmail) {
        this.userEmail = userEmail;
    }

    @NonNull
    public String getUserRatting() {
        return userRatting;
    }

    public void setUserRatting(@NonNull String userRatting) {
        this.userRatting = userRatting;
    }

    @Override
    public String toString() {
        return "USER_PROFILE{" +
                "userId='" + userId + '\'' +
                ", userFullName='" + userFullName + '\'' +
                ", userPhoneNo='" + userPhoneNo + '\'' +
                ", userImage='" + userImage + '\'' +
                ", userEmail='" + userEmail + '\'' +
                ", userRatting='" + userRatting + '\'' +
                '}';
    }
}
