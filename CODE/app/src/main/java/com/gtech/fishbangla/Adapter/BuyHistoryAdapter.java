package com.gtech.fishbangla.Adapter;

import android.content.Context;
import android.graphics.Typeface;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.gtech.fishbangla.Library.KeyWord;
import com.gtech.fishbangla.Library.Utility;
import com.gtech.fishbangla.MODEL.HISTORY.GET_BUY_HISTORY;
import com.gtech.fishbangla.MODEL.HISTORY.GET_BUY_PRODUCT;
import com.gtech.fishbangla.MODEL.RECYCLER_MODEL.History_Model;
import com.gtech.fishbangla.R;

import java.util.ArrayList;
import java.util.List;

public class BuyHistoryAdapter extends RecyclerView.Adapter<BuyHistoryAdapter.Todo_View_Holder> {
    Context context;
    List<GET_BUY_HISTORY> historyModels;
    Utility utility;

    HistoryProductAdapter historyProductAdapter;
    List<GET_BUY_PRODUCT> get_buy_products;


    public BuyHistoryAdapter(List<GET_BUY_HISTORY> to, Context c) {
        historyModels = to;
        context = c;
        utility = new Utility(context);
    }

    public class Todo_View_Holder extends RecyclerView.ViewHolder {
        CardView history_more;
        TextView history_orderid;
        TextView history_status;
        TextView history_time;
        TextView history_totalprice;
        Button history_drop;
        LinearLayout history_details;
        RecyclerView history_product;
        TextView history_ice;
        TextView history_service;
        TextView history_delivery;
        TextView history_vat;
        TextView history_discount;
        TextView history_subtotal;
        TextView history_total;
        TextView history_name;
        TextView history_mobile;
        TextView history_village;
        TextView history_road;

        public Todo_View_Holder(View view) {
            super(view);
            history_more = view.findViewById(R.id.history_view);
            history_orderid = view.findViewById(R.id.buyhistory_orderid);
            history_status = view.findViewById(R.id.buyhistory_orderstatus);
            history_time = view.findViewById(R.id.buyhistory_time);
            history_totalprice = view.findViewById(R.id.buyhistory_total_price);
            history_drop = view.findViewById(R.id.history_more);
            history_details = view.findViewById(R.id.history_details);
            history_product = view.findViewById(R.id.buyhistory_product);
            history_ice = view.findViewById(R.id.buyhistory_ice);
            history_service = view.findViewById(R.id.buyhistory_service);
            history_delivery = view.findViewById(R.id.buyhistory_delivery);
            history_vat = view.findViewById(R.id.buyhistory_vat);
            history_discount = view.findViewById(R.id.buyhistory_discount);
            history_subtotal = view.findViewById(R.id.buyhistory_subtotal);
            history_total = view.findViewById(R.id.buyhistory_total);
            history_name = view.findViewById(R.id.buyhistory_address_name);
            history_mobile = view.findViewById(R.id.buyhistory_address_mbl);
            history_village = view.findViewById(R.id.buyhistory_address_village);
            history_road = view.findViewById(R.id.buyhistory_address_road);
        }
    }

    @Override
    public BuyHistoryAdapter.Todo_View_Holder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.recycler_buy_history, parent, false);

        return new BuyHistoryAdapter.Todo_View_Holder(itemView);
    }

    @Override
    public void onBindViewHolder(final BuyHistoryAdapter.Todo_View_Holder holder, int position) {
        final GET_BUY_HISTORY bodyResponse = historyModels.get(position);
        try {
            holder.history_more.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (holder.history_details.getVisibility() == View.VISIBLE) {
                        holder.history_details.setVisibility(View.GONE);
                        holder.history_drop.setBackground(context.getResources().getDrawable(R.drawable.ic_down_arrow));
                    } else if (holder.history_details.getVisibility() == View.GONE) {
                        holder.history_details.setVisibility(View.VISIBLE);
                        holder.history_drop.setBackground(context.getResources().getDrawable(R.drawable.ic_up_arrow));
                    }
                }
            });
            holder.history_orderid.setText(context.getResources().getString(R.string.orderno_string) + " " + utility.convertToBangle(bodyResponse.getTransactionId()));
            if (bodyResponse.getOrderStatus().equalsIgnoreCase(KeyWord.ORDER_ACCEPT)) {
                holder.history_status.setText(context.getResources().getText(R.string.history_accept_string));
                holder.history_status.setTextColor(context.getResources().getColor(android.R.color.holo_orange_dark));
            } else if (bodyResponse.getOrderStatus().equalsIgnoreCase(KeyWord.ORDER_CANCEL)) {
                holder.history_status.setText(context.getResources().getText(R.string.history_cancel_string));
                holder.history_status.setTextColor(context.getResources().getColor(R.color.app_red));
            } else if (bodyResponse.getOrderStatus().equalsIgnoreCase(KeyWord.ORDER_PENDING)) {
                holder.history_status.setText(context.getResources().getText(R.string.history_pending_string));
                holder.history_status.setTextColor(context.getResources().getColor(android.R.color.holo_orange_light));
            } else if (bodyResponse.getOrderStatus().equalsIgnoreCase(KeyWord.ORDER_PROCESS)) {
                holder.history_status.setText(context.getResources().getText(R.string.history_process_string));
                holder.history_status.setTextColor(context.getResources().getColor(android.R.color.holo_orange_dark));
            } else if (bodyResponse.getOrderStatus().equalsIgnoreCase(KeyWord.ORDER_ON_WAY)) {
                holder.history_status.setText(context.getResources().getText(R.string.history_onway_string));
                holder.history_status.setTextColor(context.getResources().getColor(android.R.color.holo_blue_bright));
            } else if (bodyResponse.getOrderStatus().equalsIgnoreCase(KeyWord.ORDER_DELIVER)) {
                holder.history_status.setText(context.getResources().getText(R.string.history_success_string));
                holder.history_status.setTextColor(context.getResources().getColor(R.color.app_green));
            }
            holder.history_time.setText(context.getResources().getString(R.string.history_time_string) + " " + utility.convertToBangle(bodyResponse.getOderDate()));
            holder.history_totalprice.setText(context.getResources().getString(R.string.pricesum_string) + " " + utility.convertToBangle(bodyResponse.getTotalAfterDiscount()) + " " + context.getResources().getString(R.string.taka_string));
            holder.history_ice.setText(context.getResources().getString(R.string.ice_string) + " " + utility.convertToBangle(bodyResponse.getIcePrice()) + " " + context.getResources().getString(R.string.taka_sign_string));
            holder.history_service.setText(context.getResources().getString(R.string.servicecharge_string) + " " + utility.convertToBangle(bodyResponse.getServiceCharge() + " " + context.getResources().getString(R.string.taka_sign_string)));
            holder.history_delivery.setText(context.getResources().getString(R.string.delivery_string) + " " + utility.convertToBangle(bodyResponse.getDeliveryCharge() + " " + context.getResources().getString(R.string.taka_sign_string)));
            holder.history_vat.setText(context.getResources().getString(R.string.vat_string) + " " + utility.convertToBangle(bodyResponse.getProductVat() + " " + context.getResources().getString(R.string.taka_sign_string)));
            holder.history_discount.setText(context.getResources().getString(R.string.discount_string) + " " + utility.convertToBangle(bodyResponse.getDiscountAmount() + " " + context.getResources().getString(R.string.taka_sign_string)));
            holder.history_subtotal.setText(context.getResources().getString(R.string.history_sub_price_string) + " " + utility.convertToBangle(bodyResponse.getTotalAmount() + " " + context.getResources().getString(R.string.taka_sign_string)));
            holder.history_total.setText(context.getResources().getString(R.string.pricesum_string) + " " + utility.convertToBangle(bodyResponse.getTotalAfterDiscount() + " " + context.getResources().getString(R.string.taka_string)));
            holder.history_name.setText(bodyResponse.getUserFullName());
            holder.history_mobile.setText(/*utility.Mobile_Number_Read(utility.convertToBangle(bodyResponse.get))*/bodyResponse.getUserAddress());
            holder.history_village.setText(bodyResponse.getUserVillage() + ", " + bodyResponse.getNearbyLandmark());
            holder.history_road.setText(/*bodyResponse.getUserAddress()+ ", "+ */bodyResponse.getUpazillaName() + ", " + bodyResponse.getDistrictName() + ", " + bodyResponse.getDivisionName());

            get_buy_products = new ArrayList<>();
            get_buy_products.addAll(bodyResponse.getProducts());
            historyProductAdapter = new HistoryProductAdapter(get_buy_products, context);
            RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false);
            holder.history_product.setLayoutManager(mLayoutManager);
            holder.history_product.setItemAnimator(new DefaultItemAnimator());
            holder.history_product.setAdapter(historyProductAdapter);
        } catch (Exception e) {
            Log.d("Error Line Number", Log.getStackTraceString(e));
        }
    }

    @Override
    public int getItemCount() {
        return historyModels.size();
    }
}