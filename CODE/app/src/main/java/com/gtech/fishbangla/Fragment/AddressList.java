package com.gtech.fishbangla.Fragment;


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
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.gtech.fishbangla.Activity.AddAddress;
import com.gtech.fishbangla.Adapter.AddressListAdapter;
import com.gtech.fishbangla.Adapter.HomeSuggestAdapter;
import com.gtech.fishbangla.DATABASE.REPOSITORY.USER_REPOSITORY;
import com.gtech.fishbangla.DATABASE.TABLE.USER_PROFILE;
import com.gtech.fishbangla.Http.ApiClient;
import com.gtech.fishbangla.Http.ApiInterface;
import com.gtech.fishbangla.Library.KeyWord;
import com.gtech.fishbangla.Library.Utility;
import com.gtech.fishbangla.MODEL.ADDRESS.SEND_ADDADRESS;
import com.gtech.fishbangla.MODEL.ADDRESS.SEND_UPDATEADDRESS;
import com.gtech.fishbangla.MODEL.API_RESPONSE;
import com.gtech.fishbangla.MODEL.RECYCLER_MODEL.Address_Model;
import com.gtech.fishbangla.MODEL.RECYCLER_MODEL.Fish_Model;
import com.gtech.fishbangla.MODEL.Send_UserID;
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
public class AddressList extends Fragment {


    View view;
    Context context;
    Utility utility;
    LinearLayout no_address;
    Button no_address_new;
    TextView no_address_view;
    RecyclerView address_recyclerview;
    List<SEND_UPDATEADDRESS> address_models;
    AddressListAdapter addressListAdapter;

    ApiInterface apiInterface = ApiClient.getBaseClient().create(ApiInterface.class);
    USER_REPOSITORY user_dao;
    USER_PROFILE userProfile;

    public AddressList() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_address_list, container, false);
        try {
            context = getActivity();
            utility = new Utility(context);
            user_dao = new USER_REPOSITORY(getActivity().getApplication());
            userProfile = user_dao.getuserData();
            no_address = view.findViewById(R.id.no_address);
            no_address_new = view.findViewById(R.id.no_add_new_address);
            no_address_view = view.findViewById(R.id.no_address_view);
            address_recyclerview = view.findViewById(R.id.address_recyclerview);
            no_address_new.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    startActivity(new Intent(context, AddAddress.class));

                }
            });
            if (userProfile != null) {
                initial_address();
                Get_Address_List(new Send_UserID(userProfile.getUserId()));
            }
        } catch (Exception e) {
            Log.d("Error Line Number", Log.getStackTraceString(e));
        }
        return view;
    }

    public void initial_address() {
        address_models = new ArrayList<>();
        addressListAdapter = new AddressListAdapter(address_models, context);
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false);
        address_recyclerview.setLayoutManager(mLayoutManager);
        address_recyclerview.setItemAnimator(new DefaultItemAnimator());
        address_recyclerview.setAdapter(addressListAdapter);
    }

    private void Get_Address_List(Send_UserID s) {
        try {
            if (utility.isNetworkAvailable()) {
                Log.d("Address List", s.toString());
                utility.showProgress(false, getResources().getString(R.string.wait_string));
                Call<API_RESPONSE> call = apiInterface.FishBAngla_list_address(utility.getApiKey(), utility.getUSER_ID(), utility.getFirebaseToken(), s);
                call.enqueue(new Callback<API_RESPONSE>() {
                    @Override
                    public void onResponse(Call<API_RESPONSE> call, Response<API_RESPONSE> response) {
                        try {
                            utility.hideProgress();
                            Log.d("Address List", response.toString());
                            if (response.isSuccessful() && response != null) {
                                API_RESPONSE api_response = response.body();
                                if (api_response.getCode() == 200) {
                                    Gson gson = new Gson();
                                    Type listType = new TypeToken<List<SEND_UPDATEADDRESS>>() {
                                    }.getType();
                                    List<SEND_UPDATEADDRESS> get_products = gson.fromJson(response.body().getData(), listType);
                                    Log.d("Address List", get_products.size() + "");
                                    address_models.clear();
                                    address_models.addAll(get_products);
                                    addressListAdapter.notifyDataSetChanged();
                                    if (get_products.size() > 0) {
                                        no_address_view.setVisibility(View.GONE);
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
                utility.logger("From Address Add");
                Get_Address_List(new Send_UserID(userProfile.getUserId()));
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    };

    @Override
    public void onResume() {
        try {
            context.registerReceiver(mReceiverLocation, new IntentFilter("address_update"));
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
