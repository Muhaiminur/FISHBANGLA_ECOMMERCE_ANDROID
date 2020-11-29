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

import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.gtech.fishbangla.Activity.Auction_Details;
import com.gtech.fishbangla.DATABASE.REPOSITORY.CART_REPOSITORY;
import com.gtech.fishbangla.DATABASE.TABLE.CART_TABLE;
import com.gtech.fishbangla.Library.KeyWord;
import com.gtech.fishbangla.Library.Utility;
import com.gtech.fishbangla.MODEL.AUCTION.GET_AUCTION_LIST;
import com.gtech.fishbangla.R;

import java.text.DecimalFormat;
import java.util.HashMap;
import java.util.List;

public class AuctionListAdapter extends RecyclerView.Adapter<AuctionListAdapter.Todo_View_Holder> {
    Context context;
    List<GET_AUCTION_LIST> auctionLists;
    Utility utility;

    public AuctionListAdapter(List<GET_AUCTION_LIST> to, Context c) {
        auctionLists = to;
        context = c;
        utility = new Utility(context);
    }

    public class Todo_View_Holder extends RecyclerView.ViewHolder {
        ImageView fish_image;
        TextView fish_name;
        TextView fish_price;
        TextView fish_minimum_buy;
        TextView fish_time;
        CardView fish_cardCardView;

        public Todo_View_Holder(View view) {
            super(view);
            fish_image = view.findViewById(R.id.auction_list_image);
            fish_name = view.findViewById(R.id.auction_list_name);
            fish_price = view.findViewById(R.id.auction_list_price);
            fish_minimum_buy = view.findViewById(R.id.auction_list_unit);
            fish_time = view.findViewById(R.id.auction_list_time);
            fish_cardCardView = view.findViewById(R.id.auction_list_view);
        }
    }

    @Override
    public AuctionListAdapter.Todo_View_Holder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.recycler_auction, parent, false);

        return new AuctionListAdapter.Todo_View_Holder(itemView);
    }

    @Override
    public void onBindViewHolder(final AuctionListAdapter.Todo_View_Holder holder, int position) {
        final GET_AUCTION_LIST bodyResponse = auctionLists.get(position);
        if (!TextUtils.isEmpty(bodyResponse.getProductPicture())) {
            Glide.with(context).load(bodyResponse.getProductPicture()).apply(utility.Glide_Cache_On()).into(holder.fish_image);
        }
        holder.fish_name.setText(context.getResources().getString(R.string.name_string) + " " +bodyResponse.getProductName());
        holder.fish_price.setText(context.getResources().getString(R.string.price_string) + " " +utility.convertToBangle(bodyResponse.getStartingPrice()) + " " + context.getResources().getString(R.string.taka_string));
        holder.fish_minimum_buy.setText(context.getResources().getString(R.string.upload_max_unit_string) + " : " +utility.convertToBangle(bodyResponse.getProductUnit()) + " " + context.getResources().getString(R.string.unit_unit_string));
        holder.fish_cardCardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {

                    context.startActivity(new Intent(context, Auction_Details.class).putExtra("AUCTION_DETAILS", bodyResponse.getProductId()));
                } catch (Exception e) {
                    Log.d("Error Line Number", Log.getStackTraceString(e));
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return auctionLists.size();
    }
}