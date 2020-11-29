package com.gtech.fishbangla.Adapter;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.gtech.fishbangla.Library.KeyWord;
import com.gtech.fishbangla.Library.Utility;
import com.gtech.fishbangla.MODEL.HISTORY.GET_BUY_HISTORY;
import com.gtech.fishbangla.MODEL.HISTORY.GET_BUY_PRODUCT;
import com.gtech.fishbangla.R;

import java.util.List;

public class HistoryProductAdapter extends RecyclerView.Adapter<HistoryProductAdapter.Todo_View_Holder> {
    Context context;
    List<GET_BUY_PRODUCT> product_list;
    Utility utility;


    public HistoryProductAdapter(List<GET_BUY_PRODUCT> to, Context c) {
        product_list = to;
        context = c;
        utility = new Utility(context);
    }

    public class Todo_View_Holder extends RecyclerView.ViewHolder {
        TextView product_name;
        TextView product_unit;
        TextView product_price;


        public Todo_View_Holder(View view) {
            super(view);
            product_name = view.findViewById(R.id.history_product_name);
            product_unit = view.findViewById(R.id.history_product_unit);
            product_price = view.findViewById(R.id.history_product_price);
        }
    }

    @Override
    public HistoryProductAdapter.Todo_View_Holder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.recycler_history_product, parent, false);

        return new HistoryProductAdapter.Todo_View_Holder(itemView);
    }

    @Override
    public void onBindViewHolder(final HistoryProductAdapter.Todo_View_Holder holder, int position) {
        final GET_BUY_PRODUCT bodyResponse = product_list.get(position);
        utility.logger(bodyResponse.toString());
        try {
            holder.product_name.setText(bodyResponse.getProductName());
            String unit_size = utility.convertToBangle(bodyResponse.getUnitSize());
            if (bodyResponse.getUnitType().equalsIgnoreCase(KeyWord.UNIT_KG)) {
                unit_size = unit_size + " " + context.getResources().getString(R.string.unit_kg_string);
            } else if (bodyResponse.getUnitType().equalsIgnoreCase(KeyWord.UNIT_GRAM)) {
                unit_size = unit_size + " " + context.getResources().getString(R.string.unit_gram_string);
            } else if (bodyResponse.getUnitType().equalsIgnoreCase(KeyWord.UNIT_PIECE)) {
                unit_size = unit_size + " " + context.getResources().getString(R.string.unit_piece_string);
            }
            holder.product_unit.setText(unit_size);
            holder.product_price.setText(utility.convertToBangle(bodyResponse.getPrice()));

        } catch (Exception e) {
            Log.d("Error Line Number", Log.getStackTraceString(e));
        }
    }

    @Override
    public int getItemCount() {
        return product_list.size();
    }
}