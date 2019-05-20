package ibt.pahadisabzi.ui.activity;

import android.Manifest;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AlertDialog;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;

import java.text.DecimalFormat;
import java.util.ArrayList;

import ibt.pahadisabzi.R;
import ibt.pahadisabzi.constant.Constant;
import ibt.pahadisabzi.database.DatabaseHandler;
import ibt.pahadisabzi.model.ProductDetail;
import ibt.pahadisabzi.model.contact_responce.ContcatModel;
import ibt.pahadisabzi.model.login_responce.LoginModel;
import ibt.pahadisabzi.retrofit_provider.RetrofitService;
import ibt.pahadisabzi.retrofit_provider.WebResponse;
import ibt.pahadisabzi.ui.fragment.AboutFragment;
import ibt.pahadisabzi.ui.fragment.ChangePasswordFragment;
import ibt.pahadisabzi.ui.fragment.ContactUsFragment;
import ibt.pahadisabzi.ui.fragment.HelpFragment;
import ibt.pahadisabzi.ui.fragment.HomeFragment;
import ibt.pahadisabzi.utils.Alerts;
import ibt.pahadisabzi.utils.AppPreference;
import ibt.pahadisabzi.utils.BaseActivity;
import ibt.pahadisabzi.utils.ConnectionDirector;
import ibt.pahadisabzi.utils.Utility;
import retrofit2.Response;

import static ibt.pahadisabzi.constant.Constant.CUSTOMER_SUPPORT;

public class HomeActivity extends BaseActivity implements NavigationView.OnNavigationItemSelectedListener, View.OnClickListener {

    public static Toolbar toolbar1, toolbar;
    private LinearLayout rv_home, rv_cart, rv_history, rv_profile;
    public static TextView cart_number;
    public static int cart_count = 0;
    public static ImageView iv_ShowUserImage;
    public static TextView tv_ShowUserName;
    public static TextView cart_price;
    private ImageView btnSearch;
    public DatabaseHandler databaseCart;
    private String DATABASE_CART = "cart.db";
    private LinearLayout ll_cart_price;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        cart_number = (TextView) findViewById(R.id.cart_number);
        cart_price = (TextView) findViewById(R.id.cart_price);
        btnSearch = (ImageView) findViewById(R.id.btnSearch);
        ll_cart_price = (LinearLayout) findViewById(R.id.ll_cart_price);
        //cart_count = AppPreference.getIntegerPreference(mContext, Constant.CART_ITEM_COUNT);
        cart_number.setText("" + cart_count);
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();
        mContext = this;
        cd = new ConnectionDirector(mContext);
        retrofitApiClient = RetrofitService.getRetrofit();
        databaseCart = new DatabaseHandler(mContext, DATABASE_CART);

        HomeFragment fragment = new HomeFragment();
        Utility.setFragment(fragment, mContext, Constant.Home);

        Log.e("User id", AppPreference.getStringPreference(mContext, Constant.User_Id));
        Log.e("Login", String.valueOf(AppPreference.getBooleanPreference(mContext, Constant.Is_Login)));

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        View view = navigationView.getHeaderView(0);

        rv_profile = findViewById(R.id.rl_profile);
        rv_history = findViewById(R.id.rl_history);
        rv_home = findViewById(R.id.rv_home);
        rv_cart = findViewById(R.id.rv_cart);
        iv_ShowUserImage = view.findViewById(R.id.iv_ShowUserImage);
        tv_ShowUserName = view.findViewById(R.id.tv_ShowUserName);
        profileApi();
        supportApi();
        rv_cart.setOnClickListener(this);
        rv_home.setOnClickListener(this);
        rv_history.setOnClickListener(this);
        rv_profile.setOnClickListener(this);
        ll_cart_price.setOnClickListener(this);

        btnSearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(HomeActivity.this, SearchActivity.class);
                startActivity(intent);
            }
        });

    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }

    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();
        if (id == R.id.nav_home) {
            // Handle the camera action
            HomeFragment fragment = new HomeFragment();
            Utility.setFragment(fragment, mContext, Constant.Home);
        } else if (id == R.id.nav_about) {
            // Handle the camera action
            AboutFragment fragment = new AboutFragment();
            Utility.setFragment(fragment, mContext, Constant.AboutFragment);
        } else if (id == R.id.nav_contact) {
            ContactUsFragment fragment = new ContactUsFragment();
            Utility.setFragment(fragment, mContext, Constant.ContactUsFragment);
        } else if (id == R.id.nav_help) {
            HelpFragment fragment = new HelpFragment();
            Utility.setFragment(fragment, mContext, Constant.ContactUsFragment);
        } else if (id == R.id.nav_share) {
            startActivity(new Intent(mContext, InviteFriendActivity.class));
        } else if (id == R.id.nav_customersupport) {
            phoneCall();
        } else if (id == R.id.nav_logout) {
            doLogout();
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }


    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.rv_cart:
                ArrayList<ProductDetail> cartProductList = new ArrayList<>();
                if (databaseCart.getContactsCount()) {
                    cartProductList = databaseCart.getAllUrlList();
                }
                if (cartProductList.size() > 0) {
                    Intent intent = new Intent(HomeActivity.this, AddToCartActivity.class);
                    startActivity(intent);
                }else{
                    Toast.makeText(mContext, "Cart is empty.", Toast.LENGTH_SHORT).show();
                }
                break;
            case  R.id.ll_cart_price :
                ArrayList<ProductDetail> cartProductList1 = new ArrayList<>();
                if (databaseCart.getContactsCount()) {
                    cartProductList1 = databaseCart.getAllUrlList();
                }
                if (cartProductList1.size() > 0) {
                    Intent intent = new Intent(HomeActivity.this, AddToCartActivity.class);
                    startActivity(intent);
                }else{
                    Toast.makeText(mContext, "Cart is empty.", Toast.LENGTH_SHORT).show();
                }

                break;
            case R.id.rv_home:
                Intent intent3 = new Intent(HomeActivity.this, HomeActivity.class);
                startActivity(intent3);
                finish();
                break;

            case R.id.rl_history:
                Intent intent2 = new Intent(HomeActivity.this, OrderHistoryActivity.class);
                startActivity(intent2);
                break;

            case R.id.rl_profile:
                Intent intent1 = new Intent(HomeActivity.this, ProfileActivity.class);
                intent1.putExtra("OneTime", "0");
                startActivity(intent1);
                break;

        }
    }

    private void doLogout() {
        new AlertDialog.Builder(mContext)
                .setTitle("Logout")
                .setMessage("Are you sure want to doLogout ?")
                .setPositiveButton("YES", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        AppPreference.setBooleanPreference(mContext, Constant.Is_Login, false);
                        AppPreference.setStringPreference(mContext, Constant.User_Id, "0");
                        if (databaseCart.getContactsCount()) {
                            databaseCart.deleteallCart();
                        }
                        Intent intent = new Intent(HomeActivity.this, SignInActivity.class);
                        startActivity(intent);
                        finish();
                    }
                })
                .setNegativeButton("NO", null)
                .create()
                .show();
    }

    private void profileApi() {
        String userId = AppPreference.getStringPreference(mContext, Constant.User_Id);
        Glide.with(mContext)
                .load(AppPreference.getStringPreference(mContext, Constant.LOGIN_USER_PROFILE_IMAGE))
                .placeholder(R.drawable.ic_user)
                .into(iv_ShowUserImage);
        tv_ShowUserName.setText(AppPreference.getStringPreference(mContext, Constant.LOGIN_USER_NAME));
    }

    private void onCallBtnClick(){
        if (Build.VERSION.SDK_INT < 23) {
            phoneCall();
        }else {

            if (ActivityCompat.checkSelfPermission(mContext, Manifest.permission.CALL_PHONE) == PackageManager.PERMISSION_GRANTED) {

                phoneCall();
            }else {
                final String[] PERMISSIONS_STORAGE = {Manifest.permission.CALL_PHONE};
                //Asking request Permissions
                ActivityCompat.requestPermissions(HomeActivity.this, PERMISSIONS_STORAGE, 9);
            }
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        boolean permissionGranted = false;
        switch(requestCode){
            case 9:
                permissionGranted = grantResults[0]== PackageManager.PERMISSION_GRANTED;
                break;
        }
        if(permissionGranted){
            phoneCall();
        }else {
            Toast.makeText(mContext, "You don't assign permission.", Toast.LENGTH_SHORT).show();
        }
    }

    private void phoneCall(){
        String mob_number = AppPreference.getStringPreference(mContext, Constant.CUSTOMER_SUPPORT);

      /*  AlertDialog.Builder builder1 = new AlertDialog.Builder(mContext);
        builder1.setTitle("Customer Support Number");
        builder1.setMessage(mob_number);
        builder1.setCancelable(true);

        builder1.setNegativeButton(
                "Cancel",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        dialog.cancel();
                    }
                });

        AlertDialog alert11 = builder1.create();
        alert11.show();*/

        Intent intent = new Intent(Intent.ACTION_DIAL);
        intent.setData(Uri.parse("tel:"+mob_number));
        startActivity(intent);

    }

    private void supportApi() {
        if (cd.isNetWorkAvailable()) {
            RetrofitService.getContactData(new Dialog(mContext), retrofitApiClient.contact(), new WebResponse() {
                @Override
                public void onResponseSuccess(Response<?> result) {
                    ContcatModel contcatModel = (ContcatModel) result.body();
                    if (!contcatModel.getResult()) {
                        //tvNumber.setText(contcatModel.getContent().get(0).getContent());
                        AppPreference.setStringPreference(mContext, Constant.CUSTOMER_SUPPORT, contcatModel.getContent().get(0).getContent());
                    }else {

                    }
                }

                @Override
                public void onResponseFailed(String error) {
                    Alerts.show(mContext, error);
                }
            });
        }
    }

    @Override
    protected void onResume() {
        super.onResume();

    }
}
