package ibt.sabziwala.ui.activity;

import android.Manifest;
import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Handler;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import com.crashlytics.android.Crashlytics;
import com.google.gson.Gson;

import ibt.sabziwala.R;
import ibt.sabziwala.constant.Constant;
import ibt.sabziwala.model.User;
import ibt.sabziwala.model.login_responce.LoginModel;
import ibt.sabziwala.retrofit_provider.RetrofitService;
import ibt.sabziwala.utils.AppPreference;
import ibt.sabziwala.utils.BaseActivity;
import ibt.sabziwala.utils.ConnectionDirector;
import io.fabric.sdk.android.Fabric;

public class SplashActivity extends BaseActivity {
    public static String mypreference = "sabzishoppe";
    private static final int MY_PERMISSIONS_REQUEST_CODE = 123;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Fabric.with(this, new Crashlytics());
        setContentView(R.layout.activity_splash);

        mContext = this;
        cd = new ConnectionDirector(mContext);
        retrofitApiClient = RetrofitService.getRetrofit();

        Log.e("User id" , AppPreference.getStringPreference(mContext , Constant.User_Id));
        Log.e("Login" , String.valueOf(AppPreference.getBooleanPreference(mContext , Constant.Is_Login)));
       /* String refreshedToken = FirebaseInstanceId.getInstance().getToken();
        Log.e("Firebase ", "Refreshed token: " + refreshedToken);*/

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            checkPermission();
        }else {
            testHandler();
        }

    }

    protected void checkPermission() {
        if (ContextCompat.checkSelfPermission(mContext, Manifest.permission.CAMERA)
                + ContextCompat.checkSelfPermission(mContext, Manifest.permission.READ_SMS)
                + ContextCompat.checkSelfPermission(mContext, Manifest.permission.WRITE_EXTERNAL_STORAGE)
                + ContextCompat.checkSelfPermission(mContext, Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED) {

            // Do something, when permissions not granted
            if (ActivityCompat.shouldShowRequestPermissionRationale((Activity) mContext, Manifest.permission.CAMERA) ||
                    ActivityCompat.shouldShowRequestPermissionRationale((Activity) mContext, Manifest.permission.READ_SMS)
                    || ActivityCompat.shouldShowRequestPermissionRationale((Activity) mContext, Manifest.permission.WRITE_EXTERNAL_STORAGE)
                    || ActivityCompat.shouldShowRequestPermissionRationale((Activity) mContext, Manifest.permission.ACCESS_FINE_LOCATION)) {
                // If we should give explanation of requested permissions
                // Show an alert dialog here with request explanation
                AlertDialog.Builder builder = new AlertDialog.Builder(mContext);
                builder.setMessage("Camera, Read Contacts, Write External and Location" +
                        " Storage permissions are required to do the task.");
                builder.setTitle("Please grant those permissions");
                builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        ActivityCompat.requestPermissions(
                                (Activity) mContext,
                                new String[]{Manifest.permission.CAMERA, Manifest.permission.READ_SMS,
                                        Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.ACCESS_FINE_LOCATION
                                },
                                MY_PERMISSIONS_REQUEST_CODE
                        );
                    }
                });
                builder.setNeutralButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        finish();
                    }
                });
                AlertDialog dialog = builder.create();
                dialog.show();
            } else {
                // Directly request for required permissions, without explanation
                ActivityCompat.requestPermissions(
                        (Activity) mContext,
                        new String[]{
                                Manifest.permission.CAMERA, Manifest.permission.READ_SMS,
                                Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.ACCESS_FINE_LOCATION
                        },
                        MY_PERMISSIONS_REQUEST_CODE
                );
            }
        } else {
            if (cd.isNetWorkAvailable()) {
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {

                        if (AppPreference.getBooleanPreference(mContext , Constant.Is_Login)) {
                            Gson gson = new Gson();
                            String userData = AppPreference.getStringPreference(mContext, Constant.User_Data);
                            LoginModel loginModal = gson.fromJson(userData, LoginModel.class);
                            User.setUser(loginModal);

                            Intent intent = new Intent(SplashActivity.this, HomeActivity.class);
                            startActivity(intent);
                            finish();

                        }else {
                            Intent intent = new Intent(SplashActivity.this, LoginActivity.class);
                            //Intent intent = new Intent(SplashActivity.this, SignInActivity.class);
                            startActivity(intent);
                            finish();

                        }
                    }
                }, 2000);
            } else {
                cd.show(mContext);
            }
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        switch (requestCode) {
            case MY_PERMISSIONS_REQUEST_CODE: {
                // When request is cancelled, the results array are empty
                if ((grantResults.length > 0) &&
                        (grantResults[0]
                                + grantResults[1]
                                + grantResults[2]
                                + grantResults[3]
                                == PackageManager.PERMISSION_GRANTED)) {
                    // Permissions are granted
                    //  Toast.makeText(mContext, "Permissions granted.", Toast.LENGTH_SHORT).show();
                    testHandler();
                    // close this activity
                } else {
                    // Permissions are denied
                    Toast.makeText(mContext, "Permissions denied.", Toast.LENGTH_SHORT).show();
                    finish();
                }
                return;
            }
        }
    }

    private void testHandler(){
        if (cd.isNetWorkAvailable()) {
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {

                    if (AppPreference.getBooleanPreference(mContext , Constant.Is_Login)) {
                        Gson gson = new Gson();
                        String userData = AppPreference.getStringPreference(mContext, Constant.User_Data);
                        LoginModel loginModal = gson.fromJson(userData, LoginModel.class);
                        User.setUser(loginModal);

                        Intent intent = new Intent(SplashActivity.this, HomeActivity.class);
                        startActivity(intent);
                        finish();

                    }else {
                        //Intent intent = new Intent(SplashActivity.this, SignInActivity.class);
                        Intent intent = new Intent(SplashActivity.this, LoginActivity.class);
                        startActivity(intent);
                        finish();

                    }
                }
            }, 2000);
        } else {
            cd.show(mContext);
        }
    }
}
