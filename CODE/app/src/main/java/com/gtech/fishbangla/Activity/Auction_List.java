package com.gtech.fishbangla.Activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.gtech.fishbangla.Adapter.AuctionListAdapter;
import com.gtech.fishbangla.Http.ApiClient;
import com.gtech.fishbangla.Http.ApiInterface;
import com.gtech.fishbangla.Library.KeyWord;
import com.gtech.fishbangla.Library.Utility;
import com.gtech.fishbangla.MODEL.API_RESPONSE;
import com.gtech.fishbangla.MODEL.AUCTION.GET_AUCTION_LIST;
import com.gtech.fishbangla.R;
import com.gtech.fishbangla.databinding.ActivityAuctionListBinding;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class Auction_List extends AppCompatActivity {

    Utility utility;
    Context context;
    ApiInterface apiInterface = ApiClient.getBaseClient().create(ApiInterface.class);
    ActivityAuctionListBinding activityAuctionListBinding;
    AuctionListAdapter auctionListAdapter;
    List<GET_AUCTION_LIST> auctionLists = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_auction__list);
        getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle(R.string.auction_string);
        try {
            context = Auction_List.this;
            activityAuctionListBinding = DataBindingUtil.setContentView(this, R.layout.activity_auction__list);
            utility = new Utility(context);
            initial_Auction();
        } catch (Exception e) {
            Log.d("Error Line Number", Log.getStackTraceString(e));
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        onBackPressed();
        return true;
    }

    public void initial_Auction() {
        auctionListAdapter = new AuctionListAdapter(auctionLists, context);
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false);
        activityAuctionListBinding.auctionListRecyclerview.setLayoutManager(mLayoutManager);
        activityAuctionListBinding.auctionListRecyclerview.setItemAnimator(new DefaultItemAnimator());
        activityAuctionListBinding.auctionListRecyclerview.setAdapter(auctionListAdapter);
        GET_AUCTION_LIST_API();
    }

    private void GET_AUCTION_LIST_API() {
        try {
            if (utility.isNetworkAvailable()) {
                utility.showProgress(false, getResources().getString(R.string.wait_string));
                Call<API_RESPONSE> call = apiInterface.FishBAngla_Auction_list(utility.getApiKey(), utility.getUSER_ID(), utility.getFirebaseToken());
                call.enqueue(new Callback<API_RESPONSE>() {
                    @Override
                    public void onResponse(Call<API_RESPONSE> call, Response<API_RESPONSE> response) {
                        try {
                            utility.hideProgress();
                            Log.d("GET AUCTIONLIST", response.toString());
                            if (response.isSuccessful() && response != null) {
                                API_RESPONSE api_response = response.body();
                                if (api_response.getCode() == 200) {
                                    Gson gson = new Gson();
                                    Type listType = new TypeToken<List<GET_AUCTION_LIST>>() {
                                    }.getType();
                                    List<GET_AUCTION_LIST> getAuctionLists = gson.fromJson(response.body().getData(), listType);
                                    Log.d("AUCTIONLIST List Size", getAuctionLists.size() + "");
                                    if (getAuctionLists.size() > 0) {
                                        auctionLists.clear();
                                        auctionLists.addAll(getAuctionLists);
                                        auctionListAdapter.notifyDataSetChanged();
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


    //from auction details
    @Override
    public void onResume() {
        context.registerReceiver(mReceiverLocation, new IntentFilter("auction_details"));
        super.onResume();
    }

    @Override
    public void onPause() {
        context.unregisterReceiver(mReceiverLocation);
        super.onPause();
    }

    private BroadcastReceiver mReceiverLocation = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            try {
                GET_AUCTION_LIST_API();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    };
}
