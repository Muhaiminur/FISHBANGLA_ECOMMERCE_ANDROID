package com.gtech.fishbangla.Adapter;

import android.app.Activity;
import android.app.Application;
import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.navigation.NavController;
import androidx.navigation.fragment.NavHostFragment;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.manager.SupportRequestManagerFragment;
import com.bumptech.glide.request.RequestOptions;
import com.gtech.fishbangla.DATABASE.REPOSITORY.CART_REPOSITORY;
import com.gtech.fishbangla.DATABASE.TABLE.CART_TABLE;
import com.gtech.fishbangla.Library.KeyWord;
import com.gtech.fishbangla.Library.Utility;
import com.gtech.fishbangla.MODEL.PRODUCT.Get_Product;
import com.gtech.fishbangla.MODEL.RECYCLER_MODEL.Fish_Model;
import com.gtech.fishbangla.R;

import java.util.List;

public class HomeSuggestAdapter extends RecyclerView.Adapter<HomeSuggestAdapter.Todo_View_Holder> {
    Context context;
    List<Get_Product> suggest_list;
    Utility utility;
    CART_REPOSITORY cart_repository;
    CART_TABLE cartTable;


    public HomeSuggestAdapter(List<Get_Product> to, Context c) {
        suggest_list = to;
        context = c;
        utility = new Utility(context);
        cart_repository = new CART_REPOSITORY(((Activity) context).getApplication());
    }

    public class Todo_View_Holder extends RecyclerView.ViewHolder {
        ImageView fish_image;
        ImageView fish_wholesale;
        TextView fish_name;
        RatingBar fish_ratting;
        TextView fish_price;
        TextView fish_miminum_sell;
        TextView fish_seller;
        RelativeLayout fish_add;
        FrameLayout fish_add_exist;
        TextView fish_add_minus;
        TextView fish_add_exist_count;
        TextView fish_add_plus;
        TextView fish_add_count;
        CardView fish_view;


        public Todo_View_Holder(View view) {
            super(view);
            fish_image = view.findViewById(R.id.fish_image);
            fish_name = view.findViewById(R.id.fish_name);
            fish_ratting = view.findViewById(R.id.fish_ratting);
            fish_price = view.findViewById(R.id.fish_price);
            fish_miminum_sell = view.findViewById(R.id.fish_minimum_sell);
            fish_seller = view.findViewById(R.id.fish_seller);
            fish_add = view.findViewById(R.id.fish_add_cart);
            fish_add_count = view.findViewById(R.id.fish_add_cart_count);
            fish_add_exist = view.findViewById(R.id.fish_add_cart_exist);
            fish_add_minus = view.findViewById(R.id.fish_add_cart_minus);
            fish_add_plus = view.findViewById(R.id.fish_add_cart_plus);
            fish_add_exist_count = view.findViewById(R.id.fish_add_cart_exist_count);
            fish_view = view.findViewById(R.id.recycler_fish);
            fish_wholesale = view.findViewById(R.id.fish_wholesale);
        }
    }

    @Override
    public HomeSuggestAdapter.Todo_View_Holder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.recycler_fishview, parent, false);

        return new HomeSuggestAdapter.Todo_View_Holder(itemView);
    }

    @Override
    public void onBindViewHolder(final HomeSuggestAdapter.Todo_View_Holder holder, int position) {
        final Get_Product bodyResponse = suggest_list.get(position);
        try {
            holder.fish_name.setText(bodyResponse.getProductName());
            String price = utility.convertToBangle(bodyResponse.getPrice()) + " " + context.getResources().getString(R.string.taka_string) + "/ " + utility.convertToBangle(bodyResponse.getUnitSize());
            String avg_weight = context.getResources().getString(R.string.avg_string) + " " + utility.convertToBangle(bodyResponse.getAvgWeight());
            String minimum_buy = context.getResources().getString(R.string.min_buy_string) + " " + utility.convertToBangle(bodyResponse.getMinUnitQty()) + " " + context.getResources().getString(R.string.unit_unit_string);
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
            //setImage(holder.fish_image, bodyResponse.getProductPicture());
            cartTable = cart_repository.find_single_Cart(bodyResponse.getProductId());
            if (cartTable != null && cartTable.getCart_Id().equalsIgnoreCase(bodyResponse.getProductId())) {
                holder.fish_add_count.setText(utility.convertToBangle(cartTable.getCart_unit_amount()));
            } else {
                holder.fish_add_count.setText(context.getResources().getString(R.string.plus));
            }
            holder.fish_view.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Bundle bundle = new Bundle();
                    bundle.putString("product_id", bodyResponse.getProductId());
                    Fragment navhost = ((AppCompatActivity) context).getSupportFragmentManager().findFragmentById(R.id.nav_host_fragment);
                    NavController c = NavHostFragment.findNavController(navhost);
                    //c.popBackStack();
                    c.navigate(R.id.frag_product_details, bundle);
                }
            });
            cartTable = cart_repository.find_single_Cart(bodyResponse.getProductId());
            if (cartTable != null && cartTable.getCart_Id().equalsIgnoreCase(bodyResponse.getProductId())) {
                holder.fish_add_exist.setVisibility(View.VISIBLE);
                holder.fish_add.setVisibility(View.GONE);
                holder.fish_add_exist_count.setText(utility.convertToBangle(cartTable.getCart_unit_amount()));
            } else {
                holder.fish_add_exist.setVisibility(View.GONE);
                holder.fish_add.setVisibility(View.VISIBLE);
            }
            holder.fish_add.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    try {
                        if (!bodyResponse.getUserId().toString().equalsIgnoreCase(utility.getUSER_ID())) {
                            //utility.showDialog(context.getResources().getString(R.string.add_cart_string));
                            cartTable = cart_repository.find_single_Cart(bodyResponse.getProductId());
                            CART_TABLE c = new CART_TABLE(bodyResponse.getProductId(), bodyResponse.getProductPicture(), bodyResponse.getProductName(), bodyResponse.getMinUnitQty(), bodyResponse.getUnitType(), bodyResponse.getUserFullName(), bodyResponse.getPrice(), bodyResponse.getUnitSize(), bodyResponse.getUnitType(), bodyResponse.getMinUnitQty());
                            if (cartTable == null) {
                                cart_repository.insert_single_cart_data(c);
                            } else if (cartTable != null && cartTable.getCart_Id().equalsIgnoreCase(bodyResponse.getProductId())) {
                                c.setCart_unit_amount(String.valueOf(Integer.parseInt(cartTable.getCart_unit_amount()) + /*Integer.parseInt(c.getCart_unit_size())*/1));
                                cart_repository.insert_single_cart_data(c);
                            }
                        } else {
                            utility.showDialog(context.getResources().getString(R.string.own_product_buying_string));
                        }
                    } catch (Exception e) {
                        Log.d("Error Line Number", Log.getStackTraceString(e));
                    }
                }
            });
            if (bodyResponse.getProductPostType().equalsIgnoreCase(KeyWord.SELLTYPE_WHOLESALE)) {
                holder.fish_wholesale.setVisibility(View.VISIBLE);
            } else {
                holder.fish_wholesale.setVisibility(View.GONE);
            }
            holder.fish_add_minus.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    try {
                        if (!bodyResponse.getUserId().toString().equalsIgnoreCase(utility.getUSER_ID())) {
                            cartTable = cart_repository.find_single_Cart(bodyResponse.getProductId());
                            CART_TABLE c = new CART_TABLE(bodyResponse.getProductId(), bodyResponse.getProductPicture(), bodyResponse.getProductName(), bodyResponse.getMinUnitQty(), bodyResponse.getUnitType(), bodyResponse.getUserFullName(), bodyResponse.getPrice(), bodyResponse.getUnitSize(), bodyResponse.getUnitType(), bodyResponse.getMinUnitQty());
                            if (cartTable == null) {
                                //cart_repository.insert_single_cart_data(c);
                                holder.fish_add_exist.setVisibility(View.GONE);
                                holder.fish_add.setVisibility(View.VISIBLE);
                            } else if (cartTable != null && cartTable.getCart_Id().equalsIgnoreCase(bodyResponse.getProductId())) {
                                int check = Integer.parseInt(cartTable.getCart_unit_amount());
                                utility.logger("che1" + check);
                                utility.logger("che2" + bodyResponse.getMinUnitQty());
                                if (check == Integer.parseInt(bodyResponse.getMinUnitQty())) {
                                    utility.logger("delete");
                                    cart_repository.delete_cart_data(bodyResponse.getProductId());
                                    holder.fish_add_exist.setVisibility(View.GONE);
                                    holder.fish_add.setVisibility(View.VISIBLE);
                                }
                                if (check > 1 && check > Integer.parseInt(bodyResponse.getMinUnitQty())) {
                                    c.setCart_unit_amount(String.valueOf((check - 1)));
                                    cart_repository.insert_single_cart_data(c);
                                }
                                /*if (check == Integer.parseInt(bodyResponse.getMinUnitQty())) {
                                    holder.fish_add_delete.setVisibility(View.VISIBLE);
                                    holder.fish_add_minus.setVisibility(View.GONE);
                                } else {
                                    holder.fish_add_delete.setVisibility(View.GONE);
                                    holder.fish_add_minus.setVisibility(View.VISIBLE);
                                }*/
                            }
                        } else {
                            utility.showDialog(context.getResources().getString(R.string.own_product_buying_string));
                        }
                    } catch (Exception e) {
                        Log.d("Error Line Number", Log.getStackTraceString(e));
                    }
                }
            });
            holder.fish_add_plus.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    try {
                        if (!bodyResponse.getUserId().toString().equalsIgnoreCase(utility.getUSER_ID())) {
                            cartTable = cart_repository.find_single_Cart(bodyResponse.getProductId());
                            CART_TABLE c = new CART_TABLE(bodyResponse.getProductId(), bodyResponse.getProductPicture(), bodyResponse.getProductName(), bodyResponse.getMinUnitQty(), bodyResponse.getUnitType(), bodyResponse.getUserFullName(), bodyResponse.getPrice(), bodyResponse.getUnitSize(), bodyResponse.getUnitType(), bodyResponse.getMinUnitQty());
                            if (cartTable == null) {
                                cart_repository.insert_single_cart_data(c);
                            } else if (cartTable != null && cartTable.getCart_Id().equalsIgnoreCase(bodyResponse.getProductId())) {
                                int check = Integer.parseInt(cartTable.getCart_unit_amount());
                                if (check > 0 && check < Integer.parseInt(bodyResponse.getMaxUnitQty())) {
                                    c.setCart_unit_amount(String.valueOf((check + 1)));
                                    cart_repository.insert_single_cart_data(c);
                                }
                            }
                        } else {
                            utility.showDialog(context.getResources().getString(R.string.own_product_buying_string));
                        }
                    } catch (Exception e) {
                        Log.d("Error Line Number", Log.getStackTraceString(e));
                    }
                }
            });
            cart_repository.get_all_Cart().observe((AppCompatActivity) context, new Observer<List<CART_TABLE>>() {
                @Override
                public void onChanged(@Nullable final List<CART_TABLE> cart_tables) {
                    // Update the cached copy of the words in the adapter.
                    if (cart_tables != null) {
                        try {
                            for (CART_TABLE c : cart_tables) {
                                if (c.getCart_Id().equalsIgnoreCase(bodyResponse.getProductId())) {
                                    holder.fish_add_exist.setVisibility(View.VISIBLE);
                                    holder.fish_add.setVisibility(View.GONE);
                                    holder.fish_add_exist_count.setText(utility.convertToBangle(c.getCart_unit_amount()));
                                    /*int check = Integer.parseInt(c.getCart_unit_amount());
                                    if (check == Integer.parseInt(bodyResponse.getMinUnitQty())) {
                                        holder.fish_add_delete.setVisibility(View.VISIBLE);
                                        holder.fish_add_minus.setVisibility(View.GONE);
                                    } else {
                                        holder.fish_add_delete.setVisibility(View.GONE);
                                        holder.fish_add_minus.setVisibility(View.VISIBLE);
                                    }*/
                                }/* else {
                                    holder.fish_add_exist.setVisibility(View.GONE);
                                    holder.fish_add.setVisibility(View.VISIBLE);
                                }*/
                            }
                        } catch (Exception e) {
                            Log.d("Error Line Number", Log.getStackTraceString(e));
                        }
                    }
                }
            });
        } catch (Exception e) {
            Log.d("Error Line Number", Log.getStackTraceString(e));
        }
    }

    @Override
    public int getItemCount() {
        return suggest_list.size();
    }

    private void setImage(ImageView iv, String url) {
        Glide.with(context)
                .load(url)
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .error(R.drawable.ic_default)
                .placeholder(R.drawable.ic_default)
                .dontTransform()
                //.override(1000, 200)
                .into(iv);
    }
}