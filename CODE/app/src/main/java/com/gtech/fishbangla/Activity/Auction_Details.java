package com.gtech.fishbangla.Activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Point;
import android.net.Uri;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Handler;
import android.text.TextUtils;
import android.util.Log;
import android.view.Display;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.LinearLayout;

import com.google.android.material.textfield.TextInputEditText;
import com.google.gson.Gson;
import com.gtech.fishbangla.Adapter.ProductDetailsAdapter;
import com.gtech.fishbangla.Http.ApiClient;
import com.gtech.fishbangla.Http.ApiInterface;
import com.gtech.fishbangla.Library.KeyWord;
import com.gtech.fishbangla.Library.Utility;
import com.gtech.fishbangla.MODEL.API_RESPONSE;
import com.gtech.fishbangla.MODEL.AUCTION.GET_AUTION_DETAILS;
import com.gtech.fishbangla.MODEL.AUCTION.SEND_AUCTION_PRICE;
import com.gtech.fishbangla.MODEL.Banner.Product_Details_Banner;
import com.gtech.fishbangla.MODEL.PRODUCT.Product_Image;
import com.gtech.fishbangla.MODEL.PRODUCT.Send_Product_Details;
import com.gtech.fishbangla.R;
import com.gtech.fishbangla.databinding.ActivityAuctionDetailsBinding;
import com.gtech.fishbangla.databinding.ActivityAuctionListBinding;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class Auction_Details extends AppCompatActivity {

    Utility utility;
    Context context;
    ApiInterface apiInterface = ApiClient.getBaseClient().create(ApiInterface.class);
    ActivityAuctionDetailsBinding activityAuctionDetailsBinding;

    List<Product_Details_Banner> detailsBanners = new ArrayList<>();

    String auction_id = "";
    GET_AUTION_DETAILS productDetails;
    int count = 0;
    CountDownTimer countDownTimer;
    Handler handler = new Handler();
    Runnable runnable = new Runnable() {
        @Override
        public void run() {
            if (!TextUtils.isEmpty(auction_id)) {
                GET_AUCTION_TOP_AMOUNT(new Send_Product_Details(auction_id));
            }
            handler.postDelayed(runnable, 5000);
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_auction__details);
        getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle(R.string.auction_string);
        try {
            context = Auction_Details.this;
            activityAuctionDetailsBinding = DataBindingUtil.setContentView(this, R.layout.activity_auction__details);
            utility = new Utility(context);
            auction_id = getIntent().getStringExtra("AUCTION_DETAILS");
            utility.logger(auction_id);
            if (!TextUtils.isEmpty(auction_id)) {
                GET_AUCTION_DETAILS(new Send_Product_Details(auction_id));
            }
            if (productDetails != null) {
                //final long minutes = Long.parseLong(productDetails.getExpireAt()) - System.currentTimeMillis();
                final long minutes = Long.parseLong(productDetails.getExpireAt());
                countDownTimer = new CountDownTimer(minutes, 1000) {

                    @Override
                    public void onTick(long l) {
                        String value = utility.humanReadableDateTime(l);
                        activityAuctionDetailsBinding.auctionCountdown.setText(utility.convertToBangle(value));
                    }

                    @Override
                    public void onFinish() {
                        activityAuctionDetailsBinding.auctionCountdown.setText(utility.convertToBangle("00:00:00"));
                        finish();
                    }
                };
                countDownTimer.start();
            }
            activityAuctionDetailsBinding.auctionFishSubmit.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    AUCTION_PRICE_ASK();
                }
            });

            /*activityAuctionDetailsBinding.auctionSellerDetails.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    try{
                        if (productDetails!=null){
                            Intent intent = new Intent(context, Home_Page.class);
                            intent.putExtra("seller_details", productDetails.get);
                            context.startActivity(intent);
                        }
                    }catch(Exception e){
                      Log.d("Error Line Number",Log.getStackTraceString(e));
                    }
                }
            });*/
        } catch (Exception e) {
            Log.d("Error Line Number", Log.getStackTraceString(e));
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        onBackPressed();
        return true;
    }

    private void GET_AUCTION_DETAILS(Send_Product_Details send_product_details) {
        try {
            if (utility.isNetworkAvailable()) {
                utility.showProgress(false, getResources().getString(R.string.wait_string));
                Call<API_RESPONSE> call = apiInterface.FishBAngla_Auction_details(utility.getApiKey(), utility.getUSER_ID(), utility.getFirebaseToken(), send_product_details);
                call.enqueue(new Callback<API_RESPONSE>() {
                    @Override
                    public void onResponse(Call<API_RESPONSE> call, Response<API_RESPONSE> response) {
                        try {
                            utility.hideProgress();
                            Log.d("AUCTION DETAILS", response.toString());
                            if (response.isSuccessful() && response != null) {
                                API_RESPONSE api_response = response.body();
                                if (api_response.getCode() == 200) {
                                    Gson gson = new Gson();
                                    productDetails = gson.fromJson(api_response.getData().toString(), GET_AUTION_DETAILS.class);
                                    Log.d("AUCTION DETAILS", productDetails.toString());
                                    if (productDetails != null) {
                                        activityAuctionDetailsBinding.auctionFishName.setText(getResources().getString(R.string.name_string) + " " + productDetails.getProductName());
                                        activityAuctionDetailsBinding.auctionFishPrice.setText(getResources().getString(R.string.price_string) + " " + utility.convertToBangle(productDetails.getStartingPrice()) + " " + getResources().getString(R.string.taka_string));
                                        activityAuctionDetailsBinding.auctionFishWeight.setText(getResources().getString(R.string.upload_max_unit_string) + ":" + " " + utility.convertToBangle(productDetails.getProductUnit()) + " " + getResources().getString(R.string.unit_unit_string));
                                        activityAuctionDetailsBinding.auctionSellerName.setText(getResources().getString(R.string.seller_name_string) + " " + productDetails.getUserFullName());
                                        activityAuctionDetailsBinding.auctionSellerNumber.setText(getResources().getString(R.string.seller_mbl_string) + " " + utility.Mobile_Number_Read(utility.convertToBangle(productDetails.getUserPhoneNo())));
                                        if (count == 0) {
                                            activityAuctionDetailsBinding.auctionCountdown.setText(productDetails.getExpireAt());
                                            for (Product_Image s : productDetails.getProductPicture()) {
                                                detailsBanners.add(new Product_Details_Banner(s.getImage(), KeyWord.IMAGE_TYPE));
                                            }
                                            if (!TextUtils.isEmpty(productDetails.getProductVideo())) {
                                                detailsBanners.add(new Product_Details_Banner(productDetails.getProductVideo(), KeyWord.VIDEO_TYPE));
                                                utility.logger("video check");
                                            }
                                            ProductDetailsAdapter banner_adapter = new ProductDetailsAdapter(detailsBanners);
                                            activityAuctionDetailsBinding.fishAuctionBanner.create(banner_adapter, getLifecycle());
                                            activityAuctionDetailsBinding.fishAuctionBanner.setCustomIndicator(activityAuctionDetailsBinding.customIndicatorAuction);
                                            banner_adapter.setOnItemClickListener((item, position) -> {
                                                //handle click here
                                                if (item.getMedia_type().equalsIgnoreCase(KeyWord.VIDEO_TYPE)) {
                                                    try {
                                                        Uri uri = Uri.parse(item.getMedia_url());
                                                        Intent intent = new Intent(Intent.ACTION_VIEW, uri);
                                                        intent.setDataAndType(uri, "video/mp4");
                                                        startActivity(intent);
                                                    } catch (Exception e) {
                                                        Log.d("Error Line Number", Log.getStackTraceString(e));
                                                        utility.showToast(getResources().getString(R.string.video_exception));
                                                    }

                                                }
                                                utility.logger("video" + item.toString());
                                            });


                                            count++;
                                            //final long minutes = Long.parseLong(productDetails.getExpireAt()) - System.currentTimeMillis();
                                            final long minutes = Long.parseLong(productDetails.getExpireAt());
                                            countDownTimer = new CountDownTimer(minutes, 1000) {

                                                @Override
                                                public void onTick(long l) {
                                                    String value = utility.humanReadableDateTime(l);
                                                    activityAuctionDetailsBinding.auctionCountdown.setText(utility.convertToBangle(value));
                                                }

                                                @Override
                                                public void onFinish() {
                                                    activityAuctionDetailsBinding.auctionCountdown.setText(utility.convertToBangle("00:00:00"));
                                                    activityAuctionDetailsBinding.auctionFishSubmit.setClickable(false);
                                                    finish();
                                                }
                                            };
                                            countDownTimer.start();
                                        }
                                        activityAuctionDetailsBinding.auctionFishHighestPrice.setText(utility.convertToBangle(productDetails.getCurrentPrice()) + " " + getResources().getString(R.string.taka_string));
                                    }
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

    private void GET_AUCTION_TOP_AMOUNT(Send_Product_Details send_product_details) {
        try {
            if (utility.isNetworkAvailable()) {
                Call<API_RESPONSE> call = apiInterface.FishBAngla_Auction_details(utility.getApiKey(), utility.getUSER_ID(), utility.getFirebaseToken(), send_product_details);
                call.enqueue(new Callback<API_RESPONSE>() {
                    @Override
                    public void onResponse(Call<API_RESPONSE> call, Response<API_RESPONSE> response) {
                        try {
                            Log.d("AUCTION TOP AMOUNT", response.toString());
                            if (response.isSuccessful() && response != null) {
                                API_RESPONSE api_response = response.body();
                                if (api_response.getCode() == 200) {
                                    Gson gson = new Gson();
                                    productDetails = gson.fromJson(api_response.getData().toString(), GET_AUTION_DETAILS.class);
                                    Log.d("AUCTION TOP AMOUNT", productDetails.toString());
                                    if (productDetails != null) {
                                        activityAuctionDetailsBinding.auctionFishHighestPrice.setText(utility.convertToBangle(productDetails.getCurrentPrice()) + " " + getResources().getString(R.string.taka_string));
                                    }
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

    private void SEND_AUCTION_PRICE(SEND_AUCTION_PRICE auction_price) {
        try {
            if (utility.isNetworkAvailable()) {
                utility.logger("AUCTION PRICE SEND" + auction_price.toString());
                utility.showProgress(false, getResources().getString(R.string.wait_string));
                Call<API_RESPONSE> call = apiInterface.FishBAngla_Auction_Submit(utility.getApiKey(), utility.getUSER_ID(), utility.getFirebaseToken(), auction_price);
                call.enqueue(new Callback<API_RESPONSE>() {
                    @Override
                    public void onResponse(Call<API_RESPONSE> call, Response<API_RESPONSE> response) {
                        try {
                            utility.hideProgress();
                            Log.d("AUCTION PRICE", response.toString());
                            if (response.isSuccessful() && response != null) {
                                API_RESPONSE api_response = response.body();
                                if (api_response.getCode() == 200) {
                                    utility.showDialog(getResources().getString(R.string.auction_success_string));
                                    if (!TextUtils.isEmpty(auction_id)) {
                                        GET_AUCTION_TOP_AMOUNT(new Send_Product_Details(auction_id));
                                    }
                                } else if (api_response.getCode() == 101) {
                                    utility.showDialog(getResources().getString(R.string.auction_valodation_string));
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

    public void AUCTION_PRICE_ASK() {
        try {
            HashMap<String, Integer> screen = utility.getScreenRes();
            int width = screen.get(KeyWord.SCREEN_WIDTH);
            int height = screen.get(KeyWord.SCREEN_HEIGHT);
            int mywidth = (width / 10) * 7;
            final Dialog dialog = new Dialog(context);
            dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
            dialog.getWindow().setBackgroundDrawableResource(android.R.color.transparent);
            dialog.setContentView(R.layout.dialog_auction_update);
            TextInputEditText update_price = dialog.findViewById(R.id.auction_update_price);
            Button update_yes = dialog.findViewById(R.id.btn_yes);
            Button update_no = dialog.findViewById(R.id.btn_no);
            LinearLayout ll = dialog.findViewById(R.id.dialog_layout_size);
            LinearLayout.LayoutParams params = (LinearLayout.LayoutParams) ll.getLayoutParams();
            params.height = LinearLayout.LayoutParams.WRAP_CONTENT;
            params.width = mywidth;
            ll.setLayoutParams(params);
            update_yes.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    dialog.dismiss();
                    if (!TextUtils.isEmpty(update_price.getEditableText().toString())) {
                        if (!TextUtils.isEmpty(utility.getUSER_ID())) {
                            if (!utility.getUSER_ID().equalsIgnoreCase(KeyWord.USERID)) {
                                if (Integer.parseInt(productDetails.getCurrentPrice()) < Integer.parseInt(update_price.getEditableText().toString())) {
                                    SEND_AUCTION_PRICE(new SEND_AUCTION_PRICE(utility.getUSER_ID(), productDetails.getProductId(), update_price.getEditableText().toString()));
                                } else {
                                    utility.showDialog(getResources().getString(R.string.auction_valodation_string));
                                }
                            } else {
                                utility.showDialog(context.getResources().getString(R.string.login_string));
                            }
                        } else {
                            Intent intent = new Intent(context, Home_Page.class);
                            intent.putExtra("login", "yes");
                            context.startActivity(intent);
                            finish();
                        }
                    } else {
                        utility.showDialog(getResources().getString(R.string.validation_string));
                    }
                }
            });
            update_no.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    dialog.dismiss();
                }
            });
            dialog.setCancelable(true);
            dialog.show();
        } catch (Exception e) {
            Log.d("Error Line Number", Log.getStackTraceString(e));
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        handler.postDelayed(runnable, 0);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        handler.removeCallbacks(runnable);
        if (countDownTimer != null) {
            countDownTimer.cancel();
        }

        //send Auction list
        Intent in = new Intent("auction_details");
        context.sendBroadcast(in);
    }
}
