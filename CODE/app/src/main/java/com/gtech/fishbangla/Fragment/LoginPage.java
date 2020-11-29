package com.gtech.fishbangla.Fragment;


import android.app.Dialog;
import android.content.Context;
import android.graphics.Point;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.fragment.NavHostFragment;

import android.text.TextUtils;
import android.util.Log;
import android.view.Display;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.gtech.fishbangla.Http.ApiClient;
import com.gtech.fishbangla.Http.ApiInterface;
import com.gtech.fishbangla.Library.KeyWord;
import com.gtech.fishbangla.Library.Utility;
import com.gtech.fishbangla.MODEL.API_RESPONSE;
import com.gtech.fishbangla.MODEL.User.Send_Mbl;
import com.gtech.fishbangla.R;
import com.gtech.fishbangla.databinding.FragmentLoginPageBinding;

import java.util.HashMap;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * A simple {@link Fragment} subclass.
 */
public class LoginPage extends Fragment {
    Context context;
    Utility utility;
    ApiInterface apiInterface = ApiClient.getBaseClient().create(ApiInterface.class);
    Send_Mbl send_mbl;
    FragmentLoginPageBinding loginPageBinding;

    public LoginPage() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        //view = inflater.inflate(R.layout.fragment_login_page, container, false);
        loginPageBinding = DataBindingUtil.inflate(inflater, R.layout.fragment_login_page, container, false);
        try {
            context = getActivity();
            utility = new Utility(context);
            loginPageBinding.loginSubmit.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    utility.hideKeyboard(loginPageBinding.getRoot());
                    if (!TextUtils.isEmpty(loginPageBinding.loginMobile.getText()) && utility.validateMsisdn(loginPageBinding.loginMobile.getText().toString())) {
                        send_mbl = new Send_Mbl(loginPageBinding.loginMobile.getText().toString());
                        Login_send(send_mbl);
                    } else {
                        utility.showDialog(getResources().getString(R.string.send_otp_validation_string));
                    }
                }
            });
        } catch (Exception e) {
            Log.d("Error Line Number", Log.getStackTraceString(e));
        }
        return loginPageBinding.getRoot();
    }

    private void Login_send(Send_Mbl send_mbl) {
        try {
            if (utility.isNetworkAvailable()) {
                Log.d("SEND LOGIN", send_mbl.toString());
                utility.showProgress(false, getResources().getString(R.string.wait_string));
                Call<API_RESPONSE> call = apiInterface.FishBAngla_Login(utility.getApiKey(), utility.getUSER_ID(), utility.getFirebaseToken(), send_mbl);
                call.enqueue(new Callback<API_RESPONSE>() {
                    @Override
                    public void onResponse(Call<API_RESPONSE> call, Response<API_RESPONSE> response) {
                        try {
                            utility.hideProgress();
                            Log.d("SEND LOGIN", response.toString());
                            if (response.isSuccessful() && response != null) {
                                API_RESPONSE api_response = response.body();
                                if (api_response.getCode() == 200) {
                                    if (api_response.getData().getAsString().equalsIgnoreCase("NEWUSER")) {
                                        Dioalog_Yes(getResources().getString(R.string.login_new_string));
                                    } else {
                                        Dioalog_Yes(getResources().getString(R.string.login_old_string));
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

    public void Dioalog_Yes(String msg) {
        try {
            Display display = getActivity().getWindowManager().getDefaultDisplay();
            Point size = new Point();
            display.getSize(size);
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
                    Bundle args = new Bundle();
                    args.putString("mbl_number", send_mbl.getPhone());
                    Fragment navhost = ((AppCompatActivity) context).getSupportFragmentManager().findFragmentById(R.id.nav_host_fragment);
                    NavController c = NavHostFragment.findNavController(navhost);
                    //for not back to previous fragment
                    c.popBackStack();
                    c.navigate(R.id.nav_password, args);
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
