package ibt.pahadisabzi.ui.activity;


import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

import ibt.pahadisabzi.R;
import ibt.pahadisabzi.constant.Constant;
import ibt.pahadisabzi.database.DatabaseHandler;
import ibt.pahadisabzi.ui.fragment.ConfirmationFragment;
import ibt.pahadisabzi.ui.fragment.ShoppingFragment;
import ibt.pahadisabzi.utils.Utility;


public class CheckOutActivity extends AppCompatActivity {

    Context ctx;
    ImageView back_iv;

    private Toolbar toolbar;
    private TabLayout tabLayout;
    private FrameLayout viewPager;

    public DatabaseHandler databaseCart;
    private String fragmentPass;
    // private GoogleApiClient googleApiClient;
    public static TextView tv_confirmation, tv_address;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_check_out);
        initXml();
    }

    private void initXml() {
        ctx = this;
        back_iv = findViewById(R.id.iv_chekout_back);
        toolbar = (Toolbar) findViewById(R.id.toolbar_checkout);
        tv_confirmation = findViewById(R.id.tv_confirmation);
        tv_address = findViewById(R.id.tv_confirm_address);

        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        fragmentPass = getIntent().getStringExtra("FragmentPass");

        back_iv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                /*Intent intent = new Intent(CheckOutActivity.this, AddToCartActivity.class);
                startActivity(intent);
                finish();*/
                onBackPressed();
            }
        });

        if (fragmentPass.equals("ConfirmationFragment"))
        {
            setFragment2();
        }else {
            setFragment();
        }
    }

    private void setFragment() {
        ShoppingFragment fragment = new ShoppingFragment(ctx);
        Utility.setFragment2(fragment, ctx, Constant.ShoppingFragment);
    }

    private void setFragment2() {
        ConfirmationFragment fragment = new ConfirmationFragment(ctx);
        Utility.setFragment1(fragment, ctx, Constant.ShoppingFragment);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        if (requestCode == 567) {
            setFragment();
        }
    }

   /* @Override
    public void onBackPressed() {
        super.onBackPressed();
        Intent intent = new Intent(CheckOutActivity.this, HomeNavigationActivity.class);
        startActivity(intent);
        finish();
    }*/
}
