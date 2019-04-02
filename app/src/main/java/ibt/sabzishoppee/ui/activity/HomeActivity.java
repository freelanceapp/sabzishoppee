package ibt.sabzishoppee.ui.activity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;

import ibt.sabzishoppee.R;
import ibt.sabzishoppee.constant.Constant;
import ibt.sabzishoppee.model.User;
import ibt.sabzishoppee.retrofit_provider.RetrofitService;
import ibt.sabzishoppee.ui.fragment.AboutFragment;
import ibt.sabzishoppee.ui.fragment.ContactUsFragment;
import ibt.sabzishoppee.ui.fragment.HelpFragment;
import ibt.sabzishoppee.ui.fragment.HomeFragment;
import ibt.sabzishoppee.utils.AppPreference;
import ibt.sabzishoppee.utils.BaseActivity;
import ibt.sabzishoppee.utils.ConnectionDirector;
import ibt.sabzishoppee.utils.Utility;

public class HomeActivity extends BaseActivity implements NavigationView.OnNavigationItemSelectedListener, View.OnClickListener {

    public static Toolbar toolbar1,toolbar;
    private RelativeLayout rv_home,rv_cart, rv_history, rv_profile;
    public static TextView cart_number;
    public static int cart_count = 0;
    public static ImageView iv_ShowUserImage;
    public static TextView tv_ShowUserName;
    public static TextView cart_price;
    private ImageView btnSearch;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar1 = (Toolbar) findViewById(R.id.toolbar1);
        setSupportActionBar(toolbar);

        cart_number = (TextView)findViewById(R.id.cart_number);
        cart_price = (TextView)findViewById(R.id.cart_price);
        btnSearch = (ImageView) findViewById(R.id.btnSearch);
        cart_count = AppPreference.getIntegerPreference(mContext, Constant.CART_ITEM_COUNT);
        cart_number.setText("" + cart_count);
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();
        mContext = this;
        cd = new ConnectionDirector(mContext);
        retrofitApiClient = RetrofitService.getRetrofit();

        HomeFragment fragment = new HomeFragment();
        Utility.setFragment(fragment , mContext , Constant.Home);

        Log.e("User id" , AppPreference.getStringPreference(mContext , Constant.User_Id));
        Log.e("Login" , String.valueOf(AppPreference.getBooleanPreference(mContext , Constant.Is_Login)));

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        View view = navigationView.getHeaderView(0);

        rv_profile = findViewById(R.id.rl_profile);
        rv_history = findViewById(R.id.rl_history);
        rv_home = findViewById(R.id.rv_home);
        rv_cart = findViewById(R.id.rv_cart);
        iv_ShowUserImage = view.findViewById(R.id.iv_ShowUserImage);
        tv_ShowUserName = view.findViewById(R.id.tv_ShowUserName);

        rv_cart.setOnClickListener(this);
        rv_home.setOnClickListener(this);
        rv_history.setOnClickListener(this);
        rv_profile.setOnClickListener(this);


        if (User.getUser().getUser().getUserName() == null) {
            tv_ShowUserName.setText("User Name");
        } else {
            tv_ShowUserName.setText(User.getUser().getUser().getUserName());
        }

        if (User.getUser().getUser().getUserProfilePicture() == null) {
            iv_ShowUserImage.setImageResource(R.drawable.profile_img);
        } else {
            Glide.with(mContext).load(User.getUser().getUser().getUserProfilePicture()).into(iv_ShowUserImage);
        }

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
            Utility.setFragment(fragment , mContext , Constant.Home);
        } else if (id == R.id.nav_about) {
            // Handle the camera action
            AboutFragment fragment = new AboutFragment();
            Utility.setFragment(fragment , mContext , Constant.AboutFragment);
        }
        else if (id == R.id.nav_contact) {
            ContactUsFragment fragment = new ContactUsFragment();
            Utility.setFragment(fragment , mContext , Constant.ContactUsFragment);
        } else if (id == R.id.nav_help) {
            HelpFragment fragment = new HelpFragment();
            Utility.setFragment(fragment , mContext , Constant.ContactUsFragment);
        } else if (id == R.id.nav_setting) {

        } else if (id == R.id.nav_logout) {

            AppPreference.setBooleanPreference(mContext, Constant.Is_Login, false);
            AppPreference.setStringPreference(mContext, Constant.User_Id, "0");

            Intent intent = new Intent(HomeActivity.this , LoginActivity.class);
            startActivity(intent);
            finish();
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }


    @Override
    public void onClick(View view) {
        switch (view.getId())
        {
            case R.id.rv_cart :
                Intent intent = new Intent(HomeActivity.this , AddtoCartActivity.class);
                startActivity(intent);
                break;

            case R.id.rv_home :

                break;

            case R.id.rl_history :
                Intent intent2 = new Intent(HomeActivity.this , OrderHistoryActivity.class);
                startActivity(intent2);
                break;

            case R.id.rl_profile :

                Intent intent1 = new Intent(HomeActivity.this , ProfileActivity.class);
                startActivity(intent1);

                break;

        }
    }
}
