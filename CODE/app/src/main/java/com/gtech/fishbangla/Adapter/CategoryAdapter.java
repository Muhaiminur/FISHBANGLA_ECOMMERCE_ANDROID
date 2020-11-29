package com.gtech.fishbangla.Adapter;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.fragment.NavHostFragment;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.gtech.fishbangla.Activity.Auction_Details;
import com.gtech.fishbangla.Activity.Auction_List;
import com.gtech.fishbangla.Library.KeyWord;
import com.gtech.fishbangla.Library.Utility;
import com.gtech.fishbangla.MODEL.Category_Data;
import com.gtech.fishbangla.MODEL.Category_Model;
import com.gtech.fishbangla.R;

import java.util.List;

public class CategoryAdapter extends RecyclerView.Adapter<CategoryAdapter.Todo_View_Holder> {
    Context context;
    List<Category_Data> category_list;
    Utility utility;


    public CategoryAdapter(List<Category_Data> to, Context c) {
        category_list = to;
        context = c;
        utility = new Utility(context);
    }

    public class Todo_View_Holder extends RecyclerView.ViewHolder {
        ImageView category_image;
        TextView category_name;

        public Todo_View_Holder(View view) {
            super(view);
            category_image = view.findViewById(R.id.category_image);
            category_name = view.findViewById(R.id.category_name);
        }
    }

    @Override
    public Todo_View_Holder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.recycler_category, parent, false);

        return new Todo_View_Holder(itemView);
    }

    @Override
    public void onBindViewHolder(final Todo_View_Holder holder, int position) {
        final Category_Data bodyResponse = category_list.get(position);
        holder.category_name.setText(bodyResponse.getAppMenuTitle());
        Glide.with(context).load(/*utility.getBaseUrl() +*/ bodyResponse.getAppMenuPicture()).apply(utility.Glide_Cache_On()).into(holder.category_image);
        holder.category_image.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (bodyResponse.getAppMenuIsCatg().equalsIgnoreCase(KeyWord.NO)) {
                    if (bodyResponse.getAppMenuMetadata().equalsIgnoreCase(KeyWord.AUCTION)) {
                        context.startActivity(new Intent(context, Auction_List.class));
                    } else if (bodyResponse.getAppMenuMetadata().equalsIgnoreCase(KeyWord.REQUEST)) {
                        Fragment navhost = ((AppCompatActivity) context).getSupportFragmentManager().findFragmentById(R.id.nav_host_fragment);
                        NavController c = NavHostFragment.findNavController(navhost);
                        c.navigate(R.id.nav_request);
                    }
                } else if (bodyResponse.getAppMenuIsCatg().equalsIgnoreCase(KeyWord.YES)) {
                    Bundle bundle = new Bundle();
                    bundle.putString("category_id", bodyResponse.getAppMenuId());
                    Fragment navhost = ((AppCompatActivity) context).getSupportFragmentManager().findFragmentById(R.id.nav_host_fragment);
                    NavController c = NavHostFragment.findNavController(navhost);
                    //c.popBackStack();
                    c.navigate(R.id.frag_category_list, bundle);
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return category_list.size();
    }
}