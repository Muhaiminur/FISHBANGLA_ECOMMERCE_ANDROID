package com.gtech.fishbangla.Fragment;


import android.annotation.SuppressLint;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;

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
import com.gtech.fishbangla.Library.KeyWord;
import com.gtech.fishbangla.Library.Utility;
import com.gtech.fishbangla.MODEL.API_RESPONSE;
import com.gtech.fishbangla.MODEL.PRODUCT.Get_Product;
import com.gtech.fishbangla.MODEL.PRODUCT.SEND_FILTER_PRODUCT;
import com.gtech.fishbangla.MODEL.PRODUCT.Send_Product_Category;
import com.gtech.fishbangla.MODEL.RECYCLER_MODEL.Fish_Model;
import com.gtech.fishbangla.R;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * A simple {@link Fragment} subclass.
 */
public class AllWholesaleFragment extends Fragment {


    View view;
    Context context;
    Utility utility;
    List<Get_Product> products;
    HomeSuggestAdapter homeSuggestAdapter;
    RecyclerView whole_recyclerView;

    ApiInterface apiInterface = ApiClient.getBaseClient().create(ApiInterface.class);
    String category_id = "0";

    public AllWholesaleFragment() {
        // Required empty public constructor
    }

    @SuppressLint("ValidFragment")
    public AllWholesaleFragment(Context context, String s) {
        this.context = context;
        utility = new Utility(this.context);
        products = new ArrayList<>();
        category_id = s;
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_all_wholesale, container, false);
        try {
            whole_recyclerView = view.findViewById(R.id.wholesale_recycleview);
            initial_wholesale();
        } catch (Exception e) {
            Log.d("Error Line Number", Log.getStackTraceString(e));
        }
        return view;
    }

    public void initial_wholesale() {
        homeSuggestAdapter = new HomeSuggestAdapter(products, context);
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false);
        whole_recyclerView.setLayoutManager(mLayoutManager);
        whole_recyclerView.setItemAnimator(new DefaultItemAnimator());
        whole_recyclerView.setAdapter(homeSuggestAdapter);
        Get_Product(new Send_Product_Category(category_id, "2"));
    }

    private void Get_Product(Send_Product_Category s) {
        try {
            if (utility.isNetworkAvailable()) {
                Log.d("PRODUCT CATEGORY", s.toString());
                utility.showProgress(false, getResources().getString(R.string.wait_string));
                Call<API_RESPONSE> call = apiInterface.FishBAngla_Category(utility.getApiKey(), utility.getUSER_ID(), utility.getFirebaseToken(), s);
                call.enqueue(new Callback<API_RESPONSE>() {
                    @Override
                    public void onResponse(Call<API_RESPONSE> call, Response<API_RESPONSE> response) {
                        try {
                            utility.hideProgress();
                            Log.d("PRODUCT CATEGORY", response.toString());
                            if (response.isSuccessful() && response != null) {
                                API_RESPONSE api_response = response.body();
                                if (api_response.getCode() == 200) {
                                    Gson gson = new Gson();
                                    Type listType = new TypeToken<List<Get_Product>>() {
                                    }.getType();
                                    List<Get_Product> get_products = gson.fromJson(response.body().getData(), listType);
                                    Log.d("PRODUCT CATEGORY Size", get_products.size() + "");
                                    products.clear();
                                    products.addAll(get_products);
                                    homeSuggestAdapter.notifyDataSetChanged();
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

    private void Get_Product_FIlster(SEND_FILTER_PRODUCT s) {
        try {
            if (utility.isNetworkAvailable()) {
                Log.d("PRODUCT FILTER CATEGORY", s.toString());
                utility.showProgress(false, getResources().getString(R.string.wait_string));
                Call<API_RESPONSE> call = apiInterface.FishBAngla_Product_Filter(utility.getApiKey(), utility.getUSER_ID(), utility.getFirebaseToken(), s);
                call.enqueue(new Callback<API_RESPONSE>() {
                    @Override
                    public void onResponse(Call<API_RESPONSE> call, Response<API_RESPONSE> response) {
                        try {
                            utility.hideProgress();
                            Log.d("PRODUCT FILTER CATEGORY", response.toString());
                            if (response.isSuccessful() && response != null) {
                                API_RESPONSE api_response = response.body();
                                if (api_response.getCode() == 200) {
                                    Gson gson = new Gson();
                                    Type listType = new TypeToken<List<Get_Product>>() {
                                    }.getType();
                                    List<Get_Product> get_products = gson.fromJson(response.body().getData(), listType);
                                    Log.d("PRODUCT FILTER CATEGORY", get_products.size() + "");
                                    products.clear();
                                    products.addAll(get_products);
                                    homeSuggestAdapter.notifyDataSetChanged();
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

    private BroadcastReceiver mReceiverLocation = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            try {
                Gson gson = new Gson();
                SEND_FILTER_PRODUCT send_filter_product = gson.fromJson(intent.getStringExtra("filter"), SEND_FILTER_PRODUCT.class);
                send_filter_product.setPostTypeId("2");
                if (send_filter_product != null) {
                    Get_Product_FIlster(send_filter_product);
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    };

    @Override
    public void onResume() {
        try {
            context.registerReceiver(mReceiverLocation, new IntentFilter("PRODUCT_FILTER"));
            context.registerReceiver(cart_boradcast, new IntentFilter("cart_delete"));
        } catch (Exception e) {
            Log.d("Error Line Number", Log.getStackTraceString(e));
        }
        super.onResume();
    }

    @Override
    public void onDestroy() {
        try {
            context.unregisterReceiver(mReceiverLocation);
            context.unregisterReceiver(cart_boradcast);
        } catch (Exception e) {
            Log.d("Error Line Number", Log.getStackTraceString(e));
        }
        super.onDestroy();
    }

    //from Cart activity
    private BroadcastReceiver cart_boradcast = new BroadcastReceiver() {
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
