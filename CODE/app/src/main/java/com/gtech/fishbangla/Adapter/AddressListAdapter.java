package com.gtech.fishbangla.Adapter;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Point;
import android.util.Log;
import android.view.Display;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.google.gson.Gson;
import com.gtech.fishbangla.Activity.AddAddress;
import com.gtech.fishbangla.Http.ApiClient;
import com.gtech.fishbangla.Http.ApiInterface;
import com.gtech.fishbangla.Library.KeyWord;
import com.gtech.fishbangla.Library.Utility;
import com.gtech.fishbangla.MODEL.ADDRESS.SEND_ADDRESSID;
import com.gtech.fishbangla.MODEL.ADDRESS.SEND_UPDATEADDRESS;
import com.gtech.fishbangla.MODEL.API_RESPONSE;
import com.gtech.fishbangla.R;

import java.util.HashMap;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class AddressListAdapter extends RecyclerView.Adapter<AddressListAdapter.Todo_View_Holder> {
    Context context;
    List<SEND_UPDATEADDRESS> address_models;
    Utility utility;

    ApiInterface apiInterface = ApiClient.getBaseClient().create(ApiInterface.class);

    public AddressListAdapter(List<SEND_UPDATEADDRESS> to, Context c) {
        address_models = to;
        context = c;
        utility = new Utility(context);
    }

    public class Todo_View_Holder extends RecyclerView.ViewHolder {
        ImageView primary;
        TextView address_details;
        ImageView address_update;
        TextView address_name;
        TextView address_mbl;
        TextView address_village;
        TextView address_area;
        TextView address_road;
        ImageView address_delete;


        public Todo_View_Holder(View view) {
            super(view);
            primary = view.findViewById(R.id.address_primary_icon);
            address_details = view.findViewById(R.id.address_details);
            address_update = view.findViewById(R.id.address_update);
            address_name = view.findViewById(R.id.address_name);
            address_mbl = view.findViewById(R.id.address_mbl);
            address_village = view.findViewById(R.id.address_village);
            address_road = view.findViewById(R.id.address_road);
            address_area = view.findViewById(R.id.address_area);
            address_delete = view.findViewById(R.id.address_delete);
        }
    }

    @Override
    public AddressListAdapter.Todo_View_Holder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.recycler_address, parent, false);

        return new AddressListAdapter.Todo_View_Holder(itemView);
    }

    @Override
    public void onBindViewHolder(final AddressListAdapter.Todo_View_Holder holder, int position) {
        final SEND_UPDATEADDRESS bodyResponse = address_models.get(position);
        if (bodyResponse.getIsPrimary().equalsIgnoreCase("YES")) {
            Glide.with(context).load(context.getResources().getDrawable(R.drawable.ic_home_red)).apply(utility.Glide_Cache_Off()).into(holder.primary);
        }
        holder.address_name.setText(bodyResponse.getReceiverName());
        holder.address_mbl.setText(utility.Mobile_Number_Read(utility.convertToBangle(bodyResponse.getReceiverPhone())));
        holder.address_details.setText(bodyResponse.getUserAddress());
        holder.address_road.setText(context.getResources().getString(R.string.address_flor_string) + ": " + bodyResponse.getFlatNo() + ", " + context.getResources().getString(R.string.address_home_string) + ": " + bodyResponse.getBuildingNo() /*+ ", "*/);
        holder.address_village.setText(context.getResources().getString(R.string.address_area_string) + ": " + bodyResponse.getUserVillage());
        holder.address_area.setText(context.getResources().getString(R.string.address_road_string) + ": " + bodyResponse.getRoadNo());
        holder.address_delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                show_delete(context.getResources().getString(R.string.address_delete_confirmation_string), position);
            }
        });
        holder.address_update.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                show_update(context.getResources().getString(R.string.change_string), position);
            }
        });
    }

    @Override
    public int getItemCount() {
        return address_models.size();
    }

    public void show_delete(String message, final int pos) {
        try {
            Display display = ((Activity) context).getWindowManager().getDefaultDisplay();
            Point size = new Point();
            display.getSize(size);
            HashMap<String, Integer> screen = utility.getScreenRes();
            int width = screen.get("width");
            int height = screen.get("height");
            int mywidth = (width / 10) * 7;
            final Dialog dialog = new Dialog(context);
            dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
            dialog.getWindow().setBackgroundDrawableResource(android.R.color.transparent);
            dialog.setContentView(R.layout.dialog_toast_yes_no);
            TextView tvMessage = dialog.findViewById(R.id.tv_message);
            Button btnOk = dialog.findViewById(R.id.btn_yes);
            Button btnNo = dialog.findViewById(R.id.btn_no);
            tvMessage.setText(message);
            LinearLayout ll = dialog.findViewById(R.id.dialog_layout_size);
            LinearLayout.LayoutParams params = (LinearLayout.LayoutParams) ll.getLayoutParams();
            params.height = LinearLayout.LayoutParams.WRAP_CONTENT;
            params.width = mywidth;
            ll.setLayoutParams(params);
            tvMessage.setTextColor(context.getResources().getColor(R.color.colorPrimaryDark));
            btnOk.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    try {
                        dialog.dismiss();
                        Delete_address(new SEND_ADDRESSID(address_models.get(pos).getAddressId()));
                    } catch (Exception e) {
                        Log.d("Error Line Number", Log.getStackTraceString(e));
                    }

                }
            });
            btnNo.setOnClickListener(new View.OnClickListener() {
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

    private void Delete_address(SEND_ADDRESSID s) {
        try {
            if (utility.isNetworkAvailable()) {
                Log.d("ADDRESS DELETE", s.toString());
                utility.showProgress(false, context.getResources().getString(R.string.wait_string));
                Call<API_RESPONSE> call = apiInterface.FishBAngla_delete_address(utility.getApiKey(), utility.getUSER_ID(), utility.getFirebaseToken(), s);
                call.enqueue(new Callback<API_RESPONSE>() {
                    @Override
                    public void onResponse(Call<API_RESPONSE> call, Response<API_RESPONSE> response) {
                        try {
                            utility.hideProgress();
                            Log.d("ADDRESS DELETE", response.toString());
                            if (response.isSuccessful() && response != null) {
                                API_RESPONSE api_response = response.body();
                                if (api_response.getCode() == 200) {
                                    utility.showDialog(context.getResources().getString(R.string.address_delete_string));
                                    Intent in = new Intent("address_update");
                                    //in.putExtra("category", category);
                                    context.sendBroadcast(in);
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

    public void show_update(String message, final int pos) {
        try {
            Display display = ((Activity) context).getWindowManager().getDefaultDisplay();
            Point size = new Point();
            display.getSize(size);
            HashMap<String, Integer> screen = utility.getScreenRes();
            int width = screen.get("width");
            int height = screen.get("height");
            int mywidth = (width / 10) * 7;
            final Dialog dialog = new Dialog(context);
            dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
            dialog.getWindow().setBackgroundDrawableResource(android.R.color.transparent);
            dialog.setContentView(R.layout.dialog_toast_yes_no);
            TextView tvMessage = dialog.findViewById(R.id.tv_message);
            Button btnOk = dialog.findViewById(R.id.btn_yes);
            Button btnNo = dialog.findViewById(R.id.btn_no);
            tvMessage.setText(message);
            LinearLayout ll = dialog.findViewById(R.id.dialog_layout_size);
            LinearLayout.LayoutParams params = (LinearLayout.LayoutParams) ll.getLayoutParams();
            params.height = LinearLayout.LayoutParams.WRAP_CONTENT;
            params.width = mywidth;
            ll.setLayoutParams(params);
            tvMessage.setTextColor(context.getResources().getColor(R.color.colorPrimaryDark));
            btnOk.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    try {
                        dialog.dismiss();
                        Gson gson = new Gson();
                        String address = gson.toJson(address_models.get(pos));
                        Intent intent = new Intent(context, AddAddress.class);
                        intent.putExtra("edit_address", address);
                        context.startActivity(intent);
                    } catch (Exception e) {
                        Log.d("Error Line Number", Log.getStackTraceString(e));
                    }

                }
            });
            btnNo.setOnClickListener(new View.OnClickListener() {
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