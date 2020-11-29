package com.gtech.fishbangla.Activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.Manifest;
import android.app.Activity;
import android.app.Dialog;
import android.content.ActivityNotFoundException;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.media.ThumbnailUtils;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.textfield.TextInputEditText;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.gtech.fishbangla.Adapter.ImagePreviewtAdapter;
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
import com.gtech.fishbangla.MODEL.PRODUCT.Send_Product_New;
import com.gtech.fishbangla.MODEL.Product_Data;
import com.gtech.fishbangla.MODEL.Unit_Data;
import com.gtech.fishbangla.MODEL.User.Send_Reference;
import com.gtech.fishbangla.R;
import com.gtech.fishbangla.databinding.ActivitySellProductBinding;
import com.jmolsmobile.landscapevideocapture.VideoCaptureActivity;
import com.jmolsmobile.landscapevideocapture.configuration.CaptureConfiguration;
import com.jmolsmobile.landscapevideocapture.configuration.PredefinedCaptureConfigurations;
import com.nguyenhoanglam.imagepicker.model.Config;
import com.nguyenhoanglam.imagepicker.model.Image;
import com.nguyenhoanglam.imagepicker.ui.imagepicker.ImagePicker;

import java.io.File;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class Sell_Product extends AppCompatActivity {

    Utility utility;
    Context context;
    ActivitySellProductBinding sellProductBinding;
    DIVISION_REPOSITORY divisionRepository;
    List<DIVISION_TABLE> division_tables;
    DISTRICT_REPOSITORY districtRepository;
    List<DISTRICT_TABLE> districtTables;
    UPOZILLA_REPOSITORY upozillaRepository;
    List<UPAZILLA_TABLE> upazillaTables;
    //average
    List<Unit_Data> units = new ArrayList<>();
    List<Unit_Data> minunits = new ArrayList<>();

    Send_Product_New send_product_new;
    ApiInterface apiInterface = ApiClient.getBaseClient().create(ApiInterface.class);
    USER_PROFILE userProfile;
    USER_REPOSITORY user_dao;
    CONFIG_REPOSITORY config_dao;
    Product_Data productData;
    File product_video;
    String product_video_filename;
    ArrayList<Image> imagesList;
    ImagePreviewtAdapter imagePreviewtAdapter;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sell__product);
        try {
            sellProductBinding = DataBindingUtil.setContentView(this, R.layout.activity_sell__product);
            getSupportActionBar().setHomeButtonEnabled(true);
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setTitle(getResources().getString(R.string.sell_product));
            context = this;
            utility = new Utility(context);
            user_dao = new USER_REPOSITORY(getApplication());
            config_dao = new CONFIG_REPOSITORY(getApplication());
            userProfile = user_dao.getuserData();
            spinner_work();
            video_work();
            image_work();

            sellProductBinding.productCreate.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    try {
                        if (userProfile != null && !TextUtils.isEmpty(userProfile.getUserId())) {
                            send_product_new = new Send_Product_New();
                            send_product_new.setUserId(userProfile.getUserId());
                            if (productData != null && !TextUtils.isEmpty(productData.getProductName())) {
                                send_product_new.setProductMenuId(String.valueOf(productData.getProductMenuId()));
                                if (!TextUtils.isEmpty(sellProductBinding.uploadAverageWeight.getText().toString())) {
                                    send_product_new.setAvgWeight(sellProductBinding.uploadAverageWeight.getText().toString());
                                    Unit_Data avg_unit = (Unit_Data) sellProductBinding.uploadAverageUnit.getSelectedItem();
                                    send_product_new.setAvgWeightUnit(avg_unit.getName());
                                    if (!TextUtils.isEmpty(sellProductBinding.uploadTotalProduct.getText().toString())) {
                                        send_product_new.setMaxUnitQty(sellProductBinding.uploadTotalProduct.getText().toString());
                                        //Unit_Data total_unit = (Unit_Data) sellProductBinding.uploadTotalUnit.getSelectedItem();
                                        //send_product_new.setAvgWeightUnit(total_unit.getName());
                                        if (!TextUtils.isEmpty(sellProductBinding.uploadUnitSize.getText().toString())) {
                                            send_product_new.setUnitSize(sellProductBinding.uploadUnitSize.getText().toString());
                                            Unit_Data size_unit = (Unit_Data) sellProductBinding.uploadUnitweightUnit.getSelectedItem();
                                            send_product_new.setUnitType(size_unit.getName());
                                            if (!TextUtils.isEmpty(sellProductBinding.uploadUnitPrice.getText().toString())) {
                                                send_product_new.setPrice(sellProductBinding.uploadUnitPrice.getText().toString());
                                                if (sellProductBinding.wholesell.isChecked()) {
                                                    send_product_new.setPostTypeId("2");
                                                    if (!TextUtils.isEmpty(sellProductBinding.fishMinimumQuantity.getText().toString())) {
                                                        send_product_new.setMinUnitQty(sellProductBinding.fishMinimumQuantity.getText().toString());
                                                    } else {
                                                        sellProductBinding.fishMinimumQuantity.setError(getResources().getString(R.string.upload_wholesale_weight_string));
                                                        sellProductBinding.fishMinimumQuantity.requestFocusFromTouch();
                                                    }
                                                } else if (sellProductBinding.retail.isChecked()) {
                                                    send_product_new.setPostTypeId("1");
                                                    send_product_new.setMinUnitQty("1");
                                                }
                                                UPAZILLA_TABLE upo_data = (UPAZILLA_TABLE) sellProductBinding.uploadUpozilla.getSelectedItem();
                                                if (upo_data != null && !TextUtils.isEmpty(upo_data.getUpazillaId())) {
                                                    send_product_new.setUpazillaId(upo_data.getUpazillaId());
                                                    utility.logger("CREATE PRODUCT" + send_product_new.toString());
                                                    if (imagesList != null) {
                                                        Create_Product(send_product_new);
                                                    } else {
                                                        utility.showDialog(getResources().getString(R.string.add_image_string));
                                                    }
                                                } else {
                                                    sellProductBinding.uploadUpozilla.setError(getResources().getString(R.string.upozilla_string));
                                                    sellProductBinding.uploadUpozilla.requestFocusFromTouch();
                                                }
                                            } else {
                                                sellProductBinding.uploadUnitPrice.setError(getResources().getString(R.string.upload_unit_price_string));
                                                sellProductBinding.uploadUnitPrice.requestFocusFromTouch();
                                            }
                                        } else {
                                            sellProductBinding.uploadUnitSize.setError(getResources().getString(R.string.upload_unit_size_string));
                                            sellProductBinding.uploadUnitSize.requestFocusFromTouch();
                                        }
                                    } else {
                                        sellProductBinding.uploadTotalProduct.setError(getResources().getString(R.string.upload_max_unit_string));
                                        sellProductBinding.uploadTotalProduct.requestFocusFromTouch();
                                    }
                                } else {
                                    sellProductBinding.uploadAverageWeight.setError(getResources().getString(R.string.upload_avg_weight_string));
                                    sellProductBinding.uploadAverageWeight.requestFocusFromTouch();
                                }
                            } else {
                                sellProductBinding.uploadProduct.setError(getResources().getString(R.string.upload_product_name_string));
                                sellProductBinding.uploadProduct.requestFocusFromTouch();
                            }
                        } else {
                            utility.showDialog(getResources().getString(R.string.login_invalid_string));
                        }
                    } catch (Exception e) {
                        Log.d("Error Line Number", Log.getStackTraceString(e));
                    }
                }
            });
            sellProductBinding.uploadPosttype.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(RadioGroup group, int checkedId) {
                    if (sellProductBinding.wholesell.isChecked()) {
                        sellProductBinding.wholesaleView.setVisibility(View.VISIBLE);
                    } else if (sellProductBinding.retail.isChecked()) {
                        sellProductBinding.wholesaleView.setVisibility(View.GONE);
                    }
                }
            });
            sellProductBinding.maxPlus.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    try {
                        String zero1 = sellProductBinding.fishMinimumQuantity.getText().toString();
                        if (!TextUtils.isEmpty(zero1) && Integer.parseInt(zero1) >= 0 && !TextUtils.isEmpty(sellProductBinding.uploadUnitSize.getText().toString())) {
                            int i = Integer.parseInt(zero1);
                            i = i + 1;
                            sellProductBinding.fishMinimumQuantity.setText(utility.convertToBangle(String.valueOf(i)));
                            sellProductBinding.maxResult.setText(utility.convertToBangle((Integer.parseInt(sellProductBinding.uploadUnitSize.getText().toString()) * i) + "") + " " + minunits.get(sellProductBinding.uploadUnitweightUnit.getSelectedItemPosition()).getName_bn());
                        }
                    } catch (Exception e) {
                        Log.d("Error Line Number", Log.getStackTraceString(e));
                    }
                }
            });
            sellProductBinding.maxMinus.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    try {
                        String zero2 = sellProductBinding.fishMinimumQuantity.getText().toString();
                        if (!TextUtils.isEmpty(zero2) && !zero2.equalsIgnoreCase("0") && Integer.parseInt(zero2) > 1 && !TextUtils.isEmpty(sellProductBinding.uploadUnitSize.getText().toString())) {
                            int i = Integer.parseInt(zero2);
                            i = i - 1;
                            sellProductBinding.fishMinimumQuantity.setText(utility.convertToBangle(String.valueOf(i)));
                            sellProductBinding.maxResult.setText(utility.convertToBangle((Integer.parseInt(sellProductBinding.uploadUnitSize.getText().toString()) * i) + "") + " " + minunits.get(sellProductBinding.uploadUnitweightUnit.getSelectedItemPosition()).getName_bn());
                        }
                    } catch (Exception e) {
                        Log.d("Error Line Number", Log.getStackTraceString(e));
                    }
                }
            });

            //total upload product size hints
            sellProductBinding.uploadTotalProduct.addTextChangedListener(new TextWatcher() {

                @Override
                public void afterTextChanged(Editable s) {
                }

                @Override
                public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                }

                @Override
                public void onTextChanged(CharSequence s, int start, int before, int count) {
                    if (!TextUtils.isEmpty(sellProductBinding.uploadUnitSize.getEditableText().toString())) {
                        try {
                            Unit_Data size_unit = (Unit_Data) sellProductBinding.uploadUnitweightUnit.getSelectedItem();
                            String u = size_unit.getName_bn();
                            if (u.equalsIgnoreCase(context.getResources().getString(R.string.unit_gram_string))) {
                                int g = Integer.parseInt(sellProductBinding.uploadUnitSize.getEditableText().toString()) * Integer.parseInt(s.toString());
                                if (g > 999) {
                                    sellProductBinding.uploadTotalUnit.setText(g / 1000 + " " + context.getResources().getString(R.string.unit_kg_string));
                                } else {
                                    sellProductBinding.uploadTotalUnit.setText(Integer.parseInt(sellProductBinding.uploadUnitSize.getEditableText().toString()) * Integer.parseInt(s.toString()) + " " + u);
                                }
                            } else {
                                sellProductBinding.uploadTotalUnit.setText(Integer.parseInt(sellProductBinding.uploadUnitSize.getEditableText().toString()) * Integer.parseInt(s.toString()) + " " + u);
                            }
                        } catch (Exception e) {
                            Log.d("Error Line Number", Log.getStackTraceString(e));
                        }
                    }
                }
            });
            sellProductBinding.uploadUnitSize.addTextChangedListener(new TextWatcher() {

                @Override
                public void afterTextChanged(Editable s) {
                }

                @Override
                public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                }

                @Override
                public void onTextChanged(CharSequence s, int start, int before, int count) {
                    if (!TextUtils.isEmpty(sellProductBinding.uploadTotalProduct.getEditableText().toString())) {
                        try {
                            Unit_Data size_unit = (Unit_Data) sellProductBinding.uploadUnitweightUnit.getSelectedItem();
                            String u = size_unit.getName_bn();
                            if (u.equalsIgnoreCase(context.getResources().getString(R.string.unit_gram_string))) {
                                int g = Integer.parseInt(sellProductBinding.uploadTotalProduct.getEditableText().toString()) * Integer.parseInt(s.toString());
                                if (g > 999) {
                                    sellProductBinding.uploadTotalUnit.setText(g / 1000 + " " + context.getResources().getString(R.string.unit_kg_string));
                                } else {
                                    sellProductBinding.uploadTotalUnit.setText(Integer.parseInt(sellProductBinding.uploadTotalProduct.getEditableText().toString()) * Integer.parseInt(s.toString()) + " " + u);
                                }
                            } else {
                                sellProductBinding.uploadTotalUnit.setText(Integer.parseInt(sellProductBinding.uploadTotalProduct.getEditableText().toString()) * Integer.parseInt(s.toString()) + " " + u);
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

    private void image_work() {
        try {
            sellProductBinding.uploadImage.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    try {
                        ImagePicker.with(Sell_Product.this)                         //  Initialize ImagePicker with activity or fragment context
                                .setToolbarColor("#ff0e0e")         //  Toolbar color
                                .setStatusBarColor("#000000")       //  StatusBar color (works with SDK >= 21  )
                                .setToolbarTextColor("#FFFFFF")     //  Toolbar text color (Title and Done button)
                                .setToolbarIconColor("#FFFFFF")     //  Toolbar icon color (Back and Camera button)
                                .setProgressBarColor("#4CAF50")     //  ProgressBar color
                                .setBackgroundColor("#212121")      //  Background color
                                .setCameraOnly(false)               //  Camera mode
                                .setMultipleMode(true)              //  Select multiple images or single image
                                .setFolderMode(false)                //  Folder mode
                                .setShowCamera(true)                //  Show camera button
                                .setFolderTitle(getResources().getString(R.string.app_name))           //  Folder title (works with FolderMode = true)
                                .setImageTitle(getResources().getString(R.string.add_image_string))         //  Image title (works with FolderMode = false)
                                .setDoneTitle("Done")               //  Done button title
                                .setLimitMessage("You have reached selection limit")    // Selection limit message
                                .setMaxSize(5)                     //  Max images can be selected
                                .setSavePath(getResources().getString(R.string.app_name))         //  Image capture folder name
                                .setAlwaysShowDoneButton(true)      //  Set always show done button in multiple mode
                                .setRequestCode(Config.RC_PICK_IMAGES)                //  Set request code, default Config.RC_PICK_IMAGES
                                .setKeepScreenOn(true)              //  Keep screen on when selecting images
                                .start();
                    } catch (Exception e) {
                        Log.d("Error Line Number", Log.getStackTraceString(e));
                    }
                }
            });
        } catch (Exception e) {
            Log.d("Error Line Number", Log.getStackTraceString(e));
        }
    }

    private void video_work() {
        try {
            sellProductBinding.uploadVideo.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    try {
                        if (isReadStorageAllowed()) {
                            final CaptureConfiguration config = createCaptureConfiguration();
                            final String filename = "FishBangla.mp4";
                            final Intent intent = new Intent(context, VideoCaptureActivity.class);
                            intent.putExtra(VideoCaptureActivity.EXTRA_CAPTURE_CONFIGURATION, config);
                            intent.putExtra(VideoCaptureActivity.EXTRA_OUTPUT_FILENAME, filename);
                            startActivityForResult(intent, KeyWord.VIDEO_PERMISSION);
                            utility.logger("permission");
                        } else {
                            utility.logger("permission not");
                            requestVideoPermission();
                        }
                    } catch (Exception e) {
                        Log.d("Error Line Number", Log.getStackTraceString(e));
                    }
                }
            });
            sellProductBinding.uploadVideoPreviewAdd.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    try {
                        if (isReadStorageAllowed()) {
                            final CaptureConfiguration config = createCaptureConfiguration();
                            final String filename = "FishBangla.mp4";
                            final Intent intent = new Intent(context, VideoCaptureActivity.class);
                            intent.putExtra(VideoCaptureActivity.EXTRA_CAPTURE_CONFIGURATION, config);
                            intent.putExtra(VideoCaptureActivity.EXTRA_OUTPUT_FILENAME, filename);
                            startActivityForResult(intent, KeyWord.VIDEO_PERMISSION);
                            utility.logger("permission");
                        } else {
                            utility.logger("permission not");
                            requestVideoPermission();
                        }
                    } catch (Exception e) {
                        Log.d("Error Line Number", Log.getStackTraceString(e));
                    }
                }
            });
            sellProductBinding.uploadVideoPreviewPlay.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    playVideo();
                }
            });
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
        sellProductBinding.uploadProduct.setAdapter(adapter);
        sellProductBinding.uploadCategory.setAdapter(adapter);
        sellProductBinding.uploadZilla.setAdapter(adapter);
        sellProductBinding.uploadDivision.setAdapter(adapter);
        sellProductBinding.uploadUpozilla.setAdapter(adapter);

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
        sellProductBinding.uploadAverageUnit.setAdapter(unit_adapter);
        sellProductBinding.uploadUnitweightUnit.setAdapter(min_unit_adapter);

        Get_category_List();

        division_adpater();
        sellProductBinding.uploadProduct.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                productData = (Product_Data) parent.getSelectedItem();

            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        sellProductBinding.uploadCategory.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                Category_Data category_data = (Category_Data) parent.getSelectedItem();
                if (category_data != null) {
                    Get_Product_Name(new Send_CategoryID(category_data.getAppMenuId()));
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        sellProductBinding.uploadUnitweightUnit.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if (!TextUtils.isEmpty(sellProductBinding.uploadUnitSize.getEditableText().toString()) && !TextUtils.isEmpty(sellProductBinding.uploadTotalProduct.getEditableText().toString())) {
                    try {
                        Unit_Data size_unit = (Unit_Data) sellProductBinding.uploadUnitweightUnit.getSelectedItem();
                        String u = size_unit.getName_bn();
                        if (u.equalsIgnoreCase(context.getResources().getString(R.string.unit_gram_string))) {
                            int g = Integer.parseInt(sellProductBinding.uploadUnitSize.getEditableText().toString()) * Integer.parseInt(sellProductBinding.uploadTotalProduct.getEditableText().toString());
                            if (g > 999) {
                                sellProductBinding.uploadTotalUnit.setText(g / 1000 + " " + context.getResources().getString(R.string.unit_kg_string));
                            } else {
                                sellProductBinding.uploadTotalUnit.setText(Integer.parseInt(sellProductBinding.uploadUnitSize.getEditableText().toString()) * Integer.parseInt(sellProductBinding.uploadTotalProduct.getEditableText().toString()) + " " + u);
                            }
                        } else {
                            sellProductBinding.uploadTotalUnit.setText(Integer.parseInt(sellProductBinding.uploadUnitSize.getEditableText().toString()) * Integer.parseInt(sellProductBinding.uploadTotalProduct.getEditableText().toString()) + " " + u);
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
            sellProductBinding.uploadDivision.setAdapter(division_adapter);
            sellProductBinding.uploadDivision.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                @Override
                public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
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
                sellProductBinding.uploadZilla.setAdapter(district_adapter);
                sellProductBinding.uploadZilla.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                    @Override
                    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
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
                sellProductBinding.uploadUpozilla.setAdapter(upazilla_adapter);
                sellProductBinding.uploadUpozilla.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                    @Override
                    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
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

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        onBackPressed();
        return true;
    }

    private void Get_Product_Name(Send_CategoryID sendProductCategory) {
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
                                    /*product_data.clear();
                                    product_data.addAll(get_products);*/
                                    ArrayAdapter<Product_Data> product_adapter = new ArrayAdapter<Product_Data>(context, android.R.layout.simple_spinner_item, get_products);
                                    product_adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                                    sellProductBinding.uploadProduct.setAdapter(product_adapter);
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
                                    sellProductBinding.uploadCategory.setAdapter(product_adapter);
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

    public void Create_Product(Send_Product_New send_product_new) {
        try {
            if (utility.isNetworkAvailable()) {
                utility.showProgress(false, getResources().getString(R.string.wait_string));
                Log.d("Upload PRODUCT", send_product_new.toString());
                Call<API_RESPONSE> call = apiInterface.FishBAngla_Upload_Product(utility.getApiKey(), utility.getUSER_ID(), utility.getFirebaseToken(), send_product_new);
                call.enqueue(new Callback<API_RESPONSE>() {
                    @Override
                    public void onResponse(Call<API_RESPONSE> call, Response<API_RESPONSE> response) {
                        try {
                            utility.hideProgress();
                            Log.d("Upload PRODUCT", response.toString());
                            if (response.isSuccessful() && response != null) {
                                API_RESPONSE api_response = response.body();
                                if (api_response.getCode() == 200) {
                                    utility.logger("Product_Upload" + response.body().getData().getAsString());
                                    Send_Image_data(response.body().getData().getAsString(), imagesList);
                                } else if (api_response.getCode() == 399) {
                                    utility.logger("Product_Upload" + response.body().getData().getAsString());
                                    Reference_Check();
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

    public void Send_Image_data(String productid, List<Image> image_pic_list) {
        try {
            if (utility.isNetworkAvailable()) {
                utility.showProgress(false, getResources().getString(R.string.wait_string));
                List<MultipartBody.Part> parts = new ArrayList<>();
                if (image_pic_list != null) {
                    for (Image i : image_pic_list) {
                        parts.add(prepareFilePart(i.getPath()));
                    }
                }

                /*RequestBody requestFile = RequestBody.create(MediaType.parse("image/*"), new File(image_pic_list.get(0).getPath()));
                // MultipartBody.Part is used to send also the actual file name
                MultipartBody.Part image_body = MultipartBody.Part.createFormData("image1", image_pic_list.get(0).getName(), requestFile);*/

                Call<API_RESPONSE> call = apiInterface.FishBAngla_Product_Image_Upload(utility.getApiKey(), utility.getUSER_ID(), utility.getFirebaseToken(), productid, parts);
                call.enqueue(new Callback<API_RESPONSE>() {
                    @Override
                    public void onResponse(Call<API_RESPONSE> call, Response<API_RESPONSE> response) {
                        try {
                            utility.hideProgress();
                            Log.d("Image Upload", response.toString());
                            if (response.isSuccessful() && response != null) {
                                API_RESPONSE api_response = response.body();
                                if (api_response.getCode() == 200) {
                                    if (product_video != null) {
                                        Send_Video_data(productid, product_video);
                                    } else {
                                        Dioalog_Yes(getResources().getString(R.string.product_succes_string));
                                    }
                                    utility.logger("Image Upload" + response.body().getData().getAsString());
                                } else {
                                    utility.showToast(getResources().getString(R.string.try_again_string));
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

    public void Send_Video_data(String productid, File video) {
        try {
            if (utility.isNetworkAvailable()) {
                utility.showProgress(false, getResources().getString(R.string.wait_string));
                RequestBody requestFile = RequestBody.create(MediaType.parse("video/*"), video);
                // MultipartBody.Part is used to send also the actual file name
                MultipartBody.Part video_body = MultipartBody.Part.createFormData("video", video.getName(), requestFile);
                Call<API_RESPONSE> call = apiInterface.FishBAngla_Product_Video_Upload(utility.getApiKey(), utility.getUSER_ID(), utility.getFirebaseToken(), productid, video_body);
                call.enqueue(new Callback<API_RESPONSE>() {
                    @Override
                    public void onResponse(Call<API_RESPONSE> call, Response<API_RESPONSE> response) {
                        try {
                            utility.hideProgress();
                            Log.d("Video Upload", response.toString());
                            if (response.isSuccessful() && response != null) {
                                API_RESPONSE api_response = response.body();
                                if (api_response.getCode() == 200) {
                                    utility.logger("Video Upload" + response.body().getData().getAsString());
                                    Dioalog_Yes(getResources().getString(R.string.product_succes_string));
                                } else {
                                    utility.showToast(getResources().getString(R.string.try_again_string));
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

    public void Reference_Check_data(Send_Reference reference) {
        try {
            if (utility.isNetworkAvailable()) {
                utility.showProgress(false, getResources().getString(R.string.wait_string));
                Log.d("Reference Check", send_product_new.toString());
                Call<API_RESPONSE> call = apiInterface.FishBAngla_Reference_Check(utility.getApiKey(), utility.getUSER_ID(), utility.getFirebaseToken(), reference);
                call.enqueue(new Callback<API_RESPONSE>() {
                    @Override
                    public void onResponse(Call<API_RESPONSE> call, Response<API_RESPONSE> response) {
                        try {
                            utility.hideProgress();
                            Log.d("Reference Check", response.toString());
                            if (response.isSuccessful() && response != null) {
                                API_RESPONSE api_response = response.body();
                                if (api_response.getCode() == 200) {
                                    utility.logger("Reference Check" + response.body().getData().getAsString());
                                } else {
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

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == Config.RC_PICK_IMAGES && resultCode == RESULT_OK && data != null) {
            imagesList = data.getParcelableArrayListExtra(Config.EXTRA_IMAGES);
            // do your logic here...
            /*for (Image image : imagesList) {
                utility.logger(image.getName());
            }*/
            utility.logger("Image Work");
            try {
                if (imagesList.size() > 0) {
                    imagePreviewtAdapter = new ImagePreviewtAdapter(imagesList, context);
                    RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false);
                    sellProductBinding.uploadImagePreviewRecycle.setLayoutManager(new GridLayoutManager(context, 2));
                    sellProductBinding.uploadImagePreviewRecycle.setItemAnimator(new DefaultItemAnimator());
                    sellProductBinding.uploadImagePreviewRecycle.setAdapter(imagePreviewtAdapter);
                    sellProductBinding.uploadImage.setVisibility(View.GONE);
                    sellProductBinding.uploadImagePreview.setVisibility(View.VISIBLE);
                }
            } catch (Exception e) {
                Log.d("Error Line Number", Log.getStackTraceString(e));
            }
        }

        if (resultCode == Activity.RESULT_OK && requestCode == KeyWord.VIDEO_PERMISSION) {
            utility.logger("Video Work");
            product_video_filename = data.getStringExtra(VideoCaptureActivity.EXTRA_OUTPUT_FILENAME);
            product_video = new File(product_video_filename);
            Log.d("Video Name", product_video_filename);
            if (!TextUtils.isEmpty(product_video_filename)) {
                final Bitmap thumbnail = getThumbnail();
                if (thumbnail != null) {
                    sellProductBinding.uploadVideoPreviewPlayImage.setImageBitmap(thumbnail);
                }
            }
            sellProductBinding.uploadVideoPreview.setVisibility(View.VISIBLE);
            sellProductBinding.uploadVideo.setVisibility(View.GONE);
        } else if (resultCode == Activity.RESULT_CANCELED && requestCode == KeyWord.VIDEO_PERMISSION) {
            product_video_filename = null;
        } else if (resultCode == VideoCaptureActivity.RESULT_ERROR && requestCode == KeyWord.VIDEO_PERMISSION) {
            product_video_filename = null;
        }
        super.onActivityResult(requestCode, resultCode, data);
    }

    public void Reference_Check() {
        try {
            HashMap<String, Integer> screen = utility.getScreenRes();
            int width = screen.get(KeyWord.SCREEN_WIDTH);
            int height = screen.get(KeyWord.SCREEN_HEIGHT);
            int mywidth = (width / 10) * 9;
            final Dialog dialog = new Dialog(context);
            dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
            dialog.getWindow().setBackgroundDrawableResource(android.R.color.transparent);
            dialog.setContentView(R.layout.dialog_reference);
            Button yes = dialog.findViewById(R.id.reference_add);
            Button no = dialog.findViewById(R.id.reference_cancel);
            TextInputEditText refer_number = dialog.findViewById(R.id.reference_input);
            LinearLayout ll = dialog.findViewById(R.id.dialog_layout_size);
            LinearLayout.LayoutParams params = (LinearLayout.LayoutParams) ll.getLayoutParams();
            params.height = LinearLayout.LayoutParams.WRAP_CONTENT;
            params.width = mywidth;
            ll.setLayoutParams(params);
            refer_number.setText(config_dao.getconfigData().getReferrer());
            yes.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    dialog.dismiss();
                    if (!TextUtils.isEmpty(refer_number.getEditableText().toString())) {
                        Reference_Check_data(new Send_Reference(refer_number.getEditableText().toString(), userProfile.getUserId()));
                    } else {
                        utility.showDialog(getResources().getString(R.string.reference_title_string));
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


    private CaptureConfiguration createCaptureConfiguration() {
        final PredefinedCaptureConfigurations.CaptureResolution resolution = PredefinedCaptureConfigurations.CaptureResolution.RES_480P;
        final PredefinedCaptureConfigurations.CaptureQuality quality = PredefinedCaptureConfigurations.CaptureQuality.LOW;

        CaptureConfiguration.Builder builder = new CaptureConfiguration.Builder(resolution, quality);

        try {
            int maxDuration = Integer.valueOf("10");
            builder.maxDuration(maxDuration);
        } catch (final Exception e) {
            //NOP
        }
        try {
            int maxFileSize = Integer.valueOf("2");
            builder.maxFileSize(maxFileSize);
        } catch (final Exception e) {
            //NOP
        }
        if (/*showTimerCb.isChecked()*/true) {
            builder.showRecordingTime();
        }
        if (/*!allowFrontCameraCb.isChecked()*/false) {
            builder.noCameraToggle();
        }
        return builder.build();
    }

    private boolean isReadStorageAllowed() {
        //Getting the permission status
        int result1 = ContextCompat.checkSelfPermission(context, Manifest.permission.READ_EXTERNAL_STORAGE);
        int result2 = ContextCompat.checkSelfPermission(context, Manifest.permission.WRITE_EXTERNAL_STORAGE);
        int result3 = ContextCompat.checkSelfPermission(context, Manifest.permission.CAMERA);
        int result4 = ContextCompat.checkSelfPermission(context, Manifest.permission.RECORD_AUDIO);

        //If permission is granted returning true
        return result1 == PackageManager.PERMISSION_GRANTED && result2 == PackageManager.PERMISSION_GRANTED && result3 == PackageManager.PERMISSION_GRANTED && result4 == PackageManager.PERMISSION_GRANTED;
        //If permission is not granted returning false
    }

    private void requestVideoPermission() {

        if (ActivityCompat.shouldShowRequestPermissionRationale(Sell_Product.this, Manifest.permission.READ_EXTERNAL_STORAGE)) {
            //If the user has denied the permission previously your code will come to this block
            //Here you can explain why you need this permission
            //Explain here why you need this permission
        }
        if (ActivityCompat.shouldShowRequestPermissionRationale(Sell_Product.this, Manifest.permission.WRITE_EXTERNAL_STORAGE)) {
        }
        if (ActivityCompat.shouldShowRequestPermissionRationale(Sell_Product.this, Manifest.permission.CAMERA)) {
        }
        if (ActivityCompat.shouldShowRequestPermissionRationale(Sell_Product.this, Manifest.permission.RECORD_AUDIO)) {
        }
        //And finally ask for the permission
        ActivityCompat.requestPermissions(Sell_Product.this, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.CAMERA, Manifest.permission.RECORD_AUDIO}, KeyWord.VIDEO_PERMISSION);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if (requestCode == KeyWord.VIDEO_PERMISSION) {

            //If permission is granted
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED && grantResults[1] == PackageManager.PERMISSION_GRANTED && grantResults[2] == PackageManager.PERMISSION_GRANTED && grantResults[3] == PackageManager.PERMISSION_GRANTED) {

                if (isReadStorageAllowed()) {
                    final CaptureConfiguration config = createCaptureConfiguration();
                    final String filename = "FishBangla.mp4";
                    final Intent intent = new Intent(context, VideoCaptureActivity.class);
                    intent.putExtra(VideoCaptureActivity.EXTRA_CAPTURE_CONFIGURATION, config);
                    intent.putExtra(VideoCaptureActivity.EXTRA_OUTPUT_FILENAME, filename);
                    startActivityForResult(intent, KeyWord.VIDEO_PERMISSION);
                } else {
                    requestVideoPermission();
                }
            } else {
                Toast.makeText(context, "you just denied the permission", Toast.LENGTH_LONG).show();
            }
        }
    }

    @NonNull
    private MultipartBody.Part prepareFilePart(String partName) {
        // use the FileUtils to get the actual file by uri
        File file = new File(partName);

        // create RequestBody instance from file
        RequestBody requestFile = RequestBody.create(MediaType.parse("image/*"), file);

        // MultipartBody.Part is used to send also the actual file name
        return MultipartBody.Part.createFormData(partName, file.getName(), requestFile);
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
                    if (msg.equalsIgnoreCase(getResources().getString(R.string.product_succes_string))) {
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

    public void playVideo() {
        if (product_video_filename == null) return;

        final Intent videoIntent = new Intent(Intent.ACTION_VIEW);
        videoIntent.setDataAndType(Uri.parse(product_video_filename), "video/*");
        try {
            startActivity(videoIntent);
        } catch (ActivityNotFoundException e) {
            // NOP
            utility.logger(e.toString());
        }
    }

    private Bitmap getThumbnail() {
        if (product_video_filename == null) return null;
        return ThumbnailUtils.createVideoThumbnail(product_video_filename, MediaStore.Video.Thumbnails.FULL_SCREEN_KIND);
    }


    //from image remove adapter
    @Override
    public void onResume() {
        context.registerReceiver(mReceiverLocation, new IntentFilter("image_remove"));
        super.onResume();
    }

    @Override
    public void onPause() {
        context.unregisterReceiver(mReceiverLocation);
        super.onPause();
    }

    private BroadcastReceiver mReceiverLocation = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            try {

                if (imagesList != null && imagePreviewtAdapter != null && imagesList.size() > 0) {
                    String pos = intent.getStringExtra("image_id");
                    imagesList.remove(Integer.parseInt(pos));
                    imagePreviewtAdapter.notifyDataSetChanged();
                    if (imagesList.size() == 0) {
                        sellProductBinding.uploadImagePreview.setVisibility(View.GONE);
                        sellProductBinding.uploadImage.setVisibility(View.VISIBLE);
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    };


}
