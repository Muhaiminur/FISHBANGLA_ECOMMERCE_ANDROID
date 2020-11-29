package com.gtech.fishbangla.Activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.fragment.NavHostFragment;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.LinearLayout;

import com.google.android.material.textfield.TextInputEditText;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.gtech.fishbangla.Adapter.SearchAdapter;
import com.gtech.fishbangla.Adapter.TagListAdapter;
import com.gtech.fishbangla.DATABASE.REPOSITORY.USER_REPOSITORY;
import com.gtech.fishbangla.DATABASE.TABLE.USER_PROFILE;
import com.gtech.fishbangla.Http.ApiClient;
import com.gtech.fishbangla.Http.ApiInterface;
import com.gtech.fishbangla.Library.KeyWord;
import com.gtech.fishbangla.Library.Utility;
import com.gtech.fishbangla.MODEL.API_RESPONSE;
import com.gtech.fishbangla.MODEL.ISSUE.SEND_ISSUE;
import com.gtech.fishbangla.MODEL.PRODUCT.Get_Product;
import com.gtech.fishbangla.MODEL.PRODUCT.SEND_SEARCH;
import com.gtech.fishbangla.MODEL.Product_Data;
import com.gtech.fishbangla.MODEL.RECYCLER_MODEL.Fish_Model;
import com.gtech.fishbangla.MODEL.TAG_PRODUCT.GET_TAGLIST;
import com.gtech.fishbangla.R;

import org.json.JSONObject;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import fr.ganfra.materialspinner.MaterialSpinner;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import com.gtech.fishbangla.databinding.ActivitySearchProductBinding;

public class SearchProduct extends AppCompatActivity {

    Utility utility;
    Context context;
    List<Get_Product> suggestion_list = new ArrayList<>();
    List<GET_TAGLIST> tag_list = new ArrayList<>();
    SearchAdapter homeSuggestAdapter;
    TagListAdapter tagListAdapter;
    ApiInterface apiInterface = ApiClient.getBaseClient().create(ApiInterface.class);
    ActivitySearchProductBinding searchProductBinding;
    USER_REPOSITORY user_dao;
    USER_PROFILE userProfile;
    Gson gson = new Gson();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search_product);
        getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle(getResources().getString(R.string.search_string));
        try {
            context = this;
            utility = new Utility(context);
            user_dao = new USER_REPOSITORY(getApplication());
            userProfile = user_dao.getuserData();
            searchProductBinding = DataBindingUtil.setContentView(this, R.layout.activity_search_product);
            searchProductBinding.searchAddView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    try {
                        //showNewFishDialog();
                        //open request fragment
                        Intent intent = new Intent(context, Home_Page.class);
                        intent.putExtra("search_product", "search");
                        context.startActivity(intent);
                    } catch (Exception e) {
                        Log.d("Error Line Number", Log.getStackTraceString(e));
                    }
                }
            });
            Get_Product_Name();
            searchProductBinding.searchProduct.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                @Override
                public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                    try {
                        Product_Data product_data = (Product_Data) parent.getSelectedItem();
                        if (product_data != null) {
                            searchProductBinding.searchAddView.setVisibility(View.GONE);
                            initial_suggestion();
                            Get_Search_Product(new SEND_SEARCH(String.valueOf(product_data.getProductMenuId())));
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

    public void initial_suggestion() {
        homeSuggestAdapter = new SearchAdapter(suggestion_list, context);
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false);
        searchProductBinding.searchRecycler.setLayoutManager(mLayoutManager);
        searchProductBinding.searchRecycler.setItemAnimator(new DefaultItemAnimator());
        searchProductBinding.searchRecycler.setAdapter(homeSuggestAdapter);
    }

    private void Get_Product_Name() {
        try {
            if (utility.isNetworkAvailable()) {
                utility.showProgress(false, getResources().getString(R.string.wait_string));
                Call<API_RESPONSE> call = apiInterface.FishBAngla_Product_Name(utility.getApiKey(), utility.getUSER_ID(), utility.getFirebaseToken());
                call.enqueue(new Callback<API_RESPONSE>() {
                    @Override
                    public void onResponse(Call<API_RESPONSE> call, Response<API_RESPONSE> response) {
                        try {
                            //utility.hideProgress();
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
                                    searchProductBinding.searchProduct.setAdapter(product_adapter);
                                    initial_tag();
                                } else {
                                    utility.hideProgress();
                                    utility.showDialog(getResources().getString(R.string.try_again_string));
                                }
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

    public void showNewFishDialog() {
        try {
            HashMap<String, Integer> screen = utility.getScreenRes();
            int width = screen.get(KeyWord.SCREEN_WIDTH);
            int height = screen.get(KeyWord.SCREEN_HEIGHT);
            int mywidth = (width / 10) * 9;
            final Dialog dialog = new Dialog(this);
            dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
            dialog.getWindow().setBackgroundDrawableResource(android.R.color.transparent);
            dialog.setContentView(R.layout.dialog_new_search);
            final TextInputEditText name = dialog.findViewById(R.id.search_new_input);
            Button yes = dialog.findViewById(R.id.search_add);
            Button no = dialog.findViewById(R.id.searchcancel);
            //utility.setFonts(new View[]{no, yes});
            LinearLayout ll = dialog.findViewById(R.id.dialog_layout_size);
            LinearLayout.LayoutParams params = (LinearLayout.LayoutParams) ll.getLayoutParams();
            params.height = LinearLayout.LayoutParams.WRAP_CONTENT;
            params.width = mywidth;
            ll.setLayoutParams(params);
            yes.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    dialog.dismiss();
                    if (!TextUtils.isEmpty(name.getEditableText().toString())) {
                        try {
                            if (userProfile != null && !TextUtils.isEmpty(name.getEditableText().toString())) {
                                Send_Issue(new SEND_ISSUE(userProfile.getUserId(), "2", name.getEditableText().toString()));
                            }
                        } catch (Exception e) {
                            Log.d("Error Line Number", Log.getStackTraceString(e));
                        }
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

    private void Send_Issue(SEND_ISSUE send_issue) {
        try {
            if (utility.isNetworkAvailable()) {
                utility.logger("Send Issue" + send_issue.toString());
                utility.showProgress(false, getResources().getString(R.string.wait_string));
                Call<API_RESPONSE> call = apiInterface.FishBAngla_Send_Issue(utility.getApiKey(), utility.getUSER_ID(), utility.getFirebaseToken(), send_issue);
                call.enqueue(new Callback<API_RESPONSE>() {
                    @Override
                    public void onResponse(Call<API_RESPONSE> call, Response<API_RESPONSE> response) {
                        try {
                            utility.hideProgress();
                            Log.d("Send Issue", response.toString());
                            if (response.isSuccessful() && response != null) {
                                API_RESPONSE api_response = response.body();
                                if (api_response.getCode() == 200) {
                                    utility.showDialog(context.getResources().getString(R.string.request_success_string));
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

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        onBackPressed();
        return true;
    }

    private void Get_Search_Product(SEND_SEARCH s) {
        try {
            if (utility.isNetworkAvailable()) {
                Log.d("GET PRODUCT", s.toString());
                utility.showProgress(false, getResources().getString(R.string.wait_string));
                Call<API_RESPONSE> call = apiInterface.FishBAngla_Search_Product(utility.getApiKey(), utility.getUSER_ID(), utility.getFirebaseToken(), s);
                call.enqueue(new Callback<API_RESPONSE>() {
                    @Override
                    public void onResponse(Call<API_RESPONSE> call, Response<API_RESPONSE> response) {
                        try {
                            utility.hideProgress();
                            Log.d("GET PRODUCT", response.toString());
                            if (response.isSuccessful() && response != null) {
                                API_RESPONSE api_response = response.body();
                                if (api_response.getCode() == 200) {

                                    Type listType = new TypeToken<List<Get_Product>>() {
                                    }.getType();
                                    List<Get_Product> get_products = gson.fromJson(response.body().getData(), listType);
                                    Log.d("Product List Size", get_products.size() + "");
                                    if (get_products.size() > 0) {
                                        suggestion_list.clear();
                                        suggestion_list.addAll(get_products);
                                        homeSuggestAdapter.notifyDataSetChanged();
                                    } else {
                                        utility.showDialog(context.getResources().getString(R.string.product_no_data_string));
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

    public void initial_tag() {
        tagListAdapter = new TagListAdapter(tag_list, context);
        //LinearLayoutManager mLayoutManager = new LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false);
        LinearLayoutManager mLayoutManager = new LinearLayoutManager(context);
        searchProductBinding.searchRecycler.setLayoutManager(mLayoutManager);
        searchProductBinding.searchRecycler.setItemAnimator(new DefaultItemAnimator());
        searchProductBinding.searchRecycler.setAdapter(tagListAdapter);
        Get_Product();
    }

    private void Get_Product() {
        try {
            if (utility.isNetworkAvailable()) {
                //utility.showProgress(false, getResources().getString(R.string.wait_string));
                Call<API_RESPONSE> call = apiInterface.FishBAngla_Tag_List(utility.getApiKey(), utility.getUSER_ID(), utility.getFirebaseToken());
                call.enqueue(new Callback<API_RESPONSE>() {
                    @Override
                    public void onResponse(Call<API_RESPONSE> call, Response<API_RESPONSE> response) {
                        try {
                            utility.hideProgress();
                            Log.d("GET PRODUCT", response.toString());
                            if (response.isSuccessful() && response != null) {
                                API_RESPONSE api_response = response.body();
                                if (api_response.getCode() == 200) {
                                    Type listType = new TypeToken<List<GET_TAGLIST>>() {
                                    }.getType();
                                    List<GET_TAGLIST> get_products = gson.fromJson(response.body().getData(), listType);
                                    Log.d("Product List Size", get_products.size() + "");
                                    tag_list.clear();
                                    tag_list.addAll(get_products);
                                    tagListAdapter.notifyDataSetChanged();

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
