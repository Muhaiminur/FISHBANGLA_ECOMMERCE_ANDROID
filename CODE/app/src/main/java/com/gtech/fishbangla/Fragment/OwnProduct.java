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
import com.gtech.fishbangla.Activity.Sell_Product;
import com.gtech.fishbangla.Adapter.OwnProductAdapter;
import com.gtech.fishbangla.DATABASE.REPOSITORY.USER_REPOSITORY;
import com.gtech.fishbangla.DATABASE.TABLE.USER_PROFILE;
import com.gtech.fishbangla.Http.ApiClient;
import com.gtech.fishbangla.Http.ApiInterface;
import com.gtech.fishbangla.Library.KeyWord;
import com.gtech.fishbangla.Library.Utility;
import com.gtech.fishbangla.MODEL.API_RESPONSE;
import com.gtech.fishbangla.MODEL.PRODUCT.Get_OwnProduct;
import com.gtech.fishbangla.MODEL.Send_UserID;
import com.gtech.fishbangla.R;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

import com.gtech.fishbangla.MODEL.RECYCLER_MODEL.Fish_Model;
import com.gtech.fishbangla.databinding.FragmentOwnProductBinding;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * A simple {@link Fragment} subclass.
 */
public class OwnProduct extends Fragment {
    Context context;
    Utility utility;
    List<Get_OwnProduct> products;
    OwnProductAdapter ownProductAdapter;
    ApiInterface apiInterface = ApiClient.getBaseClient().create(ApiInterface.class);
    USER_REPOSITORY user_dao;
    USER_PROFILE userProfile;
    FragmentOwnProductBinding fragmentOwnProductBinding;

    public OwnProduct() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        fragmentOwnProductBinding = DataBindingUtil.inflate(inflater, R.layout.fragment_own_product, container, false);
        try {
            context = getActivity();
            utility = new Utility(context);
            user_dao = new USER_REPOSITORY(getActivity().getApplication());
            userProfile = user_dao.getuserData();
            fragmentOwnProductBinding.sellerOwnproductAddproduct.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    startActivity(new Intent(context, Sell_Product.class));
                }
            });
            initial_ownproduct();
        } catch (Exception e) {
            Log.d("Error Line Number", Log.getStackTraceString(e));
        }
        return fragmentOwnProductBinding.getRoot();
    }

    public void initial_ownproduct() {
        products = new ArrayList<>();
        ownProductAdapter = new OwnProductAdapter(products, context);
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false);
        fragmentOwnProductBinding.sellerOwnproductRecycler.setLayoutManager(mLayoutManager);
        fragmentOwnProductBinding.sellerOwnproductRecycler.setItemAnimator(new DefaultItemAnimator());
        fragmentOwnProductBinding.sellerOwnproductRecycler.setAdapter(ownProductAdapter);
        if (userProfile != null) {
            Get_Own_Product(new Send_UserID(userProfile.getUserId()));
        }
    }

    private void Get_Own_Product(Send_UserID s) {
        try {
            if (utility.isNetworkAvailable()) {
                Log.d("Own product", s.toString());
                utility.showProgress(false, getResources().getString(R.string.wait_string));
                Call<API_RESPONSE> call = apiInterface.FishBAngla_Own_Product(utility.getApiKey(), utility.getUSER_ID(), utility.getFirebaseToken(), s);
                call.enqueue(new Callback<API_RESPONSE>() {
                    @Override
                    public void onResponse(Call<API_RESPONSE> call, Response<API_RESPONSE> response) {
                        try {
                            utility.hideProgress();
                            Log.d("Own product", response.toString());
                            if (response.isSuccessful() && response != null) {
                                API_RESPONSE api_response = response.body();
                                if (api_response.getCode() == 200) {
                                    Gson gson = new Gson();
                                    Type listType = new TypeToken<List<Get_OwnProduct>>() {
                                    }.getType();
                                    List<Get_OwnProduct> get_products = gson.fromJson(response.body().getData(), listType);
                                    Log.d("Own product", get_products.size() + "");
                                    if (get_products.size() > 0) {
                                        products.clear();
                                        products.addAll(get_products);
                                        ownProductAdapter.notifyDataSetChanged();
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

    private BroadcastReceiver mReceiverLocation = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            try {
                utility.logger("Product Edits Add");
                if (userProfile != null) {
                    Get_Own_Product(new Send_UserID(userProfile.getUserId()));
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    };

    @Override
    public void onResume() {
        try {
            context.registerReceiver(mReceiverLocation, new IntentFilter("product_update"));
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

}
