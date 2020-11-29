package com.gtech.fishbangla.Adapter;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RatingBar;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.fragment.NavHostFragment;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.gtech.fishbangla.Library.KeyWord;
import com.gtech.fishbangla.Library.Utility;
import com.gtech.fishbangla.MODEL.User.Get_Sellerlist;
import com.gtech.fishbangla.R;
import com.mikhaellopez.circularimageview.CircularImageView;
import com.nguyenhoanglam.imagepicker.model.Image;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class ImagePreviewtAdapter extends RecyclerView.Adapter<ImagePreviewtAdapter.Todo_View_Holder> {
    Context context;
    List<Image> image_list;
    Utility utility;


    public ImagePreviewtAdapter(List<Image> to, Context c) {
        image_list = to;
        context = c;
        utility = new Utility(context);
    }

    public class Todo_View_Holder extends RecyclerView.ViewHolder {
        ImageView product_image;
        Button image_remove;

        public Todo_View_Holder(View view) {
            super(view);
            product_image = view.findViewById(R.id.image_preview);
            image_remove = view.findViewById(R.id.image_preview_delete);
        }
    }

    @Override
    public ImagePreviewtAdapter.Todo_View_Holder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.recycler_imagepreview, parent, false);

        return new ImagePreviewtAdapter.Todo_View_Holder(itemView);
    }

    @Override
    public void onBindViewHolder(final ImagePreviewtAdapter.Todo_View_Holder holder, int position) {
        try {
            final Image bodyResponse = image_list.get(position);
            Glide.with(context).load(bodyResponse.getPath()).apply(utility.Glide_Cache_Off()).into(holder.product_image);
            holder.image_remove.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Dioalog_Yes(context.getResources().getString(R.string.add_image_delete_string),position);
                }
            });
        } catch (Exception e) {
            Log.d("Error Line Number", Log.getStackTraceString(e));
        }
    }

    @Override
    public int getItemCount() {
        return image_list.size();
    }

    public void Dioalog_Yes(String msg,int pos) {
        try {
            HashMap<String, Integer> screen = utility.getScreenRes();
            int width = screen.get(KeyWord.SCREEN_WIDTH);
            int height = screen.get(KeyWord.SCREEN_HEIGHT);
            int mywidth = (width / 10) * 7;
            final Dialog dialog = new Dialog(context);
            dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
            dialog.getWindow().setBackgroundDrawableResource(android.R.color.transparent);
            dialog.setContentView(R.layout.dialog_toast_yes_no);
            TextView tv = dialog.findViewById(R.id.tv_message);
            Button yes = dialog.findViewById(R.id.btn_yes);
            Button no = dialog.findViewById(R.id.btn_no);
            no.setVisibility(View.GONE);
            tv.setText(msg);
            LinearLayout ll = dialog.findViewById(R.id.dialog_layout_size);
            LinearLayout.LayoutParams params = (LinearLayout.LayoutParams) ll.getLayoutParams();
            params.height = LinearLayout.LayoutParams.WRAP_CONTENT;
            params.width = mywidth;
            ll.setLayoutParams(params);
            yes.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    dialog.dismiss();
                    Intent in = new Intent("image_remove");
                    in.putExtra("image_id", String.valueOf(pos));
                    context.sendBroadcast(in);
                }
            });
            no.setOnClickListener(new View.OnClickListener() {
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