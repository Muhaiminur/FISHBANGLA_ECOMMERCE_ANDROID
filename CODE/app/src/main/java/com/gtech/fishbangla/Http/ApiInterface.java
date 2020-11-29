package com.gtech.fishbangla.Http;

import com.gtech.fishbangla.MODEL.ADDRESS.SEND_ADDADRESS;
import com.gtech.fishbangla.MODEL.ADDRESS.SEND_ADDRESSID;
import com.gtech.fishbangla.MODEL.ADDRESS.SEND_UPDATEADDRESS;
import com.gtech.fishbangla.MODEL.API_RESPONSE;
import com.gtech.fishbangla.MODEL.AUCTION.SEND_AUCTION_PRICE;
import com.gtech.fishbangla.MODEL.CREATE_PRODUCT.Send_CategoryID;
import com.gtech.fishbangla.MODEL.DISCOUNT.SEND_DISCOUNT;
import com.gtech.fishbangla.MODEL.FEEDBACK.SEND_FEEDBACK;
import com.gtech.fishbangla.MODEL.FOLLOW.SEND_FOLLOW;
import com.gtech.fishbangla.MODEL.ISSUE.SEND_ISSUE;
import com.gtech.fishbangla.MODEL.NOTIFICATION.SEND_NOTIFICATION;
import com.gtech.fishbangla.MODEL.ORDER.SEND_ORDER;
import com.gtech.fishbangla.MODEL.PRODUCT.SEND_FILTER_PRODUCT;
import com.gtech.fishbangla.MODEL.PRODUCT.SEND_PRODUCT_STATUS;
import com.gtech.fishbangla.MODEL.PRODUCT.SEND_SEARCH;
import com.gtech.fishbangla.MODEL.PRODUCT.SEND_UPDATE_PRODUCT;
import com.gtech.fishbangla.MODEL.PRODUCT.Send_Product_Category;
import com.gtech.fishbangla.MODEL.PRODUCT.Send_Product_Details;
import com.gtech.fishbangla.MODEL.PRODUCT.Send_Product_New;
import com.gtech.fishbangla.MODEL.PRODUCT.Send_Product_Suggestion;
import com.gtech.fishbangla.MODEL.SEND_FVRT;
import com.gtech.fishbangla.MODEL.Send_UserID;
import com.gtech.fishbangla.MODEL.TAG_PRODUCT.SEND_TAGID;
import com.gtech.fishbangla.MODEL.User.Send_Mbl;
import com.gtech.fishbangla.MODEL.User.Send_Pin;
import com.gtech.fishbangla.MODEL.User.Send_Pin_Change;
import com.gtech.fishbangla.MODEL.User.Send_Reference;
import com.gtech.fishbangla.MODEL.User.Send_User_Update;

import java.util.List;

import okhttp3.MultipartBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.Headers;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;
import retrofit2.http.Path;
import retrofit2.http.Query;

public interface ApiInterface {
    //1 APP CONFIG
    @POST("fbmobile/getConfigs")
    Call<API_RESPONSE> FishBAngla_Config(@Header("Authorization") String apiKey, @Header("usersId") String usersId, @Header("token") String token);

    //2 GET BANNER LIST
    @POST("fbmobile/banner/all")
    Call<API_RESPONSE> FishBAngla_Banner_List(@Header("Authorization") String apiKey, @Header("usersId") String usersId, @Header("token") String token);

    //3 get category
    @POST("fbmobile/appMenu/all")
    Call<API_RESPONSE> FishBAngla_Category_List(@Header("Authorization") String apiKey, @Header("usersId") String usersId, @Header("token") String token);

    //4 get all Product
    @POST("fbmobile/product/all")
    Call<API_RESPONSE> FishBAngla_All_Product(@Header("Authorization") String apiKey, @Header("usersId") String usersId, @Header("token") String token, @Query("page") String page);

    //5 product Details
    @POST("fbmobile/product")
    Call<API_RESPONSE> FishBAngla_Product_Details(@Header("Authorization") String apiKey, @Header("usersId") String usersId, @Header("token") String token, @Body Send_Product_Details sendProductDetails);

    //6 product Suggestion
    @POST("fbmobile/product/suggestion")
    Call<API_RESPONSE> FishBAngla_Product_Suggestion(@Header("Authorization") String apiKey, @Header("usersId") String usersId, @Header("token") String token, @Body Send_Product_Suggestion sendProductSuggestion);

    //7 get Address Database
    @POST("fbmobile/divlist")
    Call<API_RESPONSE> FishBAngla_Address_Database(@Header("Authorization") String apiKey, @Header("usersId") String usersId, @Header("token") String token);

    //8 USER LOGIN
    @POST("fbmobile/pin/create")
    Call<API_RESPONSE> FishBAngla_Login(@Header("Authorization") String apiKey, @Header("usersId") String usersId, @Header("token") String token, @Body Send_Mbl send_mbl);

    //9 USER LOGIN verify
    @POST("fbmobile/login")
    Call<API_RESPONSE> FishBAngla_Login_Verify(@Header("Authorization") String apiKey, @Header("usersId") String usersId, @Header("token") String token, @Body Send_Pin send_pin);

    //10 USER LOGIN FORGOT
    @POST("fbmobile/pin/forgot")
    Call<API_RESPONSE> FishBAngla_Login_Forgot(@Header("Authorization") String apiKey, @Header("usersId") String usersId, @Header("token") String token, @Body Send_Mbl send_mbl);


    //11 User Update
    @POST("fbmobile/user/update")
    Call<API_RESPONSE> FishBAngla_Login_update(@Header("Authorization") String apiKey, @Header("usersId") String usersId, @Header("token") String token, @Body Send_User_Update send_user_update);

    //12 User DETAILS
    @POST("fbmobile/userProfile")
    Call<API_RESPONSE> FishBAngla_User_Details(@Header("Authorization") String apiKey, @Header("usersId") String usersId, @Header("token") String token, @Body Send_UserID send_userID);


    //13 User Pin Change
    @POST("fbmobile/pin/change")
    Call<API_RESPONSE> FishBAngla_Pin_Change(@Header("Authorization") String apiKey, @Header("usersId") String usersId, @Header("token") String token, @Body Send_Pin_Change send_pin_change);


    //14 get Seller List
    @POST("fbmobile/seller/all")
    Call<API_RESPONSE> FishBAngla_Seller_List(@Header("Authorization") String apiKey, @Header("usersId") String usersId, @Header("token") String token);

    //15 product Category
    @POST("fbmobile/product/list/appMenuPostType")
    Call<API_RESPONSE> FishBAngla_Category(@Header("Authorization") String apiKey, @Header("usersId") String usersId, @Header("token") String token, @Body Send_Product_Category sendProductCategory);


    //16 user product list
    @POST("fbmobile/product/all/seller")
    Call<API_RESPONSE> FishBAngla_User_Product_List(@Header("Authorization") String apiKey, @Header("usersId") String usersId, @Header("token") String token, @Body Send_UserID send_userID);

    //17 product name list
    @POST("fbmobile/productMenu/all")
    Call<API_RESPONSE> FishBAngla_Product_Name(@Header("Authorization") String apiKey, @Header("usersId") String usersId, @Header("token") String token);

    //18 Upload New Product
    @POST("fbmobile/product/create")
    Call<API_RESPONSE> FishBAngla_Upload_Product(@Header("Authorization") String apiKey, @Header("usersId") String usersId, @Header("token") String token, @Body Send_Product_New send_product_new);


    //19 get category
    @POST("fbmobile/appMenu/isCatg")
    Call<API_RESPONSE> FishBAngla_Category_List_upload(@Header("Authorization") String apiKey, @Header("usersId") String usersId, @Header("token") String token);

    //20 product name list from category
    @POST("fbmobile/productMenu/appMenuWise/all")
    Call<API_RESPONSE> FishBAngla_Product_name_category(@Header("Authorization") String apiKey, @Header("usersId") String usersId, @Header("token") String token, @Body Send_CategoryID sendProductCategory);

    //21 Reference Check
    @POST("fbmobile/agent/refer")
    Call<API_RESPONSE> FishBAngla_Reference_Check(@Header("Authorization") String apiKey, @Header("usersId") String usersId, @Header("token") String token, @Body Send_Reference sendReference);

    //22 Product Image Upload
    @Multipart
    @POST("fbmobile/product/imageUpload/{productid}")
    Call<API_RESPONSE> FishBAngla_Product_Image_Upload(@Header("Authorization") String apiKey, @Header("usersId") String usersId, @Header("token") String token, @Path("productid") String productid, @Part List<MultipartBody.Part> files);


    //23 Product Video Upload
    @Multipart
    @POST("fbmobile/product/videoUpload/{productid}")
    Call<API_RESPONSE> FishBAngla_Product_Video_Upload(@Header("Authorization") String apiKey, @Header("usersId") String usersId, @Header("token") String token, @Path("productid") String productid, @Part MultipartBody.Part files);

    //24 Create Order
    @POST("fbmobile/order/create")
    Call<API_RESPONSE> FishBAngla_Create_Order(@Header("Authorization") String apiKey, @Header("usersId") String usersId, @Header("token") String token, @Body SEND_ORDER send_order);

    //25 Create Order
    @POST("fbmobile/discount")
    Call<API_RESPONSE> FishBAngla_discount_check(@Header("Authorization") String apiKey, @Header("usersId") String usersId, @Header("token") String token, @Body SEND_DISCOUNT send_discount);

    //26 Buy History
    @POST("fbmobile/order/history/buy")
    Call<API_RESPONSE> FishBAngla_buy_History(@Header("Authorization") String apiKey, @Header("usersId") String usersId, @Header("token") String token, @Body Send_UserID send_userID);

    //27 Sell History
    @POST("fbmobile/order/history/sell")
    Call<API_RESPONSE> FishBAngla_sell_History(@Header("Authorization") String apiKey, @Header("usersId") String usersId, @Header("token") String token, @Body Send_UserID send_userID);

    //28 Sell History
    @POST("fbmobile/product/activeInactive/all/seller")
    Call<API_RESPONSE> FishBAngla_Own_Product(@Header("Authorization") String apiKey, @Header("usersId") String usersId, @Header("token") String token, @Body Send_UserID send_userID);

    //29 USER Image Upload
    @Multipart
    @POST("fbmobile/userImageUpload/{userid}")
    Call<API_RESPONSE> FishBAngla_User_Image_Upload(@Header("Authorization") String apiKey, @Header("usersId") String usersId, @Header("token") String token, @Path("userid") String productid, @Part MultipartBody.Part files);

    //30 Product Status Change
    @POST("fbmobile/product/tokenUpdate")
    Call<API_RESPONSE> FishBAngla_Product_status_change(@Header("Authorization") String apiKey, @Header("usersId") String usersId, @Header("token") String token, @Body SEND_PRODUCT_STATUS send_product_status);

    //31 Get Issue List
    @POST("fbmobile/userIssue/all")
    Call<API_RESPONSE> FishBAngla_issue_list(@Header("Authorization") String apiKey, @Header("usersId") String usersId, @Header("token") String token);

    //32 SEND ISSUE
    @POST("fbmobile/userRequest/create")
    Call<API_RESPONSE> FishBAngla_Send_Issue(@Header("Authorization") String apiKey, @Header("usersId") String usersId, @Header("token") String token, @Body SEND_ISSUE send_issue);

    //33 search Product
    @POST("fbmobile/product/all/filter/productMenu")
    Call<API_RESPONSE> FishBAngla_Search_Product(@Header("Authorization") String apiKey, @Header("usersId") String usersId, @Header("token") String token, @Body SEND_SEARCH send_search);

    //34 Add Address
    @POST("fbmobile/address/create")
    Call<API_RESPONSE> FishBAngla_add_address(@Header("Authorization") String apiKey, @Header("usersId") String usersId, @Header("token") String token, @Body SEND_ADDADRESS send_addadress);

    //35 Address LIST
    @POST("fbmobile/address/all/filter/user")
    Call<API_RESPONSE> FishBAngla_list_address(@Header("Authorization") String apiKey, @Header("usersId") String usersId, @Header("token") String token, @Body Send_UserID send_userID);

    //36 Address delete
    @POST("fbmobile/address/delete")
    Call<API_RESPONSE> FishBAngla_delete_address(@Header("Authorization") String apiKey, @Header("usersId") String usersId, @Header("token") String token, @Body SEND_ADDRESSID send_addadress);

    //37 Address Update
    @POST("fbmobile/address/update")
    Call<API_RESPONSE> FishBAngla_update_address(@Header("Authorization") String apiKey, @Header("usersId") String usersId, @Header("token") String token, @Body SEND_UPDATEADDRESS send_addadress);

    //38 Product Cancel
    @POST("fbmobile/product/update")
    Call<API_RESPONSE> FishBAngla_Product_Edit(@Header("Authorization") String apiKey, @Header("usersId") String usersId, @Header("token") String token, @Body SEND_UPDATE_PRODUCT send_update_product);

    //39 Product filter
    @POST("fbmobile/product/filter/throughAll")
    Call<API_RESPONSE> FishBAngla_Product_Filter(@Header("Authorization") String apiKey, @Header("usersId") String usersId, @Header("token") String token, @Body SEND_FILTER_PRODUCT send_filter_product);

    //40 Product FEEDBACk
    @POST("fbmobile/rating/create")
    Call<API_RESPONSE> FishBAngla_Product_Feedback(@Header("Authorization") String apiKey, @Header("usersId") String usersId, @Header("token") String token, @Body List<SEND_FEEDBACK> send_filter_product);

    //41 Notification List
    @POST("fbmobile/profile/notification/all")
    Call<API_RESPONSE> FishBAngla_Notification_list(@Header("Authorization") String apiKey, @Header("usersId") String usersId, @Header("token") String token, @Query("page") String page);

    //42 AUCTION List
    @POST("fbmobile/auction/product/all")
    Call<API_RESPONSE> FishBAngla_Auction_list(@Header("Authorization") String apiKey, @Header("usersId") String usersId, @Header("token") String token);

    //43 AUCTION DETAILS
    @POST("fbmobile/auction/product")
    Call<API_RESPONSE> FishBAngla_Auction_details(@Header("Authorization") String apiKey, @Header("usersId") String usersId, @Header("token") String token, @Body Send_Product_Details send_product_details);

    //44 AUCTION SUBMIT
    @POST("fbmobile/create/bid")
    Call<API_RESPONSE> FishBAngla_Auction_Submit(@Header("Authorization") String apiKey, @Header("usersId") String usersId, @Header("token") String token, @Body SEND_AUCTION_PRICE auction_price);

    //45 USER FOLLOW
    @POST("fbmobile/follow")
    Call<API_RESPONSE> FishBAngla_User_Follow(@Header("Authorization") String apiKey, @Header("usersId") String usersId, @Header("token") String token, @Body SEND_FOLLOW send_follow);

    //46 USER UNFOLLOW
    @POST("fbmobile/unfollow")
    Call<API_RESPONSE> FishBAngla_User_Unfollow(@Header("Authorization") String apiKey, @Header("usersId") String usersId, @Header("token") String token, @Body SEND_FOLLOW send_follow);

    //47 FILTER SELLER
    @POST("fbmobile/seller/filter")
    Call<API_RESPONSE> FishBAngla_Filter_seller(@Header("Authorization") String apiKey, @Header("usersId") String usersId, @Header("token") String token, @Body SEND_SEARCH send_search);

    //48 GET FVRT LIST
    @POST("fbmobile/favourite/all")
    Call<API_RESPONSE> FishBAngla_Fvrt_list(@Header("Authorization") String apiKey, @Header("usersId") String usersId, @Header("token") String token);

    //49 FVRT LIST ADD
    @POST("fbmobile/favourite/add")
    Call<API_RESPONSE> FishBAngla_Fvrt_Add(@Header("Authorization") String apiKey, @Header("usersId") String usersId, @Header("token") String token, @Body SEND_FVRT send_fvrt);

    //50 Notification add
    @POST("fbmobile/profile/notification/update")
    Call<API_RESPONSE> FishBAngla_Notification_update(@Header("Authorization") String apiKey, @Header("usersId") String usersId, @Header("token") String token, @Body SEND_NOTIFICATION send_notification);

    //51 GET TAG LIST
    @POST("fbmobile/product/tagWise")
    Call<API_RESPONSE> FishBAngla_Tag_List(@Header("Authorization") String apiKey, @Header("usersId") String usersId, @Header("token") String token);

    //52 GET TAG LIST MORE
    @POST("fbmobile/product/tag")
    Call<API_RESPONSE> FishBAngla_Tag_List_more(@Header("Authorization") String apiKey, @Header("usersId") String usersId, @Header("token") String token, @Body SEND_TAGID sendTagid, @Query("page") String page);
}
