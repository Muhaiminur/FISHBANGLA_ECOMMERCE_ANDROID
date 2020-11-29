package com.gtech.fishbangla.Activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.google.gson.Gson;
import com.gtech.fishbangla.DATABASE.REPOSITORY.DISTRICT_REPOSITORY;
import com.gtech.fishbangla.DATABASE.REPOSITORY.DIVISION_REPOSITORY;
import com.gtech.fishbangla.DATABASE.REPOSITORY.UPOZILLA_REPOSITORY;
import com.gtech.fishbangla.DATABASE.REPOSITORY.USER_REPOSITORY;
import com.gtech.fishbangla.DATABASE.TABLE.DISTRICT_TABLE;
import com.gtech.fishbangla.DATABASE.TABLE.DIVISION_TABLE;
import com.gtech.fishbangla.DATABASE.TABLE.UPAZILLA_TABLE;
import com.gtech.fishbangla.DATABASE.TABLE.USER_PROFILE;
import com.gtech.fishbangla.Http.ApiClient;
import com.gtech.fishbangla.Http.ApiInterface;
import com.gtech.fishbangla.Library.KeyWord;
import com.gtech.fishbangla.Library.Utility;
import com.gtech.fishbangla.MODEL.ADDRESS.SEND_ADDADRESS;
import com.gtech.fishbangla.MODEL.ADDRESS.SEND_UPDATEADDRESS;
import com.gtech.fishbangla.MODEL.API_RESPONSE;
import com.gtech.fishbangla.R;
import com.gtech.fishbangla.databinding.ActivityAddAddressBinding;

import java.util.HashMap;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class AddAddress extends AppCompatActivity {
    Context context;
    Utility utility;
    ActivityAddAddressBinding activityAddAddressBinding;
    ApiInterface apiInterface = ApiClient.getBaseClient().create(ApiInterface.class);
    USER_REPOSITORY user_dao;
    USER_PROFILE userProfile;

    DIVISION_REPOSITORY divisionRepository;
    List<DIVISION_TABLE> division_tables;
    DISTRICT_REPOSITORY districtRepository;
    List<DISTRICT_TABLE> districtTables;
    UPOZILLA_REPOSITORY upozillaRepository;
    List<UPAZILLA_TABLE> upazillaTables;
    String prime = "YES";
    SEND_UPDATEADDRESS send_updateaddress;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_address);
        getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        try {
            activityAddAddressBinding = DataBindingUtil.setContentView(this, R.layout.activity_add_address);
            user_dao = new USER_REPOSITORY(getApplication());
            userProfile = user_dao.getuserData();
            context = AddAddress.this;
            utility = new Utility(context);
            activityAddAddressBinding.submitNewAddress.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (userProfile != null) {
                        SEND_ADDADRESS addadress = new SEND_ADDADRESS();
                        addadress.setUserId(userProfile.getUserId());
                        if (!TextUtils.isEmpty(activityAddAddressBinding.addadressHomenumber.getEditableText().toString())) {
                            addadress.setBuildingNo(activityAddAddressBinding.addadressHomenumber.getEditableText().toString());
                        }
                        if (!TextUtils.isEmpty(activityAddAddressBinding.addadressFlat.getEditableText().toString())) {
                            addadress.setFlatNo(activityAddAddressBinding.addadressFlat.getEditableText().toString());
                        }
                        if (!TextUtils.isEmpty(activityAddAddressBinding.addadressRoad.getEditableText().toString())) {
                            addadress.setRoadNo(activityAddAddressBinding.addadressRoad.getEditableText().toString());
                            if (!TextUtils.isEmpty(activityAddAddressBinding.addadressArea.getEditableText().toString())) {
                                addadress.setUserVillage(activityAddAddressBinding.addadressArea.getEditableText().toString());
                                if (!TextUtils.isEmpty(activityAddAddressBinding.addadressName.getEditableText().toString())) {
                                    addadress.setReceiverName(activityAddAddressBinding.addadressName.getEditableText().toString());
                                    if (!TextUtils.isEmpty(activityAddAddressBinding.addadressMbl.getEditableText().toString()) && utility.validateMsisdn(activityAddAddressBinding.addadressMbl.getEditableText().toString())) {
                                        addadress.setReceiverPhone(activityAddAddressBinding.addadressMbl.getEditableText().toString());
                                        if (send_updateaddress != null) {
                                            addadress.setIsPrimary(prime);
                                            addadress.setUserAddress(activityAddAddressBinding.addadressArea.getEditableText().toString());
                                            send_updateaddress.setReceiverName(addadress.getReceiverName());
                                            send_updateaddress.setReceiverPhone(addadress.getReceiverPhone());
                                            send_updateaddress.setUserAddress(addadress.getUserAddress());
                                            send_updateaddress.setRoadNo(addadress.getRoadNo());
                                            send_updateaddress.setBuildingNo(addadress.getBuildingNo());
                                            send_updateaddress.setFlatNo(addadress.getFlatNo());
                                            send_updateaddress.setUserVillage(addadress.getUserVillage());
                                            send_updateaddress.setIsPrimary(addadress.getIsPrimary());
                                            Update_address(send_updateaddress);
                                        } else {
                                            UPAZILLA_TABLE upo_data = (UPAZILLA_TABLE) activityAddAddressBinding.addressUpozilla.getSelectedItem();
                                            if (upo_data != null && !TextUtils.isEmpty(upo_data.getUpazillaId())) {
                                                addadress.setUpazillaId(upo_data.getUpazillaId());
                                                addadress.setIsPrimary(prime);
                                                addadress.setUserAddress(activityAddAddressBinding.addadressArea.getEditableText().toString());
                                                Add_address(addadress);
                                            } else {
                                                activityAddAddressBinding.addressUpozilla.setError(getResources().getString(R.string.upozilla_string));
                                                activityAddAddressBinding.addressUpozilla.requestFocusFromTouch();
                                            }
                                        }
                                    } else {
                                        activityAddAddressBinding.addadressMbl.setError(context.getResources().getString(R.string.seller_mbl_string));
                                    }
                                } else {
                                    activityAddAddressBinding.addadressName.setError(context.getResources().getString(R.string.request_name_string));
                                }
                            } else {
                                activityAddAddressBinding.addadressArea.setError(context.getResources().getString(R.string.address_area_string));
                            }
                        } else {
                            activityAddAddressBinding.addadressRoad.setError(context.getResources().getString(R.string.address_road_string));
                        }
                    }
                }
            });
            activityAddAddressBinding.addadressPrimary.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    prime = "YES";
                    activityAddAddressBinding.addadressNormal.setTextColor(context.getResources().getColor(R.color.app_hint2));
                    activityAddAddressBinding.addadressNormal.setCompoundDrawablesWithIntrinsicBounds(R.drawable.ic_location_black, 0, 0, 0);
                    activityAddAddressBinding.addadressPrimary.setTextColor(context.getResources().getColor(R.color.app_red));
                    activityAddAddressBinding.addadressPrimary.setCompoundDrawablesWithIntrinsicBounds(R.drawable.ic_home_red, 0, 0, 0);
                }
            });
            activityAddAddressBinding.addadressNormal.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    prime = "NO";
                    activityAddAddressBinding.addadressNormal.setTextColor(context.getResources().getColor(R.color.app_red));
                    activityAddAddressBinding.addadressNormal.setCompoundDrawablesWithIntrinsicBounds(R.drawable.ic_location_red, 0, 0, 0);
                    activityAddAddressBinding.addadressPrimary.setTextColor(context.getResources().getColor(R.color.app_hint2));
                    activityAddAddressBinding.addadressPrimary.setCompoundDrawablesWithIntrinsicBounds(R.drawable.ic_home_black, 0, 0, 0);
                }
            });

            spinner_work();

            String address_edit = getIntent().getStringExtra("edit_address");
            if (address_edit != null) {
                updatework(address_edit);
            }
        } catch (Exception e) {
            Log.d("Error Line Number", Log.getStackTraceString(e));
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        onBackPressed();
        return true;
    }

    private void Add_address(SEND_ADDADRESS send_addadress) {
        try {
            if (utility.isNetworkAvailable()) {
                utility.logger("Send Address" + send_addadress.toString());
                utility.showProgress(false, getResources().getString(R.string.wait_string));
                Call<API_RESPONSE> call = apiInterface.FishBAngla_add_address(utility.getApiKey(), utility.getUSER_ID(), utility.getFirebaseToken(), send_addadress);
                call.enqueue(new Callback<API_RESPONSE>() {
                    @Override
                    public void onResponse(Call<API_RESPONSE> call, Response<API_RESPONSE> response) {
                        try {
                            utility.hideProgress();
                            Log.d("Send Address", response.toString());
                            if (response.isSuccessful() && response != null) {
                                API_RESPONSE api_response = response.body();
                                if (api_response.getCode() == 200) {
                                    Dioalog_Yes(context.getResources().getString(R.string.address_add_success_string));
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

    public void spinner_work() {
        divisionRepository = new DIVISION_REPOSITORY(getApplication());
        districtRepository = new DISTRICT_REPOSITORY(getApplication());
        upozillaRepository = new UPOZILLA_REPOSITORY(getApplication());
        String[] ITEMS = {};
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, ITEMS);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        activityAddAddressBinding.addressDivision.setAdapter(adapter);
        activityAddAddressBinding.addressZilla.setAdapter(adapter);
        activityAddAddressBinding.addressUpozilla.setAdapter(adapter);
        division_adpater();
    }

    public void division_adpater() {
        try {
            division_tables = divisionRepository.getdivisionData();
            ArrayAdapter<DIVISION_TABLE> division_adapter = new ArrayAdapter<DIVISION_TABLE>(this, android.R.layout.simple_spinner_item, division_tables);
            division_adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            activityAddAddressBinding.addressDivision.setAdapter(division_adapter);
            activityAddAddressBinding.addressDivision.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                @Override
                public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                    DIVISION_TABLE selectedItem = (DIVISION_TABLE) parent.getSelectedItem();
                    district_adapter(selectedItem);
                    utility.hideKeyboard(view);
                }

                @Override
                public void onNothingSelected(AdapterView<?> parent) {

                }
            });
        } catch (Exception e) {
            Log.d("Error Line Number", Log.getStackTraceString(e));
        }
    }

    public void district_adapter(DIVISION_TABLE district_table) {
        try {
            if (district_table != null) {
                districtTables = districtRepository.finddistrictData(district_table.getDivisionId());
                ArrayAdapter<DISTRICT_TABLE> district_adapter = new ArrayAdapter<DISTRICT_TABLE>(this, android.R.layout.simple_spinner_item, districtTables);
                district_adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                activityAddAddressBinding.addressZilla.setAdapter(district_adapter);
                activityAddAddressBinding.addressZilla.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                    @Override
                    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                        DISTRICT_TABLE selectedItem = (DISTRICT_TABLE) parent.getSelectedItem();
                        upozilla_adapter(selectedItem);
                    }

                    @Override
                    public void onNothingSelected(AdapterView<?> parent) {

                    }
                });
            }
        } catch (Exception e) {
            Log.d("Error Line Number", Log.getStackTraceString(e));
        }
    }

    public void upozilla_adapter(DISTRICT_TABLE upazilla_table) {
        try {
            if (upazilla_table != null) {
                upazillaTables = upozillaRepository.findupazillaData(upazilla_table.getDistrictId());
                ArrayAdapter<UPAZILLA_TABLE> upazilla_adapter = new ArrayAdapter<UPAZILLA_TABLE>(this, android.R.layout.simple_spinner_item, upazillaTables);
                upazilla_adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                activityAddAddressBinding.addressUpozilla.setAdapter(upazilla_adapter);
                activityAddAddressBinding.addressUpozilla.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                    @Override
                    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                        UPAZILLA_TABLE selectedItem = (UPAZILLA_TABLE) parent.getSelectedItem();

                    }

                    @Override
                    public void onNothingSelected(AdapterView<?> parent) {

                    }
                });
            }
        } catch (Exception e) {
            Log.d("Error Line Number", Log.getStackTraceString(e));
        }
    }

    private void Update_address(SEND_UPDATEADDRESS send_addadress) {
        try {
            if (utility.isNetworkAvailable()) {
                utility.logger("Update Address" + send_addadress.toString());
                utility.showProgress(false, getResources().getString(R.string.wait_string));
                Call<API_RESPONSE> call = apiInterface.FishBAngla_update_address(utility.getApiKey(), utility.getUSER_ID(), utility.getFirebaseToken(), send_addadress);
                call.enqueue(new Callback<API_RESPONSE>() {
                    @Override
                    public void onResponse(Call<API_RESPONSE> call, Response<API_RESPONSE> response) {
                        try {
                            utility.hideProgress();
                            Log.d("Update Address", response.toString());
                            if (response.isSuccessful() && response != null) {
                                API_RESPONSE api_response = response.body();
                                if (api_response.getCode() == 200) {
                                    utility.logger("update"+api_response.getData().toString());
                                    Dioalog_Yes(context.getResources().getString(R.string.address_add_success_string));
                                    /*Intent in = new Intent("address_update");
                                    //in.putExtra("category", category);
                                    sendBroadcast(in);
                                    finish();*/
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

    public void updatework(String s) {
        Gson gson = new Gson();
        send_updateaddress = gson.fromJson(s, SEND_UPDATEADDRESS.class);
        if (send_updateaddress != null) {
            activityAddAddressBinding.addadressHomenumber.setText(send_updateaddress.getBuildingNo());
            activityAddAddressBinding.addadressFlat.setText(send_updateaddress.getFlatNo());
            activityAddAddressBinding.addadressRoad.setText(send_updateaddress.getRoadNo());
            activityAddAddressBinding.addadressArea.setText(send_updateaddress.getUserVillage());
            activityAddAddressBinding.addadressName.setText(send_updateaddress.getReceiverName());
            activityAddAddressBinding.addadressMbl.setText(utility.Mobile_Number_Read(send_updateaddress.getReceiverPhone()));
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
                    utility.logger("update address");
                    Intent in = new Intent("address_update");
                    //in.putExtra("category", category);
                    sendBroadcast(in);
                    finish();
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
}