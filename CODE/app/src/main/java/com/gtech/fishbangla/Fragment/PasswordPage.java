package com.gtech.fishbangla.Fragment;


import android.content.ActivityNotFoundException;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;

import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.android.gms.auth.api.phone.SmsRetriever;
import com.google.android.gms.auth.api.phone.SmsRetrieverClient;
import com.google.android.gms.common.api.CommonStatusCodes;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.gson.Gson;
import com.gtech.fishbangla.Activity.Home_Page;
import com.gtech.fishbangla.DATABASE.REPOSITORY.USER_REPOSITORY;
import com.gtech.fishbangla.DATABASE.TABLE.USER_PROFILE;
import com.gtech.fishbangla.Http.ApiClient;
import com.gtech.fishbangla.Http.ApiInterface;
import com.gtech.fishbangla.Library.KeyWord;
import com.gtech.fishbangla.Library.Utility;
import com.gtech.fishbangla.MODEL.API_RESPONSE;
import com.gtech.fishbangla.MODEL.User.Get_UserLogin;
import com.gtech.fishbangla.MODEL.User.Send_Mbl;
import com.gtech.fishbangla.MODEL.User.Send_Pin;
import com.gtech.fishbangla.R;
import com.gtech.fishbangla.databinding.FragmentPasswordPageBinding;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static android.app.Activity.RESULT_OK;

/**
 * A simple {@link Fragment} subclass.
 */
public class PasswordPage extends Fragment {
    Context context;
    Utility utility;
    ApiInterface apiInterface = ApiClient.getBaseClient().create(ApiInterface.class);

    FragmentPasswordPageBinding passwordPageBinding;
    String mbl_number;

    //database
    USER_REPOSITORY user_repository;

    public PasswordPage() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        passwordPageBinding = DataBindingUtil.inflate(inflater, R.layout.fragment_password_page, container, false);
        try {
            context = getActivity();
            utility = new Utility(context);
            user_repository = new USER_REPOSITORY(getActivity().getApplication());
            mbl_number = getArguments().getString("mbl_number");
            utility.logger("Mobile Number" + mbl_number);
            passwordPageBinding.passwordSubmit.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    utility.hideKeyboard(passwordPageBinding.getRoot());
                    if (!TextUtils.isEmpty(mbl_number) && !TextUtils.isEmpty(passwordPageBinding.passwordInput.getEditableText().toString())) {
                        Send_Pin send_pin = new Send_Pin(mbl_number, passwordPageBinding.passwordInput.getEditableText().toString());
                        Login_verify(send_pin);
                    } else {
                        utility.showDialog(getResources().getString(R.string.validation_string));
                    }
                }
            });
            passwordPageBinding.passwordForget.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (!TextUtils.isEmpty(mbl_number)) {
                        Send_Mbl sendMbl = new Send_Mbl(mbl_number);
                        Pin_send(sendMbl);
                    } else {
                        utility.showDialog(getResources().getString(R.string.validation_string));
                    }
                }
            });

            sms_read();
        } catch (Exception e) {
            Log.d("Error Line Number", Log.getStackTraceString(e));
        }
        return passwordPageBinding.getRoot();
    }

    private void Login_verify(Send_Pin send_pin) {
        try {
            if (utility.isNetworkAvailable()) {
                Log.d("LOGIN Verify", send_pin.toString());
                utility.showProgress(false, getResources().getString(R.string.wait_string));
                Call<API_RESPONSE> call = apiInterface.FishBAngla_Login_Verify(utility.getApiKey(), utility.getUSER_ID(), utility.getFirebaseToken(), send_pin);
                call.enqueue(new Callback<API_RESPONSE>() {
                    @Override
                    public void onResponse(Call<API_RESPONSE> call, Response<API_RESPONSE> response) {
                        try {
                            utility.hideProgress();
                            Log.d("LOGIN verify", response.toString());
                            if (response.isSuccessful() && response != null) {
                                API_RESPONSE api_response = response.body();
                                if (api_response.getCode() == 200) {
                                    Gson gson = new Gson();
                                    Get_UserLogin userLogin = gson.fromJson(api_response.getData().toString(), Get_UserLogin.class);
                                    Log.d("user login", userLogin.getUserPhoneNo());
                                    if (userLogin != null && !TextUtils.isEmpty(userLogin.getUserId()) && user_repository != null) {
                                        user_repository.deleteall();
                                        USER_PROFILE userProfile = new USER_PROFILE();
                                        userProfile.setUserId(userLogin.getUserId());
                                        userProfile.setUserFullName(userLogin.getUserFullName());
                                        userProfile.setUserPhoneNo(userLogin.getUserPhoneNo());
                                        userProfile.setUserImage(userLogin.getUserImage());
                                        userProfile.setUserEmail(userLogin.getUserEmail());
                                        userProfile.setUserRatting(userLogin.getAvgRating());
                                        user_repository.insert_user(userProfile);
                                        //Log.d("from database", user_repository.getuserData().getUserId());
                                        /*Intent intent = getActivity().getIntent();
                                        getActivity().finish();
                                        startActivity(intent);*/
                                        utility.setUSER_ID(userLogin.getUserId());
                                        startActivity(new Intent(context, Home_Page.class));
                                        getActivity().finish();
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

    private void Pin_send(Send_Mbl send_mbl) {
        try {
            if (utility.isNetworkAvailable()) {
                Log.d("LOGIN FORGOT", send_mbl.toString());
                utility.showProgress(false, getResources().getString(R.string.wait_string));
                Call<API_RESPONSE> call = apiInterface.FishBAngla_Login_Forgot(utility.getApiKey(), utility.getUSER_ID(), utility.getFirebaseToken(), send_mbl);
                call.enqueue(new Callback<API_RESPONSE>() {
                    @Override
                    public void onResponse(Call<API_RESPONSE> call, Response<API_RESPONSE> response) {
                        try {
                            utility.hideProgress();
                            Log.d("LOGIN FORGOT", response.toString());
                            if (response.isSuccessful() && response != null) {
                                API_RESPONSE api_response = response.body();
                                if (api_response.getCode() == 200) {
                                    utility.showDialog(getResources().getString(R.string.send_otp_string));
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


    public void sms_read() {
        Task<Void> task = SmsRetriever.getClient(context).startSmsUserConsent(/*senderPhoneNumber*/  null);
        IntentFilter intentFilter = new IntentFilter(SmsRetriever.SMS_RETRIEVED_ACTION);
        context.registerReceiver(smsVerificationReceiver, intentFilter);
    }

    private static final int SMS_CONSENT_REQUEST = 2;  // Set to an unused request code
    private final BroadcastReceiver smsVerificationReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            if (SmsRetriever.SMS_RETRIEVED_ACTION.equals(intent.getAction())) {
                Bundle extras = intent.getExtras();
                Status smsRetrieverStatus = (Status) extras.get(SmsRetriever.EXTRA_STATUS);

                switch (smsRetrieverStatus.getStatusCode()) {
                    case CommonStatusCodes.SUCCESS:
                        // Get consent intent
                        Intent consentIntent = extras.getParcelable(SmsRetriever.EXTRA_CONSENT_INTENT);
                        try {
                            // Start activity to show consent dialog to user, activity must be started in
                            // 5 minutes, otherwise you'll receive another TIMEOUT intent
                            startActivityForResult(consentIntent, SMS_CONSENT_REQUEST);
                        } catch (ActivityNotFoundException e) {
                            // Handle the exception ...
                        }
                        break;
                    case CommonStatusCodes.TIMEOUT:
                        // Time out occurred, handle the error.
                        break;
                }
            }
        }
    };

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode) {
            // ...
            case SMS_CONSENT_REQUEST:
                if (resultCode == RESULT_OK) {
                    // Get SMS message content
                    String message = data.getStringExtra(SmsRetriever.EXTRA_SMS_MESSAGE);
                    // Extract one-time code from the message and complete verification
                    // `sms` contains the entire text of the SMS message, so you will need
                    // to parse the string.
                    //String oneTimeCode = parseOneTimeCode(message); // define this function
                    try {
                        if (!TextUtils.isEmpty(message)) {
                            String sms = message;
                            String[] arrayOfString = message.split("\\s+");
                            utility.logger("Message " + arrayOfString[4]);
                            passwordPageBinding.passwordInput.setText(arrayOfString[4]);
                        }
                        Log.d("SMS SEND", message);
                    } catch (Exception e) {
                        Log.d("Error Line Number", Log.getStackTraceString(e));
                    }
                } else {
                    // Consent canceled, handle the error ...
                }
                break;
        }
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        context.unregisterReceiver(smsVerificationReceiver);
    }
}
