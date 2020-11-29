package com.gtech.fishbangla.Adapter;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Point;
import android.text.TextUtils;
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
import com.gtech.fishbangla.DATABASE.REPOSITORY.CART_REPOSITORY;
import com.gtech.fishbangla.DATABASE.TABLE.CART_TABLE;
import com.gtech.fishbangla.Library.KeyWord;
import com.gtech.fishbangla.Library.Utility;
import com.gtech.fishbangla.R;

import java.text.DecimalFormat;
import java.util.HashMap;
import java.util.List;

public class CartAdapter extends RecyclerView.Adapter<CartAdapter.Todo_View_Holder> {
    Context context;
    List<CART_TABLE> cartModelList;
    Utility utility;
    CART_REPOSITORY cart_repository;

    DecimalFormat df = new DecimalFormat("#.00");

    public CartAdapter(List<CART_TABLE> to, Context c) {
        cartModelList = to;
        context = c;
        utility = new Utility(context);
        cart_repository = new CART_REPOSITORY(((Activity) context).getApplication());
    }

    public class Todo_View_Holder extends RecyclerView.ViewHolder {
        ImageView fish_image;
        TextView fish_name;
        TextView fish_minimum_buy;
        TextView fish_seller;
        TextView fish_unit_price;
        TextView fish_total_price;
        TextView fish_unit;
        Button cart_minus;
        Button cart_plus;
        Button cart_delete;

        public Todo_View_Holder(View view) {
            super(view);
            fish_image = view.findViewById(R.id.cart_image);
            fish_name = view.findViewById(R.id.cart_name);
            fish_minimum_buy = view.findViewById(R.id.cart_minimum_buy);
            fish_seller = view.findViewById(R.id.cart_seller);
            fish_unit_price = view.findViewById(R.id.cart_fish_single_price);
            cart_minus = view.findViewById(R.id.cart_minus);
            cart_plus = view.findViewById(R.id.cart_plus);
            fish_unit = view.findViewById(R.id.cart_unit);
            fish_total_price = view.findViewById(R.id.cart_fish_total_price);
            cart_delete = view.findViewById(R.id.cart_delete);
        }
    }

    @Override
    public CartAdapter.Todo_View_Holder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.recycler_cart, parent, false);

        return new CartAdapter.Todo_View_Holder(itemView);
    }

    @Override
    public void onBindViewHolder(final CartAdapter.Todo_View_Holder holder, int position) {
        try{
            final CART_TABLE bodyResponse = cartModelList.get(position);
            if (!TextUtils.isEmpty(bodyResponse.getCart_url())) {
                Glide.with(context).load(bodyResponse.getCart_url()).apply(utility.Glide_Cache_On()).into(holder.fish_image);
            }
            String minimum_buy = context.getResources().getString(R.string.min_buy_string) + " " + utility.convertToBangle(bodyResponse.getCart_minimum()) + " " + context.getResources().getString(R.string.unit_unit_string);
            String unit_price = utility.convertToBangle(bodyResponse.getCart_unit_price()) + " " + context.getResources().getString(R.string.taka_string) + "/ " + utility.convertToBangle(bodyResponse.getCart_unit_size());
            String total_unit_hint = "(" + " " + utility.convertToBangle(df.format(Integer.parseInt(bodyResponse.getCart_unit_size()) * Integer.parseInt(bodyResponse.getCart_unit_amount()))) + " ";

        /*if (bodyResponse.getCart_minimum_unit().equalsIgnoreCase(KeyWord.UNIT_KG)) {
            minimum_buy = minimum_buy + " " + context.getResources().getString(R.string.unit_kg_string);
        } else if (bodyResponse.getCart_minimum_unit().equalsIgnoreCase(KeyWord.UNIT_GRAM)) {
            minimum_buy = minimum_buy + " " + context.getResources().getString(R.string.unit_gram_string);
        } else if (bodyResponse.getCart_minimum_unit().equalsIgnoreCase(KeyWord.UNIT_PIECE)) {
            minimum_buy = minimum_buy + " " + context.getResources().getString(R.string.unit_piece_string);
        }*/
            if (bodyResponse.getCart_unit_akok().equalsIgnoreCase(KeyWord.UNIT_KG)) {
                unit_price = unit_price + " " + context.getResources().getString(R.string.unit_kg_string);
                total_unit_hint = total_unit_hint + context.getResources().getString(R.string.unit_kg_string) + ")";
            } else if (bodyResponse.getCart_unit_akok().equalsIgnoreCase(KeyWord.UNIT_GRAM)) {
                unit_price = unit_price + " " + context.getResources().getString(R.string.unit_gram_string);
                int g = Integer.parseInt(bodyResponse.getCart_unit_size()) * Integer.parseInt(bodyResponse.getCart_unit_amount());
                if (g > 999) {
                    total_unit_hint = "(" + " " + utility.convertToBangle(df.format(Double.parseDouble(g+"") / 1000)) + " " + context.getResources().getString(R.string.unit_kg_string) + ")";
                } else {
                    total_unit_hint = total_unit_hint + context.getResources().getString(R.string.unit_gram_string) + ")";
                }
            } else if (bodyResponse.getCart_unit_akok().equalsIgnoreCase(KeyWord.UNIT_PIECE)) {
                unit_price = unit_price + " " + context.getResources().getString(R.string.unit_piece_string);
                total_unit_hint = total_unit_hint + context.getResources().getString(R.string.unit_piece_string) + ")";
            }
            holder.fish_name.setText(bodyResponse.getCart_name());
            holder.fish_minimum_buy.setText(minimum_buy);
            holder.fish_seller.setText(context.getResources().getString(R.string.seller_nameonly_string) + " " + bodyResponse.getCart_seller());
            holder.fish_unit_price.setText(unit_price);
            holder.fish_unit.setText(utility.convertToBangle(bodyResponse.getCart_unit_amount()));
            holder.fish_total_price.setText(utility.convertToBangle(df.format(Integer.parseInt(bodyResponse.getCart_unit_price()) * Integer.parseInt(bodyResponse.getCart_unit_amount()))) + " " + context.getResources().getString(R.string.taka_string) + " " + total_unit_hint);
            holder.cart_plus.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    try {
                        int quan = Integer.parseInt(bodyResponse.getCart_unit_amount());
                        if (quan > 0) {
                            quan += 1;
                            cart_repository.update_cart_data(bodyResponse.getCart_Id(), String.valueOf(quan));
                        }
                    } catch (Exception e) {
                        Log.d("Error Line Number", Log.getStackTraceString(e));
                    }
                }
            });
            holder.cart_minus.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    try {
                        int quan = Integer.parseInt(bodyResponse.getCart_unit_amount());
                        if (quan > 1 && quan > Integer.parseInt(bodyResponse.getCart_minimum())) {
                            quan -= 1;
                            cart_repository.update_cart_data(bodyResponse.getCart_Id(), String.valueOf(quan));
                        }
                    } catch (Exception e) {
                        Log.d("Error Line Number", Log.getStackTraceString(e));
                    }
                }
            });
            holder.cart_delete.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    try {
                        show_Active_Dialog(context.getResources().getString(R.string.change_string), position);
                    } catch (Exception e) {
                        Log.d("Error Line Number", Log.getStackTraceString(e));
                    }
                }
            });
        }catch(Exception e){
          Log.d("Error Line Number",Log.getStackTraceString(e));
        }
    }

    @Override
    public int getItemCount() {
        return cartModelList.size();
    }

    public void show_Active_Dialog(String message, final int pos) {
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
                        cart_repository.delete_cart_data(cartModelList.get(pos).getCart_Id());
                        Intent in = new Intent("cart_delete");
                        context.sendBroadcast(in);
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