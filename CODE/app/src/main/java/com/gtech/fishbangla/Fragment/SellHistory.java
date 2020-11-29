package com.gtech.fishbangla.Fragment;

import android.content.Context;
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
import com.gtech.fishbangla.Adapter.SellHistoryAdapter;
import com.gtech.fishbangla.DATABASE.REPOSITORY.USER_REPOSITORY;
import com.gtech.fishbangla.DATABASE.TABLE.USER_PROFILE;
import com.gtech.fishbangla.Http.ApiClient;
import com.gtech.fishbangla.Http.ApiInterface;
import com.gtech.fishbangla.Library.KeyWord;
import com.gtech.fishbangla.Library.Utility;
import com.gtech.fishbangla.MODEL.API_RESPONSE;
import com.gtech.fishbangla.MODEL.HISTORY.GET_SELL_HISTORY;
import com.gtech.fishbangla.MODEL.Send_UserID;
import com.gtech.fishbangla.R;
import com.gtech.fishbangla.databinding.FragmentSellHistoryBinding;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * A simple {@link Fragment} subclass.
 */
public class SellHistory extends Fragment {

    public SellHistory() {
        // Required empty public constructor
    }


    Context context;
    Utility utility;
    ApiInterface apiInterface = ApiClient.getBaseClient().create(ApiInterface.class);
    USER_REPOSITORY user_dao;
    USER_PROFILE userProfile;
    FragmentSellHistoryBinding fragmentSellHistoryBinding;
    List<GET_SELL_HISTORY> history_models;
    SellHistoryAdapter sellHistoryAdapter;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        fragmentSellHistoryBinding = DataBindingUtil.inflate(inflater, R.layout.fragment_sell_history, container, false);
        try {
            context = getActivity();
            utility = new Utility(context);
            user_dao = new USER_REPOSITORY(getActivity().getApplication());
            userProfile = user_dao.getuserData();
            initial_history();
        } catch (Exception e) {
            Log.d("Error Line Number", Log.getStackTraceString(e));
        }
        return fragmentSellHistoryBinding.getRoot();
    }

    public void initial_history() {
        history_models = new ArrayList<>();
        sellHistoryAdapter = new SellHistoryAdapter(history_models, context);
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false);
        fragmentSellHistoryBinding.sellRecycler.setLayoutManager(mLayoutManager);
        fragmentSellHistoryBinding.sellRecycler.setItemAnimator(new DefaultItemAnimator());
        fragmentSellHistoryBinding.sellRecycler.setAdapter(sellHistoryAdapter);
        if (userProfile != null) {
            Get_Buy_History(new Send_UserID(userProfile.getUserId()));
        }
    }

    private void Get_Buy_History(Send_UserID s) {
        try {
            if (utility.isNetworkAvailable()) {
                Log.d("Sell HISTORY", s.toString());
                utility.showProgress(false, getResources().getString(R.string.wait_string));
                Call<API_RESPONSE> call = apiInterface.FishBAngla_sell_History(utility.getApiKey(), utility.getUSER_ID(), utility.getFirebaseToken(), s);
                call.enqueue(new Callback<API_RESPONSE>() {
                    @Override
                    public void onResponse(Call<API_RESPONSE> call, Response<API_RESPONSE> response) {
                        try {
                            utility.hideProgress();
                            Log.d("Sell HISTORY", response.toString());
                            if (response.isSuccessful() && response != null) {
                                API_RESPONSE api_response = response.body();
                                if (api_response.getCode() == 200) {
                                    Gson gson = new Gson();
                                    Type listType = new TypeToken<List<GET_SELL_HISTORY>>() {
                                    }.getType();
                                    List<GET_SELL_HISTORY> get_products = gson.fromJson(response.body().getData(), listType);
                                    Log.d("Sell HISTORY", get_products.size() + "");
                                    if (get_products.size() > 0) {
                                        history_models.clear();
                                        history_models.addAll(get_products);
                                        sellHistoryAdapter.notifyDataSetChanged();
                                    } else {
                                        utility.showDialog(context.getResources().getString(R.string.sell_no_data_string));
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
}
