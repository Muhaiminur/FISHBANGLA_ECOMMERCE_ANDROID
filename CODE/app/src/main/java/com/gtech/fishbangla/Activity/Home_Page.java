package com.gtech.fishbangla.Activity;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Point;
import android.net.Uri;
import android.os.Bundle;

import com.google.android.material.bottomappbar.BottomAppBar;

import android.text.TextUtils;
import android.util.Log;
import android.view.Display;
import android.view.MenuItem;
import android.view.View;

import androidx.annotation.Nullable;
import androidx.browser.customtabs.CustomTabsIntent;
import androidx.core.content.ContextCompat;
import androidx.core.view.GravityCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.fragment.NavHostFragment;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.internal.NavigationMenuView;
import com.google.android.material.navigation.NavigationView;
import com.gtech.fishbangla.BuildConfig;
import com.gtech.fishbangla.DATABASE.CARTDATABASE_VIEWMODEL;
import com.gtech.fishbangla.DATABASE.REPOSITORY.USER_REPOSITORY;
import com.gtech.fishbangla.DATABASE.TABLE.CART_TABLE;
import com.gtech.fishbangla.DATABASE.TABLE.USER_PROFILE;
import com.gtech.fishbangla.Library.KeyWord;
import com.gtech.fishbangla.Library.Utility;
import com.gtech.fishbangla.R;

import androidx.drawerlayout.widget.DrawerLayout;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.DividerItemDecoration;

import android.view.Window;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.HashMap;
import java.util.List;

public class Home_Page extends AppCompatActivity {

    private AppBarConfiguration mAppBarConfiguration;
    NavigationView navigationView;
    BottomAppBar bottomAppBar;
    LinearLayout home;
    LinearLayout search;
    LinearLayout cart;
    LinearLayout profile;
    DrawerLayout drawer;
    FragmentManager fragmentManager;
    FloatingActionButton home_button;
    MenuItem sell_product;
    MenuItem all_product;
    Utility utility = new Utility(this);

    CARTDATABASE_VIEWMODEL cartdatabaseViewmodel;
    TextView cart_count;

    USER_PROFILE userProfile;
    USER_REPOSITORY user_dao;
    Context context;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home__page);

        cartdatabaseViewmodel = new ViewModelProvider(Home_Page.this).get(CARTDATABASE_VIEWMODEL.class);

        try {
            context = Home_Page.this;
            user_dao = new USER_REPOSITORY(getApplication());
            userProfile = user_dao.getuserData();
            Toolbar toolbar = findViewById(R.id.toolbar);
            setSupportActionBar(toolbar);
            toolbar.setVisibility(View.GONE);
            drawer = findViewById(R.id.drawer_layout);
            navigationView = findViewById(R.id.nav_view);

            NavigationMenuView navMenuView = (NavigationMenuView) navigationView.getChildAt(0);
            navMenuView.addItemDecoration(new DividerItemDecoration(Home_Page.this, DividerItemDecoration.VERTICAL));
            // Passing each menu ID as a set of Ids because each
            // menu should be considered as top level destinations.
            mAppBarConfiguration = new AppBarConfiguration.Builder(
                    R.id.nav_home, R.id.nav_seller, R.id.nav_contact,
                    R.id.nav_buy)
                    .setDrawerLayout(drawer)
                    .build();
            NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment);
            NavigationUI.setupActionBarWithNavController(this, navController, mAppBarConfiguration);
            NavigationUI.setupWithNavController(navigationView, navController);

            bottomAppBar = findViewById(R.id.bottom_bar);
            home = findViewById(R.id.nav_home);
            search = findViewById(R.id.nav_search);
            cart = findViewById(R.id.nav_cart);
            profile = findViewById(R.id.nav_profile);
            home_button = findViewById(R.id.bottom_home);
            cart_count = findViewById(R.id.cart_count);

            if (userProfile != null && !TextUtils.isEmpty(userProfile.getUserId())) {
                navigationView.getMenu().findItem(R.id.nav_login_register).setVisible(false);
            } else {
                navigationView.getMenu().findItem(R.id.nav_sell).setVisible(false);
                navigationView.getMenu().findItem(R.id.nav_buy).setVisible(false);
                //navigationView.getMenu().findItem(R.id.nav_login_register).setVisible(false);
            }

            //header version work
            try {
                View navHeaderView = navigationView.getHeaderView(0);
                TextView tvHeaderName = navHeaderView.findViewById(R.id.head_version);
                String versionName = BuildConfig.VERSION_NAME;
                //tvHeaderName.setText("ভার্সন: " + utility.convertToBangle(utility.decodeBase64(db.getConfigByCode("VERSION_CODE").getValue())));
                tvHeaderName.setText(context.getResources().getString(R.string.version_string) + " " + utility.convertToBangle(versionName));
            } catch (Exception e) {
                Log.d("Error Line Number", Log.getStackTraceString(e));
            }

            home.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (drawer.isDrawerOpen(GravityCompat.START)) {
                        //drawer is open
                        drawer.closeDrawer(GravityCompat.START);
                    } else {
                        drawer.openDrawer(GravityCompat.START);
                    }
                /*Fragment navhost = getSupportFragmentManager().findFragmentById(R.id.nav_host_fragment);
                NavController c = NavHostFragment.findNavController(navhost);
                c.navigate(R.id.nav_home);*/
                }
            });
            search.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                /*Fragment navhost = getSupportFragmentManager().findFragmentById(R.id.nav_host_fragment);
                NavController c = NavHostFragment.findNavController(navhost);
                c.popBackStack();
                c.navigate(R.id.nav_send);*/
                    startActivity(new Intent(Home_Page.this, SearchProduct.class));
                }
            });
            cart.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                /*Fragment navhost = getSupportFragmentManager().findFragmentById(R.id.nav_host_fragment);
                NavController c = NavHostFragment.findNavController(navhost);
                c.popBackStack();
                c.navigate(R.id.nav_send);*/
                    startActivity(new Intent(Home_Page.this, Cart_Activity.class));
                }
            });
            profile.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (userProfile != null && !TextUtils.isEmpty(userProfile.getUserId())) {
                        Fragment navhost = getSupportFragmentManager().findFragmentById(R.id.nav_host_fragment);
                        NavController c = NavHostFragment.findNavController(navhost);
                        c.popBackStack();
                        c.navigate(R.id.frag_profile);
                    } else {
                        Fragment navhost = getSupportFragmentManager().findFragmentById(R.id.nav_host_fragment);
                        NavController c = NavHostFragment.findNavController(navhost);
                        c.popBackStack();
                        c.navigate(R.id.nav_login_register);
                    }
                }
            });
            fragmentManager = getSupportFragmentManager();
            home_button.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Fragment navhost = getSupportFragmentManager().findFragmentById(R.id.nav_host_fragment);
                    NavController c = NavHostFragment.findNavController(navhost);
                    c.popBackStack();
                    c.navigate(R.id.nav_home, null);
                }
            });

            sell_product = navigationView.getMenu().findItem(R.id.nav_sell);
            sell_product.setOnMenuItemClickListener(new MenuItem.OnMenuItemClickListener() {
                @Override
                public boolean onMenuItemClick(MenuItem item) {
                    if (drawer.isDrawerOpen(GravityCompat.START)) {
                        //drawer is open
                        drawer.closeDrawer(GravityCompat.START);
                    } else {
                        drawer.openDrawer(GravityCompat.START);
                    }
                    startActivity(new Intent(Home_Page.this, Sell_Product.class));
                    return true;
                }
            });
            all_product = navigationView.getMenu().findItem(R.id.nav_home);
            all_product.setOnMenuItemClickListener(new MenuItem.OnMenuItemClickListener() {
                @Override
                public boolean onMenuItemClick(MenuItem item) {
                    if (drawer.isDrawerOpen(GravityCompat.START)) {
                        //drawer is open
                        drawer.closeDrawer(GravityCompat.START);
                    } else {
                        drawer.openDrawer(GravityCompat.START);
                    }
                    Fragment navhost = getSupportFragmentManager().findFragmentById(R.id.nav_host_fragment);
                    NavController c = NavHostFragment.findNavController(navhost);
                    c.navigate(R.id.frag_all_product);
                    all_product.setChecked(true);
                    return true;
                }
            });

            cartdatabaseViewmodel.getCart_tableLiveData().observe(Home_Page.this, new Observer<List<CART_TABLE>>() {
                @Override
                public void onChanged(@Nullable final List<CART_TABLE> cart_tables) {
                    // Update the cached copy of the words in the adapter.
                    if (cart_tables != null) {
                        cart_count.setText(utility.convertToBangle(String.valueOf(cart_tables.size())));
                    }
                }
            });
            //from cart login check
            String login = getIntent().getStringExtra("login");
            if (login != null && !TextUtils.isEmpty(login)) {
                Fragment navhost = getSupportFragmentManager().findFragmentById(R.id.nav_host_fragment);
                NavController c = NavHostFragment.findNavController(navhost);
                c.popBackStack();
                c.navigate(R.id.nav_login_register);
            }
            //from search product
            String product_details = getIntent().getStringExtra("product_details");
            if (product_details != null && !TextUtils.isEmpty(product_details)) {
                try {
                    Bundle bundle = new Bundle();
                    bundle.putString("product_id", product_details);
                    Fragment navhost = getSupportFragmentManager().findFragmentById(R.id.nav_host_fragment);
                    NavController c = NavHostFragment.findNavController(navhost);
                    c.popBackStack();
                    c.navigate(R.id.frag_product_details, bundle);
                } catch (Exception e) {
                    Log.d("Error Line Number", Log.getStackTraceString(e));
                }
            }

            //from search product request
            String searchProduct = getIntent().getStringExtra("search_product");
            if (searchProduct != null && !TextUtils.isEmpty(searchProduct)) {
                try {
                    Fragment navhost = ((AppCompatActivity) context).getSupportFragmentManager().findFragmentById(R.id.nav_host_fragment);
                    NavController c = NavHostFragment.findNavController(navhost);
                    c.navigate(R.id.nav_request);
                } catch (Exception e) {
                    Log.d("Error Line Number", Log.getStackTraceString(e));
                }
            }


            //FROM Notification
            if (getIntent().getExtras() != null) {
                String notification_work = getIntent().getExtras().getString("NOTIFICATION");
                String metadataBrowse = getIntent().getExtras().getString("metadataBrowse");
                String metadata = getIntent().getExtras().getString("metadata");
                utility.logger("cheack" + metadata + "ok" + metadataBrowse);
                if (notification_work != null && !TextUtils.isEmpty(notification_work) && metadataBrowse != null && !TextUtils.isEmpty(metadataBrowse) && metadata != null && !TextUtils.isEmpty(metadata)) {
                    try {
                        utility.logger("NOTIFICATION BROWSING");
                        notification_browsing(metadataBrowse, metadata);
                    } catch (Exception e) {
                        Log.d("Error Line Number", Log.getStackTraceString(e));
                    }
                }
            }

            /*//from search product
            String seller_details = getIntent().getStringExtra("seller_details");
            if (seller_details != null && !TextUtils.isEmpty(seller_details)) {
                try {
                    Bundle bundle = new Bundle();
                    bundle.putString("seller_id", seller_details);
                    Fragment navhost = getSupportFragmentManager().findFragmentById(R.id.nav_host_fragment);
                    NavController c = NavHostFragment.findNavController(navhost);
                    c.popBackStack();
                    c.navigate(R.id.frag_product_details, bundle);
                } catch (Exception e) {
                    Log.d("Error Line Number", Log.getStackTraceString(e));
                }
            }*/


            /*FirebaseInstanceId.getInstance().getInstanceId()
                    .addOnCompleteListener(new OnCompleteListener<InstanceIdResult>() {
                        @Override
                        public void onComplete(@NonNull Task<InstanceIdResult> task) {
                            if (!task.isSuccessful()) {
                                Log.w("check", "getInstanceId failed", task.getException());
                                return;
                            }

                            // Get new Instance ID token
                            String token = task.getResult().getToken();

                            // Log and toast
                            //String msg = getString(R.string.msg_token_fmt, token);
                            Log.d("firebase token", token);
                            Toast.makeText(Home_Page.this, token, Toast.LENGTH_SHORT).show();
                        }
                    });*/
        } catch (Exception e) {
            Log.d("Error Line Number", Log.getStackTraceString(e));
        }
    }

    @Override
    public boolean onSupportNavigateUp() {
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment);
        return NavigationUI.navigateUp(navController, mAppBarConfiguration)
                || super.onSupportNavigateUp();
    }

    @Override
    public void onBackPressed() {
        try {
            if (/*drawerLayout.isDrawerOpen(GravityCompat.START)*/false) {
                //drawerLayout.closeDrawers();
            } else {
                Fragment navhost = getSupportFragmentManager().findFragmentById(R.id.nav_host_fragment);
                if (navhost.getChildFragmentManager().getBackStackEntryCount() == 0) {
                    if (isTaskRoot()) {
                        //super.onBackPressed();
                        Display display = getWindowManager().getDefaultDisplay();
                        Point size = new Point();
                        display.getSize(size);
                        HashMap<String, Integer> screen = utility.getScreenRes();
                        int width = screen.get(KeyWord.SCREEN_WIDTH);
                        int height = screen.get(KeyWord.SCREEN_HEIGHT);
                        int mywidth = (width / 10) * 7;
                        final Dialog dialog = new Dialog(this);
                        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
                        dialog.getWindow().setBackgroundDrawableResource(android.R.color.transparent);
                        dialog.setContentView(R.layout.dialog_toast_yes_no);
                        TextView tv = dialog.findViewById(R.id.tv_message);
                        Button yes = dialog.findViewById(R.id.btn_yes);
                        Button no = dialog.findViewById(R.id.btn_no);
                        tv.setText(getResources().getString(R.string.exit_string));
                        LinearLayout ll = dialog.findViewById(R.id.dialog_layout_size);
                        LinearLayout.LayoutParams params = (LinearLayout.LayoutParams) ll.getLayoutParams();
                        params.height = LinearLayout.LayoutParams.WRAP_CONTENT;
                        params.width = mywidth;
                        ll.setLayoutParams(params);
                        yes.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                dialog.dismiss();
                                android.os.Process.killProcess(android.os.Process.myPid());
                                System.exit(1);
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
                    } else {
                        super.onBackPressed();
                    }
                } else {
                    getSupportFragmentManager().popBackStackImmediate();
                }
            }
        } catch (Exception ex) {
            Log.d("Error Line Number", Log.getStackTraceString(ex));
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);  // You MUST have this line to be here
        // so ImagePicker can work with fragment mode
    }

    public void notification_browsing(String metadataBrowse, String metadata) {
        try {
            if (metadataBrowse.equalsIgnoreCase(KeyWord.EXTERNAL)) {
                try {
                    String url = metadata;
                    CustomTabsIntent.Builder builder = new CustomTabsIntent.Builder();
                    builder.setToolbarColor(ContextCompat.getColor(context, R.color.colorAccent));
                    CustomTabsIntent customTabsIntent = builder.build();
                    customTabsIntent.launchUrl(context, Uri.parse(url));
                } catch (Exception e) {
                    Log.d("Error Line Number", Log.getStackTraceString(e));
                    try {
                        Uri uri = Uri.parse(metadata); // missing 'http://' will cause crashed
                        Intent intent = new Intent(Intent.ACTION_VIEW, uri);
                        context.startActivity(intent);
                    } catch (Exception e2) {
                        Log.d("Error Line Number", Log.getStackTraceString(e2));
                        utility.showDialog(context.getResources().getString(R.string.no_browser_string));
                    }
                }
            } else if (metadataBrowse.equalsIgnoreCase(KeyWord.INTERNAL)) {
                if (metadata.contains(KeyWord.INAPP_SELLER_LIST)) {
                            /*Bundle bundle = new Bundle();
                            bundle.putString("product_id", bodyResponse.getProductId());*/
                    Fragment navhost = ((AppCompatActivity) context).getSupportFragmentManager().findFragmentById(R.id.nav_host_fragment);
                    NavController c = NavHostFragment.findNavController(navhost);
                    //c.popBackStack();
                    c.navigate(R.id.nav_seller);
                } else if (metadata.contains(KeyWord.INAPP_SELLER_DETAILS)) {
                    String[] parts = metadata.split("-");
                    String id = parts[1];
                    utility.logger("seller_id" + id);
                    Bundle bundle = new Bundle();
                    bundle.putString("seller_id", id);
                    Fragment navhost = ((AppCompatActivity) context).getSupportFragmentManager().findFragmentById(R.id.nav_host_fragment);
                    NavController c = NavHostFragment.findNavController(navhost);
                    //c.popBackStack();
                    c.navigate(R.id.nav_seller_details, bundle);
                } else if (metadata.contains(KeyWord.INAPP_PRODUCT_DETAILS)) {
                    String[] parts = metadata.split("-");
                    String id = parts[1];
                    utility.logger("product_id" + id);
                    Bundle bundle = new Bundle();
                    bundle.putString("product_id", id);
                    Fragment navhost = ((AppCompatActivity) context).getSupportFragmentManager().findFragmentById(R.id.nav_host_fragment);
                    NavController c = NavHostFragment.findNavController(navhost);
                    //c.popBackStack();
                    c.navigate(R.id.frag_product_details, bundle);
                } else if (metadata.contains(KeyWord.INAPP_CATEGORY)) {
                    String[] parts = metadata.split("-");
                    String id = parts[1];
                    utility.logger("category_id" + id);
                    Bundle bundle = new Bundle();
                    bundle.putString("category_id", id);
                    Fragment navhost = ((AppCompatActivity) context).getSupportFragmentManager().findFragmentById(R.id.nav_host_fragment);
                    NavController c = NavHostFragment.findNavController(navhost);
                    //c.popBackStack();
                    c.navigate(R.id.frag_category_list, bundle);
                } else if (metadata.contains(KeyWord.INAPP_NOTIFICATION)) {
                    Fragment navhost = ((AppCompatActivity) context).getSupportFragmentManager().findFragmentById(R.id.nav_host_fragment);
                    NavController c = NavHostFragment.findNavController(navhost);
                    //c.popBackStack();
                    c.navigate(R.id.frag_notification);
                } else if (metadata.contains(KeyWord.INAPP_CART)) {
                    context.startActivity(new Intent(context, Cart_Activity.class));
                } else if (metadata.contains(KeyWord.INAPP_DISCOUNT)) {
                    context.startActivity(new Intent(context, Cart_Activity.class));
                } else if (metadata.contains(KeyWord.INAPP_PROFILE)) {
                    Fragment navhost = ((AppCompatActivity) context).getSupportFragmentManager().findFragmentById(R.id.nav_host_fragment);
                    NavController c = NavHostFragment.findNavController(navhost);
                    //c.popBackStack();
                    c.navigate(R.id.frag_profile);
                } else if (metadata.contains(KeyWord.INAPP_BUY_HISTORY)) {
                    Fragment navhost = ((AppCompatActivity) context).getSupportFragmentManager().findFragmentById(R.id.nav_host_fragment);
                    NavController c = NavHostFragment.findNavController(navhost);
                    //c.popBackStack();
                    c.navigate(R.id.nav_buy);
                } else if (metadata.contains(KeyWord.INAPP_SELL_HISTORY)) {
                    Fragment navhost = ((AppCompatActivity) context).getSupportFragmentManager().findFragmentById(R.id.nav_host_fragment);
                    NavController c = NavHostFragment.findNavController(navhost);
                    //c.popBackStack();
                    c.navigate(R.id.nav_sell);
                } else if (metadata.contains(KeyWord.INAPP_OWN_PRODUCT)) {
                    Fragment navhost = ((AppCompatActivity) context).getSupportFragmentManager().findFragmentById(R.id.nav_host_fragment);
                    NavController c = NavHostFragment.findNavController(navhost);
                    //c.popBackStack();
                    c.navigate(R.id.nav_Own_product);
                } else if (metadata.contains(KeyWord.INAPP_RATTING)) {
                    startActivity(new Intent(context, Landing_Page.class));
                    finish();
                }

            }
        } catch (Exception e) {
            Log.d("Error Line Number", Log.getStackTraceString(e));
        }
    }
}
