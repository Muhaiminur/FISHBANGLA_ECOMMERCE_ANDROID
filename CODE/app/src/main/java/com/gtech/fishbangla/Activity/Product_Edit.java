package com.gtech.fishbangla.Activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.RadioGroup;
import android.widget.TextView;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.gtech.fishbangla.DATABASE.REPOSITORY.CONFIG_REPOSITORY;
import com.gtech.fishbangla.DATABASE.REPOSITORY.DISTRICT_REPOSITORY;
import com.gtech.fishbangla.DATABASE.REPOSITORY.DIVISION_REPOSITORY;
import com.gtech.fishbangla.DATABASE.REPOSITORY.UPOZILLA_REPOSITORY;
import com.gtech.fishbangla.DATABASE.REPOSITORY.USER_REPOSITORY;
import com.gtech.fishbangla.DATABASE.TABLE.DISTRICT_TABLE;
import com.gtech.fishbangla.DATABASE.TABLE.DIVISION_TABLE;
import com.gtech.fishbangla.DATABASE.TABLE.UPAZILLA_TABLE;
import com.gtech.fishbangla.DATABASE.TABLE.USER_PROFILE;
import com.gtech.fishbangla.Http.ApiClient;
import com.gtech.fishbangla.Http.ApiInterface;
import com.gtech.fishbangla.Library.KeyWord;
import com.gtech.fishbangla.Library.Utility;
import com.gtech.fishbangla.MODEL.API_RESPONSE;
import com.gtech.fishbangla.MODEL.CREATE_PRODUCT.Send_CategoryID;
import com.gtech.fishbangla.MODEL.Category_Data;
import com.gtech.fishbangla.MODEL.PRODUCT.Get_Product_Details;
import com.gtech.fishbangla.MODEL.PRODUCT.SEND_UPDATE_PRODUCT;
import com.gtech.fishbangla.MODEL.PRODUCT.Send_Product_Details;
import com.gtech.fishbangla.MODEL.Product_Data;
import com.gtech.fishbangla.MODEL.Unit_Data;
import com.gtech.fishbangla.R;
import com.gtech.fishbangla.databinding.ActivityProductEditBinding;
import com.nguyenhoanglam.imagepicker.model.Image;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class Product_Edit extends AppCompatActivity {

    Utility utility;
    Context context;
    DIVISION_REPOSITORY divisionRepository;
    List<DIVISION_TABLE> division_tables;
    DISTRICT_REPOSITORY districtRepository;
    List<DISTRICT_TABLE> districtTables;
    UPOZILLA_REPOSITORY upozillaRepository;
    List<UPAZILLA_TABLE> upazillaTables;
    //average
    List<Unit_Data> units = new ArrayList<>();
    List<Unit_Data> minunits = new ArrayList<>();

    SEND_UPDATE_PRODUCT send_update_product;
    ApiInterface apiInterface = ApiClient.getBaseClient().create(ApiInterface.class);
    USER_PROFILE userProfile;
    USER_REPOSITORY user_dao;
    CONFIG_REPOSITORY config_dao;
    Product_Data productData;
    Category_Data category_data;
    ArrayList<Image> imagesList;

    Get_Product_Details productDetails;
    ActivityProductEditBinding editBinding;

    private boolean category_spinner = false;
    private boolean div_spinner = false;
    private boolean zilla_spinner = false;
    private boolean upazilla_spinner = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_product__edit);
        try {
            getSupportActionBar().setHomeButtonEnabled(true);
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setTitle(getResources().getString(R.string.product_edit_string));
            context = this;
            utility = new Utility(context);
            editBinding = DataBindingUtil.setContentView(this, R.layout.activity_product__edit);
            user_dao = new USER_REPOSITORY(getApplication());
            config_dao = new CONFIG_REPOSITORY(getApplication());
            userProfile = user_dao.getuserData();
            String product_id = getIntent().getStringExtra("product_id");
            if (product_id != null && !TextUtils.isEmpty(product_id)) {
                Get_Product_Details(new Send_Product_Details(product_id));
            }

            editBinding.productUpdate.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    try {
                        if (userProfile != null && !TextUtils.isEmpty(userProfile.getUserId())) {
                            send_update_product = new SEND_UPDATE_PRODUCT();
                            send_update_product.setUserId(userProfile.getUserId());
                            if (productData != null && !TextUtils.isEmpty(productData.getProductName())) {
                                send_update_product.setProductMenuId(String.valueOf(productData.getProductMenuId()));
                                send_update_product.setProductId(productDetails.getProductId());
                                if (!TextUtils.isEmpty(editBinding.updateAverageWeight.getText().toString())) {
                                    send_update_product.setAvgWeight(editBinding.updateAverageWeight.getText().toString());
                                    Unit_Data avg_unit = (Unit_Data) editBinding.updateAverageUnit.getSelectedItem();
                                    send_update_product.setAvgWeightUnit(avg_unit.getName());
                                    if (!TextUtils.isEmpty(editBinding.updateTotalProduct.getText().toString())) {
                                        send_update_product.setMaxUnitQty(editBinding.updateTotalProduct.getText().toString());
                                        //Unit_Data total_unit = (Unit_Data) sellProductBinding.uploadTotalUnit.getSelectedItem();
                                        //send_product_new.setAvgWeightUnit(total_unit.getName());
                                        if (!TextUtils.isEmpty(editBinding.updateUnitSize.getText().toString())) {
                                            send_update_product.setUnitSize(editBinding.updateUnitSize.getText().toString());
                                            Unit_Data size_unit = (Unit_Data) editBinding.updateUnitweightUnit.getSelectedItem();
                                            send_update_product.setUnitType(size_unit.getName());
                                            if (!TextUtils.isEmpty(editBinding.updateUnitPrice.getText().toString())) {
                                                send_update_product.setPrice(editBinding.updateUnitPrice.getText().toString());
                                                if (editBinding.updateWholesell.isChecked()) {
                                                    send_update_product.setPostTypeId("2");
                                                    if (!TextUtils.isEmpty(editBinding.updatefishMinimumQuantity.getText().toString())) {
                                                        send_update_product.setMinUnitQty(editBinding.updatefishMinimumQuantity.getText().toString());
                                                    } else {
                                                        editBinding.updatefishMinimumQuantity.setError(getResources().getString(R.string.upload_wholesale_weight_string));
                                                        editBinding.updatefishMinimumQuantity.requestFocusFromTouch();
                                                    }
                                                } else if (editBinding.updateRetail.isChecked()) {
                                                    send_update_product.setPostTypeId("1");
                                                    send_update_product.setMinUnitQty("1");
                                                }
                                                UPAZILLA_TABLE upo_data = (UPAZILLA_TABLE) editBinding.updateUpozilla.getSelectedItem();
                                                if (upo_data != null && !TextUtils.isEmpty(upo_data.getUpazillaId())) {
                                                    send_update_product.setUpazillaId(upo_data.getUpazillaId());
                                                    utility.logger("EDIT PRODUCT" + send_update_product.toString());
                                                    update_product(send_update_product);
                                                } else {
                                                    editBinding.updateUpozilla.setError(getResources().getString(R.string.upozilla_string));
                                                    editBinding.updateUpozilla.requestFocusFromTouch();
                                                }
                                            } else {
                                                editBinding.updateUnitPrice.setError(getResources().getString(R.string.upload_unit_price_string));
                                                editBinding.updateUnitPrice.requestFocusFromTouch();
                                            }
                                        } else {
                                            editBinding.updateUnitSize.setError(getResources().getString(R.string.upload_unit_size_string));
                                            editBinding.updateUnitSize.requestFocusFromTouch();
                                        }
                                    } else {
                                        editBinding.updateTotalProduct.setError(getResources().getString(R.string.upload_max_unit_string));
                                        editBinding.updateTotalProduct.requestFocusFromTouch();
                                    }
                                } else {
                                    editBinding.updateAverageWeight.setError(getResources().getString(R.string.upload_avg_weight_string));
                                    editBinding.updateAverageWeight.requestFocusFromTouch();
                                }
                            } else {
                                editBinding.updateProduct.setError(getResources().getString(R.string.upload_product_name_string));
                                editBinding.updateProduct.requestFocusFromTouch();
                            }
                        } else {
                            utility.showDialog(getResources().getString(R.string.login_invalid_string));
                        }

                    } catch (Exception e) {
                        Log.d("Error Line Number", Log.getStackTraceString(e));
                    }
                }
            });

            editBinding.updatePosttype.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(RadioGroup group, int checkedId) {
                    if (editBinding.updateWholesell.isChecked()) {
                        editBinding.updatewholesaleView.setVisibility(View.VISIBLE);
                    } else if (editBinding.updateRetail.isChecked()) {
                        editBinding.updatewholesaleView.setVisibility(View.GONE);
                    }
                }
            });
            editBinding.updateMaxPlus.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    try {
                        String zero1 = editBinding.updatefishMinimumQuantity.getText().toString();
                        if (!TextUtils.isEmpty(zero1) && Integer.parseInt(zero1) >= 0 && !TextUtils.isEmpty(editBinding.updateUnitSize.getText().toString())) {
                            int i = Integer.parseInt(zero1);
                            i = i + 1;
                            editBinding.updatefishMinimumQuantity.setText(utility.convertToBangle(String.valueOf(i)));
                            editBinding.updateMaxResult.setText(utility.convertToBangle((Integer.parseInt(editBinding.updateUnitSize.getText().toString()) * i) + "") + " " + minunits.get(editBinding.updateUnitweightUnit.getSelectedItemPosition()).getName_bn());
                        }
                    } catch (Exception e) {
                        Log.d("Error Line Number", Log.getStackTraceString(e));
                    }
                }
            });
            editBinding.updateMaxMinus.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    try {
                        String zero2 = editBinding.updatefishMinimumQuantity.getText().toString();
                        if (!TextUtils.isEmpty(zero2) && !zero2.equalsIgnoreCase("0") && Integer.parseInt(zero2) > 1 && !TextUtils.isEmpty(editBinding.updateUnitSize.getText().toString())) {
                            int i = Integer.parseInt(zero2);
                            i = i - 1;
                            editBinding.updatefishMinimumQuantity.setText(utility.convertToBangle(String.valueOf(i)));
                            editBinding.updateMaxResult.setText(utility.convertToBangle((Integer.parseInt(editBinding.updateUnitSize.getText().toString()) * i) + "") + " " + minunits.get(editBinding.updateUnitweightUnit.getSelectedItemPosition()).getName_bn());
                        }
                    } catch (Exception e) {
                        Log.d("Error Line Number", Log.getStackTraceString(e));
                    }
                }
            });

            //total upload product size hints
            editBinding.updateTotalProduct.addTextChangedListener(new TextWatcher() {

                @Override
                public void afterTextChanged(Editable s) {
                }

                @Override
                public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                }

                @Override
                public void onTextChanged(CharSequence s, int start, int before, int count) {
                    if (!TextUtils.isEmpty(editBinding.updateUnitSize.getEditableText().toString())) {
                        try {
                            Unit_Data size_unit = (Unit_Data) editBinding.updateUnitweightUnit.getSelectedItem();
                            String u = size_unit.getName_bn();
                            if (u.equalsIgnoreCase(context.getResources().getString(R.string.unit_gram_string))) {
                                int g = Integer.parseInt(editBinding.updateUnitSize.getEditableText().toString()) * Integer.parseInt(s.toString());
                                if (g > 999) {
                                    editBinding.updateTotalUnit.setText(g / 1000 + " " + context.getResources().getString(R.string.unit_kg_string));
                                } else {
                                    editBinding.updateTotalUnit.setText(Integer.parseInt(editBinding.updateUnitSize.getEditableText().toString()) * Integer.parseInt(s.toString()) + " " + u);
                                }
                            } else {
                                editBinding.updateTotalUnit.setText(Integer.parseInt(editBinding.updateUnitSize.getEditableText().toString()) * Integer.parseInt(s.toString()) + " " + u);
                            }
                        } catch (Exception e) {
                            Log.d("Error Line Number", Log.getStackTraceString(e));
                        }
                    }
                }
            });
            editBinding.updateUnitSize.addTextChangedListener(new TextWatcher() {

                @Override
                public void afterTextChanged(Editable s) {
                }

                @Override
                public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                }

                @Override
                public void onTextChanged(CharSequence s, int start, int before, int count) {
                    if (!TextUtils.isEmpty(editBinding.updateTotalProduct.getEditableText().toString())) {
                        try {
                            Unit_Data size_unit = (Unit_Data) editBinding.updateUnitweightUnit.getSelectedItem();
                            String u = size_unit.getName_bn();
                            if (u.equalsIgnoreCase(context.getResources().getString(R.string.unit_gram_string))) {
                                int g = Integer.parseInt(editBinding.updateTotalProduct.getEditableText().toString()) * Integer.parseInt(s.toString());
                                if (g > 999) {
                                    editBinding.updateTotalUnit.setText(g / 1000 + " " + context.getResources().getString(R.string.unit_kg_string));
                                } else {
                                    editBinding.updateTotalUnit.setText(Integer.parseInt(editBinding.updateTotalProduct.getEditableText().toString()) * Integer.parseInt(s.toString()) + " " + u);
                                }
                            } else {
                                editBinding.updateTotalUnit.setText(Integer.parseInt(editBinding.updateTotalProduct.getEditableText().toString()) * Integer.parseInt(s.toString()) + " " + u);
                            }
                        } catch (Exception e) {
                            Log.d("Error Line Number", Log.getStackTraceString(e));
                        }
                    }
                }
            });
        } catch (Exception e) {
            Log.d("Error Line Number", Log.getStackTraceString(e));
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        onBackPressed();
        return true;
    }

    private void Get_Product_Details(Send_Product_Details s) {
        try {
            if (utility.isNetworkAvailable()) {
                Log.d("GET PRODUCT DETAILS", s.toString());
                utility.showProgress(false, getResources().getString(R.string.wait_string));
                Call<API_RESPONSE> call = apiInterface.FishBAngla_Product_Details(utility.getApiKey(), utility.getUSER_ID(), utility.getFirebaseToken(), s);
                call.enqueue(new Callback<API_RESPONSE>() {
                    @Override
                    public void onResponse(Call<API_RESPONSE> call, Response<API_RESPONSE> response) {
                        try {
                            utility.hideProgress();
                            Log.d("GET PRODUCT DETAILS", response.toString());
                            if (response.isSuccessful() && response != null) {
                                API_RESPONSE api_response = response.body();
                                if (api_response.getCode() == 200) {
                                    Gson gson = new Gson();
                                    productDetails = gson.fromJson(api_response.getData().toString(), Get_Product_Details.class);
                                    Log.d("GET PRODUCT DETAILS", productDetails.toString());
                                    if (productDetails != null) {
                                        spinner_work();
                                        editBinding.updateAverageWeight.setText(productDetails.getAvgWeight());
                                        editBinding.updateTotalProduct.setText(productDetails.getMaxUnitQty());
                                        editBinding.updateUnitSize.setText(productDetails.getUnitSize());
                                        editBinding.updateUnitPrice.setText(productDetails.getPrice());
                                        if (productDetails.getProductPostType().equalsIgnoreCase(KeyWord.SELLTYPE_WHOLESALE)) {
                                            editBinding.updateWholesell.setChecked(true);
                                            editBinding.updatewholesaleView.setVisibility(View.VISIBLE);
                                            editBinding.updateRetail.setChecked(false);
                                            editBinding.updatefishMinimumQuantity.setText(productDetails.getMinUnitQty());
                                        }
                                        selectdiv(productDetails.getDivisionName());
                                        selectzilla(productDetails.getDistrictName());
                                        selectupaziila(productDetails.getUpazillaName());
                                        selectaverage(productDetails.getAvgWeightUnit());
                                        selectunitsize(productDetails.getUnitType());
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

    private void Get_Product_Name(Send_CategoryID sendProductCategory, int i) {
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
                                    editBinding.updateProduct.setAdapter(product_adapter);
                                    if (i == 0) {
                                        selectname(productDetails.getProductName());
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

    private void Get_category_List() {
        try {
            if (utility.isNetworkAvailable()) {
                utility.showProgress(false, getResources().getString(R.string.wait_string));
                Call<API_RESPONSE> call = apiInterface.FishBAngla_Category_List_upload(utility.getApiKey(), utility.getUSER_ID(), utility.getFirebaseToken());
                call.enqueue(new Callback<API_RESPONSE>() {
                    @Override
                    public void onResponse(Call<API_RESPONSE> call, Response<API_RESPONSE> response) {
                        try {
                            utility.hideProgress();
                            Log.d("CATEGORY LIST", response.toString());
                            if (response.isSuccessful() && response != null) {
                                API_RESPONSE api_response = response.body();
                                if (api_response.getCode() == 200) {
                                    Gson gson = new Gson();
                                    Type listType = new TypeToken<List<Category_Data>>() {
                                    }.getType();
                                    List<Category_Data> get_category = gson.fromJson(response.body().getData(), listType);
                                    Log.d("CATEGORY LIST Size", get_category.size() + "");

                                    ArrayAdapter<Category_Data> product_adapter = new ArrayAdapter<Category_Data>(context, android.R.layout.simple_spinner_item, get_category);
                                    product_adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                                    editBinding.updateCategory.setAdapter(product_adapter);
                                    selectcategory(productDetails.getAppMenuTitle());
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

    public void spinner_work() {
        divisionRepository = new DIVISION_REPOSITORY(getApplication());
        districtRepository = new DISTRICT_REPOSITORY(getApplication());
        upozillaRepository = new UPOZILLA_REPOSITORY(getApplication());
        String[] ITEMS = {};
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, ITEMS);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        editBinding.updateProduct.setAdapter(adapter);
        editBinding.updateCategory.setAdapter(adapter);
        editBinding.updateDivision.setAdapter(adapter);
        editBinding.updateZilla.setAdapter(adapter);
        editBinding.updateUpozilla.setAdapter(adapter);

        //min unit
        minunits.add(new Unit_Data("kg", getResources().getString(R.string.unit_kg_string)));
        minunits.add(new Unit_Data("gram", getResources().getString(R.string.unit_gram_string)));
        minunits.add(new Unit_Data("piece", getResources().getString(R.string.unit_piece_string)));
        units.add(new Unit_Data("kg", getResources().getString(R.string.unit_kg_string)));
        units.add(new Unit_Data("gram", getResources().getString(R.string.unit_gram_string)));
        ArrayAdapter<Unit_Data> unit_adapter = new ArrayAdapter<Unit_Data>(this, android.R.layout.simple_spinner_item, units);
        unit_adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        ArrayAdapter<Unit_Data> min_unit_adapter = new ArrayAdapter<Unit_Data>(this, android.R.layout.simple_spinner_item, minunits);
        min_unit_adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        editBinding.updateAverageUnit.setAdapter(unit_adapter);
        editBinding.updateUnitweightUnit.setAdapter(min_unit_adapter);

        Get_category_List();

        division_adpater();
        editBinding.updateProduct.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                productData = (Product_Data) parent.getSelectedItem();

            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        editBinding.updateCategory.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                category_spinner = true;
                return false;
            }
        });
        editBinding.updateCategory.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if (!category_spinner) return;
                if (position != -1) {
                    category_data = (Category_Data) parent.getSelectedItem();
                    if (category_data != null) {
                        Get_Product_Name(new Send_CategoryID(category_data.getAppMenuId()), 1);
                    }
                }

            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        editBinding.updateUnitweightUnit.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if (!TextUtils.isEmpty(editBinding.updateUnitSize.getEditableText().toString()) && !TextUtils.isEmpty(editBinding.updateTotalProduct.getEditableText().toString())) {
                    try {
                        Unit_Data size_unit = (Unit_Data) editBinding.updateUnitweightUnit.getSelectedItem();
                        String u = size_unit.getName_bn();
                        if (u.equalsIgnoreCase(context.getResources().getString(R.string.unit_gram_string))) {
                            int g = Integer.parseInt(editBinding.updateUnitSize.getEditableText().toString()) * Integer.parseInt(editBinding.updateTotalProduct.getEditableText().toString());
                            if (g > 999) {
                                editBinding.updateTotalUnit.setText(g / 1000 + " " + context.getResources().getString(R.string.unit_kg_string));
                            } else {
                                editBinding.updateTotalUnit.setText(Integer.parseInt(editBinding.updateUnitSize.getEditableText().toString()) * Integer.parseInt(editBinding.updateTotalProduct.getEditableText().toString()) + " " + u);
                            }
                        } else {
                            editBinding.updateTotalUnit.setText(Integer.parseInt(editBinding.updateUnitSize.getEditableText().toString()) * Integer.parseInt(editBinding.updateTotalProduct.getEditableText().toString()) + " " + u);
                        }
                    } catch (Exception e) {
                        Log.d("Error Line Number", Log.getStackTraceString(e));
                    }
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
    }

    public void division_adpater() {
        try {
            division_tables = divisionRepository.getdivisionData();
            ArrayAdapter<DIVISION_TABLE> division_adapter = new ArrayAdapter<DIVISION_TABLE>(this, android.R.layout.simple_spinner_item, division_tables);
            division_adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            editBinding.updateDivision.setAdapter(division_adapter);
            editBinding.updateDivision.setOnTouchListener(new View.OnTouchListener() {
                @Override
                public boolean onTouch(View v, MotionEvent event) {
                    div_spinner = true;
                    return false;
                }
            });
            editBinding.updateDivision.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                @Override
                public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                    if (!div_spinner) return;
                    DIVISION_TABLE selectedItem = (DIVISION_TABLE) parent.getSelectedItem();
                    district_adapter(selectedItem);
                }

                @Override
                public void onNothingSelected(AdapterView<?> parent) {

                }
            });
        } catch (Exception e) {
            Log.d("Error Line Number", Log.getStackTraceString(e));
        }
    }

    public void district_adapter(DIVISION_TABLE district_table) {
        try {
            if (district_table != null) {
                districtTables = districtRepository.finddistrictData(district_table.getDivisionId());
                ArrayAdapter<DISTRICT_TABLE> district_adapter = new ArrayAdapter<DISTRICT_TABLE>(this, android.R.layout.simple_spinner_item, districtTables);
                district_adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                editBinding.updateZilla.setAdapter(district_adapter);
                editBinding.updateZilla.setOnTouchListener(new View.OnTouchListener() {
                    @Override
                    public boolean onTouch(View v, MotionEvent event) {
                        zilla_spinner = true;
                        return false;
                    }
                });
                editBinding.updateZilla.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                    @Override
                    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                        if (!zilla_spinner) return;
                        DISTRICT_TABLE selectedItem = (DISTRICT_TABLE) parent.getSelectedItem();
                        upozilla_adapter(selectedItem);
                    }

                    @Override
                    public void onNothingSelected(AdapterView<?> parent) {

                    }
                });
            }
        } catch (Exception e) {
            Log.d("Error Line Number", Log.getStackTraceString(e));
        }
    }

    public void upozilla_adapter(DISTRICT_TABLE upazilla_table) {
        try {
            if (upazilla_table != null) {
                upazillaTables = upozillaRepository.findupazillaData(upazilla_table.getDistrictId());
                ArrayAdapter<UPAZILLA_TABLE> upazilla_adapter = new ArrayAdapter<UPAZILLA_TABLE>(this, android.R.layout.simple_spinner_item, upazillaTables);
                upazilla_adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                editBinding.updateUpozilla.setAdapter(upazilla_adapter);
                editBinding.updateUpozilla.setOnTouchListener(new View.OnTouchListener() {
                    @Override
                    public boolean onTouch(View v, MotionEvent event) {
                        upazilla_spinner = true;
                        return false;
                    }
                });
                editBinding.updateUpozilla.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                    @Override
                    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                        if (!upazilla_spinner) return;
                        UPAZILLA_TABLE selectedItem = (UPAZILLA_TABLE) parent.getSelectedItem();

                    }

                    @Override
                    public void onNothingSelected(AdapterView<?> parent) {

                    }
                });
            }
        } catch (Exception e) {
            Log.d("Error Line Number", Log.getStackTraceString(e));
        }
    }

    private void selectcategory(String c) {
        for (int i = 0; i < editBinding.updateCategory.getAdapter().getCount(); i++) {
            if (((Category_Data) editBinding.updateCategory.getAdapter().getItem(i)).getAppMenuTitle().equalsIgnoreCase(c)) {
                editBinding.updateCategory.setSelection(i + 1);
                Get_Product_Name(new Send_CategoryID(((Category_Data) editBinding.updateCategory.getAdapter().getItem(i)).getAppMenuId()), 0);
                break;
            }
        }
    }

    private void selectname(String c) {
        for (int i = 0; i < editBinding.updateProduct.getAdapter().getCount(); i++) {
            if (((Product_Data) editBinding.updateProduct.getAdapter().getItem(i)).getProductName().equalsIgnoreCase(c)) {
                editBinding.updateProduct.setSelection(i + 1);
                break;
            }
        }
    }

    private void selectdiv(String c) {
        for (int i = 0; i < editBinding.updateDivision.getAdapter().getCount(); i++) {
            if (((DIVISION_TABLE) editBinding.updateDivision.getAdapter().getItem(i)).getDivisionName().equalsIgnoreCase(c)) {
                editBinding.updateDivision.setSelection(i + 1);
                district_adapter(((DIVISION_TABLE) editBinding.updateDivision.getAdapter().getItem(i)));
                break;
            }
        }
    }

    private void selectzilla(String c) {
        for (int i = 0; i < editBinding.updateZilla.getAdapter().getCount(); i++) {
            if (((DISTRICT_TABLE) editBinding.updateZilla.getAdapter().getItem(i)).getDistrictName().equalsIgnoreCase(c)) {
                editBinding.updateZilla.setSelection(i + 1);
                upozilla_adapter(((DISTRICT_TABLE) editBinding.updateZilla.getAdapter().getItem(i)));
                break;
            }
        }
    }

    private void selectupaziila(String c) {
        for (int i = 0; i < editBinding.updateUpozilla.getAdapter().getCount(); i++) {
            if (((UPAZILLA_TABLE) editBinding.updateUpozilla.getAdapter().getItem(i)).getUpazillaName().equalsIgnoreCase(c)) {
                editBinding.updateUpozilla.setSelection(i + 1);
                //district_adapter(((DIVISION_TABLE) editBinding.updateDivision.getAdapter().getItem(i)));
                break;
            }
        }
    }

    private void selectaverage(String c) {
        for (int i = 0; i < editBinding.updateAverageUnit.getAdapter().getCount(); i++) {
            if (((Unit_Data) editBinding.updateAverageUnit.getAdapter().getItem(i)).getName().equalsIgnoreCase(c)) {
                editBinding.updateAverageUnit.setSelection(i);
                utility.logger("check0");
                //district_adapter(((DIVISION_TABLE) editBinding.updateDivision.getAdapter().getItem(i)));
                break;
            }
        }
    }

    private void selectunitsize(String c) {
        for (int i = 0; i < editBinding.updateUnitweightUnit.getAdapter().getCount(); i++) {
            if (((Unit_Data) editBinding.updateUnitweightUnit.getAdapter().getItem(i)).getName().equalsIgnoreCase(c)) {
                editBinding.updateUnitweightUnit.setSelection(i);
                utility.logger("check2");
                //district_adapter(((DIVISION_TABLE) editBinding.updateDivision.getAdapter().getItem(i)));
                break;
            }
        }
    }

    public void update_product(SEND_UPDATE_PRODUCT send_order) {
        try {
            if (utility.isNetworkAvailable()) {
                utility.showProgress(false, getResources().getString(R.string.wait_string));
                Log.d("send_order", send_order.toString());
                Call<API_RESPONSE> call = apiInterface.FishBAngla_Product_Edit(utility.getApiKey(), utility.getUSER_ID(), utility.getFirebaseToken(), send_order);
                call.enqueue(new Callback<API_RESPONSE>() {
                    @Override
                    public void onResponse(Call<API_RESPONSE> call, Response<API_RESPONSE> response) {
                        try {
                            utility.hideProgress();
                            Log.d("send_order", response.toString());
                            if (response.isSuccessful() && response != null) {
                                API_RESPONSE api_response = response.body();
                                if (api_response.getCode() == 200) {
                                    utility.logger("send_order" + response.body().getData().getAsString());
                                    Dioalog_Yes(getResources().getString(R.string.product_edit_title_string));
                                    Intent in = new Intent("product_update");
                                    //in.putExtra("category", category);
                                    sendBroadcast(in);
                                } else {
                                    utility.logger("send_order" + response.body().getData().getAsString());
                                    utility.showDialog(getResources().getString(R.string.reference_invalid_string));
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

    public void Dioalog_Yes(String msg) {
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
                    if (msg.equalsIgnoreCase(getResources().getString(R.string.product_edit_title_string))) {
                        finish();
                    }
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
