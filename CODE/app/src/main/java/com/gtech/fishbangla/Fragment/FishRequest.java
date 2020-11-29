package com.gtech.fishbangla.Fragment;


import android.content.Context;
import android.os.Bundle;

import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;

import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.gtech.fishbangla.DATABASE.REPOSITORY.USER_REPOSITORY;
import com.gtech.fishbangla.DATABASE.TABLE.USER_PROFILE;
import com.gtech.fishbangla.Http.ApiClient;
import com.gtech.fishbangla.Http.ApiInterface;
import com.gtech.fishbangla.Library.KeyWord;
import com.gtech.fishbangla.Library.Utility;
import com.gtech.fishbangla.MODEL.API_RESPONSE;
import com.gtech.fishbangla.MODEL.ISSUE.GET_ISSUE_LIST;
import com.gtech.fishbangla.MODEL.ISSUE.SEND_ISSUE;
import com.gtech.fishbangla.R;
import com.gtech.fishbangla.databinding.FragmentFishRequestBinding;

import java.lang.reflect.Type;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * A simple {@link Fragment} subclass.
 */
public class FishRequest extends Fragment {
    Context context;
    Utility utility;
    ApiInterface apiInterface = ApiClient.getBaseClient().create(ApiInterface.class);
    FragmentFishRequestBinding fishRequestBinding;
    USER_REPOSITORY user_dao;
    USER_PROFILE userProfile;
    GET_ISSUE_LIST get_issue_list;

    public FishRequest() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        fishRequestBinding = DataBindingUtil.inflate(inflater, R.layout.fragment_fish_request, container, false);
        try {
            context = getActivity();
            utility = new Utility(context);
            user_dao = new USER_REPOSITORY(getActivity().getApplication());
            userProfile = user_dao.getuserData();
            fishRequestBinding.requestSubmit.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (userProfile != null && !TextUtils.isEmpty(fishRequestBinding.requestMessage.getEditableText().toString()) && get_issue_list != null) {
                        Send_Issue(new SEND_ISSUE(userProfile.getUserId(), get_issue_list.getUserIssueId(), fishRequestBinding.requestMessage.getEditableText().toString()));
                    }
                }
            });
            fishRequestBinding.requestCategory.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                @Override
                public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                    get_issue_list = (GET_ISSUE_LIST) parent.getSelectedItem();
                }

                @Override
                public void onNothingSelected(AdapterView<?> parent) {

                }
            });
            Get_Issue_List();

        } catch (Exception e) {
            Log.d("Error Line Number", Log.getStackTraceString(e));
        }
        return fishRequestBinding.getRoot();
    }

    private void Get_Issue_List() {
        try {
            if (utility.isNetworkAvailable()) {
                utility.showProgress(false, getResources().getString(R.string.wait_string));
                Call<API_RESPONSE> call = apiInterface.FishBAngla_issue_list(utility.getApiKey(), utility.getUSER_ID(), utility.getFirebaseToken());
                call.enqueue(new Callback<API_RESPONSE>() {
                    @Override
                    public void onResponse(Call<API_RESPONSE> call, Response<API_RESPONSE> response) {
                        try {
                            utility.hideProgress();
                            Log.d("Issue LIST", response.toString());
                            if (response.isSuccessful() && response != null) {
                                API_RESPONSE api_response = response.body();
                                if (api_response.getCode() == 200) {
                                    Gson gson = new Gson();
                                    Type listType = new TypeToken<List<GET_ISSUE_LIST>>() {
                                    }.getType();
                                    List<GET_ISSUE_LIST> get_products = gson.fromJson(response.body().getData(), listType);
                                    Log.d("Issue LIST Size", get_products.size() + "");
                                    /*product_data.clear();
                                    product_data.addAll(get_products);*/
                                    if (get_products.size() > 0) {
                                        ArrayAdapter<GET_ISSUE_LIST> product_adapter = new ArrayAdapter<GET_ISSUE_LIST>(context, android.R.layout.simple_spinner_item, get_products);
                                        product_adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                                        fishRequestBinding.requestCategory.setAdapter(product_adapter);
                                        fishRequestBinding.requestCategory.setSelection(1);
                                    } else {
                                        utility.showDialog(context.getResources().getString(R.string.no_data_string));
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

    private void Send_Issue(SEND_ISSUE send_issue) {
        try {
            if (utility.isNetworkAvailable()) {
                utility.logger("Send Issue" + send_issue.toString());
                utility.showProgress(false, getResources().getString(R.string.wait_string));
                Call<API_RESPONSE> call = apiInterface.FishBAngla_Send_Issue(utility.getApiKey(), utility.getUSER_ID(), utility.getFirebaseToken(), send_issue);
                call.enqueue(new Callback<API_RESPONSE>() {
                    @Override
                    public void onResponse(Call<API_RESPONSE> call, Response<API_RESPONSE> response) {
                        try {
                            utility.hideProgress();
                            Log.d("Send Issue", response.toString());
                            if (response.isSuccessful() && response != null) {
                                API_RESPONSE api_response = response.body();
                                if (api_response.getCode() == 200) {
                                    utility.showDialog(context.getResources().getString(R.string.request_success_string));
                                    fishRequestBinding.requestMessage.setText("");
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
