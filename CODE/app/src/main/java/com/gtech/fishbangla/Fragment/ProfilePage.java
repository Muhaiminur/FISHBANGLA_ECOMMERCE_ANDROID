package com.gtech.fishbangla.Fragment;


import android.app.Dialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Point;
import android.net.Uri;
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

import com.bumptech.glide.Glide;
import com.google.android.material.textfield.TextInputEditText;
import com.google.gson.Gson;
import com.gtech.fishbangla.Activity.IMAGE_CROP;
import com.gtech.fishbangla.DATABASE.REPOSITORY.CART_REPOSITORY;
import com.gtech.fishbangla.DATABASE.REPOSITORY.USER_REPOSITORY;
import com.gtech.fishbangla.DATABASE.TABLE.USER_PROFILE;
import com.gtech.fishbangla.Http.ApiClient;
import com.gtech.fishbangla.Http.ApiInterface;
import com.gtech.fishbangla.Library.KeyWord;
import com.gtech.fishbangla.Library.Utility;
import com.gtech.fishbangla.MODEL.API_RESPONSE;
import com.gtech.fishbangla.MODEL.User.Get_UserLogin;
import com.gtech.fishbangla.MODEL.User.Send_Pin_Change;
import com.gtech.fishbangla.MODEL.User.Send_User_Update;
import com.gtech.fishbangla.R;
import com.gtech.fishbangla.databinding.FragmentProfilePageBinding;
import com.nguyenhoanglam.imagepicker.model.Config;
import com.nguyenhoanglam.imagepicker.model.Image;
import com.nguyenhoanglam.imagepicker.ui.imagepicker.ImagePicker;
import com.poovam.pinedittextfield.CirclePinField;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.util.ArrayList;
import java.util.HashMap;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static android.app.Activity.RESULT_OK;

/**
 * A simple {@link Fragment} subclass.
 */
public class ProfilePage extends Fragment {
    FragmentProfilePageBinding profilePageBinding;
    Context context;
    Utility utility;
    USER_REPOSITORY user_dao;
    CART_REPOSITORY cart_repository;
    ApiInterface apiInterface = ApiClient.getBaseClient().create(ApiInterface.class);
    USER_PROFILE userProfile;
    Image profile_image;

    public ProfilePage() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        profilePageBinding = DataBindingUtil.inflate(inflater, R.layout.fragment_profile_page, container, false);
        try {
            context = getActivity();
            utility = new Utility(context);
            user_dao = new USER_REPOSITORY(getActivity().getApplication());
            cart_repository = new CART_REPOSITORY(getActivity().getApplication());
            userProfile = user_dao.getuserData();
            if (userProfile != null) {
                if (!TextUtils.isEmpty(userProfile.getUserImage())) {
                    utility.logger("IMAGE" + userProfile.getUserImage());
                    Glide.with(context).load(userProfile.getUserImage()).apply(utility.Glide_Cache_Off()).into(profilePageBinding.userProfileImage);
                }
                if (!TextUtils.isEmpty(userProfile.getUserFullName())) {
                    profilePageBinding.profileName.setText(userProfile.getUserFullName());
                }
                if (!TextUtils.isEmpty(userProfile.getUserPhoneNo())) {
                    profilePageBinding.profilePhone.setText(utility.Mobile_Number_Read(utility.convertToBangle(userProfile.getUserPhoneNo())));
                }
                if (!TextUtils.isEmpty(userProfile.getUserEmail())) {
                    profilePageBinding.profileEmail.setText(userProfile.getUserEmail());
                }
                if (!TextUtils.isEmpty(userProfile.getUserRatting())) {
                    profilePageBinding.profileRating.setRating(Float.valueOf(userProfile.getUserRatting()));
                }

            }
            profilePageBinding.notificationCount.setText(utility.convertToBangle(utility.getNotificationdata()));
            profilePageBinding.profileOwnDetails.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Fragment navhost = ((AppCompatActivity) context).getSupportFragmentManager().findFragmentById(R.id.nav_host_fragment);
                    NavController c = NavHostFragment.findNavController(navhost);
                    //c.popBackStack();
                    c.navigate(R.id.frag_addresslist);
                }
            });
            profilePageBinding.profileBuyHistory.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Fragment navhost = ((AppCompatActivity) context).getSupportFragmentManager().findFragmentById(R.id.nav_host_fragment);
                    NavController c = NavHostFragment.findNavController(navhost);
                    //c.popBackStack();
                    c.navigate(R.id.nav_buy);
                }
            });
            profilePageBinding.profileSellHistory.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Fragment navhost = ((AppCompatActivity) context).getSupportFragmentManager().findFragmentById(R.id.nav_host_fragment);
                    NavController c = NavHostFragment.findNavController(navhost);
                    //c.popBackStack();
                    c.navigate(R.id.nav_sell);
                }
            });
            profilePageBinding.profileReferel.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    utility.showDialog(context.getResources().getString(R.string.no_data_string));
                }
            });
            profilePageBinding.profilePinchange.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    showPinCHnageDialouge();
                }
            });
            profilePageBinding.profileWishlist.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Fragment navhost = ((AppCompatActivity) context).getSupportFragmentManager().findFragmentById(R.id.nav_host_fragment);
                    NavController c = NavHostFragment.findNavController(navhost);
                    //c.popBackStack();
                    c.navigate(R.id.frag_fvrt_list);
                }
            });
            profilePageBinding.profileOwnproduct.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Fragment navhost = ((AppCompatActivity) context).getSupportFragmentManager().findFragmentById(R.id.nav_host_fragment);
                    NavController c = NavHostFragment.findNavController(navhost);
                    //c.popBackStack();
                    c.navigate(R.id.nav_Own_product);
                }
            });
            profilePageBinding.profileNotification.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Fragment navhost = ((AppCompatActivity) context).getSupportFragmentManager().findFragmentById(R.id.nav_host_fragment);
                    NavController c = NavHostFragment.findNavController(navhost);
                    //c.popBackStack();
                    c.navigate(R.id.frag_notification);
                }
            });


            profilePageBinding.profileLogout.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (userProfile != null) {
                        logout();
                    }
                }
            });
            profilePageBinding.profileDetailsEdit.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (userProfile != null) {
                        profile_change();
                    }
                }
            });
            profilePageBinding.profileImageEdit.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    try {
                        ImagePicker.with(ProfilePage.this)                         //  Initialize ImagePicker with activity or fragment context
                                .setToolbarColor("#ff0e0e")         //  Toolbar color
                                .setStatusBarColor("#000000")       //  StatusBar color (works with SDK >= 21  )
                                .setToolbarTextColor("#FFFFFF")     //  Toolbar text color (Title and Done button)
                                .setToolbarIconColor("#FFFFFF")     //  Toolbar icon color (Back and Camera button)
                                .setProgressBarColor("#4CAF50")     //  ProgressBar color
                                .setBackgroundColor("#212121")      //  Background color
                                .setCameraOnly(false)               //  Camera mode
                                .setMultipleMode(false)              //  Select multiple images or single image
                                .setFolderMode(false)                //  Folder mode
                                .setShowCamera(true)                //  Show camera button
                                .setFolderTitle(getResources().getString(R.string.app_name))           //  Folder title (works with FolderMode = true)
                                .setImageTitle(getResources().getString(R.string.add_image_string))         //  Image title (works with FolderMode = false)
                                .setDoneTitle("Done")               //  Done button title
                                .setLimitMessage("You have reached selection limit")    // Selection limit message
                                .setMaxSize(1)                     //  Max images can be selected
                                .setSavePath(getResources().getString(R.string.app_name))         //  Image capture folder name
                                .setAlwaysShowDoneButton(true)      //  Set always show done button in multiple mode
                                .setRequestCode(Config.RC_PICK_IMAGES)                //  Set request code, default Config.RC_PICK_IMAGES
                                .setKeepScreenOn(true)              //  Keep screen on when selecting images
                                .start();
                    } catch (Exception e) {
                        Log.d("Error Line Number", Log.getStackTraceString(e));
                    }
                }
            });


        } catch (Exception e) {
            Log.d("Error Line Number", Log.getStackTraceString(e));
        }

        return profilePageBinding.getRoot();
    }

    public void showPinCHnageDialouge() {
        try {
            HashMap<String, Integer> screen = utility.getScreenRes();
            int width = screen.get(KeyWord.SCREEN_WIDTH);
            int height = screen.get(KeyWord.SCREEN_HEIGHT);
            int mywidth = (width / 10) * 9;
            final Dialog dialog = new Dialog(context);
            dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
            dialog.getWindow().setBackgroundDrawableResource(android.R.color.transparent);
            dialog.setContentView(R.layout.dialog_pin_change);
            Button yes = dialog.findViewById(R.id.pinchangeadd);
            Button no = dialog.findViewById(R.id.pinchangecancel);
            CirclePinField old_pin = dialog.findViewById(R.id.pin_change_old);
            CirclePinField new_pin = dialog.findViewById(R.id.pin_change_new);
            LinearLayout ll = dialog.findViewById(R.id.dialog_layout_size);
            LinearLayout.LayoutParams params = (LinearLayout.LayoutParams) ll.getLayoutParams();
            params.height = LinearLayout.LayoutParams.WRAP_CONTENT;
            params.width = mywidth;
            ll.setLayoutParams(params);
            yes.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    dialog.dismiss();
                    try {
                        if (!TextUtils.isEmpty(old_pin.getEditableText().toString()) && !TextUtils.isEmpty(new_pin.getEditableText().toString())) {
                            Pin_Change(new Send_Pin_Change(userProfile.getUserId(), old_pin.getEditableText().toString(), new_pin.getEditableText().toString()));
                        } else {
                            utility.showDialog(getResources().getString(R.string.validation_string));
                        }

                    } catch (Exception e) {
                        Log.d("Error Line Number", Log.getStackTraceString(e));
                    }
                }
            });
            no.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    dialog.dismiss();
                }
            });
            dialog.setCancelable(true);
            dialog.show();
        } catch (Exception e) {
            Log.d("Error Line Number", Log.getStackTraceString(e));
        }
    }


    private void Pin_Change(Send_Pin_Change send_pin_change) {
        try {
            if (utility.isNetworkAvailable()) {
                Log.d("PIN CHANGE", send_pin_change.toString());
                utility.showProgress(false, getResources().getString(R.string.wait_string));
                Call<API_RESPONSE> call = apiInterface.FishBAngla_Pin_Change(utility.getApiKey(), utility.getUSER_ID(), utility.getFirebaseToken(), send_pin_change);
                call.enqueue(new Callback<API_RESPONSE>() {
                    @Override
                    public void onResponse(Call<API_RESPONSE> call, Response<API_RESPONSE> response) {
                        try {
                            utility.hideProgress();
                            Log.d("PIN CHANGE", response.toString());
                            if (response.isSuccessful() && response != null) {
                                API_RESPONSE api_response = response.body();
                                if (api_response.getCode() == 200) {
                                    utility.showDialog(getResources().getString(R.string.pin_change_success_string));
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

    public void logout() {
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
            tv.setText(getResources().getString(R.string.exit_string));
            LinearLayout ll = dialog.findViewById(R.id.dialog_layout_size);
            LinearLayout.LayoutParams params = (LinearLayout.LayoutParams) ll.getLayoutParams();
            params.height = LinearLayout.LayoutParams.WRAP_CONTENT;
            params.width = mywidth;
            ll.setLayoutParams(params);
            yes.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    dialog.dismiss();
                    user_dao.deleteall();
                    utility.clearUSER_ID();
                    cart_repository.deleteall_cart_data();
                    Intent intent = getActivity().getIntent();
                    getActivity().finish();
                    startActivity(intent);
                }
            });
            no.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    dialog.dismiss();
                }
            });
            dialog.setCancelable(true);
            dialog.show();
        } catch (Exception e) {
            Log.d("Error Line Number", Log.getStackTraceString(e));
        }
    }


    public void profile_change() {
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
            dialog.setContentView(R.layout.dialog_user_update);
            TextInputEditText update_email = dialog.findViewById(R.id.user_update_email);
            TextInputEditText update_name = dialog.findViewById(R.id.user_update_name);
            Button update_yes = dialog.findViewById(R.id.btn_yes);
            Button update_no = dialog.findViewById(R.id.btn_no);
            if (userProfile != null) {
                update_name.setText(userProfile.getUserFullName());
                update_email.setText(userProfile.getUserEmail());
            }
            //tv.setText(getResources().getString(R.string.exit_string));
            LinearLayout ll = dialog.findViewById(R.id.dialog_layout_size);
            LinearLayout.LayoutParams params = (LinearLayout.LayoutParams) ll.getLayoutParams();
            params.height = LinearLayout.LayoutParams.WRAP_CONTENT;
            params.width = mywidth;
            ll.setLayoutParams(params);
            update_yes.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    dialog.dismiss();
                    if (!TextUtils.isEmpty(update_name.getEditableText().toString()) /*&& !TextUtils.isEmpty(update_email.getEditableText().toString())*/ && !TextUtils.isEmpty(userProfile.getUserId())) {
                        User_Update_Info(new Send_User_Update(userProfile.getUserId(), update_email.getEditableText().toString(), update_name.getEditableText().toString()));
                    } else {
                        utility.showDialog(getResources().getString(R.string.validation_string));
                    }
                }
            });
            update_no.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    dialog.dismiss();
                }
            });
            dialog.setCancelable(true);
            dialog.show();
        } catch (Exception e) {
            Log.d("Error Line Number", Log.getStackTraceString(e));
        }
    }

    private void User_Update_Info(Send_User_Update send_user_update) {
        try {
            if (utility.isNetworkAvailable()) {
                Log.d("profile update", send_user_update.toString());
                utility.showProgress(false, getResources().getString(R.string.wait_string));
                Call<API_RESPONSE> call = apiInterface.FishBAngla_Login_update(utility.getApiKey(), utility.getUSER_ID(), utility.getFirebaseToken(), send_user_update);
                call.enqueue(new Callback<API_RESPONSE>() {
                    @Override
                    public void onResponse(Call<API_RESPONSE> call, Response<API_RESPONSE> response) {
                        try {
                            utility.hideProgress();
                            Log.d("profile update", response.toString());
                            if (response.isSuccessful() && response != null) {
                                API_RESPONSE api_response = response.body();
                                if (api_response.getCode() == 200) {
                                    utility.showDialog(getResources().getString(R.string.profile_update_success_string));

                                    try {
                                        Gson gson = new Gson();
                                        Get_UserLogin userLogin = gson.fromJson(api_response.getData().toString(), Get_UserLogin.class);
                                        Log.d("user updated", userLogin.getUserPhoneNo());
                                        if (userLogin != null) {
                                            if (!TextUtils.isEmpty(userLogin.getUserImage())) {
                                                Glide.with(context).load(userLogin.getUserImage()).apply(utility.Glide_Cache_Off()).into(profilePageBinding.userProfileImage);
                                            }
                                            if (!TextUtils.isEmpty(userLogin.getUserFullName())) {
                                                profilePageBinding.profileName.setText(userLogin.getUserFullName());
                                            }
                                            if (!TextUtils.isEmpty(userLogin.getUserPhoneNo())) {
                                                profilePageBinding.profilePhone.setText(utility.Mobile_Number_Read(utility.convertToBangle(userLogin.getUserPhoneNo())));
                                            }
                                            if (!TextUtils.isEmpty(userLogin.getUserEmail())) {
                                                profilePageBinding.profileEmail.setText(userLogin.getUserEmail());
                                            }
                                            if (!TextUtils.isEmpty(userLogin.getAvgRating())) {
                                                profilePageBinding.profileRating.setRating(Float.valueOf(userLogin.getAvgRating()));
                                            }

                                        }
                                        if (userLogin != null && !TextUtils.isEmpty(userLogin.getUserId()) && user_dao != null) {
                                            user_dao.deleteall();
                                            USER_PROFILE userProfile = new USER_PROFILE();
                                            userProfile.setUserId(userLogin.getUserId());
                                            userProfile.setUserFullName(userLogin.getUserFullName());
                                            userProfile.setUserPhoneNo(userLogin.getUserPhoneNo());
                                            userProfile.setUserImage(userLogin.getUserImage());
                                            userProfile.setUserEmail(userLogin.getUserEmail());
                                            userProfile.setUserRatting(userLogin.getAvgRating());
                                            user_dao.insert_user(userProfile);
                                        }
                                    } catch (Exception e) {
                                        Log.d("Error Line Number", Log.getStackTraceString(e));
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

    public void Send_User_Image(String userid, File video) {
        try {
            if (utility.isNetworkAvailable()) {
                utility.showProgress(false, getResources().getString(R.string.wait_string));
                RequestBody requestFile = RequestBody.create(MediaType.parse("image/*"), video);
                // MultipartBody.Part is used to send also the actual file name
                MultipartBody.Part video_body = MultipartBody.Part.createFormData("image", video.getName(), requestFile);
                Call<API_RESPONSE> call = apiInterface.FishBAngla_User_Image_Upload(utility.getApiKey(), utility.getUSER_ID(), utility.getFirebaseToken(), userid, video_body);
                call.enqueue(new Callback<API_RESPONSE>() {
                    @Override
                    public void onResponse(Call<API_RESPONSE> call, Response<API_RESPONSE> response) {
                        try {
                            utility.hideProgress();
                            Log.d("Image Upload", response.toString());
                            if (response.isSuccessful() && response != null) {
                                API_RESPONSE api_response = response.body();
                                if (api_response.getCode() == 200) {
                                    utility.logger("Image Upload" + response.body().getData().getAsString());
                                    utility.showDialog(getResources().getString(R.string.profile_update_success_string));
                                    Glide.with(context).load(response.body().getData().getAsString()).apply(utility.Glide_Cache_Off()).into(profilePageBinding.userProfileImage);
                                    user_dao.update_user_Image(userProfile.getUserId(), response.body().getData().getAsString());
                                } else {
                                    utility.showToast(getResources().getString(R.string.try_again_string));
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

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == Config.RC_PICK_IMAGES && resultCode == RESULT_OK && data != null) {
            ArrayList<Image> images = data.getParcelableArrayListExtra(Config.EXTRA_IMAGES);
            // do your logic here...
            /*for (Image image : images) {
                utility.logger(image.getName());

            }*/
            if (images.get(0) != null) {
                Intent intent = new Intent(Intent.ACTION_VIEW);
                intent.setClass(context, IMAGE_CROP.class);
                intent.putExtra("image", Uri.fromFile(new File(images.get(0).getPath())).toString());
                startActivityForResult(intent, KeyWord.CROP_PERMISSION);


                //Send_User_Image(userProfile.getUserId(), new File(images.get(0).getPath()));
            } else {
                utility.showDialog(context.getResources().getString(R.string.try_again_string));
            }
            utility.logger("Image Work");
        } else if (requestCode == KeyWord.CROP_PERMISSION && resultCode == RESULT_OK) {
            try {
                Uri cropUri = Uri.parse(data.getStringExtra("data"));
                Bitmap bitmap = BitmapFactory.decodeStream(context.getContentResolver().openInputStream(cropUri));
                File file = new File(new File(context.getCacheDir().getAbsolutePath()), "temp.jpg");
                if (file.exists())
                    file.delete();
                try {
                    FileOutputStream out = new FileOutputStream(file);
                    bitmap.compress(Bitmap.CompressFormat.JPEG, 100, out);
                    out.flush();
                    out.close();
                } catch (Exception ex) {
                    utility.logger(ex.toString());
                }
                if (file.exists()) {
                    Send_User_Image(userProfile.getUserId(), file);
                }
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }
            utility.logger("Image CROP");
        }

        super.onActivityResult(requestCode, resultCode, data);  // You MUST have this line to be here
        // so ImagePicker can work with fragment mode
    }

    //from auction details
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
                profilePageBinding.notificationCount.setText(utility.convertToBangle(utility.getNotificationdata()));
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    };

}
