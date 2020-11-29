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
import com.gtech.fishbangla.MODEL.HISTORY.GET_SELL_HISTORY;
import com.gtech.fishbangla.R;

import java.util.ArrayList;
import java.util.List;

public class SellHistoryAdapter extends RecyclerView.Adapter<SellHistoryAdapter.Todo_View_Holder> {
    Context context;
    List<GET_SELL_HISTORY> historyModels;
    Utility utility;


    HistoryProductAdapter historyProductAdapter;
    List<GET_BUY_PRODUCT> get_buy_products;

    public SellHistoryAdapter(List<GET_SELL_HISTORY> to, Context c) {
        historyModels = to;
        context = c;
        utility = new Utility(context);
    }

    public class Todo_View_Holder extends RecyclerView.ViewHolder {
        CardView history_more;
        TextView history_orderid;
        TextView history_status;
        TextView history_time;
        TextView history_total;
        Button history_drop;
        LinearLayout history_details;
        RecyclerView history_dynamic;
        TextView history_total_Down;

        public Todo_View_Holder(View view) {
            super(view);
            history_more = view.findViewById(R.id.sell_history_view);
            history_orderid = view.findViewById(R.id.sellhistory_orderid);
            history_status = view.findViewById(R.id.sellhistory_orderstatus);
            history_time = view.findViewById(R.id.sellhistory_time);
            history_drop = view.findViewById(R.id.sell_history_more);
            history_details = view.findViewById(R.id.sell_history_details);
            history_dynamic = view.findViewById(R.id.sell_history_product);
            history_total = view.findViewById(R.id.sell_history_total_price);
            history_total_Down = view.findViewById(R.id.sellhistory_total);
        }
    }

    @Override
    public SellHistoryAdapter.Todo_View_Holder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.recycler_sellhistory, parent, false);

        return new SellHistoryAdapter.Todo_View_Holder(itemView);
    }

    @Override
    public void onBindViewHolder(final SellHistoryAdapter.Todo_View_Holder holder, int position) {
        final GET_SELL_HISTORY bodyResponse = historyModels.get(position);
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
            holder.history_total_Down.setText(context.getResources().getString(R.string.pricesum_string) + " " + utility.convertToBangle(bodyResponse.getTotalAmount() + " " + context.getResources().getString(R.string.taka_string)));
            holder.history_total.setText(context.getResources().getString(R.string.pricesum_string) + " " + utility.convertToBangle(bodyResponse.getTotalAmount() + " " + context.getResources().getString(R.string.taka_string)));
            get_buy_products = new ArrayList<>();
            get_buy_products.addAll(bodyResponse.getProducts());
            historyProductAdapter = new HistoryProductAdapter(get_buy_products, context);
            RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false);
            holder.history_dynamic.setLayoutManager(mLayoutManager);
            holder.history_dynamic.setItemAnimator(new DefaultItemAnimator());
            holder.history_dynamic.setAdapter(historyProductAdapter);
        } catch (Exception e) {
            Log.d("Error Line Number", Log.getStackTraceString(e));
        }
    }

    @Override
    public int getItemCount() {
        return historyModels.size();
    }
}