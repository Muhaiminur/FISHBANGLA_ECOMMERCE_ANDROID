package com.gtech.fishbangla.Fragment;

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

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.gtech.fishbangla.Adapter.HomeSuggestAdapter;
import com.gtech.fishbangla.Http.ApiClient;
import com.gtech.fishbangla.Http.ApiInterface;
import com.gtech.fishbangla.Library.ENDLESSRECYCLER;
import com.gtech.fishbangla.Library.EndlessRecyclerViewScrollListener;
import com.gtech.fishbangla.Library.Utility;
import com.gtech.fishbangla.MODEL.API_RESPONSE;
import com.gtech.fishbangla.MODEL.PRODUCT.Get_Product;
import com.gtech.fishbangla.R;
import com.gtech.fishbangla.databinding.FragmentAllProductBinding;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * A simple {@link Fragment} subclass.
 */
public class AllProduct extends Fragment {
    Gson gson;
    Context context;
    Utility utility;
    ApiInterface apiInterface = ApiClient.getBaseClient().create(ApiInterface.class);
    FragmentAllProductBinding allProductBinding;
    ENDLESSRECYCLER scrollListener;
    HomeSuggestAdapter homeSuggestAdapter;
    List<Get_Product> productList = new ArrayList<>();

    public AllProduct() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        if (allProductBinding == null) {
            allProductBinding = DataBindingUtil.inflate(inflater, R.layout.fragment_all_product, container, false);
            try {
                context = getActivity();
                utility = new Utility(context);
                gson = new Gson();
                initial_allproduct();
            } catch (Exception e) {
                Log.d("Error Line Number", Log.getStackTraceString(e));
            }
        }
        return allProductBinding.getRoot();
    }

    public void initial_allproduct() {
        LinearLayoutManager mLayoutManager = new LinearLayoutManager(context);
        allProductBinding.allproductRecycler.setLayoutManager(mLayoutManager);
        scrollListener = new ENDLESSRECYCLER(mLayoutManager) {
            @Override
            public void onLoadMore(int page, int totalItemsCount, RecyclerView view) {
                // Triggered only when new data needs to be appended to the list
                // Add whatever code is needed to append new items to the bottom of the list
                Get_Product(page + "");
            }
        };
        // Adds the scroll listener to RecyclerView
        allProductBinding.allproductRecycler.addOnScrollListener(scrollListener);
        Get_Product("0");
    }

    private void Get_Product(String s) {
        try {
            if (utility.isNetworkAvailable()) {
                Log.d("GET PRODUCT", s);
                //utility.showProgress(false, getResources().getString(R.string.wait_string));
                Call<API_RESPONSE> call = apiInterface.FishBAngla_All_Product(utility.getApiKey(), utility.getUSER_ID(), utility.getFirebaseToken(), s);
                call.enqueue(new Callback<API_RESPONSE>() {
                    @Override
                    public void onResponse(Call<API_RESPONSE> call, Response<API_RESPONSE> response) {
                        try {
                            utility.hideProgress();
                            Log.d("GET PRODUCT", response.toString());
                            if (response.isSuccessful() && response != null) {
                                API_RESPONSE api_response = response.body();
                                if (api_response.getCode() == 200) {
                                    Type listType = new TypeToken<List<Get_Product>>() {
                                    }.getType();
                                    List<Get_Product> get_products = gson.fromJson(response.body().getData(), listType);
                                    Log.d("Product List Size", get_products.size() + "");
                                    productList.addAll(get_products);
                                    if (get_products.size() > 0) {
                                        homeSuggestAdapter = new HomeSuggestAdapter(productList, context);
                                        homeSuggestAdapter.notifyDataSetChanged();
                                        allProductBinding.allproductRecycler.setAdapter(homeSuggestAdapter);
                                        allProductBinding.allproductRecycler.scrollToPosition(productList.size() > 20 ? productList.size() - get_products.size() - 2 : 0);
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
                if (homeSuggestAdapter != null) {
                    homeSuggestAdapter.notifyDataSetChanged();
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    };
}
