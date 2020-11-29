package com.gtech.fishbangla.Fragment;


import android.app.Dialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;

import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.gtech.fishbangla.Adapter.HomeSuggestAdapter;
import com.gtech.fishbangla.Http.ApiClient;
import com.gtech.fishbangla.Http.ApiInterface;
import com.gtech.fishbangla.Library.KeyWord;
import com.gtech.fishbangla.Library.Utility;
import com.gtech.fishbangla.MODEL.API_RESPONSE;
import com.gtech.fishbangla.MODEL.FOLLOW.SEND_FOLLOW;
import com.gtech.fishbangla.MODEL.PRODUCT.Get_Product;
import com.gtech.fishbangla.MODEL.Send_UserID;
import com.gtech.fishbangla.MODEL.User.Get_UserLogin;
import com.gtech.fishbangla.R;
import com.gtech.fishbangla.databinding.FragmentSellerDetailsBinding;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * A simple {@link Fragment} subclass.
 */
public class SellerDetails extends Fragment {


    View view;
    Context context;
    Utility utility;
    List<Get_Product> products;
    HomeSuggestAdapter sellerSuggestAdapter;

    //
    FragmentSellerDetailsBinding sellerDetailsBinding;
    ApiInterface apiInterface = ApiClient.getBaseClient().create(ApiInterface.class);
    String seller_id;

    Get_UserLogin userProfile;

    public SellerDetails() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        sellerDetailsBinding = DataBindingUtil.inflate(inflater, R.layout.fragment_seller_details, container, false);
        try {
            context = getActivity();
            utility = new Utility(this.context);
            if (getArguments() != null) {
                seller_id = getArguments().getString("seller_id");
                if (!TextUtils.isEmpty(seller_id)) {
                    User_Details(new Send_UserID(seller_id));
                }
            }
            sellerDetailsBinding.sellerDetailsFollow.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    try {
                        if (userProfile != null && !utility.getUSER_ID().equalsIgnoreCase(KeyWord.USERID)) {

                            if (userProfile.getBeingFollowed().equalsIgnoreCase(KeyWord.NO)) {
                                USER_FOLOW(new SEND_FOLLOW(userProfile.getUserId(), utility.getUSER_ID()));
                            } else if (userProfile.getBeingFollowed().equalsIgnoreCase(KeyWord.YES)) {
                                USER_UNFOLOW(new SEND_FOLLOW(userProfile.getUserId(), utility.getUSER_ID()));
                            }
                        } else {
                            utility.showDialog(context.getResources().getString(R.string.login_string));
                        }
                    } catch (Exception e) {
                        Log.d("Error Line Number", Log.getStackTraceString(e));
                    }
                }
            });
            initial_ratail();
        } catch (Exception e) {
            Log.d("Error Line Number", Log.getStackTraceString(e));
        }
        return sellerDetailsBinding.getRoot();
    }

    public void initial_ratail() {
        products = new ArrayList<>();
        sellerSuggestAdapter = new HomeSuggestAdapter(products, context);
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false);
        sellerDetailsBinding.sellerDetailsRecycler.setLayoutManager(mLayoutManager);
        sellerDetailsBinding.sellerDetailsRecycler.setItemAnimator(new DefaultItemAnimator());
        sellerDetailsBinding.sellerDetailsRecycler.setAdapter(sellerSuggestAdapter);
    }


    private void User_Details(Send_UserID send_userID) {
        try {
            if (utility.isNetworkAvailable()) {
                Log.d("User Details1", send_userID.toString());
                utility.showProgress(false, getResources().getString(R.string.wait_string));
                Call<API_RESPONSE> call = apiInterface.FishBAngla_User_Details(utility.getApiKey(), utility.getUSER_ID(), utility.getFirebaseToken(), send_userID);
                call.enqueue(new Callback<API_RESPONSE>() {
                    @Override
                    public void onResponse(Call<API_RESPONSE> call, Response<API_RESPONSE> response) {
                        try {
                            //utility.hideProgress();
                            Log.d("User Details2", response.toString());
                            if (response.isSuccessful() && response != null) {
                                API_RESPONSE api_response = response.body();
                                if (api_response.getCode() == 200) {
                                    Gson gson = new Gson();
                                    userProfile = gson.fromJson(api_response.getData().toString(), Get_UserLogin.class);
                                    //Log.d("User Details", userProfile.getUserPhoneNo());
                                    if (userProfile != null && !TextUtils.isEmpty(userProfile.getUserId())) {
                                        if (!TextUtils.isEmpty(userProfile.getUserImage())) {
                                            Glide.with(context).load(userProfile.getUserImage()).apply(utility.Glide_Cache_Off()).into(sellerDetailsBinding.sellerImage);
                                        }
                                        if (!TextUtils.isEmpty(userProfile.getUserFullName())) {
                                            sellerDetailsBinding.sellerDetailsName.setText(userProfile.getUserFullName());
                                        }
                                        if (!TextUtils.isEmpty(userProfile.getUserPhoneNo())) {
                                            sellerDetailsBinding.sellerDetailsMbl.setText(utility.Mobile_Number_Read(utility.convertToBangle(userProfile.getUserPhoneNo())));
                                        }
                                        if (!TextUtils.isEmpty(userProfile.getUserEmail())) {
                                            sellerDetailsBinding.sellerDetailsEmail.setText(userProfile.getUserEmail());
                                        }
                                        if (!TextUtils.isEmpty(userProfile.getAvgRating())) {
                                            sellerDetailsBinding.sellerDetailsRating.setRating(Float.valueOf(userProfile.getAvgRating()));
                                        }
                                        if (userProfile.getBeingFollowed() != null && !TextUtils.isEmpty(userProfile.getBeingFollowed())) {
                                            sellerDetailsBinding.sellerDetailsFollow.setVisibility(View.VISIBLE);
                                            if (userProfile.getBeingFollowed().equalsIgnoreCase(KeyWord.NO)) {
                                                sellerDetailsBinding.sellerDetailsFollow.setTextColor(context.getResources().getColor(R.color.app_white));
                                                sellerDetailsBinding.sellerDetailsFollow.setBackground(context.getResources().getDrawable(R.drawable.rectangular_red));
                                                sellerDetailsBinding.sellerDetailsFollow.setText(context.getResources().getString(R.string.seller_follow_string));
                                            } else if (userProfile.getBeingFollowed().equalsIgnoreCase(KeyWord.YES)) {
                                                sellerDetailsBinding.sellerDetailsFollow.setTextColor(context.getResources().getColor(R.color.colorDark));
                                                sellerDetailsBinding.sellerDetailsFollow.setBackground(context.getResources().getDrawable(R.drawable.rectangular_black_border_white));
                                                sellerDetailsBinding.sellerDetailsFollow.setText(context.getResources().getString(R.string.seller_unfollow_string));
                                            }
                                            if (userProfile.getUserId().equalsIgnoreCase(utility.getUSER_ID())) {
                                                sellerDetailsBinding.sellerDetailsFollow.setVisibility(View.GONE);
                                            }
                                        }
                                    }
                                    Get_Product_List(send_userID);
                                } else {
                                    utility.showDialog(getResources().getString(R.string.try_again_string));
                                    utility.hideProgress();
                                }
                            } else {
                                utility.showDialog(getResources().getString(R.string.try_again_string));
                                utility.hideProgress();
                            }
                        } catch (Exception ex) {
                            utility.logger(ex.toString());
                            utility.hideProgress();
                        }
                    }

                    @Override
                    public void onFailure(Call<API_RESPONSE> call, Throwable t) {
                        utility.logger(t.toString());
                        utility.showToast(getResources().getString(R.string.try_again_string));
                        utility.hideProgress();
                    }
                });
            } else {
                utility.showDialog(getResources().getString(R.string.no_internet_string));
            }
        } catch (Exception e) {
            Log.d("Error Line Number", Log.getStackTraceString(e));
        }
    }


    private void Get_Product_List(Send_UserID s) {
        try {
            if (utility.isNetworkAvailable()) {
                Log.d("SELLER PRODUCT LIST1", s.toString());
                //utility.showProgress(false, getResources().getString(R.string.wait_string));
                Call<API_RESPONSE> call = apiInterface.FishBAngla_User_Product_List(utility.getApiKey(), utility.getUSER_ID(), utility.getFirebaseToken(), s);
                call.enqueue(new Callback<API_RESPONSE>() {
                    @Override
                    public void onResponse(Call<API_RESPONSE> call, Response<API_RESPONSE> response) {
                        try {
                            utility.hideProgress();
                            Log.d("SELLER PRODUCT LIST2", response.toString());
                            if (response.isSuccessful() && response != null) {
                                API_RESPONSE api_response = response.body();
                                if (api_response.getCode() == 200) {
                                    Gson gson = new Gson();
                                    Type listType = new TypeToken<List<Get_Product>>() {
                                    }.getType();
                                    List<Get_Product> get_products = gson.fromJson(response.body().getData(), listType);
                                    Log.d("SELLERPRODUCT LIST Size", get_products.size() + "");
                                    if (get_products.size() > 0) {
                                        products.clear();
                                        products.addAll(get_products);
                                        sellerSuggestAdapter.notifyDataSetChanged();
                                    } else {
                                        utility.showDialog(context.getResources().getString(R.string.product_no_data_string));
                                    }
                                } else {
                                    utility.showDialog(getResources().getString(R.string.try_again_string));
                                }
                            } else {
                                utility.showDialog(getResources().getString(R.string.try_again_string));
                            }
                        } catch (Exception ex) {
                            utility.logger(ex.toString());
                            utility.hideProgress();
                        }
                    }

                    @Override
                    public void onFailure(Call<API_RESPONSE> call, Throwable t) {
                        utility.logger(t.toString());
                        utility.showToast(getResources().getString(R.string.try_again_string));
                        utility.hideProgress();
                    }
                });
            } else {
                utility.showDialog(getResources().getString(R.string.no_internet_string));
            }
        } catch (Exception e) {
            Log.d("Error Line Number", Log.getStackTraceString(e));
        }
    }

    private void USER_FOLOW(SEND_FOLLOW send_follow) {
        try {
            if (utility.isNetworkAvailable()) {
                Log.d("User follow", send_follow.toString());
                utility.showProgress(false, getResources().getString(R.string.wait_string));
                Call<API_RESPONSE> call = apiInterface.FishBAngla_User_Follow(utility.getApiKey(), utility.getUSER_ID(), utility.getFirebaseToken(), send_follow);
                call.enqueue(new Callback<API_RESPONSE>() {
                    @Override
                    public void onResponse(Call<API_RESPONSE> call, Response<API_RESPONSE> response) {
                        try {
                            utility.hideProgress();
                            Log.d("User follow", response.toString());
                            if (response.isSuccessful() && response != null) {
                                API_RESPONSE api_response = response.body();
                                if (api_response.getCode() == 200) {
                                    Dioalog_Yes(context.getResources().getString(R.string.seller_follow_success_string));
                                    /*sellerDetailsBinding.sellerDetailsFollow.setTextColor(context.getResources().getColor(R.color.colorDark));
                                    sellerDetailsBinding.sellerDetailsFollow.setBackground(context.getResources().getDrawable(R.drawable.rectangular_black_border_white));
                                    sellerDetailsBinding.sellerDetailsFollow.setText(context.getResources().getString(R.string.seller_unfollow_string));
                                    */
                                } else {
                                    utility.showDialog(getResources().getString(R.string.try_again_string));
                                }
                            } else {
                                utility.showDialog(getResources().getString(R.string.try_again_string));
                            }
                        } catch (Exception ex) {
                            utility.logger(ex.toString());
                        }
                    }

                    @Override
                    public void onFailure(Call<API_RESPONSE> call, Throwable t) {
                        utility.logger(t.toString());
                        utility.showToast(getResources().getString(R.string.try_again_string));
                        utility.hideProgress();
                    }
                });
            } else {
                utility.showDialog(getResources().getString(R.string.no_internet_string));
            }
        } catch (Exception e) {
            Log.d("Error Line Number", Log.getStackTraceString(e));
        }
    }

    private void USER_UNFOLOW(SEND_FOLLOW send_follow) {
        try {
            if (utility.isNetworkAvailable()) {
                Log.d("User follow", send_follow.toString());
                utility.showProgress(false, getResources().getString(R.string.wait_string));
                Call<API_RESPONSE> call = apiInterface.FishBAngla_User_Unfollow(utility.getApiKey(), utility.getUSER_ID(), utility.getFirebaseToken(), send_follow);
                call.enqueue(new Callback<API_RESPONSE>() {
                    @Override
                    public void onResponse(Call<API_RESPONSE> call, Response<API_RESPONSE> response) {
                        try {
                            utility.hideProgress();
                            Log.d("User follow", response.toString());
                            if (response.isSuccessful() && response != null) {
                                API_RESPONSE api_response = response.body();
                                if (api_response.getCode() == 200) {
                                    Dioalog_Yes(context.getResources().getString(R.string.seller_unfollow_success_string));
                                    /*sellerDetailsBinding.sellerDetailsFollow.setTextColor(context.getResources().getColor(R.color.app_white));
                                    sellerDetailsBinding.sellerDetailsFollow.setBackground(context.getResources().getDrawable(R.drawable.rectangular_red));
                                    sellerDetailsBinding.sellerDetailsFollow.setText(context.getResources().getString(R.string.seller_follow_string));*/
                                } else {
                                    utility.showDialog(getResources().getString(R.string.try_again_string));
                                }
                            } else {
                                utility.showDialog(getResources().getString(R.string.try_again_string));
                            }
                        } catch (Exception ex) {
                            utility.logger(ex.toString());
                        }
                    }

                    @Override
                    public void onFailure(Call<API_RESPONSE> call, Throwable t) {
                        utility.logger(t.toString());
                        utility.showToast(getResources().getString(R.string.try_again_string));
                        utility.hideProgress();
                    }
                });
            } else {
                utility.showDialog(getResources().getString(R.string.no_internet_string));
            }
        } catch (Exception e) {
            Log.d("Error Line Number", Log.getStackTraceString(e));
        }
    }


    public void Dioalog_Yes(String msg) {
        try {
            HashMap<String, Integer> screen = utility.getScreenRes();
            int width = screen.get(KeyWord.SCREEN_WIDTH);
            int height = screen.get(KeyWord.SCREEN_HEIGHT);
            int mywidth = (width / 10) * 7;
            final Dialog dialog = new Dialog(context);
            dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
            dialog.getWindow().setBackgroundDrawableResource(android.R.color.transparent);
            dialog.setContentView(R.layout.dialog_toast_yes_no);
            TextView tv = dialog.findViewById(R.id.tv_message);
            Button yes = dialog.findViewById(R.id.btn_yes);
            Button no = dialog.findViewById(R.id.btn_no);
            no.setVisibility(View.GONE);
            tv.setText(msg);
            LinearLayout ll = dialog.findViewById(R.id.dialog_layout_size);
            LinearLayout.LayoutParams params = (LinearLayout.LayoutParams) ll.getLayoutParams();
            params.height = LinearLayout.LayoutParams.WRAP_CONTENT;
            params.width = mywidth;
            ll.setLayoutParams(params);
            yes.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    dialog.dismiss();
                    if (!TextUtils.isEmpty(seller_id)) {
                        User_Details(new Send_UserID(seller_id));
                    }
                }
            });
            no.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    dialog.dismiss();
                }
            });
            dialog.setCancelable(false);
            dialog.show();
        } catch (Exception e) {
            Log.d("Error Line Number", Log.getStackTraceString(e));
        }
    }

    //from Cart activity
    @Override
    public void onResume() {
        try {
            context.registerReceiver(mReceiverLocation, new IntentFilter("cart_delete"));
        } catch (Exception e) {
            Log.d("Error Line Number", Log.getStackTraceString(e));
        }
        super.onResume();
    }

    @Override
    public void onDestroy() {
        try {
            context.unregisterReceiver(mReceiverLocation);
        } catch (Exception e) {
            Log.d("Error Line Number", Log.getStackTraceString(e));
        }
        super.onDestroy();
    }

    private BroadcastReceiver mReceiverLocation = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            try {
                if (sellerSuggestAdapter != null) {
                    sellerSuggestAdapter.notifyDataSetChanged();
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    };

}
