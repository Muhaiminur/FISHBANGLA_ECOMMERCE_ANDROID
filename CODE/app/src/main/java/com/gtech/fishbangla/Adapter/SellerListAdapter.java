package com.gtech.fishbangla.Adapter;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.RatingBar;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.fragment.NavHostFragment;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.gtech.fishbangla.Library.Utility;
import com.gtech.fishbangla.MODEL.RECYCLER_MODEL.Seller_Model;
import com.gtech.fishbangla.MODEL.User.Get_Sellerlist;
import com.gtech.fishbangla.R;
import com.mikhaellopez.circularimageview.CircularImageView;

import java.util.ArrayList;
import java.util.List;

public class SellerListAdapter extends RecyclerView.Adapter<SellerListAdapter.Todo_View_Holder> {
    Context context;
    List<Get_Sellerlist> sellerModels;
    Utility utility;


    public SellerListAdapter(List<Get_Sellerlist> to, Context c) {
        sellerModels = to;
        context = c;
        utility = new Utility(context);
    }

    public class Todo_View_Holder extends RecyclerView.ViewHolder {
        TextView sellerName;
        LinearLayout sellerDetails;
        CircularImageView sellerImage;
        TextView sellerRatting;
        TextView sellerReview;
        RatingBar sellerRattingbar;
        TextView sellerMbl;

        public Todo_View_Holder(View view) {
            super(view);
            sellerName = view.findViewById(R.id.seller_name);
            sellerDetails = view.findViewById(R.id.seller_view);
            sellerImage = view.findViewById(R.id.seller_image);
            sellerRatting = view.findViewById(R.id.seller_ratting);
            sellerReview = view.findViewById(R.id.seller_review);
            sellerRattingbar = view.findViewById(R.id.seller_rattingbar);
            sellerMbl = view.findViewById(R.id.seller_mbl);
        }
    }

    @Override
    public SellerListAdapter.Todo_View_Holder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.recycler_seller, parent, false);

        return new SellerListAdapter.Todo_View_Holder(itemView);
    }

    @Override
    public void onBindViewHolder(final SellerListAdapter.Todo_View_Holder holder, int position) {
        final Get_Sellerlist bodyResponse = sellerModels.get(position);
        holder.sellerName.setText(bodyResponse.getUserFullName());
        holder.sellerRatting.setText(context.getResources().getString(R.string.seller_ratting_string) + " " + utility.convertToBangle(bodyResponse.getSellerRating()) + context.getResources().getString(R.string.seller_ratting_5_string));
        holder.sellerReview.setText(utility.convertToBangle(bodyResponse.getTotalRating()) + " " + context.getResources().getString(R.string.seller_review_name_string));
        holder.sellerRattingbar.setRating(Float.valueOf(bodyResponse.getSellerRating()));
        holder.sellerMbl.setText(context.getResources().getString(R.string.seller_mbl_string) + utility.Mobile_Number_Read(utility.convertToBangle(bodyResponse.getUserPhoneNo())));
        Glide.with(context).load(bodyResponse.getUserImage()).apply(utility.Glide_Cache_Off()).into(holder.sellerImage);
        holder.sellerDetails.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Bundle bundle = new Bundle();
                bundle.putString("seller_id", bodyResponse.getUserId());
                Fragment navhost = ((AppCompatActivity) context).getSupportFragmentManager().findFragmentById(R.id.nav_host_fragment);
                NavController c = NavHostFragment.findNavController(navhost);
                //c.popBackStack();
                c.navigate(R.id.nav_seller_details, bundle);
            }
        });
    }

    @Override
    public int getItemCount() {
        return sellerModels.size();
    }

    public void filterList(ArrayList<Get_Sellerlist> filterdNames) {
        sellerModels = filterdNames;
        notifyDataSetChanged();
    }
}