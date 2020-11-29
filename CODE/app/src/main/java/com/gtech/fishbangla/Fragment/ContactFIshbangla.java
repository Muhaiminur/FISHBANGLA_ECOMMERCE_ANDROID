package com.gtech.fishbangla.Fragment;


import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;

import androidx.browser.customtabs.CustomTabsIntent;
import androidx.core.content.ContextCompat;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;

import android.text.Html;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.gtech.fishbangla.DATABASE.REPOSITORY.CONFIG_REPOSITORY;
import com.gtech.fishbangla.DATABASE.TABLE.CONFIG_TABLE;
import com.gtech.fishbangla.Library.Utility;
import com.gtech.fishbangla.R;
import com.gtech.fishbangla.databinding.FragmentContactFishbanglaBinding;

/**
 * A simple {@link Fragment} subclass.
 */
public class ContactFIshbangla extends Fragment {
    Context context;
    Utility utility;
    CONFIG_REPOSITORY config_repository;
    FragmentContactFishbanglaBinding contactFishbanglaBinding;
    CONFIG_TABLE config_table;

    public ContactFIshbangla() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        contactFishbanglaBinding = DataBindingUtil.inflate(inflater, R.layout.fragment_contact_fishbangla, container, false);
        try {
            context = getActivity();
            utility = new Utility(context);
            config_repository = new CONFIG_REPOSITORY(getActivity().getApplication());
            config_table = config_repository.getconfigData();
            contactFishbanglaBinding.contactMobile.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(Intent.ACTION_DIAL);
                    intent.setData(Uri.parse("tel:" + getResources().getString(R.string.contact_mobile_title)));
                    startActivity(intent);
                }
            });
            contactFishbanglaBinding.contactWebsite.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    try {
                        String url = context.getResources().getString(R.string.contact_website_url);
                        CustomTabsIntent.Builder builder = new CustomTabsIntent.Builder();
                        builder.setToolbarColor(ContextCompat.getColor(context, R.color.colorAccent));
                        CustomTabsIntent customTabsIntent = builder.build();
                        customTabsIntent.launchUrl(context, Uri.parse(url));
                    } catch (Exception e) {
                        Log.d("Error Line Number", Log.getStackTraceString(e));
                        try {
                            Uri uri = Uri.parse(context.getResources().getString(R.string.contact_website_url)); // missing 'http://' will cause crashed
                            Intent intent = new Intent(Intent.ACTION_VIEW, uri);
                            context.startActivity(intent);
                        } catch (Exception e2) {
                            Log.d("Error Line Number", Log.getStackTraceString(e2));
                            utility.showDialog(context.getResources().getString(R.string.no_browser_string));
                        }
                    }
                }
            });
            contactFishbanglaBinding.contactFb.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    try {
                        String url = context.getResources().getString(R.string.contact_facebook_url);
                        CustomTabsIntent.Builder builder = new CustomTabsIntent.Builder();
                        builder.setToolbarColor(ContextCompat.getColor(context, R.color.colorAccent));
                        CustomTabsIntent customTabsIntent = builder.build();
                        customTabsIntent.launchUrl(context, Uri.parse(url));
                    } catch (Exception e) {
                        Log.d("Error Line Number", Log.getStackTraceString(e));
                        try {
                            Uri uri = Uri.parse(context.getResources().getString(R.string.contact_facebook_url)); // missing 'http://' will cause crashed
                            Intent intent = new Intent(Intent.ACTION_VIEW, uri);
                            context.startActivity(intent);
                        } catch (Exception e2) {
                            Log.d("Error Line Number", Log.getStackTraceString(e2));
                            utility.showDialog(context.getResources().getString(R.string.no_browser_string));
                        }
                    }
                }
            });
            if (config_repository != null && !TextUtils.isEmpty(config_table.getFishbangla_address())) {
                //utility.logger("address" + config_table.getFishbangla_address());
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                    contactFishbanglaBinding.cantactData.setText(Html.fromHtml(config_table.getFishbangla_address(), Html.FROM_HTML_MODE_COMPACT));
                } else {
                    contactFishbanglaBinding.cantactData.setText(Html.fromHtml(config_table.getFishbangla_address()));
                }
            }
        } catch (Exception e) {
            Log.d("Error Line Number", Log.getStackTraceString(e));
        }
        return contactFishbanglaBinding.getRoot();
    }

}
