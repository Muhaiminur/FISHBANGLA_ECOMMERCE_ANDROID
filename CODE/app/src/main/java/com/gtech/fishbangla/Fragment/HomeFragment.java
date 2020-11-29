package com.gtech.fishbangla.Fragment;

import android.app.Dialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.Uri;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.RatingBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.browser.customtabs.CustomTabsIntent;
import androidx.core.content.ContextCompat;
import androidx.core.widget.NestedScrollView;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.NavController;
import androidx.navigation.fragment.NavHostFragment;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.google.android.material.textfield.TextInputEditText;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.gtech.fishbangla.Activity.Auction_List;
import com.gtech.fishbangla.Activity.Cart_Activity;
import com.gtech.fishbangla.Activity.Home_Page;
import com.gtech.fishbangla.Activity.Landing_Page;
import com.gtech.fishbangla.Adapter.CategoryAdapter;
import com.gtech.fishbangla.Adapter.HomeSuggestAdapter;
import com.gtech.fishbangla.Adapter.HomepageBannerAdapter;
import com.gtech.fishbangla.Adapter.TagListAdapter;
import com.gtech.fishbangla.DATABASE.CARTDATABASE_VIEWMODEL;
import com.gtech.fishbangla.DATABASE.REPOSITORY.CART_REPOSITORY;
import com.gtech.fishbangla.DATABASE.TABLE.CART_TABLE;
import com.gtech.fishbangla.Http.ApiClient;
import com.gtech.fishbangla.Http.ApiInterface;
import com.gtech.fishbangla.Library.EndlessRecyclerViewScrollListener;
import com.gtech.fishbangla.Library.KeyWord;
import com.gtech.fishbangla.Library.Utility;
import com.gtech.fishbangla.MODEL.API_RESPONSE;
import com.gtech.fishbangla.MODEL.Banner.Banner_Data;
import com.gtech.fishbangla.MODEL.Category_Data;
import com.gtech.fishbangla.MODEL.FEEDBACK.Feedback;
import com.gtech.fishbangla.MODEL.FEEDBACK.SEND_FEEDBACK;
import com.gtech.fishbangla.MODEL.PRODUCT.Get_Product;
import com.gtech.fishbangla.MODEL.TAG_PRODUCT.GET_TAGLIST;
import com.gtech.fishbangla.R;
import com.gtech.fishbangla.databinding.FragmentHomeBinding;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class HomeFragment extends Fragment {

    Gson gson;
    Utility utility;
    Context context;
    List<Category_Data> category_list = new ArrayList<>();
    //List<Get_Product> suggestion_list = new ArrayList<>();
    List<GET_TAGLIST> suggestion_list = new ArrayList<>();
    List<Banner_Data> banner_list = new ArrayList<>();
    CategoryAdapter categoryAdapter;
    //HomeSuggestAdapter homeSuggestAdapter;
    TagListAdapter homeSuggestAdapter;
    ApiInterface apiInterface = ApiClient.getBaseClient().create(ApiInterface.class);

    EndlessRecyclerViewScrollListener recyclerViewScrollListener;
    boolean isloading = false;
    boolean nomoredata = false;
    int suggestion_page = 0;

    int feedbacksize = 0;
    List<Feedback> feedbackList;
    List<SEND_FEEDBACK> send_feedbacks = new ArrayList<>();

    FragmentHomeBinding homeBinding;
    HomepageBannerAdapter banner_adapter;
    public HomeFragment() {
    }

    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        if (homeBinding == null) {
            homeBinding = DataBindingUtil.inflate(inflater, R.layout.fragment_home, container, false);
            try {
                context = getActivity();
                utility = new Utility(context);
                gson = new Gson();
                initial_category();
                initial_suggestion();
                homeBinding.homeRequest.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        Fragment navhost = ((AppCompatActivity) context).getSupportFragmentManager().findFragmentById(R.id.nav_host_fragment);
                        NavController c = NavHostFragment.findNavController(navhost);
                        c.navigate(R.id.nav_request);
                    }
                });
                homeBinding.homeAuction.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        startActivity(new Intent(context, Auction_List.class));
                    }
                });
                Get_Banner();
                //Get_Category();

        /*homeBinding.homeNested.setOnScrollChangeListener((NestedScrollView.OnScrollChangeListener) (v, scrollX, scrollY, oldScrollX, oldScrollY) -> {
            if (v.getChildAt(v.getChildCount() - 1) != null) {
                if ((scrollY >= (v.getChildAt(v.getChildCount() - 1).getMeasuredHeight() - v.getMeasuredHeight())) &&
                        scrollY > oldScrollY) {
                    //code to fetch more data for endless scrolling
                    Get_Product("2");

                }
            }
        });*/

                String feedback = utility.getFeedback();
                if (!TextUtils.isEmpty(feedback)) {
                    Type listType = new TypeToken<List<Feedback>>() {
                    }.getType();
                    feedbackList = gson.fromJson(feedback, listType);
                    if (feedbackList != null && feedbackList.size() > 0) {
                        feedbacksize = feedbackList.size();
                        feedbacksize--;
                        showrateDialouge(feedbackList.get(feedbacksize));
                    }
                    utility.logger("FEEDBACK SIZE" + feedbackList.size());
                    utility.clearFeedback();
                }

                if (banner_adapter != null) {
                    homeBinding.homepageBanner.setCustomIndicator(homeBinding.customIndicatorHome);
                }

                //refresh view
                homeBinding.homeRefresh.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
                    @Override
                    public void onRefresh() {
                        homeBinding.homeRefresh.setRefreshing(false);
                        Fragment navhost = ((AppCompatActivity) context).getSupportFragmentManager().findFragmentById(R.id.nav_host_fragment);
                        NavController c = NavHostFragment.findNavController(navhost);
                        c.popBackStack();
                        c.navigate(R.id.nav_home);

                    }
                });
            } catch (Exception e) {
                Log.d("Error Line Number", Log.getStackTraceString(e));
            }
        }
        return homeBinding.getRoot();
    }

    public void initial_category() {
        categoryAdapter = new CategoryAdapter(category_list, context);
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false);
        homeBinding.homepageCategory.setLayoutManager(mLayoutManager);
        homeBinding.homepageCategory.setItemAnimator(new DefaultItemAnimator());
        homeBinding.homepageCategory.setAdapter(categoryAdapter);
        homeBinding.homepageCategory.setNestedScrollingEnabled(false);
    }

    public void initial_suggestion() {
        homeSuggestAdapter = new TagListAdapter(suggestion_list, context);
        LinearLayoutManager mLayoutManager = new LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false);
        //LinearLayoutManager mLayoutManager = new LinearLayoutManager(context);
        homeBinding.homepageSuggestion.setLayoutManager(mLayoutManager);
        homeBinding.homepageSuggestion.setItemAnimator(new DefaultItemAnimator());
        homeBinding.homepageSuggestion.setAdapter(homeSuggestAdapter);
    }

    /*public void initial_suggestion() {
        homeSuggestAdapter = new HomeSuggestAdapter(suggestion_list, context);
        //LinearLayoutManager mLayoutManager = new LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false);
        LinearLayoutManager mLayoutManager = new LinearLayoutManager(context);
        homeBinding.homepageSuggestion.setLayoutManager(mLayoutManager);
        homeBinding.homepageSuggestion.setItemAnimator(new DefaultItemAnimator());
        homeBinding.homepageSuggestion.setAdapter(homeSuggestAdapter);
        homeBinding.homepageSuggestion.setNestedScrollingEnabled(false);
        *//*
        recyclerViewScrollListener = new EndlessRecyclerViewScrollListener(mLayoutManager) {
            @Override
            public void onLoadMore(int page, int totalItemsCount, RecyclerView view) {
                // Triggered only when new data needs to be appended to the list
                // Add whatever code is needed to append new items to the bottom of the list
                Log.d("GET PRODUCT", "check");
                if (totalItemsCount % 20 == 0) {
                    Get_Product(String.valueOf(page - 1));
                }
            }
        };
        homeBinding.homepageSuggestion.addOnScrollListener(recyclerViewScrollListener);*//*
        //initScrollListener();
        nested_scroll();
    }*/

    /*private void preparesuggestionData() {
        for (int c = 0; c < 10; c++) {
            Fish_Model movie1 = new Fish_Model("c", "http://fishbangla.com/image/cache/catalog/Banner001-1140x380.jpg", "Name " + c, "Category " + c, "3.4", "Price " + c, "Minimum sell " + c, "Seller");
            suggestion_list.add(movie1);
        }
        homeSuggestAdapter.notifyDataSetChanged();
    }*/


    private void Get_Banner() {
        try {
            if (utility.isNetworkAvailable()) {
                utility.showProgress(false, getResources().getString(R.string.wait_string));
                Call<API_RESPONSE> call = apiInterface.FishBAngla_Banner_List(utility.getApiKey(), utility.getUSER_ID(), utility.getFirebaseToken());
                call.enqueue(new Callback<API_RESPONSE>() {
                    @Override
                    public void onResponse(Call<API_RESPONSE> call, Response<API_RESPONSE> response) {
                        try {
                            //utility.hideProgress();
                            Log.d("GET BANNER", response.toString());
                            if (response.isSuccessful() && response != null) {
                                API_RESPONSE api_response = response.body();
                                if (api_response.getCode() == 200) {
                                    Type listType = new TypeToken<List<Banner_Data>>() {
                                    }.getType();
                                    List<Banner_Data> banner_data = gson.fromJson(response.body().getData(), listType);
                                    utility.logger(banner_data.size() + "");
                                    banner_list.clear();
                                    banner_list.addAll(banner_data);
                                    banner_adapter = new HomepageBannerAdapter(banner_list);
                                    homeBinding.homepageBanner.create(banner_adapter, getLifecycle());
                                    homeBinding.homepageBanner.setCustomIndicator(homeBinding.customIndicatorHome);
                                    banner_adapter.setOnItemClickListener((item, position) -> {
                                        //handle click here
                                        banner_click_work(item);
                                    });

                                    Get_Category();
                                } else {
                                    utility.showDialog(getResources().getString(R.string.try_again_string));
                                }
                            } else {
                                utility.showDialog(getResources().getString(R.string.try_again_string));
                                utility.hideProgress();
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

    private void Get_Category() {
        try {
            if (utility.isNetworkAvailable()) {
                //utility.showProgress(false, getResources().getString(R.string.wait_string));
                Call<API_RESPONSE> call = apiInterface.FishBAngla_Category_List(utility.getApiKey(), utility.getUSER_ID(), utility.getFirebaseToken());
                call.enqueue(new Callback<API_RESPONSE>() {
                    @Override
                    public void onResponse(Call<API_RESPONSE> call, Response<API_RESPONSE> response) {
                        try {
                            //utility.hideProgress();
                            Log.d("GET CATEGORY", response.toString());
                            if (response.isSuccessful() && response != null) {
                                API_RESPONSE api_response = response.body();
                                if (api_response.getCode() == 200) {
                                    Type listType = new TypeToken<List<Category_Data>>() {
                                    }.getType();
                                    List<Category_Data> category_data = gson.fromJson(response.body().getData(), listType);
                                    utility.logger(category_data.size() + "");
                                    category_list.clear();
                                    /*for (Banner_Data b:api_response.getData()){
                                        banner_list.add(b);
                                    }*/
                                    category_list.addAll(category_data);
                                    categoryAdapter.notifyDataSetChanged();
                                    Get_Product();
                                } else {
                                    utility.showDialog(getResources().getString(R.string.try_again_string));
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
                                    suggestion_list.clear();
                                    suggestion_list.addAll(get_products);
                                    homeSuggestAdapter.notifyDataSetChanged();

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

    /*private void Get_Product(String s) {
        try {
            if (utility.isNetworkAvailable()) {
                Log.d("GET PRODUCT", s);
                //utility.showProgress(false, getResources().getString(R.string.wait_string));
                Call<API_RESPONSE> call = apiInterface.FishBAngla_All_Product(utility.getApiKey(), utility.getUSER_ID(), utility.getFirebaseToken(), s);
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
                                    //suggestion_list.clear();
                                    suggestion_list.addAll(get_products);
                                    homeSuggestAdapter.notifyDataSetChanged();
                                    homeBinding.homepageSuggestion.scrollToPosition(suggestion_list.size() > 20 ? suggestion_list.size() - suggestion_list.size() - 4 : 0);

                                    suggestion_page++;
                                    isloading = false;
                                    if (get_products.size() == 0) {
                                        nomoredata = true;
                                    }
                                    *//*suggestion_list.addAll(get_products);
                                    if (suggestion_list.size() > 0) {
                                        homeSuggestAdapter = new HomeSuggestAdapter(suggestion_list, context);
                                        homeSuggestAdapter.notifyDataSetChanged();
                                        homeBinding.homepageSuggestion.setAdapter(homeSuggestAdapter);
                                        homeBinding.homepageSuggestion.scrollToPosition(suggestion_list.size() > 20 ? suggestion_list.size() - suggestion_list.size() - 4 : 0);
                                    }*//*
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


    private void initScrollListener() {
        homeBinding.homepageSuggestion.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(@NonNull RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
            }

            @Override
            public void onScrolled(@NonNull RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                LinearLayoutManager linearLayoutManager = (LinearLayoutManager) recyclerView.getLayoutManager();
                if (linearLayoutManager != null && linearLayoutManager.findLastCompletelyVisibleItemPosition() == suggestion_list.size() - 1) {
                    //bottom of list!
                    Get_Product("2");
                }
            }
        });
    }

    public void nested_scroll() {
        try {
            if (homeBinding.homeNested != null) {
                homeBinding.homeNested.setOnScrollChangeListener(new NestedScrollView.OnScrollChangeListener() {
                                                                     @Override
                                                                     public void onScrollChange(NestedScrollView v, int scrollX, int scrollY, int oldScrollX, int oldScrollY) {
                                                                         if (v.getChildAt(v.getChildCount() - 1) != null) {
                                                                             if ((scrollY >= (v.getChildAt(v.getChildCount() - 1).getMeasuredHeight() - v.getMeasuredHeight())) && scrollY > oldScrollY) {
                                                                                 try {
                                                                                     utility.logger("pasisi");
                                                                                     if (!isloading && !nomoredata) {
                                                                                         Get_Product(String.valueOf(suggestion_page));
                                                                                         isloading = true;
                                                                                     }
                                                                                 } catch (Exception e) {
                                                                                     Log.d("Error Line Number", Log.getStackTraceString(e));
                                                                                 }

                                                                             }
                                                                         }
                                                                     }
                                                                 }
                );
            }
        } catch (Exception e) {
            Log.d("Error Line Number", Log.getStackTraceString(e));
        }
    }

*/
    public void showrateDialouge(Feedback s) {
        try {
            HashMap<String, Integer> screen = utility.getScreenRes();
            int width = screen.get(KeyWord.SCREEN_WIDTH);
            int height = screen.get(KeyWord.SCREEN_HEIGHT);
            int mywidth = (width / 10) * 9;
            final Dialog dialog = new Dialog(context);
            dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
            dialog.getWindow().setBackgroundDrawableResource(android.R.color.transparent);
            dialog.setContentView(R.layout.dialog_rate);
            Button yes = dialog.findViewById(R.id.rate_add);
            Button no = dialog.findViewById(R.id.rate_cancel);
            TextView title = dialog.findViewById(R.id.rate_title);
            title.setText(s.getProductName() + " " + context.getResources().getString(R.string.ask_ratting_string));
            RatingBar ratingBar = dialog.findViewById(R.id.seller_rating_input);
            TextInputEditText inputEditText = dialog.findViewById(R.id.seller_review_input);
            LinearLayout ll = dialog.findViewById(R.id.dialog_layout_size);
            LinearLayout.LayoutParams params = (LinearLayout.LayoutParams) ll.getLayoutParams();
            params.height = LinearLayout.LayoutParams.WRAP_CONTENT;
            params.width = mywidth;
            ll.setLayoutParams(params);
            yes.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    dialog.dismiss();
                    /*if (!TextUtils.isEmpty(name.getEditableText().toString())) {
                        try {
                            JSONObject jsonObject = new JSONObject();
                            jsonObject.put("name", name.getEditableText().toString());
                            //setProductName(jsonObject);
                        } catch (Exception e) {
                            Log.d("Error Line Number", Log.getStackTraceString(e));
                        }
                    }*/
                    if (feedbacksize != 0 && feedbackList != null) {
                        showrateDialouge(feedbackList.get(feedbacksize - 1));
                        feedbacksize--;
                    }
                    if (!TextUtils.isEmpty(inputEditText.getEditableText().toString())) {
                        send_feedbacks.add(new SEND_FEEDBACK(s.getProductId(), ratingBar.getRating() + "", inputEditText.getEditableText().toString()));
                    } else {
                        send_feedbacks.add(new SEND_FEEDBACK(s.getProductId(), ratingBar.getRating() + "", ""));
                    }
                    utility.logger(send_feedbacks.size() + "ok");
                    if (feedbackList.size() == send_feedbacks.size()) {
                        utility.logger("SENDING FEEDBACK");
                        SEND_Feedback(send_feedbacks);
                    }
                }
            });
            no.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    dialog.dismiss();
                    SEND_Feedback(send_feedbacks);
                }
            });
            dialog.setCancelable(true);
            dialog.show();
        } catch (Exception e) {
            Log.d("Error Line Number", Log.getStackTraceString(e));
        }
    }

    private void SEND_Feedback(List<SEND_FEEDBACK> send) {
        try {
            if (utility.isNetworkAvailable()) {
                utility.showProgress(false, getResources().getString(R.string.wait_string));
                utility.logger("FEEDBACK" + send.toString());
                Call<API_RESPONSE> call = apiInterface.FishBAngla_Product_Feedback(utility.getApiKey(), utility.getUSER_ID(), utility.getFirebaseToken(), send);
                call.enqueue(new Callback<API_RESPONSE>() {
                    @Override
                    public void onResponse(Call<API_RESPONSE> call, Response<API_RESPONSE> response) {
                        try {
                            utility.hideProgress();
                            Log.d("SEND FEEDBACK", response.toString());
                            if (response.isSuccessful() && response != null) {
                                API_RESPONSE api_response = response.body();
                                if (api_response.getCode() == 200) {
                                    utility.showDialog(context.getResources().getString(R.string.feedback_succes_string));
                                } else {
                                    utility.showDialog(getResources().getString(R.string.try_again_string));
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


    public void banner_click_work(Banner_Data banner_data) {
        try {
            if (banner_data.getMetadataBrowse().equalsIgnoreCase(KeyWord.EXTERNAL)) {
                try {
                    String url = banner_data.getMetadata();
                    CustomTabsIntent.Builder builder = new CustomTabsIntent.Builder();
                    builder.setToolbarColor(ContextCompat.getColor(context, R.color.colorAccent));
                    CustomTabsIntent customTabsIntent = builder.build();
                    customTabsIntent.launchUrl(context, Uri.parse(url));
                } catch (Exception e) {
                    Log.d("Error Line Number", Log.getStackTraceString(e));
                    try {
                        Uri uri = Uri.parse(banner_data.getMetadata()); // missing 'http://' will cause crashed
                        Intent intent = new Intent(Intent.ACTION_VIEW, uri);
                        context.startActivity(intent);
                    } catch (Exception e2) {
                        Log.d("Error Line Number", Log.getStackTraceString(e2));
                        utility.showDialog(context.getResources().getString(R.string.no_browser_string));
                    }
                }
            } else if (banner_data.getMetadataBrowse().equalsIgnoreCase(KeyWord.INTERNAL)) {
                if (banner_data.getMetadata().contains(KeyWord.INAPP_SELLER_LIST)) {
                            /*Bundle bundle = new Bundle();
                            bundle.putString("product_id", bodyResponse.getProductId());*/
                    Fragment navhost = ((AppCompatActivity) context).getSupportFragmentManager().findFragmentById(R.id.nav_host_fragment);
                    NavController c = NavHostFragment.findNavController(navhost);
                    //c.popBackStack();
                    c.navigate(R.id.nav_seller);
                } else if (banner_data.getMetadata().contains(KeyWord.INAPP_SELLER_DETAILS)) {
                    String[] parts = banner_data.getMetadata().split("-");
                    String id = parts[1];
                    utility.logger("seller_id" + id);
                    Bundle bundle = new Bundle();
                    bundle.putString("seller_id", id);
                    Fragment navhost = ((AppCompatActivity) context).getSupportFragmentManager().findFragmentById(R.id.nav_host_fragment);
                    NavController c = NavHostFragment.findNavController(navhost);
                    //c.popBackStack();
                    c.navigate(R.id.nav_seller_details, bundle);
                } else if (banner_data.getMetadata().contains(KeyWord.INAPP_PRODUCT_DETAILS)) {
                    String[] parts = banner_data.getMetadata().split("-");
                    String id = parts[1];
                    utility.logger("product_id" + id);
                    Bundle bundle = new Bundle();
                    bundle.putString("product_id", id);
                    Fragment navhost = ((AppCompatActivity) context).getSupportFragmentManager().findFragmentById(R.id.nav_host_fragment);
                    NavController c = NavHostFragment.findNavController(navhost);
                    //c.popBackStack();
                    c.navigate(R.id.frag_product_details, bundle);
                } else if (banner_data.getMetadata().contains(KeyWord.INAPP_NOTIFICATION)) {
                    Fragment navhost = ((AppCompatActivity) context).getSupportFragmentManager().findFragmentById(R.id.nav_host_fragment);
                    NavController c = NavHostFragment.findNavController(navhost);
                    //c.popBackStack();
                    c.navigate(R.id.frag_notification);
                } else if (banner_data.getMetadata().contains(KeyWord.INAPP_CART)) {
                    context.startActivity(new Intent(context, Cart_Activity.class));
                } else if (banner_data.getMetadata().contains(KeyWord.INAPP_DISCOUNT)) {
                    context.startActivity(new Intent(context, Cart_Activity.class));
                } else if (banner_data.getMetadata().contains(KeyWord.INAPP_CATEGORY)) {
                    String[] parts = banner_data.getMetadata().split("-");
                    String id = parts[1];
                    utility.logger("category_id" + id);
                    Bundle bundle = new Bundle();
                    bundle.putString("category_id", id);
                    Fragment navhost = ((AppCompatActivity) context).getSupportFragmentManager().findFragmentById(R.id.nav_host_fragment);
                    NavController c = NavHostFragment.findNavController(navhost);
                    //c.popBackStack();
                    c.navigate(R.id.frag_category_list, bundle);
                } else if (banner_data.getMetadata().contains(KeyWord.INAPP_PROFILE)) {
                    Fragment navhost = ((AppCompatActivity) context).getSupportFragmentManager().findFragmentById(R.id.nav_host_fragment);
                    NavController c = NavHostFragment.findNavController(navhost);
                    //c.popBackStack();
                    c.navigate(R.id.frag_profile);
                } else if (banner_data.getMetadata().contains(KeyWord.INAPP_BUY_HISTORY)) {
                    Fragment navhost = ((AppCompatActivity) context).getSupportFragmentManager().findFragmentById(R.id.nav_host_fragment);
                    NavController c = NavHostFragment.findNavController(navhost);
                    //c.popBackStack();
                    c.navigate(R.id.nav_buy);
                } else if (banner_data.getMetadata().contains(KeyWord.INAPP_SELL_HISTORY)) {
                    Fragment navhost = ((AppCompatActivity) context).getSupportFragmentManager().findFragmentById(R.id.nav_host_fragment);
                    NavController c = NavHostFragment.findNavController(navhost);
                    //c.popBackStack();
                    c.navigate(R.id.nav_sell);
                } else if (banner_data.getMetadata().contains(KeyWord.INAPP_OWN_PRODUCT)) {
                    Fragment navhost = ((AppCompatActivity) context).getSupportFragmentManager().findFragmentById(R.id.nav_host_fragment);
                    NavController c = NavHostFragment.findNavController(navhost);
                    //c.popBackStack();
                    c.navigate(R.id.nav_Own_product);
                } else if (banner_data.getMetadata().contains(KeyWord.INAPP_RATTING)) {
                    context.startActivity(new Intent(context, Landing_Page.class));
                }

            }
        } catch (Exception e) {
            Log.d("Error Line Number", Log.getStackTraceString(e));
        }
    }

    //from Cart activity
    @Override
    public void onResume() {
        context.registerReceiver(mReceiverLocation, new IntentFilter("cart_delete"));
        super.onResume();
        if (banner_adapter != null) {
            homeBinding.homepageBanner.setCustomIndicator(homeBinding.customIndicatorHome);
        }
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
                if (homeSuggestAdapter != null) {
                    homeSuggestAdapter.notifyDataSetChanged();
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    };
}