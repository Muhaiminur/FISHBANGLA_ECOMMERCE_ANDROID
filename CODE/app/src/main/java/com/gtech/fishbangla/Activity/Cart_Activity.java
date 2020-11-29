package com.gtech.fishbangla.Activity;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.Dialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Point;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.Display;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.TextView;

import com.google.android.material.textfield.TextInputEditText;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.gtech.fishbangla.Adapter.AddressListAdapter;
import com.gtech.fishbangla.Adapter.AddressListAdaptercart;
import com.gtech.fishbangla.DATABASE.CARTDATABASE_VIEWMODEL;
import com.gtech.fishbangla.DATABASE.REPOSITORY.CONFIG_REPOSITORY;
import com.gtech.fishbangla.DATABASE.REPOSITORY.DISTRICT_REPOSITORY;
import com.gtech.fishbangla.DATABASE.REPOSITORY.DIVISION_REPOSITORY;
import com.gtech.fishbangla.DATABASE.REPOSITORY.UPOZILLA_REPOSITORY;
import com.gtech.fishbangla.DATABASE.REPOSITORY.USER_REPOSITORY;
import com.gtech.fishbangla.DATABASE.TABLE.CART_TABLE;
import com.gtech.fishbangla.DATABASE.TABLE.CONFIG_TABLE;
import com.gtech.fishbangla.DATABASE.TABLE.DISTRICT_TABLE;
import com.gtech.fishbangla.DATABASE.TABLE.DIVISION_TABLE;
import com.gtech.fishbangla.DATABASE.TABLE.UPAZILLA_TABLE;
import com.gtech.fishbangla.DATABASE.TABLE.USER_PROFILE;
import com.gtech.fishbangla.Http.ApiClient;
import com.gtech.fishbangla.Http.ApiInterface;
import com.gtech.fishbangla.Library.KeyWord;
import com.gtech.fishbangla.MODEL.ADDRESS.SEND_CART_ADDRESS;
import com.gtech.fishbangla.MODEL.ADDRESS.SEND_UPDATEADDRESS;
import com.gtech.fishbangla.MODEL.API_RESPONSE;
import com.gtech.fishbangla.MODEL.DISCOUNT.GET_DISCOUNT;
import com.gtech.fishbangla.MODEL.DISCOUNT.SEND_DISCOUNT;
import com.gtech.fishbangla.MODEL.ORDER.SEND_CART;
import com.gtech.fishbangla.MODEL.ORDER.SEND_ORDER;
import com.gtech.fishbangla.MODEL.Send_UserID;
import com.gtech.fishbangla.MODEL.User.GET_NEW_PAYMENTUSER;
import com.gtech.fishbangla.MODEL.User.Send_User_Update;
import com.gtech.fishbangla.databinding.ActivityCartBinding;
import com.gtech.fishbangla.Adapter.CartAdapter;
import com.gtech.fishbangla.Library.Utility;
import com.gtech.fishbangla.R;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import fr.ganfra.materialspinner.MaterialSpinner;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class Cart_Activity extends AppCompatActivity {

    Utility utility;
    Context context;
    List<CART_TABLE> cart_list = new ArrayList<>();
    CartAdapter cartAdapter;
    ActivityCartBinding activityCartBinding;
    CARTDATABASE_VIEWMODEL cartdatabaseViewmodel;
    USER_REPOSITORY user_dao;
    CONFIG_REPOSITORY config_dao;
    USER_PROFILE userProfile;
    CONFIG_TABLE configTable;
    double product_price = 0;
    double total_price = 0;
    double total_after_discount = 0;
    double discount_amount = 0;
    double service_charge = 0;
    double delivery_charge = 0;
    double vat_amount = 0;
    double ice_amount = 0;
    double ice_quantity = 0;
    String discount_id;
    ApiInterface apiInterface = ApiClient.getBaseClient().create(ApiInterface.class);


    List<SEND_UPDATEADDRESS> address_models;
    AddressListAdaptercart addressListAdapter;


    DIVISION_REPOSITORY divisionRepository;
    List<DIVISION_TABLE> division_tables;
    DISTRICT_REPOSITORY districtRepository;
    List<DISTRICT_TABLE> districtTables;
    UPOZILLA_REPOSITORY upozillaRepository;
    List<UPAZILLA_TABLE> upazillaTables;
    SEND_CART_ADDRESS addadress;
    String payment_type = "";
    String new_user;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cart_);
        getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        try {
            activityCartBinding = DataBindingUtil.setContentView(this, R.layout.activity_cart_);
            cartdatabaseViewmodel = new ViewModelProvider(Cart_Activity.this).get(CARTDATABASE_VIEWMODEL.class);
            user_dao = new USER_REPOSITORY(getApplication());
            config_dao = new CONFIG_REPOSITORY(getApplication());
            userProfile = user_dao.getuserData();
            configTable = config_dao.getconfigData();
            context = Cart_Activity.this;
            utility = new Utility(context);
            if (configTable != null) {
                service_charge = Double.valueOf(configTable.getService_charge());
                delivery_charge = Double.valueOf(configTable.getDelivery_charge());
            }
            initial_cart();
            cartdatabaseViewmodel.getCart_tableLiveData().observe(Cart_Activity.this, new Observer<List<CART_TABLE>>() {
                @Override
                public void onChanged(@Nullable final List<CART_TABLE> cart_tables) {
                    // Update the cached copy of the words in the adapter.
                    if (cart_tables != null) {
                        try {
                            cart_list.clear();
                            cart_list.addAll(cart_tables);
                            cartAdapter.notifyDataSetChanged();
                            product_price = 0;
                            for (CART_TABLE c : cart_tables) {
                                product_price = product_price + Integer.parseInt(c.getCart_unit_price()) * Integer.parseInt(c.getCart_unit_amount());
                            }
                            vat_amount = (product_price / 100) * Double.valueOf(configTable.getVat_charge());
                            total_price = product_price + ice_amount + service_charge + delivery_charge + vat_amount;
                            total_after_discount = total_price - discount_amount;
                            activityCartBinding.cartVatprice.setText(utility.convertToBangle(String.format("%.1f", vat_amount)) + " " + context.getResources().getString(R.string.taka_string));
                            activityCartBinding.cartFishsumonly.setText(utility.convertToBangle(String.valueOf(product_price)) + " " + context.getResources().getString(R.string.taka_string));
                            activityCartBinding.cartTotalprice.setText(utility.convertToBangle(String.valueOf(total_after_discount)) + " " + context.getResources().getString(R.string.taka_string));
                        } catch (Exception e) {
                            Log.d("Error Line Number", Log.getStackTraceString(e));
                        }
                    }
                }
            });
            activityCartBinding.cartCreate.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    try {
                        payment_type = ((RadioButton) findViewById(activityCartBinding.paymentGrp.getCheckedRadioButtonId())).getTag().toString();
                        if (payment_type.equalsIgnoreCase(context.getResources().getString(R.string.cod_payment_tag))) {
                            //utility.showToast("COD"+payment_type);
                            order_process();
                        } else if (payment_type.equalsIgnoreCase(context.getResources().getString(R.string.online_payment_tag))) {
                            // utility.showToast("ONLINE"+payment_type);
                            if (userProfile != null && TextUtils.isEmpty(userProfile.getUserEmail())) {
                                profile_email();
                                utility.logger("ONLINE" + userProfile.getUserEmail());
                            } else if (userProfile != null && !TextUtils.isEmpty(userProfile.getUserEmail())) {
                                utility.logger("ONLINE email" + userProfile.getUserEmail());
                                order_process();
                            } else if (userProfile == null) {
                                //profile_email();
                                utility.showDialog(context.getResources().getString(R.string.login_string));
                            } else {
                                utility.logger("ONLINE no email" + userProfile.getUserEmail());
                            }
                        } else if (payment_type.equalsIgnoreCase(context.getResources().getString(R.string.bkash_payment_tag))) {
                            //utility.showToast("BKASH"+payment_type);
                            profile_email();
                        }
                    } catch (Exception e) {
                        Log.d("Error Line Number", Log.getStackTraceString(e));
                    }
                }
            });
            activityCartBinding.icePlus.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    ice_quantity = ice_quantity + 1;
                    ice_amount = ice_quantity * Double.valueOf(configTable.getFrozen_type());
                    activityCartBinding.iceQuantity.setText(utility.convertToBangle(String.valueOf((int) ice_quantity)));
                    activityCartBinding.cartIcepriceonly.setText(utility.convertToBangle(String.valueOf(ice_amount)) + " " + context.getResources().getString(R.string.taka_string));
                    total_price = product_price + ice_amount + service_charge + delivery_charge + vat_amount;
                    total_after_discount = total_price - discount_amount;
                    activityCartBinding.cartFishsumonly.setText(utility.convertToBangle(String.valueOf(product_price)) + " " + context.getResources().getString(R.string.taka_string));
                    activityCartBinding.cartTotalprice.setText(utility.convertToBangle(String.valueOf(total_after_discount)) + " " + context.getResources().getString(R.string.taka_string));
                }
            });
            activityCartBinding.iceMinus.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (ice_quantity >= 1) {
                        ice_quantity = ice_quantity - 1;
                        ice_amount = ice_quantity * Double.valueOf(configTable.getFrozen_type());
                        activityCartBinding.iceQuantity.setText(utility.convertToBangle(String.valueOf((int) ice_quantity)));
                        activityCartBinding.cartIcepriceonly.setText(utility.convertToBangle(String.valueOf(ice_amount)) + " " + context.getResources().getString(R.string.taka_string));
                        total_price = product_price + ice_amount + service_charge + delivery_charge + vat_amount;
                        total_after_discount = total_price - discount_amount;
                        activityCartBinding.cartFishsumonly.setText(utility.convertToBangle(String.valueOf(product_price)) + " " + context.getResources().getString(R.string.taka_string));
                        activityCartBinding.cartTotalprice.setText(utility.convertToBangle(String.valueOf(total_after_discount)) + " " + context.getResources().getString(R.string.taka_string));
                    }
                }
            });
            activityCartBinding.cartDiscountsubmit.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    try {
                        utility.hideKeyboard(activityCartBinding.getRoot());
                        if (!TextUtils.isEmpty(activityCartBinding.cartDiscountinput.getEditableText().toString())) {
                            SEND_DISCOUNT send_discount = new SEND_DISCOUNT();
                            send_discount.setDiscountKeyword(activityCartBinding.cartDiscountinput.getEditableText().toString());
                            if (userProfile != null && !TextUtils.isEmpty(userProfile.getUserId())) {
                                send_discount.setUserId(userProfile.getUserId());
                            }
                            Check_Discount(send_discount);
                        }
                    } catch (Exception e) {
                        Log.d("Error Line Number", Log.getStackTraceString(e));
                    }
                }
            });
            activityCartBinding.addressNewadd.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (userProfile == null || address_models.size() <= 0) {
                        SHOW_ADD_ADDRESS();
                    } else if (address_models.size() == 0) {
                        SHOW_ADD_ADDRESS();
                    } else {
                        utility.logger("cart no work");
                    }
                }
            });
            activityCartBinding.cartIcepriceonly.setText(utility.convertToBangle(String.format("%.1f", ice_amount)) + " " + context.getResources().getString(R.string.taka_string));
            activityCartBinding.cartServiceprice.setText(utility.convertToBangle(String.format("%.1f", service_charge)) + " " + context.getResources().getString(R.string.taka_string));
            activityCartBinding.cartDeliveryprice.setText(utility.convertToBangle(String.format("%.1f", delivery_charge)) + " " + context.getResources().getString(R.string.taka_string));
            activityCartBinding.cartVatprice.setText(utility.convertToBangle(String.format("%.1f", vat_amount)) + " " + context.getResources().getString(R.string.taka_string));
            activityCartBinding.cartDiscountprice.setText(utility.convertToBangle(String.format("%.1f", discount_amount)) + " " + context.getResources().getString(R.string.taka_string));
            activityCartBinding.cartTotalprice.setText(utility.convertToBangle(String.format("%.1f", total_after_discount)) + " " + context.getResources().getString(R.string.taka_string));
            initial_address();
        } catch (Exception e) {
            Log.d("Error Line Number", Log.getStackTraceString(e));
        }
    }

    public void initial_cart() {
        cartAdapter = new CartAdapter(cart_list, context);
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false);
        activityCartBinding.cartRecyclerview.setLayoutManager(mLayoutManager);
        activityCartBinding.cartRecyclerview.setItemAnimator(new DefaultItemAnimator());
        activityCartBinding.cartRecyclerview.setAdapter(cartAdapter);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        onBackPressed();
        return true;
    }

    public void order_process() {
        try {
            if (cart_list.size() > 0) {
                if (userProfile != null && !TextUtils.isEmpty(userProfile.getUserId()) && addadress == null && address_models.size() > 0) {
                    SEND_ORDER send_order = new SEND_ORDER();
                    send_order.setUserEmail(userProfile.getUserEmail());
                    send_order.setTotalAmount(String.valueOf(total_price));
                    send_order.setServiceCharge(String.valueOf(service_charge));
                    send_order.setProductVat(String.valueOf(vat_amount));
                    send_order.setDeliveryCharge(String.valueOf(delivery_charge));
                    send_order.setDiscountAmount(String.valueOf(discount_amount));
                    send_order.setTotalAfterDiscount(String.valueOf(total_after_discount));
                    send_order.setSubtotal(String.valueOf(product_price));
                    if (!TextUtils.isEmpty(discount_id + "")) {
                        send_order.setDiscountId(discount_id);
                    }
                    send_order.setPaymentTypeId(payment_type);
                    send_order.setIceQty(String.valueOf(ice_quantity));
                    send_order.setIcePrice(String.valueOf(ice_amount));
                    List<SEND_CART> send_carts = new ArrayList<>();
                    for (CART_TABLE c : cart_list) {
                        SEND_CART s_c = new SEND_CART(c.getCart_Id(), c.getCart_unit_amount());
                        send_carts.add(s_c);
                    }
                    send_order.setProducts(send_carts);
                    Create_order(send_order);
                } else {
                    if (addadress != null && !TextUtils.isEmpty(addadress.getUpazillaId())) {
                        SEND_ORDER send_order = new SEND_ORDER();
                        if (userProfile != null) {
                            send_order.setUserEmail(userProfile.getUserEmail());
                        }
                        send_order.setTotalAmount(String.valueOf(total_price));
                        send_order.setServiceCharge(String.valueOf(service_charge));
                        send_order.setProductVat(String.valueOf(vat_amount));
                        send_order.setDeliveryCharge(String.valueOf(delivery_charge));
                        send_order.setDiscountAmount(String.valueOf(discount_amount));
                        send_order.setTotalAfterDiscount(String.valueOf(total_after_discount));
                        send_order.setSubtotal(String.valueOf(product_price));
                        if (!TextUtils.isEmpty(discount_id + "")) {
                            send_order.setDiscountId(discount_id);
                        }
                        send_order.setPaymentTypeId(payment_type);
                        send_order.setIceQty(String.valueOf(ice_quantity));
                        send_order.setIcePrice(String.valueOf(ice_amount));
                        List<SEND_CART> send_carts = new ArrayList<>();
                        for (CART_TABLE c : cart_list) {
                            SEND_CART s_c = new SEND_CART(c.getCart_Id(), c.getCart_unit_amount());
                            send_carts.add(s_c);
                        }
                        send_order.setProducts(send_carts);
                        send_order.setAddress(addadress);
                        Create_order(send_order);
                    } else {
                        utility.showDialog(context.getResources().getString(R.string.address_new_string));
                    }
                }

            } else {
                utility.showDialog(context.getResources().getString(R.string.add_product_string));
            }

        } catch (Exception e) {
            Log.d("Error Line Number", Log.getStackTraceString(e));
        }
    }

    public void Create_order(SEND_ORDER send_order) {
        try {
            if (utility.isNetworkAvailable()) {
                utility.showProgress(false, getResources().getString(R.string.wait_string));
                Log.d("send_order", send_order.toString());
                Call<API_RESPONSE> call = apiInterface.FishBAngla_Create_Order(utility.getApiKey(), utility.getUSER_ID(), utility.getFirebaseToken(), send_order);
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
                                    cartdatabaseViewmodel.deleteall_cart_data();
                                    Dioalog_Yes(getResources().getString(R.string.payment_succes_string), 1);
                                } else if (api_response.getCode() == 201) {
                                    String userid = response.body().getData().getAsString();
                                    utility.logger("send_order new" + userid);
                                    utility.setUSER_ID(response.body().getData().getAsString());
                                    if (addadress != null && userid != null) {
                                        user_dao.deleteall();
                                        USER_PROFILE userProfile = new USER_PROFILE();
                                        userProfile.setUserId(userid);
                                        userProfile.setUserFullName(addadress.getReceiverName());
                                        userProfile.setUserPhoneNo(addadress.getReceiverPhone());
                                        userProfile.setUserImage("");
                                        userProfile.setUserEmail("");
                                        userProfile.setUserRatting("5");
                                        user_dao.insert_user(userProfile);
                                    }
                                    cartdatabaseViewmodel.deleteall_cart_data();
                                    Dioalog_Yes(getResources().getString(R.string.payment_succes_string), 2);
                                } else if (api_response.getCode() == 202) {
                                    String url = response.body().getData().getAsString();
                                    utility.logger("send_order url" + url);
                                    Intent intent = new Intent(context, BrowserActivity.class);
                                    intent.putExtra("url", url);
                                    intent.putExtra("tag", "ONLINE");
                                    context.startActivity(intent);
                                } else if (api_response.getCode() == 203) {
                                    Gson gson = new Gson();
                                    GET_NEW_PAYMENTUSER paymentuser = gson.fromJson(api_response.getData().toString(), GET_NEW_PAYMENTUSER.class);
                                    utility.logger("send_order url" + paymentuser.toString());
                                    new_user = paymentuser.getUserId();
                                    Intent intent = new Intent(context, BrowserActivity.class);
                                    intent.putExtra("url", paymentuser.getGatewayPageUrl());
                                    intent.putExtra("tag", "ONLINE");
                                    context.startActivity(intent);
                                    utility.setUSER_ID(response.body().getData().getAsString());
                                    if (addadress != null && new_user != null) {
                                        user_dao.deleteall();
                                        USER_PROFILE userProfile = new USER_PROFILE();
                                        userProfile.setUserId(new_user);
                                        userProfile.setUserFullName(addadress.getReceiverName());
                                        userProfile.setUserPhoneNo(addadress.getReceiverPhone());
                                        userProfile.setUserImage("");
                                        userProfile.setUserEmail("");
                                        userProfile.setUserRatting("5");
                                        user_dao.insert_user(userProfile);
                                    }
                                } else if (api_response.getCode() == 300) {
                                    utility.showDialog(response.body().getData().getAsString()/*context.getResources().getString(R.string.product_outof_stock_string)*/);
                                } /*else if (api_response.getCode() == 301) {
                                    //utility.showDialog(response.body().getData().getAsString());
                                    utility.showDialog(*//*response.body().getData().getAsString()*//*context.getResources().getString(R.string.fishbangla_contact_string));
                                } else if (api_response.getCode() == 302) {
                                    utility.showDialog(*//*response.body().getData().getAsString()*//*context.getResources().getString(R.string.login_string));
                                } else if (api_response.getCode() == 303) {
                                    utility.showDialog(*//*response.body().getData().getAsString()*//*context.getResources().getString(R.string.own_product_buying_string));
                                } else if (api_response.getCode() == 304) {
                                    utility.showDialog(*//*response.body().getData().getAsString()*//*context.getResources().getString(R.string.add_product_string));
                                } else if (api_response.getCode() == 305) {
                                    utility.showDialog(*//*response.body().getData().getAsString()*//*context.getResources().getString(R.string.add_address_string));
                                }*/ else {
                                    utility.logger("send_order" + response.body().getData().getAsString());
                                    utility.logger("send_order" + response.body().getCode());
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

    public void Check_Discount(SEND_DISCOUNT send_discount) {
        try {
            if (utility.isNetworkAvailable()) {
                utility.showProgress(false, getResources().getString(R.string.wait_string));
                Log.d("Check Discount", send_discount.toString());
                Call<API_RESPONSE> call = apiInterface.FishBAngla_discount_check(utility.getApiKey(), utility.getUSER_ID(), utility.getFirebaseToken(), send_discount);
                call.enqueue(new Callback<API_RESPONSE>() {
                    @Override
                    public void onResponse(Call<API_RESPONSE> call, Response<API_RESPONSE> response) {
                        try {
                            utility.hideProgress();
                            Log.d("Check Discount", response.toString());
                            if (response.isSuccessful() && response != null) {
                                API_RESPONSE api_response = response.body();
                                if (api_response.getCode() == 200) {
                                    Gson gson = new Gson();
                                    GET_DISCOUNT get_discount = gson.fromJson(api_response.getData().toString(), GET_DISCOUNT.class);
                                    utility.logger("Check Discount" + get_discount.toString());
                                    discount_id = get_discount.getDiscountId();
                                    if ((int) product_price >= Integer.parseInt(get_discount.getDiscountMinAmount())) {
                                        if (get_discount.getDiscountPercentage().equalsIgnoreCase("0")) {
                                            utility.logger(Integer.parseInt(get_discount.getDiscountMinAmount()) + " ij " + (int) product_price);
                                            if ((int) product_price >= Integer.parseInt(get_discount.getDiscountMinAmount())) {
                                                discount_amount = Double.parseDouble(get_discount.getDiscountMaxAmount());
                                                total_after_discount = total_price - discount_amount;
                                                activityCartBinding.cartDiscountprice.setText(utility.convertToBangle(String.valueOf(discount_amount)) + " " + context.getResources().getString(R.string.taka_string));
                                                activityCartBinding.cartTotalprice.setText(utility.convertToBangle(String.valueOf(total_after_discount)) + " " + context.getResources().getString(R.string.taka_string));
                                                utility.showDialog(context.getResources().getString(R.string.discount_success_string));
                                            } else {
                                                utility.showDialog(getResources().getString(R.string.min_buy_string) + get_discount.getDiscountMinAmount());
                                            }
                                        } else {
                                            discount_amount = (total_price / 100) * Double.valueOf(get_discount.getDiscountPercentage());
                                            utility.logger("DISCOUNT AMOUNT" + discount_amount);
                                            if ((int) discount_amount > Integer.parseInt(get_discount.getDiscountMaxAmount()) && !get_discount.getDiscountMaxAmount().equalsIgnoreCase("0")) {
                                                discount_amount = Double.parseDouble(get_discount.getDiscountMaxAmount());
                                                utility.logger("DISCOUNT AMOUNT" + discount_amount);
                                            }
                                            utility.logger("total AMOUNT" + total_price);
                                            total_after_discount = total_price - discount_amount;
                                            utility.logger("aftar total AMOUNT" + total_after_discount);
                                            activityCartBinding.cartDiscountprice.setText(utility.convertToBangle(String.valueOf(discount_amount)) + " " + context.getResources().getString(R.string.taka_string));
                                            activityCartBinding.cartTotalprice.setText(utility.convertToBangle(String.valueOf(total_after_discount)) + " " + context.getResources().getString(R.string.taka_string));
                                        }
                                    } else {
                                        utility.showDialog(getResources().getString(R.string.min_buy_string) + get_discount.getDiscountMinAmount());
                                    }
                                } else if (api_response.getCode() == 301) {
                                    utility.logger("Check Discount" + response.body().getData().getAsString());
                                    utility.showDialog(getResources().getString(R.string.discount_invalid_string));
                                } else if (api_response.getCode() == 302) {
                                    utility.logger("Check Discount" + response.body().getData().getAsString());
                                    utility.showDialog(getResources().getString(R.string.discount_limit_string));
                                } else if (api_response.getCode() == 303) {
                                    utility.logger("Check Discount" + response.body().getData().getAsString());
                                    utility.showDialog(getResources().getString(R.string.discount_expired_string));
                                } else {
                                    utility.logger("Check Discount" + response.body().getData().getAsString());
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

    private void Get_Address_List(Send_UserID s) {
        try {
            if (utility.isNetworkAvailable()) {
                Log.d("Address List", s.toString());
                utility.showProgress(false, getResources().getString(R.string.wait_string));
                Call<API_RESPONSE> call = apiInterface.FishBAngla_list_address(utility.getApiKey(), utility.getUSER_ID(), utility.getFirebaseToken(), s);
                call.enqueue(new Callback<API_RESPONSE>() {
                    @Override
                    public void onResponse(Call<API_RESPONSE> call, Response<API_RESPONSE> response) {
                        try {
                            utility.hideProgress();
                            Log.d("Address List", response.toString());
                            if (response.isSuccessful() && response != null) {
                                API_RESPONSE api_response = response.body();
                                if (api_response.getCode() == 200) {
                                    Gson gson = new Gson();
                                    Type listType = new TypeToken<List<SEND_UPDATEADDRESS>>() {
                                    }.getType();
                                    List<SEND_UPDATEADDRESS> get_products = gson.fromJson(response.body().getData(), listType);
                                    Log.d("Address List", get_products.size() + "");
                                    address_models.clear();
                                    address_models.addAll(get_products);
                                    address_models.add(new SEND_UPDATEADDRESS());
                                    addressListAdapter.notifyDataSetChanged();
                                    if (get_products.size() > 0) {
                                        activityCartBinding.addressNoview.setVisibility(View.GONE);
                                        activityCartBinding.cartAddressListView.setVisibility(View.VISIBLE);
                                        activityCartBinding.addressNewview.setVisibility(View.GONE);
                                    } else {
                                        activityCartBinding.addressNoview.setVisibility(View.VISIBLE);
                                        activityCartBinding.addressNewview.setVisibility(View.GONE);
                                        activityCartBinding.cartAddressListView.setVisibility(View.GONE);
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

    public void initial_address() {
        address_models = new ArrayList<>();
        addressListAdapter = new AddressListAdaptercart(address_models, context);
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false);
        activityCartBinding.cartAddressList.setLayoutManager(mLayoutManager);
        activityCartBinding.cartAddressList.setItemAnimator(new DefaultItemAnimator());
        activityCartBinding.cartAddressList.setAdapter(addressListAdapter);
        activityCartBinding.cartAddressLeft.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    activityCartBinding.cartAddressList.smoothScrollToPosition(0);
                } catch (Exception e) {
                    Log.d("Error Line Number", Log.getStackTraceString(e));
                }
            }
        });
        activityCartBinding.cartAddressRight.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    activityCartBinding.cartAddressList.smoothScrollToPosition(addressListAdapter.getItemCount() - 1);
                } catch (Exception e) {
                    Log.d("Error Line Number", Log.getStackTraceString(e));
                }
            }
        });
        if (userProfile != null) {
            Get_Address_List(new Send_UserID(userProfile.getUserId()));
        }
    }


    public void SHOW_ADD_ADDRESS() {
        try {


            HashMap<String, Integer> screen = utility.getScreenRes();
            int width = screen.get(KeyWord.SCREEN_WIDTH);
            int height = screen.get(KeyWord.SCREEN_HEIGHT);
            int mywidth = (width / 10) * 9;
            final Dialog dialog = new Dialog(context);
            dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
            dialog.getWindow().setBackgroundDrawableResource(android.R.color.transparent);
            dialog.setContentView(R.layout.dialog_cart_add_address);
            MaterialSpinner addressDivision = dialog.findViewById(R.id.adrs_division);
            MaterialSpinner addressZilla = dialog.findViewById(R.id.adrs_zilla);
            MaterialSpinner addressUpozilla = dialog.findViewById(R.id.adrs_upozilla);
            TextInputEditText addadressHomenumber = dialog.findViewById(R.id.adrs_homenumber);
            TextInputEditText addadressFlat = dialog.findViewById(R.id.adrs_flat);
            TextInputEditText addadressRoad = dialog.findViewById(R.id.adrs_road);
            TextInputEditText addadressArea = dialog.findViewById(R.id.adrs_area);
            TextInputEditText addadressName = dialog.findViewById(R.id.adrs_name);
            TextInputEditText addadressMbl = dialog.findViewById(R.id.adrs_mbl);

            Button yes = dialog.findViewById(R.id.adrs_add);
            Button no = dialog.findViewById(R.id.adrs_cancel);
            LinearLayout ll = dialog.findViewById(R.id.dialog_layout_size);
            LinearLayout.LayoutParams params = (LinearLayout.LayoutParams) ll.getLayoutParams();
            params.height = LinearLayout.LayoutParams.WRAP_CONTENT;
            params.width = mywidth;
            ll.setLayoutParams(params);

            divisionRepository = new DIVISION_REPOSITORY(getApplication());
            districtRepository = new DISTRICT_REPOSITORY(getApplication());
            upozillaRepository = new UPOZILLA_REPOSITORY(getApplication());
            String[] ITEMS = {};
            ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, ITEMS);
            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            addressDivision.setAdapter(adapter);
            addressZilla.setAdapter(adapter);
            addressUpozilla.setAdapter(adapter);
            division_adpater(addressDivision, addressZilla, addressUpozilla);


            yes.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    //dialog.dismiss();
                    addadress = new SEND_CART_ADDRESS();
                    if (!TextUtils.isEmpty(addadressHomenumber.getEditableText().toString())) {
                        addadress.setBuildingNo(addadressHomenumber.getEditableText().toString());
                    }
                    if (!TextUtils.isEmpty(addadressFlat.getEditableText().toString())) {
                        addadress.setFlatNo(addadressFlat.getEditableText().toString());
                    }
                    if (!TextUtils.isEmpty(addadressRoad.getEditableText().toString())) {
                        addadress.setRoadNo(addadressRoad.getEditableText().toString());
                        if (!TextUtils.isEmpty(addadressArea.getEditableText().toString())) {
                            addadress.setUserVillage(addadressArea.getEditableText().toString());
                            if (!TextUtils.isEmpty(addadressName.getEditableText().toString())) {
                                addadress.setReceiverName(addadressName.getEditableText().toString());
                                if (!TextUtils.isEmpty(addadressMbl.getEditableText().toString()) && utility.validateMsisdn(addadressMbl.getEditableText().toString())) {
                                    addadress.setReceiverPhone(addadressMbl.getEditableText().toString());
                                    UPAZILLA_TABLE upo_data = (UPAZILLA_TABLE) addressUpozilla.getSelectedItem();
                                    if (upo_data != null && !TextUtils.isEmpty(upo_data.getUpazillaId())) {
                                        addadress.setUpazillaId(upo_data.getUpazillaId());
                                        addadress.setUserAddress(addadressArea.getEditableText().toString());

                                        utility.logger(addadress.toString());
                                        dialog.dismiss();
                                        activityCartBinding.addressname.setText(addadress.getReceiverName());
                                        activityCartBinding.addressmbl.setText(utility.convertToBangle(addadress.getReceiverPhone()));
                                        activityCartBinding.addressdetails.setText(addadress.getUserAddress());
                                        activityCartBinding.addressroad.setText(addadress.getFlatNo() + ", " + addadress.getBuildingNo() + ", " + addadress.getRoadNo());
                                        activityCartBinding.addressvillage.setText(addadress.getUserVillage());
                                        activityCartBinding.addressNoview.setVisibility(View.GONE);
                                        activityCartBinding.addressNewview.setVisibility(View.VISIBLE);
                                    } else {
                                        addressUpozilla.setError(getResources().getString(R.string.upozilla_string));
                                        addressUpozilla.requestFocusFromTouch();
                                    }
                                } else {
                                    addadressMbl.setError(context.getResources().getString(R.string.seller_mbl_string));
                                }
                            } else {
                                addadressName.setError(context.getResources().getString(R.string.request_name_string));
                            }
                        } else {
                            addadressArea.setError(context.getResources().getString(R.string.address_area_string));
                        }
                    } else {
                        addadressRoad.setError(context.getResources().getString(R.string.address_road_string));
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

    public void division_adpater(MaterialSpinner addressDivision, MaterialSpinner addressZilla, MaterialSpinner addressUpozilla) {
        try {
            division_tables = divisionRepository.getdivisionData();
            ArrayAdapter<DIVISION_TABLE> division_adapter = new ArrayAdapter<DIVISION_TABLE>(this, android.R.layout.simple_spinner_item, division_tables);
            division_adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            addressDivision.setAdapter(division_adapter);
            addressDivision.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                @Override
                public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                    DIVISION_TABLE selectedItem = (DIVISION_TABLE) parent.getSelectedItem();
                    district_adapter(addressDivision, addressZilla, addressUpozilla, selectedItem);
                    utility.hideKeyboard(view);
                }

                @Override
                public void onNothingSelected(AdapterView<?> parent) {

                }
            });
        } catch (Exception e) {
            Log.d("Error Line Number", Log.getStackTraceString(e));
        }
    }

    public void district_adapter(MaterialSpinner addressDivision, MaterialSpinner addressZilla, MaterialSpinner addressUpozilla, DIVISION_TABLE district_table) {
        try {
            if (district_table != null) {
                districtTables = districtRepository.finddistrictData(district_table.getDivisionId());
                ArrayAdapter<DISTRICT_TABLE> district_adapter = new ArrayAdapter<DISTRICT_TABLE>(this, android.R.layout.simple_spinner_item, districtTables);
                district_adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                addressZilla.setAdapter(district_adapter);
                addressZilla.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                    @Override
                    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                        DISTRICT_TABLE selectedItem = (DISTRICT_TABLE) parent.getSelectedItem();
                        upozilla_adapter(addressDivision, addressZilla, addressUpozilla, selectedItem);
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

    public void upozilla_adapter(MaterialSpinner addressDivision, MaterialSpinner addressZilla, MaterialSpinner addressUpozilla, DISTRICT_TABLE upazilla_table) {
        try {
            if (upazilla_table != null) {
                upazillaTables = upozillaRepository.findupazillaData(upazilla_table.getDistrictId());
                ArrayAdapter<UPAZILLA_TABLE> upazilla_adapter = new ArrayAdapter<UPAZILLA_TABLE>(this, android.R.layout.simple_spinner_item, upazillaTables);
                upazilla_adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                addressUpozilla.setAdapter(upazilla_adapter);
                addressUpozilla.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
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

    public void Dioalog_Yes(String msg, int i) {
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
                    if (i == 2) {
                        //for new login
                        Intent i = getBaseContext().getPackageManager()
                                .getLaunchIntentForPackage(getBaseContext().getPackageName());
                        i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                        startActivity(i);
                        finish();
                    } else {
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

    public void profile_email() {
        try {
            HashMap<String, Integer> screen = utility.getScreenRes();
            int width = screen.get(KeyWord.SCREEN_WIDTH);
            int height = screen.get(KeyWord.SCREEN_HEIGHT);
            int mywidth = (width / 10) * 7;
            final Dialog dialog = new Dialog(context);
            dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
            dialog.getWindow().setBackgroundDrawableResource(android.R.color.transparent);
            dialog.setContentView(R.layout.dialog_user_update);
            TextInputEditText update_email = dialog.findViewById(R.id.user_update_email);
            TextInputEditText update_name = dialog.findViewById(R.id.user_update_name);
            update_name.setVisibility(View.GONE);
            Button update_yes = dialog.findViewById(R.id.btn_yes);
            Button update_no = dialog.findViewById(R.id.btn_no);
            //tv.setText(getResources().getString(R.string.exit_string));
            LinearLayout ll = dialog.findViewById(R.id.dialog_layout_size);
            LinearLayout.LayoutParams params = (LinearLayout.LayoutParams) ll.getLayoutParams();
            params.height = LinearLayout.LayoutParams.WRAP_CONTENT;
            params.width = mywidth;
            ll.setLayoutParams(params);
            update_yes.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    dialog.dismiss();
                    if (!TextUtils.isEmpty(update_email.getEditableText().toString()) && !TextUtils.isEmpty(userProfile.getUserId())) {
                        userProfile.setUserEmail(update_email.getEditableText().toString());
                    } else {
                        utility.showDialog(getResources().getString(R.string.validation_string));
                    }
                }
            });
            update_no.setOnClickListener(new View.OnClickListener() {
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

    //address update
    private BroadcastReceiver mReceiverLocation = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            try {
                utility.logger("From CART Address");
                Get_Address_List(new Send_UserID(userProfile.getUserId()));
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    };
    //online payment
    private BroadcastReceiver online_payment = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            try {
                utility.logger("From Online payment");
                cartdatabaseViewmodel.deleteall_cart_data();
                if (new_user != null && !TextUtils.isEmpty(new_user)) {
                    Dioalog_Yes(getResources().getString(R.string.payment_succes_string), 2);
                } else {
                    Dioalog_Yes(getResources().getString(R.string.payment_succes_string), 1);
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    };

    @Override
    public void onResume() {
        try {
            context.registerReceiver(online_payment, new IntentFilter("online.payment.check"));
        } catch (Exception e) {
            Log.d("Error Line Number", Log.getStackTraceString(e));
        }
        super.onResume();
    }

    @Override
    public void onDestroy() {
        try {
            context.unregisterReceiver(online_payment);
        } catch (Exception e) {
            Log.d("Error Line Number", Log.getStackTraceString(e));
        }
        super.onDestroy();
    }
}
