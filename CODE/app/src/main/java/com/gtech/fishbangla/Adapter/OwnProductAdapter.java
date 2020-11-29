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
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RatingBar;
import android.widget.Switch;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.gtech.fishbangla.Activity.Product_Edit;
import com.gtech.fishbangla.DATABASE.REPOSITORY.USER_REPOSITORY;
import com.gtech.fishbangla.DATABASE.TABLE.USER_PROFILE;
import com.gtech.fishbangla.Http.ApiClient;
import com.gtech.fishbangla.Http.ApiInterface;
import com.gtech.fishbangla.Library.KeyWord;
import com.gtech.fishbangla.Library.Utility;
import com.gtech.fishbangla.MODEL.API_RESPONSE;
import com.gtech.fishbangla.MODEL.PRODUCT.Get_OwnProduct;
import com.gtech.fishbangla.MODEL.PRODUCT.SEND_PRODUCT_STATUS;
import com.gtech.fishbangla.R;

import java.util.HashMap;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class OwnProductAdapter extends RecyclerView.Adapter<OwnProductAdapter.Todo_View_Holder> {
    Context context;
    List<Get_OwnProduct> suggest_list;
    Utility utility;
    ApiInterface apiInterface = ApiClient.getBaseClient().create(ApiInterface.class);

    USER_REPOSITORY user_dao;
    USER_PROFILE user_profile;


    public OwnProductAdapter(List<Get_OwnProduct> to, Context c) {
        suggest_list = to;
        context = c;
        utility = new Utility(context);
        try {
            user_dao = new USER_REPOSITORY(((Activity) context).getApplication());
            user_profile = user_dao.getuserData();
        } catch (Exception e) {
            Log.d("Error Line Number", Log.getStackTraceString(e));
        }
    }

    public class Todo_View_Holder extends RecyclerView.ViewHolder {
        ImageView fish_image;
        TextView fish_name;
        RatingBar fish_ratting;
        TextView fish_price;
        TextView fish_miminum_sell;
        TextView fish_seller;
        TextView fish_approven;
        Switch fish_status;
        Button fish_edit;
        ImageView fish_wholesale;

        public Todo_View_Holder(View view) {
            super(view);
            fish_image = view.findViewById(R.id.ownfish_image);
            fish_name = view.findViewById(R.id.ownfish_name);
            fish_ratting = view.findViewById(R.id.ownfish_ratting);
            fish_price = view.findViewById(R.id.ownfish_price);
            fish_miminum_sell = view.findViewById(R.id.ownfish_minimum_sell);
            fish_seller = view.findViewById(R.id.ownfish_seller);
            fish_status = view.findViewById(R.id.ownfish_status);
            fish_edit = view.findViewById(R.id.ownfish_image_edit);
            fish_wholesale = view.findViewById(R.id.ownfish_wholesale);
            fish_approven = view.findViewById(R.id.ownfish_approven);
        }
    }

    @Override
    public OwnProductAdapter.Todo_View_Holder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.recycler_ownfish, parent, false);

        return new OwnProductAdapter.Todo_View_Holder(itemView);
    }

    @Override
    public void onBindViewHolder(final OwnProductAdapter.Todo_View_Holder holder, int position) {
        final Get_OwnProduct bodyResponse = suggest_list.get(position);
        holder.fish_name.setText(bodyResponse.getProductName());
        String price = utility.convertToBangle(bodyResponse.getPrice()) + " " + context.getResources().getString(R.string.taka_string) + "/ " + utility.convertToBangle(bodyResponse.getUnitSize());
        String avg_weight = context.getResources().getString(R.string.avg_string) + " " + utility.convertToBangle(bodyResponse.getAvgWeight());
        String minimum_buy = context.getResources().getString(R.string.min_buy_string) + " " + utility.convertToBangle(bodyResponse.getMinUnitQty())+ " " + context.getResources().getString(R.string.unit_unit_string);
        if (bodyResponse.getUnitType().equalsIgnoreCase(KeyWord.UNIT_KG)) {
            price = price + " " + context.getResources().getString(R.string.unit_kg_string);
            //avg_weight = avg_weight + " " + context.getResources().getString(R.string.unit_kg_string);
            //minimum_buy = minimum_buy + " " + context.getResources().getString(R.string.unit_kg_string);
        } else if (bodyResponse.getUnitType().equalsIgnoreCase(KeyWord.UNIT_GRAM)) {
            price = price + " " + context.getResources().getString(R.string.unit_gram_string);
            //avg_weight = avg_weight + " " + context.getResources().getString(R.string.unit_gram_string);
            //minimum_buy = minimum_buy + " " + context.getResources().getString(R.string.unit_gram_string);
        } else if (bodyResponse.getUnitType().equalsIgnoreCase(KeyWord.UNIT_PIECE)) {
            price = price + " " + context.getResources().getString(R.string.unit_piece_string);
            //avg_weight = avg_weight + " " + context.getResources().getString(R.string.unit_piece_string);
            //minimum_buy = minimum_buy + " " + context.getResources().getString(R.string.unit_piece_string);
        }
        holder.fish_price.setText(price);
        holder.fish_miminum_sell.setText(minimum_buy);
        holder.fish_seller.setText(bodyResponse.getUserFullName());
        holder.fish_ratting.setRating(Float.valueOf(bodyResponse.getProductRating()));
        Glide.with(context).load(bodyResponse.getProductPicture()).apply(utility.Glide_Cache_On()).into(holder.fish_image);
        if (bodyResponse.getProductToken().equalsIgnoreCase(KeyWord.ACTIVE)) {
            holder.fish_status.setChecked(true);
        } else if (bodyResponse.getProductToken().equalsIgnoreCase(KeyWord.INACTIVE)) {
            holder.fish_status.setChecked(false);
        }
        if (bodyResponse.getProductStatus().equalsIgnoreCase(KeyWord.PRODUCT_SUCCESS)) {
            holder.fish_approven.setText(context.getResources().getString(R.string.seller_product_approved_string));
            holder.fish_approven.setTextColor(context.getResources().getColor(R.color.app_green));
        } else if (bodyResponse.getProductStatus().equalsIgnoreCase(KeyWord.PRODUCT_PENDING)) {
            holder.fish_approven.setText(context.getResources().getString(R.string.seller_product_pending_string));
            holder.fish_approven.setTextColor(context.getResources().getColor(android.R.color.holo_orange_dark));
        } else if (bodyResponse.getProductStatus().equalsIgnoreCase(KeyWord.PRODUCT_CANCEL)) {
            holder.fish_approven.setText(context.getResources().getString(R.string.seller_product_decline_string));
            holder.fish_approven.setTextColor(context.getResources().getColor(R.color.app_red));
        }
        holder.fish_status.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (buttonView.isPressed()) {
                    if (isChecked) {
                        show_Active_Dialog(context.getResources().getString(R.string.change_string), position, KeyWord.STATUS_ON, 0);
                    } else {
                        show_Active_Dialog(context.getResources().getString(R.string.change_string), position, KeyWord.STATUS_OFF, 0);
                    }
                }
            }
        });
        holder.fish_edit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    show_Active_Dialog(context.getResources().getString(R.string.change_string), position, KeyWord.STATUS_OFF, 1);
                } catch (Exception e) {
                    Log.d("Error Line Number", Log.getStackTraceString(e));
                }
            }
        });
        if (bodyResponse.getProductPostType().equalsIgnoreCase(KeyWord.SELLTYPE_WHOLESALE)) {
            holder.fish_wholesale.setVisibility(View.VISIBLE);
        }
    }

    @Override
    public int getItemCount() {
        return suggest_list.size();
    }

    public void show_Active_Dialog(String message, final int pos, final String s, int i) {
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
                        if (i == 0) {
                            status_change(new SEND_PRODUCT_STATUS(suggest_list.get(pos).getProductId(), s, user_profile.getUserId()));
                        } else if (i == 1) {
                            Intent intent = new Intent(context, Product_Edit.class);
                            intent.putExtra("product_id", suggest_list.get(pos).getProductId());
                            context.startActivity(intent);
                        }
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

    private void status_change(SEND_PRODUCT_STATUS s) {
        try {
            if (utility.isNetworkAvailable()) {
                Log.d("Product status change", s.toString());
                utility.showProgress(false, context.getResources().getString(R.string.wait_string));
                Call<API_RESPONSE> call = apiInterface.FishBAngla_Product_status_change(utility.getApiKey(), utility.getUSER_ID(), utility.getFirebaseToken(), s);
                call.enqueue(new Callback<API_RESPONSE>() {
                    @Override
                    public void onResponse(Call<API_RESPONSE> call, Response<API_RESPONSE> response) {
                        try {
                            utility.hideProgress();
                            Log.d("Product status change", response.toString());
                            if (response.isSuccessful() && response != null) {
                                API_RESPONSE api_response = response.body();
                                if (api_response.getCode() == 200) {
                                    Intent in = new Intent("product_update");
                                    //in.putExtra("category", category);
                                    context.sendBroadcast(in);
                                    utility.showDialog(context.getResources().getString(R.string.request_success_string));
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

}