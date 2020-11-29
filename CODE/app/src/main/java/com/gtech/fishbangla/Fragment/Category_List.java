package com.gtech.fishbangla.Fragment;


import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;
import androidx.viewpager.widget.ViewPager;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.google.android.material.tabs.TabLayout;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.gtech.fishbangla.DATABASE.REPOSITORY.DIVISION_REPOSITORY;
import com.gtech.fishbangla.DATABASE.TABLE.DIVISION_TABLE;
import com.gtech.fishbangla.Http.ApiClient;
import com.gtech.fishbangla.Http.ApiInterface;
import com.gtech.fishbangla.Library.KeyWord;
import com.gtech.fishbangla.Library.Utility;
import com.gtech.fishbangla.MODEL.API_RESPONSE;
import com.gtech.fishbangla.MODEL.CREATE_PRODUCT.Send_CategoryID;
import com.gtech.fishbangla.MODEL.PRODUCT.SEND_FILTER_PRODUCT;
import com.gtech.fishbangla.MODEL.Product_Data;
import com.gtech.fishbangla.R;
import com.jaygoo.widget.OnRangeChangedListener;
import com.jaygoo.widget.RangeSeekBar;

import java.lang.reflect.Type;
import java.util.HashMap;
import java.util.List;

import fr.ganfra.materialspinner.MaterialSpinner;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * A simple {@link Fragment} subclass.
 */
public class Category_List extends Fragment {

    View view;
    Context context;
    Utility utility;
    TabLayout categoryTabs;
    ViewPager categoryViewpager;
    LinearLayout filter;
    int item = 2;

    ApiInterface apiInterface = ApiClient.getBaseClient().create(ApiInterface.class);
    String category_id = "0";

    DIVISION_REPOSITORY divisionRepository;
    List<DIVISION_TABLE> division_tables;

    String min_size = "0";
    String max_size = "2000";
    String min_price = "0";
    String max_price = "5000";


    public Category_List() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_category__list, container, false);
        try {
            context = getActivity();
            utility = new Utility(context);
            if (getArguments() != null) {
                category_id = getArguments().getString("category_id");
                divisionRepository = new DIVISION_REPOSITORY(getActivity().getApplication());
                division_tables = divisionRepository.getdivisionData();
            }
            categoryTabs = view.findViewById(R.id.category_tabs);
            categoryViewpager = view.findViewById(R.id.category_viewpager);
            filter = view.findViewById(R.id.category_filter);
            categoryViewpager.setAdapter(new TAB_ADAPTER((getChildFragmentManager())));
            categoryViewpager.setOffscreenPageLimit(2);
            if (isAdded()) {
                categoryTabs.post(new Runnable() {
                    @Override
                    public void run() {
                        try {
                            categoryTabs.setupWithViewPager(categoryViewpager);
                            changeTabsFont();
                        } catch (Exception ex) {
                            //utility.call_error(ex);
                        }
                    }
                });
            }
            filter.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    showfilterDialouge();
                }
            });
        } catch (Exception e) {
            Log.d("Error Line Number", Log.getStackTraceString(e));
        }
        return view;
    }

    private void changeTabsFont() {
        try {
            ViewGroup vg = (ViewGroup) categoryTabs.getChildAt(0);
            int tabsCount = vg.getChildCount();
            for (int j = 0; j < tabsCount; j++) {
                ViewGroup vgTab = (ViewGroup) vg.getChildAt(j);
                int tabChildsCount = vgTab.getChildCount();
                for (int i = 0; i < tabChildsCount; i++) {
                    View tabViewChild = vgTab.getChildAt(i);
                    if (tabViewChild instanceof TextView) {
                        utility.setFonts(new View[]{tabViewChild});
                        ((TextView) tabViewChild).setTextSize(50);
                    }
                }
            }
        } catch (Exception ex) {
            //utility.call_error(ex);
        }
    }

    class TAB_ADAPTER extends FragmentPagerAdapter {

        public TAB_ADAPTER(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {
            switch (position) {
                case 0:
                    return new AllRetailFragment(context, category_id);
                case 1:
                    return new AllWholesaleFragment(context, category_id);
            }
            return null;
        }

        @Override
        public int getCount() {
            return item;
        }

        @Override
        public CharSequence getPageTitle(int position) {
            switch (position) {
                case 0: {
                    return context.getString(R.string.retail_string);
                }
                case 1: {
                    return context.getString(R.string.wholesale_string);
                }
                /*case 2: return "Schedule";*/
            }
            return null;
        }
    }

    public void showfilterDialouge() {
        try {
            HashMap<String, Integer> screen = utility.getScreenRes();
            int width = screen.get(KeyWord.SCREEN_WIDTH);
            int height = screen.get(KeyWord.SCREEN_HEIGHT);
            int mywidth = (width / 10) * 9;
            final Dialog dialog = new Dialog(context);
            dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
            dialog.getWindow().setBackgroundDrawableResource(android.R.color.transparent);
            dialog.setContentView(R.layout.dialog_fish_filter);
            Button yes = dialog.findViewById(R.id.filteradd);
            Button no = dialog.findViewById(R.id.filtercancel);
            MaterialSpinner filter_product = dialog.findViewById(R.id.filter_product);
            MaterialSpinner filter_division = dialog.findViewById(R.id.filter_division);
            ArrayAdapter<DIVISION_TABLE> division_adapter = new ArrayAdapter<DIVISION_TABLE>(context, android.R.layout.simple_spinner_item, division_tables);
            division_adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            filter_division.setAdapter(division_adapter);
            /*filter_division.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                @Override
                public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                    DIVISION_TABLE selectedItem = (DIVISION_TABLE) parent.getSelectedItem();
                    district_adapter(selectedItem);
                }

                @Override
                public void onNothingSelected(AdapterView<?> parent) {

                }
            });

            filter_division.setAdapter(adapter);*/
            Get_Product_Name(new Send_CategoryID(category_id), filter_product);


            final CheckBox size_check = dialog.findViewById(R.id.filter_size_check);
            final TextView size_result = dialog.findViewById(R.id.size_result);
            RangeSeekBar size_range = dialog.findViewById(R.id.size_range);
            size_range.setIndicatorTextDecimalFormat("0");
            size_range.setRange(10f, 2000f);
            size_range.setOnRangeChangedListener(new OnRangeChangedListener() {
                @Override
                public void onRangeChanged(RangeSeekBar view, float leftValue, float rightValue, boolean isFromUser) {
                    //leftValue is left seekbar value, rightValue is right seekbar value
                    //utility.showToast("o = "+leftValue+" e = "+rightValue);
                    utility.logger("o = " + leftValue + " e = " + rightValue);
                    min_size = Integer.toString((int) Math.floor(leftValue));
                    max_size = Integer.toString((int) Math.floor(rightValue));
                    size_result.setText(utility.convertToBangle(min_size) + " গ্রাম" + " - " + utility.convertToBangle(max_size) + " গ্রাম");
                }

                @Override
                public void onStartTrackingTouch(RangeSeekBar view, boolean isLeft) {
                    //start tracking touch
                }

                @Override
                public void onStopTrackingTouch(RangeSeekBar view, boolean isLeft) {
                    //stop tracking touch
                }
            });

            final CheckBox price_check = dialog.findViewById(R.id.price_check);
            final TextView price_result = dialog.findViewById(R.id.price_result);
            RangeSeekBar price_range = dialog.findViewById(R.id.price_range);
            price_range.setIndicatorTextDecimalFormat("0");
            price_range.setRange(10f, 5000f);
            price_range.setOnRangeChangedListener(new OnRangeChangedListener() {
                @Override
                public void onRangeChanged(RangeSeekBar view, float leftValue, float rightValue, boolean isFromUser) {
                    //leftValue is left seekbar value, rightValue is right seekbar value
                    //utility.showToast("o = "+leftValue+" e = "+rightValue);
                    utility.logger("op = " + leftValue + " ep = " + rightValue);
                    min_price = Integer.toString((int) Math.floor(leftValue));
                    max_price = Integer.toString((int) Math.floor(rightValue));
                    price_result.setText(utility.convertToBangle(min_price) + " ৳" + " - " + utility.convertToBangle(max_price) + " ৳");
                }

                @Override
                public void onStartTrackingTouch(RangeSeekBar view, boolean isLeft) {
                    //start tracking touch
                }

                @Override
                public void onStopTrackingTouch(RangeSeekBar view, boolean isLeft) {
                    //stop tracking touch
                }
            });


            LinearLayout ll = dialog.findViewById(R.id.dialog_layout_size);
            LinearLayout.LayoutParams params = (LinearLayout.LayoutParams) ll.getLayoutParams();
            params.height = LinearLayout.LayoutParams.WRAP_CONTENT;
            params.width = mywidth;
            ll.setLayoutParams(params);
            yes.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    dialog.dismiss();
                    try {
                        SEND_FILTER_PRODUCT send_filter_product = new SEND_FILTER_PRODUCT();
                        send_filter_product.setAppMenuId(category_id);
                        Product_Data product = ((Product_Data) filter_product.getSelectedItem());
                        DIVISION_TABLE division = ((DIVISION_TABLE) filter_division.getSelectedItem());
                        if (product != null) {
                            send_filter_product.setProductMenuId(product.getProductMenuId() + "");
                        } else {
                            send_filter_product.setProductMenuId("");
                        }
                        if (division != null) {
                            send_filter_product.setDivisionId(division.getDivisionId());
                        } else {
                            send_filter_product.setDivisionId("");
                        }
                        if (size_check.isChecked()) {
                            send_filter_product.setMaxAvgWeight(max_size);
                            send_filter_product.setMinAvgWeight(min_size);
                        } else {
                            send_filter_product.setMaxAvgWeight("");
                            send_filter_product.setMinAvgWeight("");
                        }
                        if (price_check.isChecked()) {
                            send_filter_product.setMaxPriceRange(max_price);
                            send_filter_product.setMinPriceRange(min_price);
                        } else {
                            send_filter_product.setMaxPriceRange("");
                            send_filter_product.setMinPriceRange("");
                        }
                        utility.logger("filter" + send_filter_product.toString());
                        Gson gson = new Gson();
                        Intent in = new Intent("PRODUCT_FILTER");
                        in.putExtra("filter", gson.toJson(send_filter_product));
                        context.sendBroadcast(in);
                    } catch (Exception e) {
                        Log.d("Error Line Number", Log.getStackTraceString(e));
                    }
                }
            });
            no.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    dialog.dismiss();
                }
            });
            dialog.setCancelable(true);
            dialog.show();
        } catch (Exception e) {
            Log.d("Error Line Number", Log.getStackTraceString(e));
        }
    }

    private void Get_Product_Name(Send_CategoryID sendProductCategory, MaterialSpinner m) {
        try {
            if (utility.isNetworkAvailable()) {
                Log.d("PRODUCT LIST", sendProductCategory.toString());
                utility.showProgress(false, getResources().getString(R.string.wait_string));
                Call<API_RESPONSE> call = apiInterface.FishBAngla_Product_name_category(utility.getApiKey(), utility.getUSER_ID(), utility.getFirebaseToken(), sendProductCategory);
                call.enqueue(new Callback<API_RESPONSE>() {
                    @Override
                    public void onResponse(Call<API_RESPONSE> call, Response<API_RESPONSE> response) {
                        try {
                            utility.hideProgress();
                            Log.d("PRODUCT LIST", response.toString());
                            if (response.isSuccessful() && response != null) {
                                API_RESPONSE api_response = response.body();
                                if (api_response.getCode() == 200) {
                                    Gson gson = new Gson();
                                    Type listType = new TypeToken<List<Product_Data>>() {
                                    }.getType();
                                    List<Product_Data> get_products = gson.fromJson(response.body().getData(), listType);
                                    Log.d("PRODUCT LIST Size", get_products.size() + "");
                                    ArrayAdapter<Product_Data> product_adapter = new ArrayAdapter<Product_Data>(context, android.R.layout.simple_spinner_item, get_products);
                                    product_adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                                    m.setAdapter(product_adapter);
                                } else {
                                    utility.showDialog(getResources().getString(R.string.try_again_string));
                                }
                            } else {
                                utility.showDialog(getResources().getString(R.string.try_again_string));
                            }
                        } catch (Exception ex) {
                            utility.logger(ex.toString());
                        }
                    }

                    @Override
                    public void onFailure(Call<API_RESPONSE> call, Throwable t) {
                        utility.logger(t.toString());
                        utility.showToast(getResources().getString(R.string.try_again_string));
                        utility.hideProgress();
                    }
                });
            } else {
                utility.showDialog(getResources().getString(R.string.no_internet_string));
            }
        } catch (Exception e) {
            Log.d("Error Line Number", Log.getStackTraceString(e));
        }
    }

}
