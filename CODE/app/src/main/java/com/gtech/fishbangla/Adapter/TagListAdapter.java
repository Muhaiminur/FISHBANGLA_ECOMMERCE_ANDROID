package com.gtech.fishbangla.Adapter;

import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.gtech.fishbangla.Http.ApiClient;
import com.gtech.fishbangla.Http.ApiInterface;
import com.gtech.fishbangla.Library.ENDLESSRECYCLER;
import com.gtech.fishbangla.Library.Utility;
import com.gtech.fishbangla.MODEL.API_RESPONSE;
import com.gtech.fishbangla.MODEL.PRODUCT.Get_Product;
import com.gtech.fishbangla.MODEL.TAG_PRODUCT.GET_TAGLIST;
import com.gtech.fishbangla.MODEL.TAG_PRODUCT.SEND_TAGID;
import com.gtech.fishbangla.R;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class TagListAdapter extends RecyclerView.Adapter<TagListAdapter.Todo_View_Holder> {
    Context context;
    List<GET_TAGLIST> get_taglists;
    Utility utility;

    TaglistProductAdapter taglistProductAdapter;
    List<Get_Product> productList;
    ApiInterface apiInterface = ApiClient.getBaseClient().create(ApiInterface.class);

    public TagListAdapter(List<GET_TAGLIST> to, Context c) {
        get_taglists = to;
        context = c;
        utility = new Utility(context);
    }

    public class Todo_View_Holder extends RecyclerView.ViewHolder {
        CardView tag_view;
        TextView tag_name;
        RecyclerView tag_product;

        public Todo_View_Holder(View view) {
            super(view);
            tag_view = view.findViewById(R.id.tag_view);
            tag_name = view.findViewById(R.id.tag_name);
            tag_product = view.findViewById(R.id.tag_recycler);
        }
    }

    @Override
    public TagListAdapter.Todo_View_Holder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.recycler_tag, parent, false);

        return new TagListAdapter.Todo_View_Holder(itemView);
    }

    @Override
    public void onBindViewHolder(final TagListAdapter.Todo_View_Holder holder, int position) {
        final GET_TAGLIST bodyResponse = get_taglists.get(position);
        try {
            holder.tag_name.setText(bodyResponse.getTagName());
            productList = new ArrayList<>();
            productList.addAll(bodyResponse.getTagData());
            taglistProductAdapter = new TaglistProductAdapter(productList, context);
            LinearLayoutManager mLayoutManager = new LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false);
            holder.tag_product.setLayoutManager(mLayoutManager);
            holder.tag_product.setItemAnimator(new DefaultItemAnimator());
            holder.tag_product.setAdapter(taglistProductAdapter);

            SEND_TAGID send_tagid = new SEND_TAGID(bodyResponse.getTagId() + "");
            // LinearLayoutManager mLayoutManager = new LinearLayoutManager(context);
            //holder.notificationList.setLayoutManager(mLayoutManager);
            ENDLESSRECYCLER scrollListener = new ENDLESSRECYCLER(mLayoutManager) {
                @Override
                public void onLoadMore(int page, int totalItemsCount, RecyclerView view) {
                    // Triggered only when new data needs to be appended to the list
                    // Add whatever code is needed to append new items to the bottom of the list
                    if (productList.size() > 9) {
                        Load_more_Product(send_tagid, page + "", view);
                    }
                }
            };
            // Adds the scroll listener to RecyclerView
            holder.tag_product.addOnScrollListener(scrollListener);
        } catch (Exception e) {
            Log.d("Error Line Number", Log.getStackTraceString(e));
        }
    }

    @Override
    public int getItemCount() {
        return get_taglists.size();
    }

    private void Load_more_Product(SEND_TAGID s, String page, RecyclerView holder) {
        try {
            if (utility.isNetworkAvailable()) {
                utility.logger("load more product tag " + s.toString() + " page = " + page);
                utility.showProgress(false, context.getResources().getString(R.string.wait_string));
                Call<API_RESPONSE> call = apiInterface.FishBAngla_Tag_List_more(utility.getApiKey(), utility.getUSER_ID(), utility.getFirebaseToken(), s, page);
                call.enqueue(new Callback<API_RESPONSE>() {
                    @Override
                    public void onResponse(Call<API_RESPONSE> call, Response<API_RESPONSE> response) {
                        try {
                            utility.hideProgress();
                            utility.logger("load more product tag " + response.toString());
                            if (response.isSuccessful() && response != null) {
                                API_RESPONSE api_response = response.body();
                                if (api_response.getCode() == 200) {
                                    Gson gson = new Gson();
                                    Type listType = new TypeToken<List<Get_Product>>() {
                                    }.getType();
                                    List<Get_Product> get_products = gson.fromJson(response.body().getData(), listType);
                                    utility.logger("load more product tag " + get_products.size() + "");
                                    productList.addAll(get_products);
                                    if (get_products.size() > 0) {
                                        taglistProductAdapter = new TaglistProductAdapter(productList, context);
                                        taglistProductAdapter.notifyDataSetChanged();
                                        holder.setAdapter(taglistProductAdapter);
                                        holder.scrollToPosition(productList.size() > 9 ? productList.size() - get_products.size() - 2 : 0);
                                    } else if (productList.size() == 0) {
                                        utility.showDialog(context.getResources().getString(R.string.notification_no_data_string));
                                    }
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
            utility.logger("Error Line Number" + Log.getStackTraceString(e));
        }
    }

    public void update() {
        if (taglistProductAdapter != null && productList != null) {
            taglistProductAdapter.update();
        }
    }
}