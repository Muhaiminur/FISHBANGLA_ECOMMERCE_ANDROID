package com.gtech.fishbangla.Fragment;


import android.content.Context;
import android.os.Bundle;

import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.gtech.fishbangla.Adapter.SellerListAdapter;
import com.gtech.fishbangla.Http.ApiClient;
import com.gtech.fishbangla.Http.ApiInterface;
import com.gtech.fishbangla.Library.KeyWord;
import com.gtech.fishbangla.Library.Utility;
import com.gtech.fishbangla.MODEL.API_RESPONSE;
import com.gtech.fishbangla.MODEL.PRODUCT.SEND_SEARCH;
import com.gtech.fishbangla.MODEL.Product_Data;
import com.gtech.fishbangla.MODEL.RECYCLER_MODEL.Seller_Model;
import com.gtech.fishbangla.MODEL.User.Get_Sellerlist;
import com.gtech.fishbangla.R;
import com.gtech.fishbangla.databinding.FragmentSellerListBinding;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * A simple {@link Fragment} subclass.
 */
public class SellerList extends Fragment {
    Context context;
    Utility utility;
    List<Get_Sellerlist> sellerModelList;
    SellerListAdapter sellerListAdapter;
    FragmentSellerListBinding sellerListBinding;

    ApiInterface apiInterface = ApiClient.getBaseClient().create(ApiInterface.class);

    public SellerList() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        if (sellerListBinding == null) {
            sellerListBinding = DataBindingUtil.inflate(inflater, R.layout.fragment_seller_list, container, false);
            try {
                context = getActivity();
                utility = new Utility(context);
                initial_history();
                sellerListBinding.sellerSearch.addTextChangedListener(new TextWatcher() {
                    @Override
                    public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

                    }

                    @Override
                    public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

                    }

                    @Override
                    public void afterTextChanged(Editable editable) {
                        //after the change calling the method and passing the search input
                        filter(editable.toString());
                    }
                });

                sellerListBinding.sellerSearchProduct.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                    @Override
                    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                        try {
                            Product_Data product_data = (Product_Data) parent.getSelectedItem();
                            if (product_data != null) {
                                Get_Seller_List_filter(new SEND_SEARCH(product_data.getProductMenuId() + ""));
                            }
                        } catch (Exception e) {
                            Log.d("Error Line Number", Log.getStackTraceString(e));
                        }
                    }

                    @Override
                    public void onNothingSelected(AdapterView<?> parent) {

                    }
                });
            } catch (Exception e) {
                Log.d("Error Line Number", Log.getStackTraceString(e));
            }
        }
        return sellerListBinding.getRoot();
    }

    public void initial_history() {
        sellerModelList = new ArrayList<>();
        sellerListAdapter = new SellerListAdapter(sellerModelList, context);
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false);
        sellerListBinding.sellerRecycler.setLayoutManager(mLayoutManager);
        sellerListBinding.sellerRecycler.setItemAnimator(new DefaultItemAnimator());
        sellerListBinding.sellerRecycler.setAdapter(sellerListAdapter);
        Get_Seller_List();
    }

    private void Get_Seller_List() {
        try {
            if (utility.isNetworkAvailable()) {
                utility.showProgress(false, getResources().getString(R.string.wait_string));
                Call<API_RESPONSE> call = apiInterface.FishBAngla_Seller_List(utility.getApiKey(), utility.getUSER_ID(), utility.getFirebaseToken());
                call.enqueue(new Callback<API_RESPONSE>() {
                    @Override
                    public void onResponse(Call<API_RESPONSE> call, Response<API_RESPONSE> response) {
                        try {
                            //utility.hideProgress();
                            Log.d("SELLER LIST", response.toString());
                            if (response.isSuccessful() && response != null) {
                                API_RESPONSE api_response = response.body();
                                if (api_response.getCode() == 200) {
                                    Gson gson = new Gson();
                                    Type listType = new TypeToken<List<Get_Sellerlist>>() {
                                    }.getType();
                                    List<Get_Sellerlist> get_products = gson.fromJson(response.body().getData(), listType);
                                    Log.d("SELLER LIST Size", get_products.size() + "");
                                    if (get_products.size() > 0) {
                                        sellerModelList.clear();
                                        sellerModelList.addAll(get_products);
                                        sellerListAdapter.notifyDataSetChanged();
                                    } else {
                                        utility.showDialog(context.getResources().getString(R.string.seller_no_data_string));
                                    }
                                } else {
                                    utility.showDialog(getResources().getString(R.string.try_again_string));
                                }
                                Get_Product_Name();
                            } else {
                                utility.hideProgress();
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

    private void Get_Product_Name() {
        try {
            if (utility.isNetworkAvailable()) {
                //utility.showProgress(false, getResources().getString(R.string.wait_string));
                Call<API_RESPONSE> call = apiInterface.FishBAngla_Product_Name(utility.getApiKey(), utility.getUSER_ID(), utility.getFirebaseToken());
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
                                    sellerListBinding.sellerSearchProduct.setAdapter(product_adapter);
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

    private void Get_Seller_List_filter(SEND_SEARCH send_search) {
        try {
            if (utility.isNetworkAvailable()) {
                utility.showProgress(false, getResources().getString(R.string.wait_string));
                utility.logger("FILTER size seller" + send_search.toString());
                Call<API_RESPONSE> call = apiInterface.FishBAngla_Filter_seller(utility.getApiKey(), utility.getUSER_ID(), utility.getFirebaseToken(), send_search);
                call.enqueue(new Callback<API_RESPONSE>() {
                    @Override
                    public void onResponse(Call<API_RESPONSE> call, Response<API_RESPONSE> response) {
                        try {
                            utility.hideProgress();
                            Log.d("SELLER FILTER LIST", response.toString());
                            if (response.isSuccessful() && response != null) {
                                API_RESPONSE api_response = response.body();
                                if (api_response.getCode() == 200) {
                                    Gson gson = new Gson();
                                    Type listType = new TypeToken<List<Get_Sellerlist>>() {
                                    }.getType();
                                    List<Get_Sellerlist> get_products = gson.fromJson(response.body().getData(), listType);
                                    Log.d("SELLER FILTER Size", get_products.size() + "");
                                    if (get_products.size() > 0) {
                                        sellerModelList.clear();
                                        sellerModelList.addAll(get_products);
                                        sellerListAdapter.notifyDataSetChanged();
                                    } else {
                                        utility.showDialog(context.getResources().getString(R.string.seller_no_data_string));
                                    }
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

    private void filter(String text) {
        try {
            //new array list that will hold the filtered data
            ArrayList<Get_Sellerlist> filterdNames = new ArrayList<>();

            //looping through existing elements
            for (Get_Sellerlist s : sellerModelList) {
                //if the existing elements contains the search input
                if (s.getUserFullName().toLowerCase().contains(text.toLowerCase()) || s.getUserPhoneNo().toLowerCase().contains(text.toLowerCase())) {
                    //adding the element to filtered list
                    filterdNames.add(s);
                }
            }

            //calling a method of the adapter class and passing the filtered list
            sellerListAdapter.filterList(filterdNames);
            /*sellerModelList_filter.clear();
            sellerModelList_filter.addAll(filterdNames);
            sellerListAdapter.notifyDataSetChanged();*/
        } catch (Exception e) {
            Log.d("Error Line Number", Log.getStackTraceString(e));
        }
    }
}
