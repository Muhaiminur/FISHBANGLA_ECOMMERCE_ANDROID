package com.gtech.fishbangla.Activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Point;
import android.net.Uri;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.text.TextUtils;
import android.util.Log;
import android.view.Display;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.analytics.FirebaseAnalytics;
import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.iid.InstanceIdResult;
import com.google.gson.Gson;
import com.gtech.fishbangla.DATABASE.REPOSITORY.CONFIG_REPOSITORY;
import com.gtech.fishbangla.DATABASE.REPOSITORY.DISTRICT_REPOSITORY;
import com.gtech.fishbangla.DATABASE.REPOSITORY.DIVISION_REPOSITORY;
import com.gtech.fishbangla.DATABASE.REPOSITORY.UPOZILLA_REPOSITORY;
import com.gtech.fishbangla.DATABASE.TABLE.CONFIG_TABLE;
import com.gtech.fishbangla.Http.ApiClient;
import com.gtech.fishbangla.Http.ApiInterface;
import com.gtech.fishbangla.Library.KeyWord;
import com.gtech.fishbangla.Library.Utility;
import com.gtech.fishbangla.MODEL.ADDRESS.Get_Address_List;
import com.gtech.fishbangla.MODEL.API_RESPONSE;
import com.gtech.fishbangla.MODEL.Config.Config_Data;
import com.gtech.fishbangla.R;

import java.net.Inet4Address;
import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.SocketException;
import java.util.Enumeration;
import java.util.HashMap;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class Landing_Page extends AppCompatActivity {
    Utility utility = new Utility(this);
    ApiInterface apiInterface = ApiClient.getBaseClient().create(ApiInterface.class);
    Context context;
    Config_Data config_module;
    Gson gson;

    //database
    CONFIG_REPOSITORY config_repository;
    DIVISION_REPOSITORY divisionRepository;
    DISTRICT_REPOSITORY districtRepository;
    UPOZILLA_REPOSITORY upozilla_repository;

    //analytics
    private FirebaseAnalytics mFirebaseAnalytics;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        context = Landing_Page.this;
        gson = new Gson();
        config_repository = new CONFIG_REPOSITORY(getApplication());
        divisionRepository = new DIVISION_REPOSITORY(getApplication());
        districtRepository = new DISTRICT_REPOSITORY(getApplication());
        upozilla_repository = new UPOZILLA_REPOSITORY(getApplication());
        mFirebaseAnalytics = FirebaseAnalytics.getInstance(this);
        //TextView t = findViewById(R.id.text);
        //t.setText(getAuthorizationKey());

        /*AppSignatureHelper appSignatureHelper = new AppSignatureHelper(this);
        for (String signature :
                appSignatureHelper.getAppSignatures()) {
            Log.e("APPSignature", signature + "\n");
        }*/

        if (TextUtils.isEmpty(utility.getFirebaseToken())) {
            FirebaseInstanceId.getInstance().getInstanceId()
                    .addOnCompleteListener(new OnCompleteListener<InstanceIdResult>() {
                        @Override
                        public void onComplete(@NonNull Task<InstanceIdResult> task) {
                            if (!task.isSuccessful()) {
                                utility.logger("FIREBASE TOKEN FAILED");
                                return;
                            }
                            String token = task.getResult().getToken();
                            utility.setFirebaseToken(token);
                        }
                    });
        }
        try {
            new CountDownTimer(5000, 1000) {

                @Override
                public void onTick(long l) {

                }

                @Override
                public void onFinish() {
                    if (utility.isNetworkAvailable()) {
                        //callNextActivity();
                        getConfiguration();
                        Get_address();
                    } else {
                        utility.showDialog(getResources().getString(R.string.no_internet_string));
                    }
                }
            }.start();
        } catch (Exception e) {
            Log.d("Error Line Number", Log.getStackTraceString(e));
        }

    }
    private void callNextActivity() {
        try {

            Bundle bundle = new Bundle();
            bundle.putString(FirebaseAnalytics.Param.ITEM_ID, "001");
            bundle.putString(FirebaseAnalytics.Param.ITEM_NAME, "LANDING PAGE");
            bundle.putString(FirebaseAnalytics.Param.CONTENT_TYPE, "LANDING PAGE");
            mFirebaseAnalytics.logEvent(FirebaseAnalytics.Event.SELECT_CONTENT, bundle);

            //utility.logger("Version code" + utility.getAppVersionCode());
            //utility.logger("Api version code" + Integer.parseInt(config_module.getVERSIONCODE()));
            if (Integer.parseInt(config_module.getVERSIONCODE()) > utility.getAppVersionCode()) {
                showVersionDialog(config_module.getUPDATETYPE());
            } else {
                startActivity(new Intent(Landing_Page.this, Home_Page.class));
                finish();
            }
        /*startActivity(new Intent(Landing_Page.this, Home_Page.class));
        finish();*/
        } catch (Exception e) {
            Log.d("Error Line Number", Log.getStackTraceString(e));
        }
    }


    private void getConfiguration() {
        try {
            Call<API_RESPONSE> call = apiInterface.FishBAngla_Config(utility.getApiKey(), utility.getUSER_ID(), utility.getFirebaseToken());
            utility.logger(call.request().headers().size() + "");
            call.enqueue(new Callback<API_RESPONSE>() {
                @Override
                public void onResponse(Call<API_RESPONSE> call, Response<API_RESPONSE> response) {
                    try {
                        Log.d("GET CONFIG", response.toString());
                        if (response.isSuccessful() && response != null) {
                            API_RESPONSE api_response = response.body();
                            if (api_response.getCode() == 200) {

                                config_module = gson.fromJson(api_response.getData().toString(), Config_Data.class);
                                if (config_module != null && config_repository != null) {
                                    CONFIG_TABLE config_table = new CONFIG_TABLE();
                                    config_table.setDelivery_charge(config_module.getDELIVERYCHARGE());
                                    config_table.setService_charge(config_module.getSERVICECHARGE());
                                    config_table.setVat_charge(config_module.getVAT());
                                    config_table.setFrozen_type(config_module.getFROZEN());
                                    config_table.setDelivery_charge(config_module.getDELIVERYCHARGE());
                                    config_table.setReferrer(config_module.getREFERRER());
                                    config_table.setFishbangla_address(config_module.getFISHADDRESS());
                                    utility.logger("Before CONFIG SAVE" + config_table.toString());
                                    config_repository.delete_config();
                                    config_repository.insert_config_repo(config_table);
                                    if (config_module.getFeedback().size() > 0) {
                                        utility.setFeedback(gson.toJson(config_module.getFeedback()));
                                    }
                                    utility.setNotificationdata(config_module.getUnreadNotification());
                                    /*List<Feedback> cars = new ArrayList<Feedback>();
                                    cars.add(new Feedback("1", "CHECK ONE"));
                                    cars.add(new Feedback("1", "CHECK TWO"));
                                    cars.add(new Feedback("1", "CHECK Three"));
                                    utility.setFeedback(gson.toJson(cars));*/
                                    //
                                    //utility.logger("After CONFIG SAVE" + config_repository.getconfigData().toString());
                                }
                                callNextActivity();
                            } else {
                                showConfirmation();
                            }
                        } else {
                            showConfirmation();
                        }
                    } catch (Exception ex) {
                        utility.logger(ex.toString());
                    }
                }

                @Override
                public void onFailure(Call<API_RESPONSE> call, Throwable t) {
                    utility.logger(t.toString());
                    showConfirmation();
                }
            });
        } catch (Exception e) {
            Log.d("Error Line Number", Log.getStackTraceString(e));
        }
    }

    public void showVersionDialog(String update) {
        try {
            final Dialog dialog = new Dialog(context);
            dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
            dialog.setContentView(R.layout.dialog_update);
            dialog.getWindow().setBackgroundDrawableResource(android.R.color.transparent);
            LinearLayout layout = dialog.findViewById(R.id.version_update_layout);
            TextView titile = dialog.findViewById(R.id.message_title);
            TextView textView = dialog.findViewById(R.id.message_body);
            Button yesBtn = dialog.findViewById(R.id.btn_yes);
            Button noBtn = dialog.findViewById(R.id.btn_no);
            utility.setFonts(new View[]{titile, textView, yesBtn, noBtn});
            if (update.equalsIgnoreCase("force")) {
                noBtn.setText(getResources().getString(R.string.exitcon_string));
                noBtn.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        dialog.dismiss();
                        exitApplication();
                    }
                });
                yesBtn.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        dialog.dismiss();
                        updatePage();
                    }
                });
            } else {
                noBtn.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        dialog.dismiss();
                        startActivity(new Intent(Landing_Page.this, Home_Page.class));
                        finish();
                    }
                });
                yesBtn.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        dialog.dismiss();
                        updatePage();
                    }
                });
            }
            utility.setSize(layout.getLayoutParams(), 7, 0);
            dialog.setCancelable(false);
            dialog.show();
        } catch (Exception e) {
            Log.d("Error Line Number", Log.getStackTraceString(e));
        }
    }

    public void updatePage() {
        if (utility.isNetworkAvailable()) {
            Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(getString(R.string.play_store_link) + getPackageName()));
            startActivity(intent);
        } else {
            utility.showDialog(getResources().getString(R.string.no_internet_string));
        }
        finish();
        System.exit(0);
    }

    public void exitApplication() {
        android.os.Process.killProcess(android.os.Process.myPid());
        System.exit(1);
    }

    private void showConfirmation() {
        try {
            Display display = getWindowManager().getDefaultDisplay();
            Point size = new Point();
            display.getSize(size);
            HashMap<String, Integer> screen = utility.getScreenRes();
            int width = screen.get(KeyWord.SCREEN_WIDTH);
            int height = screen.get(KeyWord.SCREEN_HEIGHT);
            int mywidth = (width / 10) * 7;
            final Dialog dialog = new Dialog(this);
            dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
            dialog.getWindow().setBackgroundDrawableResource(android.R.color.transparent);
            dialog.setContentView(R.layout.dialog_toast_yes_no);
            TextView tv = dialog.findViewById(R.id.tv_message);
            Button yes = dialog.findViewById(R.id.btn_yes);
            Button no = dialog.findViewById(R.id.btn_no);
            no.setVisibility(View.GONE);
            tv.setText(getResources().getString(R.string.try_again_string));
            LinearLayout ll = dialog.findViewById(R.id.dialog_layout_size);
            LinearLayout.LayoutParams params = (LinearLayout.LayoutParams) ll.getLayoutParams();
            params.height = LinearLayout.LayoutParams.WRAP_CONTENT;
            params.width = mywidth;
            ll.setLayoutParams(params);
            yes.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    dialog.dismiss();
                    exitApplication();
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

    private void Get_address() {
        try {
            if (utility.isNetworkAvailable()) {
                Call<API_RESPONSE> call = apiInterface.FishBAngla_Address_Database(utility.getApiKey(), utility.getUSER_ID(), utility.getFirebaseToken());
                call.enqueue(new Callback<API_RESPONSE>() {
                    @Override
                    public void onResponse(Call<API_RESPONSE> call, Response<API_RESPONSE> response) {
                        try {
                            Log.d("GET ADDRESS", response.toString());
                            if (response.isSuccessful() && response != null) {
                                API_RESPONSE api_response = response.body();
                                if (api_response.getCode() == 200) {
                                    Get_Address_List get_address_list = gson.fromJson(api_response.getData().toString(), Get_Address_List.class);
                                    Log.d("GET ADDRESS", get_address_list.getDistrict().size() + "");
                                    if (get_address_list.getDivision().size() > 0) {
                                        divisionRepository.delete_division();
                                        divisionRepository.insert_division(get_address_list.getDivision());
                                    }
                                    if (get_address_list.getDistrict().size() > 0) {
                                        districtRepository.delete_district();
                                        districtRepository.insert_district(get_address_list.getDistrict());
                                    }
                                    if (get_address_list.getUpazilla().size() > 0) {
                                        upozilla_repository.delete_upozilla();
                                        upozilla_repository.insert_upozilla(get_address_list.getUpazilla());
                                    }
                                } else {
                                    utility.showDialog(getResources().getString(R.string.login_invalid_string));
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
