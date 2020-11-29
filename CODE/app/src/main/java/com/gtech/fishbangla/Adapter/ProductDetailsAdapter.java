package com.gtech.fishbangla.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;

import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.gtech.fishbangla.Library.KeyWord;
import com.gtech.fishbangla.Library.Utility;
import com.gtech.fishbangla.MODEL.Banner.Product_Details_Banner;
import com.gtech.fishbangla.R;
import com.opensooq.pluto.base.PlutoAdapter;
import com.opensooq.pluto.base.PlutoViewHolder;

import java.util.List;

public class ProductDetailsAdapter extends PlutoAdapter<Product_Details_Banner, ProductDetailsAdapter.Product_Details_Viewmodel> {

    public ProductDetailsAdapter(List<Product_Details_Banner> items) {
        super(items);
    }

    @Override
    public ProductDetailsAdapter.Product_Details_Viewmodel getViewHolder(ViewGroup parent, int viewType) {
        return new Product_Details_Viewmodel(parent, R.layout.banner_product_details);
    }

    public static class Product_Details_Viewmodel extends PlutoViewHolder<Product_Details_Banner> {
        ImageView banner_image;
        ImageButton banner_button;

        public Product_Details_Viewmodel(ViewGroup parent, int itemLayoutId) {
            super(parent, itemLayoutId);
            banner_image = getView(R.id.banner_details_image);
            banner_button = getView(R.id.banner_details_play);
        }

        @Override
        public void set(Product_Details_Banner item, int pos) {
            /*//  yourImageLoader.with(mContext).load(item.getPosterId()).into(ivPoster);
            tvRating.setText(item.getImdbRating());*/
            RequestOptions requestOptions = new RequestOptions();
            requestOptions.error(R.drawable.ic_default);
            Glide.with(getContext()).load(item.getMedia_url()).apply(requestOptions).into(banner_image);
            if (item.getMedia_type().equalsIgnoreCase(KeyWord.VIDEO_TYPE)) {
                banner_button.setVisibility(View.VISIBLE);
            } else {
                banner_button.setVisibility(View.GONE);
            }
        }
    }
}
