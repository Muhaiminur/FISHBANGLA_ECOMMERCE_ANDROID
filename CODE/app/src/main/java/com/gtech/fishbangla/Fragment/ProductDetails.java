package com.gtech.fishbangla.Fragment;


import android.annotation.SuppressLint;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.Uri;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.fragment.NavHostFragment;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.gtech.fishbangla.Adapter.HomeSuggestAdapter;
import com.gtech.fishbangla.Adapter.ProductDetailsAdapter;
import com.gtech.fishbangla.DATABASE.REPOSITORY.CART_REPOSITORY;
import com.gtech.fishbangla.DATABASE.TABLE.CART_TABLE;
import com.gtech.fishbangla.Http.ApiClient;
import com.gtech.fishbangla.Http.ApiInterface;
import com.gtech.fishbangla.Library.KeyWord;
import com.gtech.fishbangla.Library.Utility;
import com.gtech.fishbangla.MODEL.API_RESPONSE;
import com.gtech.fishbangla.MODEL.Banner.Product_Details_Banner;
import com.gtech.fishbangla.MODEL.PRODUCT.Get_Product;
import com.gtech.fishbangla.MODEL.PRODUCT.Get_Product_Details;
import com.gtech.fishbangla.MODEL.PRODUCT.Product_Image;
import com.gtech.fishbangla.MODEL.PRODUCT.Send_Product_Details;
import com.gtech.fishbangla.MODEL.PRODUCT.Send_Product_Suggestion;
import com.gtech.fishbangla.MODEL.RECYCLER_MODEL.Fish_Model;
import com.gtech.fishbangla.MODEL.SEND_FVRT;
import com.gtech.fishbangla.R;
import com.gtech.fishbangla.databinding.FragmentProductDetailsBinding;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * A simple {@link Fragment} subclass.
 */
public class ProductDetails extends Fragment {
    Utility utility;
    Context context;
    List<Get_Product> suggestion_list = new ArrayList<>();
    HomeSuggestAdapter detailsuggestAdapter;

    String Product_id;
    ApiInterface apiInterface = ApiClient.getBaseClient().create(ApiInterface.class);
    FragmentProductDetailsBinding productDetailsBinding;
    Get_Product_Details productDetails;
    CART_REPOSITORY cart_repository;

    String isfvrt = KeyWord.YES;

    List<Product_Details_Banner> detailsBanners = new ArrayList<>();

    public ProductDetails() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        if (productDetailsBinding == null) {
            productDetailsBinding = DataBindingUtil.inflate(inflater, R.layout.fragment_product_details, container, false);
            try {
                context = getActivity();
                utility = new Utility(context);
                cart_repository = new CART_REPOSITORY(getActivity().getApplication());
                if (getArguments() != null) {
                    Product_id = getArguments().getString("product_id");
                }
                initial_suggestion();
                //init_slider();
                productDetailsBinding.productDetailsPlus.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        try {
                            int quan = Integer.parseInt(productDetailsBinding.productDetailsUnit.getText().toString());
                            if (quan > 0) {
                                quan += 1;
                                productDetailsBinding.productDetailsUnit.setText(utility.convertToBangle(String.valueOf(quan)));
                                int total_price = 0;
                                for (int c = 0; c < quan; c++) {
                                    total_price = total_price + Integer.parseInt(productDetails.getPrice());
                                }
                                productDetailsBinding.productDetailsTotalPrice.setText(utility.convertToBangle(String.valueOf(total_price)) + " " + getResources().getString(R.string.taka_string));
                                cart_repository.update_cart_data(productDetails.getProductId(), productDetailsBinding.productDetailsUnit.getText().toString());
                            }
                        } catch (Exception e) {
                            Log.d("Error Line Number", Log.getStackTraceString(e));
                        }
                    }
                });
                productDetailsBinding.productDetailsMinus.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        try {
                            int quan = Integer.parseInt(productDetailsBinding.productDetailsUnit.getText().toString());
                            if (quan > 2 && quan >= Integer.parseInt(productDetailsBinding.productDetailsUnit.getText().toString())) {
                                quan -= 1;
                                productDetailsBinding.productDetailsUnit.setText(utility.convertToBangle(String.valueOf(quan)));
                                int total_price = 0;
                                for (int c = 0; c < quan; c++) {
                                    total_price = total_price + Integer.parseInt(productDetails.getPrice());
                                }
                                productDetailsBinding.productDetailsTotalPrice.setText(utility.convertToBangle(String.valueOf(total_price)) + " " + getResources().getString(R.string.taka_string));
                                cart_repository.update_cart_data(productDetails.getProductId(), productDetailsBinding.productDetailsUnit.getText().toString());
                            }
                        } catch (Exception e) {
                            Log.d("Error Line Number", Log.getStackTraceString(e));
                        }

                    }
                });
                productDetailsBinding.productDetailsAdd.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        try {
                            if (productDetails != null && !productDetails.getUserId().equalsIgnoreCase(utility.getUSER_ID())) {
                                CART_TABLE check = cart_repository.find_single_Cart(productDetails.getProductId());
                                String s = productDetails.getMinUnitQty();
                                if (check != null && !TextUtils.isEmpty(check.getCart_unit_amount())) {
                                    s = check.getCart_unit_amount();
                                }
                                utility.showDialog(context.getResources().getString(R.string.add_cart_string));
                                if (productDetails.getProductPicture() != null && productDetails.getProductPicture().size() > 0) {
                                    cart_repository.insert_single_cart_data(new CART_TABLE(productDetails.getProductId(), productDetails.getProductPicture().get(0).getImage(), productDetails.getProductName(), productDetails.getMinUnitQty(), productDetails.getUnitType(), productDetails.getUserFullName(), productDetails.getPrice(), productDetails.getUnitSize(), productDetails.getUnitType(), s));
                                } else {
                                    cart_repository.insert_single_cart_data(new CART_TABLE(productDetails.getProductId(), "", productDetails.getProductName(), productDetails.getMinUnitQty(), productDetails.getUnitType(), productDetails.getUserFullName(), productDetails.getPrice(), productDetails.getUnitSize(), productDetails.getUnitType(), s));
                                }
                                cart_repository.update_cart_data(productDetails.getProductId(), productDetailsBinding.productDetailsUnit.getText().toString());
                            } else {
                                utility.showDialog(context.getResources().getString(R.string.own_product_buying_string));
                            }
                        } catch (Exception e) {
                            Log.d("Error Line Number", Log.getStackTraceString(e));
                        }
                    }
                });
                productDetailsBinding.fishDetailsFvrt.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        try {
                            if (!utility.getUSER_ID().equalsIgnoreCase(KeyWord.USERID)) {
                                Add_Fvrt(new SEND_FVRT(productDetails.getProductId(), isfvrt));
                            } else {
                                utility.showDialog(context.getResources().getString(R.string.login_string));
                            }
                        } catch (Exception e) {
                            Log.d("Error Line Number", Log.getStackTraceString(e));
                        }
                    }
                });
                productDetailsBinding.fishDetailsSeller.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        try {
                            if (productDetails != null && !TextUtils.isEmpty(productDetails.getUserId())) {
                                Bundle bundle = new Bundle();
                                bundle.putString("seller_id", productDetails.getUserId());
                                Fragment navhost = ((AppCompatActivity) context).getSupportFragmentManager().findFragmentById(R.id.nav_host_fragment);
                                NavController c = NavHostFragment.findNavController(navhost);
                                //c.popBackStack();
                                c.navigate(R.id.nav_seller_details, bundle);
                            }
                        } catch (Exception e) {
                            Log.d("Error Line Number", Log.getStackTraceString(e));
                        }
                    }
                });
            } catch (Exception e) {
                Log.d("Error Line Number", Log.getStackTraceString(e));
            }
        }
        return productDetailsBinding.getRoot();
    }

    public void initial_suggestion() {
        detailsuggestAdapter = new HomeSuggestAdapter(suggestion_list, context);
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false);
        productDetailsBinding.productDetailsRecycler.setLayoutManager(mLayoutManager);
        productDetailsBinding.productDetailsRecycler.setItemAnimator(new DefaultItemAnimator());
        productDetailsBinding.productDetailsRecycler.setAdapter(detailsuggestAdapter);
        if (!TextUtils.isEmpty(Product_id)) {
            Get_Product_Details(new Send_Product_Details(Product_id));
        }
    }


    private void Get_Suggestion(Send_Product_Suggestion s) {
        try {
            if (utility.isNetworkAvailable()) {
                Log.d("GET SUGGESTION", s.toString());
                //utility.showProgress(false, getResources().getString(R.string.wait_string));
                Call<API_RESPONSE> call = apiInterface.FishBAngla_Product_Suggestion(utility.getApiKey(), utility.getUSER_ID(), utility.getFirebaseToken(), s);
                call.enqueue(new Callback<API_RESPONSE>() {
                    @Override
                    public void onResponse(Call<API_RESPONSE> call, Response<API_RESPONSE> response) {
                        try {
                            utility.hideProgress();
                            Log.d("GET SUGGESTION", response.toString());
                            if (response.isSuccessful() && response != null) {
                                API_RESPONSE api_response = response.body();
                                if (api_response.getCode() == 200) {
                                    Gson gson = new Gson();
                                    Type listType = new TypeToken<List<Get_Product>>() {
                                    }.getType();
                                    List<Get_Product> get_products = gson.fromJson(response.body().getData(), listType);
                                    Log.d("SUGGESTION List Size", get_products.size() + "");
                                    suggestion_list.clear();
                                    suggestion_list.addAll(get_products);
                                    detailsuggestAdapter.notifyDataSetChanged();
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
                            //utility.hideProgress();
                            Log.d("GET PRODUCT DETAILS", response.toString());
                            if (response.isSuccessful() && response != null) {
                                API_RESPONSE api_response = response.body();
                                if (api_response.getCode() == 200) {
                                    Gson gson = new Gson();
                                    productDetails = gson.fromJson(api_response.getData().toString(), Get_Product_Details.class);
                                    Log.d("GET PRODUCT DETAILS", productDetails.toString());
                                    if (productDetails != null) {
                                        productDetailsBinding.fishDetailsName.setText(productDetails.getProductName());
                                        String price = utility.convertToBangle(productDetails.getPrice()) + " " + getResources().getString(R.string.taka_string) + "/ " + utility.convertToBangle(productDetails.getUnitSize());
                                        String avg_weight = getResources().getString(R.string.avg_string) + " " + utility.convertToBangle(productDetails.getAvgWeight());
                                        String minimum_buy = getResources().getString(R.string.min_buy_string) + " " + utility.convertToBangle(productDetails.getMinUnitQty()) + " " + context.getResources().getString(R.string.unit_unit_string);
                                        if (productDetails.getUnitType().equalsIgnoreCase(KeyWord.UNIT_KG)) {
                                            price = price + " " + getResources().getString(R.string.unit_kg_string);
                                            //minimum_buy = minimum_buy + " " + getResources().getString(R.string.unit_kg_string);
                                            //productDetailsBinding.productDetailsUnitName.setText(getResources().getString(R.string.unit_kg_string));
                                        } else if (productDetails.getUnitType().equalsIgnoreCase(KeyWord.UNIT_GRAM)) {
                                            price = price + " " + getResources().getString(R.string.unit_gram_string);
                                            //minimum_buy = minimum_buy + " " + getResources().getString(R.string.unit_gram_string);
                                            //productDetailsBinding.productDetailsUnitName.setText(getResources().getString(R.string.unit_gram_string));
                                        } else if (productDetails.getUnitType().equalsIgnoreCase(KeyWord.UNIT_PIECE)) {
                                            price = price + " " + getResources().getString(R.string.unit_piece_string);
                                            //minimum_buy = minimum_buy + " " + getResources().getString(R.string.unit_piece_string);
                                            //productDetailsBinding.productDetailsUnitName.setText(getResources().getString(R.string.unit_piece_string));
                                        }
                                        productDetailsBinding.productDetailsUnitName.setText(getResources().getString(R.string.unit_unit_string));
                                        if (productDetails.getAvgWeightUnit().equalsIgnoreCase(KeyWord.UNIT_KG)) {
                                            avg_weight = avg_weight + " " + getResources().getString(R.string.unit_kg_string);
                                        } else if (productDetails.getAvgWeightUnit().equalsIgnoreCase(KeyWord.UNIT_GRAM)) {
                                            avg_weight = avg_weight + " " + getResources().getString(R.string.unit_gram_string);
                                        } else if (productDetails.getAvgWeightUnit().equalsIgnoreCase(KeyWord.UNIT_PIECE)) {
                                            avg_weight = avg_weight + " " + getResources().getString(R.string.unit_piece_string);
                                        }
                                        productDetailsBinding.fishDetailsPrice.setText(price);
                                        productDetailsBinding.fishDetailsAvg.setText(avg_weight);
                                        productDetailsBinding.fishDetailsMinimumSell.setText(minimum_buy);
                                        productDetailsBinding.fishDetailsSeller.setText(getResources().getString(R.string.single_seller_string) + " " + productDetails.getUserFullName() + " | " + productDetails.getDistrictName() + " | " + productDetails.getDivisionName());
                                        productDetailsBinding.productDetailsUnit.setText(utility.convertToBangle(productDetails.getMinUnitQty()));
                                        int total_price = 0;
                                        for (int c = 0; c < Integer.parseInt(productDetails.getMinUnitQty()); c++) {
                                            total_price = total_price + Integer.parseInt(productDetails.getPrice());
                                        }
                                        productDetailsBinding.productDetailsTotalPrice.setText(utility.convertToBangle(String.valueOf(total_price)) + " " + getResources().getString(R.string.taka_string));
                                        for (Product_Image s : productDetails.getProductPicture()) {
                                            detailsBanners.add(new Product_Details_Banner(s.getImage(), KeyWord.IMAGE_TYPE));
                                        }
                                        if (!TextUtils.isEmpty(productDetails.getProductVideo())) {
                                            detailsBanners.add(new Product_Details_Banner(productDetails.getProductVideo(), KeyWord.VIDEO_TYPE));
                                            utility.logger("video check");
                                        }
                                        ProductDetailsAdapter banner_adapter = new ProductDetailsAdapter(detailsBanners);
                                        productDetailsBinding.fishDetailsBanner.create(banner_adapter, getLifecycle());
                                        productDetailsBinding.fishDetailsBanner.setCustomIndicator(productDetailsBinding.customIndicator);
                                        banner_adapter.setOnItemClickListener((item, position) -> {
                                            //handle click here
                                            if (item.getMedia_type().equalsIgnoreCase(KeyWord.VIDEO_TYPE)) {
                                                try {
                                                    Uri uri = Uri.parse(item.getMedia_url());
                                                    Intent intent = new Intent(Intent.ACTION_VIEW, uri);
                                                    intent.setDataAndType(uri, "video/mp4");
                                                    startActivity(intent);
                                                } catch (Exception e) {
                                                    Log.d("Error Line Number", Log.getStackTraceString(e));
                                                    utility.showToast(getResources().getString(R.string.video_exception));
                                                }

                                            }
                                            utility.logger("video" + item.toString());
                                        });

                                        if (productDetails.getIsFavourite().equalsIgnoreCase(KeyWord.YES)) {
                                            productDetailsBinding.fishDetailsFvrt.setText(context.getResources().getString(R.string.fvrt_delete_string));
                                            productDetailsBinding.fishDetailsFvrt.setTextColor(context.getResources().getColor(R.color.app_hint2));
                                            isfvrt = KeyWord.NO;
                                        } /*else if (productDetails.getIsFavourite().equalsIgnoreCase(KeyWord.NO)) {

                                        }*/

                                    }

                                    Get_Suggestion(new Send_Product_Suggestion(Product_id, KeyWord.TAG_BOTH));
                                } else {
                                    utility.showDialog(getResources().getString(R.string.try_again_string));
                                    utility.hideProgress();
                                }
                            } else {
                                utility.showDialog(getResources().getString(R.string.try_again_string));
                                utility.hideProgress();
                            }
                        } catch (Exception ex) {
                            utility.logger(ex.toString());
                            utility.hideProgress();
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

    private void Add_Fvrt(SEND_FVRT s) {
        try {
            if (utility.isNetworkAvailable()) {
                Log.d("SEND FVRT", s.toString());
                //utility.showProgress(false, getResources().getString(R.string.wait_string));
                Call<API_RESPONSE> call = apiInterface.FishBAngla_Fvrt_Add(utility.getApiKey(), utility.getUSER_ID(), utility.getFirebaseToken(), s);
                call.enqueue(new Callback<API_RESPONSE>() {
                    @Override
                    public void onResponse(Call<API_RESPONSE> call, Response<API_RESPONSE> response) {
                        try {
                            utility.hideProgress();
                            Log.d("SEND FVRT", response.toString());
                            if (response.isSuccessful() && response != null) {
                                API_RESPONSE api_response = response.body();
                                Log.d("SEND FVRT", api_response.getData().toString());
                                if (api_response.getCode() == 200) {
                                    utility.showDialog(context.getResources().getString(R.string.fvrt_add_success_string));
                                    productDetailsBinding.fishDetailsFvrt.setText(context.getResources().getString(R.string.fvrt_delete_string));
                                    productDetailsBinding.fishDetailsFvrt.setTextColor(context.getResources().getColor(R.color.app_hint2));
                                    isfvrt = KeyWord.NO;
                                } else if (api_response.getCode() == 201) {
                                    utility.showDialog(context.getResources().getString(R.string.fvrt_delete_success_string));
                                    productDetailsBinding.fishDetailsFvrt.setText(context.getResources().getString(R.string.fvrt_add_string));
                                    productDetailsBinding.fishDetailsFvrt.setTextColor(context.getResources().getColor(R.color.app_green));
                                    isfvrt = KeyWord.YES;
                                } else if (api_response.getCode() == 300) {
                                    utility.showDialog(context.getResources().getString(R.string.login_string));
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

    //from Cart activity
    @Override
    public void onResume() {
        try {
            context.registerReceiver(mReceiverLocation, new IntentFilter("cart_delete"));
        } catch (Exception e) {
            Log.d("Error Line Number", Log.getStackTraceString(e));
        }
        super.onResume();
    }

    @Override
    public void onDestroy() {
        try {
            context.unregisterReceiver(mReceiverLocation);
        } catch (Exception e) {
            Log.d("Error Line Number", Log.getStackTraceString(e));
        }
        super.onDestroy();
    }

    private BroadcastReceiver mReceiverLocation = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            try {
                if (detailsuggestAdapter != null) {
                    detailsuggestAdapter.notifyDataSetChanged();
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    };
}
