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
import com.gtech.fishbangla.Adapter.NotificationAdapter;
import com.gtech.fishbangla.Http.ApiClient;
import com.gtech.fishbangla.Http.ApiInterface;
import com.gtech.fishbangla.Library.ENDLESSRECYCLER;
import com.gtech.fishbangla.Library.KeyWord;
import com.gtech.fishbangla.Library.Utility;
import com.gtech.fishbangla.MODEL.API_RESPONSE;
import com.gtech.fishbangla.MODEL.NOTIFICATION.GET_NOTIFICATION;
import com.gtech.fishbangla.MODEL.NOTIFICATION.SEND_NOTIFICATION;
import com.gtech.fishbangla.R;
import com.gtech.fishbangla.databinding.FragmentNotificationListBinding;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * A simple {@link Fragment} subclass.
 */
public class NotificationList extends Fragment {

    Utility utility;
    Context context;
    List<GET_NOTIFICATION> notificationList = new ArrayList<>();
    NotificationAdapter notificationAdapter;
    ApiInterface apiInterface = ApiClient.getBaseClient().create(ApiInterface.class);
    FragmentNotificationListBinding notificationListBinding;

    public NotificationList() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        notificationListBinding = DataBindingUtil.inflate(inflater, R.layout.fragment_notification_list, container, false);
        try {
            context = getActivity();
            utility = new Utility(context);
            notificationListBinding.notificationRead.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Update_Notification(new SEND_NOTIFICATION(KeyWord.NOTIFICATION_ALL, KeyWord.NOTIFICATION_READ));
                }
            });
            initial_Notification();
        } catch (Exception e) {
            Log.d("Error Line Number", Log.getStackTraceString(e));
        }
        return notificationListBinding.getRoot();
    }

    public void initial_Notification() {
        notificationAdapter = new NotificationAdapter(notificationList, context);
        //RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false);
        //notificationListBinding.notificationList.setLayoutManager(mLayoutManager);
        notificationListBinding.notificationList.setItemAnimator(new DefaultItemAnimator());
        notificationListBinding.notificationList.setAdapter(notificationAdapter);

        LinearLayoutManager mLayoutManager = new LinearLayoutManager(context);
        notificationListBinding.notificationList.setLayoutManager(mLayoutManager);
        ENDLESSRECYCLER scrollListener = new ENDLESSRECYCLER(mLayoutManager) {
            @Override
            public void onLoadMore(int page, int totalItemsCount, RecyclerView view) {
                // Triggered only when new data needs to be appended to the list
                // Add whatever code is needed to append new items to the bottom of the list
                GET_NOTIFICATION_LIST(page + "");
            }
        };
        // Adds the scroll listener to RecyclerView
        notificationListBinding.notificationList.addOnScrollListener(scrollListener);
        GET_NOTIFICATION_LIST("0");
    }

    private void GET_NOTIFICATION_LIST(String s) {
        try {
            if (utility.isNetworkAvailable()) {
                utility.showProgress(false, getResources().getString(R.string.wait_string));
                Call<API_RESPONSE> call = apiInterface.FishBAngla_Notification_list(utility.getApiKey(), utility.getUSER_ID(), utility.getFirebaseToken(), s);
                call.enqueue(new Callback<API_RESPONSE>() {
                    @Override
                    public void onResponse(Call<API_RESPONSE> call, Response<API_RESPONSE> response) {
                        try {
                            utility.hideProgress();
                            Log.d("GET NOTIFICATION", response.toString());
                            if (response.isSuccessful() && response != null) {
                                API_RESPONSE api_response = response.body();
                                if (api_response.getCode() == 200) {
                                    Gson gson = new Gson();
                                    Type listType = new TypeToken<List<GET_NOTIFICATION>>() {
                                    }.getType();
                                    List<GET_NOTIFICATION> get_products = gson.fromJson(response.body().getData(), listType);
                                    Log.d("NOTIFICATION List Size", get_products.size() + "");
                                    notificationList.addAll(get_products);
                                    if (get_products.size() > 0) {
                                        notificationAdapter = new NotificationAdapter(notificationList, context);
                                        notificationAdapter.notifyDataSetChanged();
                                        notificationListBinding.notificationList.setAdapter(notificationAdapter);
                                        notificationListBinding.notificationList.scrollToPosition(notificationList.size() > 20 ? notificationList.size() - get_products.size() - 2 : 0);
                                    } else if (notificationList.size() == 0) {
                                        utility.showDialog(context.getResources().getString(R.string.notification_no_data_string));
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

    private void Update_Notification(SEND_NOTIFICATION s) {
        try {
            if (utility.isNetworkAvailable()) {
                Log.d("send notification", s.toString());
                utility.showProgress(false, context.getResources().getString(R.string.wait_string));
                Call<API_RESPONSE> call = apiInterface.FishBAngla_Notification_update(utility.getApiKey(), utility.getUSER_ID(), utility.getFirebaseToken(), s);
                call.enqueue(new Callback<API_RESPONSE>() {
                    @Override
                    public void onResponse(Call<API_RESPONSE> call, Response<API_RESPONSE> response) {
                        try {
                            utility.hideProgress();
                            Log.d("send notification", response.toString());
                            if (response.isSuccessful() && response != null) {
                                API_RESPONSE api_response = response.body();
                                if (api_response.getCode() == 200) {
                                    GET_NOTIFICATION_LIST("0");
                                } else if (api_response.getCode() == 300) {
                                    utility.showToast(api_response.getData().getAsString().toString());
                                } else {
                                    utility.showDialog(context.getResources().getString(R.string.try_again_string));
                                }
                            } else {
                                utility.showDialog(context.getResources().getString(R.string.try_again_string));
                            }
                        } catch (Exception ex) {
                            utility.logger(ex.toString());
                        }
                    }

                    @Override
                    public void onFailure(Call<API_RESPONSE> call, Throwable t) {
                        utility.logger(t.toString());
                        utility.showToast(context.getResources().getString(R.string.try_again_string));
                        utility.hideProgress();
                    }
                });
            } else {
                utility.showDialog(context.getResources().getString(R.string.no_internet_string));
            }
        } catch (Exception e) {
            Log.d("Error Line Number", Log.getStackTraceString(e));
        }
    }

    //from notification adapter
    @Override
    public void onResume() {
        context.registerReceiver(mReceiverLocation, new IntentFilter("notification_delete"));
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
                notificationList.clear();
                GET_NOTIFICATION_LIST("0");
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    };
}
