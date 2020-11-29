package com.gtech.fishbangla.Adapter;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.browser.customtabs.CustomTabsIntent;
import androidx.cardview.widget.CardView;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.fragment.NavHostFragment;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.gtech.fishbangla.Activity.Cart_Activity;
import com.gtech.fishbangla.Http.ApiClient;
import com.gtech.fishbangla.Http.ApiInterface;
import com.gtech.fishbangla.Library.KeyWord;
import com.gtech.fishbangla.Library.Utility;
import com.gtech.fishbangla.MODEL.API_RESPONSE;
import com.gtech.fishbangla.MODEL.NOTIFICATION.GET_NOTIFICATION;
import com.gtech.fishbangla.MODEL.NOTIFICATION.SEND_NOTIFICATION;
import com.gtech.fishbangla.R;

import java.lang.reflect.Type;
import java.util.HashMap;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class NotificationAdapter extends RecyclerView.Adapter<NotificationAdapter.Todo_View_Holder> {
    Context context;
    List<GET_NOTIFICATION> notificationList;
    Utility utility;
    ApiInterface apiInterface = ApiClient.getBaseClient().create(ApiInterface.class);


    public NotificationAdapter(List<GET_NOTIFICATION> to, Context c) {
        notificationList = to;
        context = c;
        utility = new Utility(context);
    }

    public class Todo_View_Holder extends RecyclerView.ViewHolder {
        CardView notification_view;
        TextView notification_title;
        TextView notification_details;
        ImageView notification_image;
        Button notification_delete;

        public Todo_View_Holder(View view) {
            super(view);
            notification_view = view.findViewById(R.id.notification_view);
            notification_title = view.findViewById(R.id.notification_title);
            notification_details = view.findViewById(R.id.notification_details);
            notification_image = view.findViewById(R.id.notification_image);
            notification_delete = view.findViewById(R.id.notification_delete);
        }
    }

    @Override
    public Todo_View_Holder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.recycler_notification, parent, false);

        return new Todo_View_Holder(itemView);
    }

    @Override
    public void onBindViewHolder(final Todo_View_Holder holder, int position) {
        final GET_NOTIFICATION bodyResponse = notificationList.get(position);
        holder.notification_title.setText(bodyResponse.getTitle());
        holder.notification_details.setText(bodyResponse.getBody());
        if (!TextUtils.isEmpty(bodyResponse.getUrl())) {
            Glide.with(context).load(bodyResponse.getUrl()).apply(utility.Glide_Cache_On()).into(holder.notification_image);
        } else {
            holder.notification_image.setVisibility(View.GONE);
        }
        if (bodyResponse.getReadStatus().equalsIgnoreCase(KeyWord.YES)) {
            holder.notification_view.setCardBackgroundColor(context.getResources().getColor(R.color.app_hint2));
        } else {
            holder.notification_view.setCardBackgroundColor(context.getResources().getColor(R.color.app_white));
        }
        holder.notification_delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Dioalog_Yes(context.getResources().getString(R.string.notification_confirmation_string), bodyResponse.getNotificationId(), KeyWord.NOTIFICATION_REMOVE);
            }
        });
        holder.notification_view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Update_Notification(new SEND_NOTIFICATION(bodyResponse.getNotificationId(), KeyWord.NOTIFICATION_READ), bodyResponse, holder);
            }
        });
    }

    @Override
    public int getItemCount() {
        return notificationList.size();
    }

    public void Dioalog_Yes(String msg, String id, String op) {
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
                    Delete_Notification(new SEND_NOTIFICATION(id, op));
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

    private void Delete_Notification(SEND_NOTIFICATION s) {
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
                                    //send to notiifcation list page
                                    String s = api_response.getData().getAsString().toString();
                                    if (isInteger(s)) {
                                        utility.setNotificationdata(s);
                                    }
                                    Intent in = new Intent("notification_delete");
                                    context.sendBroadcast(in);
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

    private void Update_Notification(SEND_NOTIFICATION s, GET_NOTIFICATION get_notification, Todo_View_Holder holder) {
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
                                    notification_work(get_notification);
                                    holder.notification_view.setCardBackgroundColor(context.getResources().getColor(R.color.app_white));
                                    String s = api_response.getData().getAsString().toString();
                                    if (isInteger(s)) {
                                        utility.setNotificationdata(s);
                                        Intent in = new Intent("notification_delete");
                                        context.sendBroadcast(in);
                                    }
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

    public void notification_work(GET_NOTIFICATION bodyResponse) {
        try {
            if (bodyResponse.getMetadataBrowse().equalsIgnoreCase(KeyWord.EXTERNAL)) {
                try {
                    String url = bodyResponse.getMetadata();
                    CustomTabsIntent.Builder builder = new CustomTabsIntent.Builder();
                    builder.setToolbarColor(ContextCompat.getColor(context, R.color.colorAccent));
                    CustomTabsIntent customTabsIntent = builder.build();
                    customTabsIntent.launchUrl(context, Uri.parse(url));
                } catch (Exception e) {
                    Log.d("Error Line Number", Log.getStackTraceString(e));
                    try {
                        Uri uri = Uri.parse(bodyResponse.getMetadata()); // missing 'http://' will cause crashed
                        Intent intent = new Intent(Intent.ACTION_VIEW, uri);
                        context.startActivity(intent);
                    } catch (Exception e2) {
                        Log.d("Error Line Number", Log.getStackTraceString(e2));
                        utility.showDialog(context.getResources().getString(R.string.no_browser_string));
                    }
                }
            } else if (bodyResponse.getMetadataBrowse().equalsIgnoreCase(KeyWord.INTERNAL)) {
                if (bodyResponse.getMetadata().contains(KeyWord.INAPP_SELLER_LIST)) {
                            /*Bundle bundle = new Bundle();
                            bundle.putString("product_id", bodyResponse.getProductId());*/
                    Fragment navhost = ((AppCompatActivity) context).getSupportFragmentManager().findFragmentById(R.id.nav_host_fragment);
                    NavController c = NavHostFragment.findNavController(navhost);
                    //c.popBackStack();
                    c.navigate(R.id.nav_seller);
                } else if (bodyResponse.getMetadata().contains(KeyWord.INAPP_SELLER_DETAILS)) {
                    String[] parts = bodyResponse.getMetadata().split("-");
                    String id = parts[1];
                    utility.logger("seller_id" + id);
                    Bundle bundle = new Bundle();
                    bundle.putString("seller_id", id);
                    Fragment navhost = ((AppCompatActivity) context).getSupportFragmentManager().findFragmentById(R.id.nav_host_fragment);
                    NavController c = NavHostFragment.findNavController(navhost);
                    //c.popBackStack();
                    c.navigate(R.id.nav_seller_details, bundle);
                } else if (bodyResponse.getMetadata().contains(KeyWord.INAPP_PRODUCT_DETAILS)) {
                    String[] parts = bodyResponse.getMetadata().split("-");
                    String id = parts[1];
                    utility.logger("product_id" + id);
                    Bundle bundle = new Bundle();
                    bundle.putString("product_id", id);
                    Fragment navhost = ((AppCompatActivity) context).getSupportFragmentManager().findFragmentById(R.id.nav_host_fragment);
                    NavController c = NavHostFragment.findNavController(navhost);
                    //c.popBackStack();
                    c.navigate(R.id.frag_product_details, bundle);
                } else if (bodyResponse.getMetadata().contains(KeyWord.INAPP_NOTIFICATION)) {

                } else if (bodyResponse.getMetadata().contains(KeyWord.INAPP_CART)) {
                    context.startActivity(new Intent(context, Cart_Activity.class));
                } else if (bodyResponse.getMetadata().contains(KeyWord.INAPP_DISCOUNT)) {
                    context.startActivity(new Intent(context, Cart_Activity.class));
                } else if (bodyResponse.getMetadata().contains(KeyWord.INAPP_CATEGORY)) {
                    String[] parts = bodyResponse.getMetadata().split("-");
                    String id = parts[1];
                    utility.logger("category_id" + id);
                    Bundle bundle = new Bundle();
                    bundle.putString("category_id", id);
                    Fragment navhost = ((AppCompatActivity) context).getSupportFragmentManager().findFragmentById(R.id.nav_host_fragment);
                    NavController c = NavHostFragment.findNavController(navhost);
                    //c.popBackStack();
                    c.navigate(R.id.frag_category_list, bundle);
                }/*else if (bodyResponse.getMetadata().contains(KeyWord.INAPP_SELLER_LIST)){

                        }else if (bodyResponse.getMetadata().contains(KeyWord.INAPP_SELLER_LIST)){

                        }*/

            }
        } catch (Exception e) {
            Log.d("Error Line Number", Log.getStackTraceString(e));
        }

                /*Bundle bundle = new Bundle();
                bundle.putString("category_id", bodyResponse.getAppMenuId());
                Fragment navhost = ((AppCompatActivity) context).getSupportFragmentManager().findFragmentById(R.id.nav_host_fragment);
                NavController c = NavHostFragment.findNavController(navhost);
                //c.popBackStack();
                c.navigate(R.id.frag_notificationList, bundle);*/
    }

    public static boolean isInteger(String s) {
        try {
            Integer.parseInt(s);
        } catch (NumberFormatException e) {
            return false;
        } catch (NullPointerException e) {
            return false;
        }
        // only got here if we didn't return false
        return true;
    }
}