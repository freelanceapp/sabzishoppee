package ibt.pahadisabzi.ui.activity;

import android.Manifest;
import android.app.Activity;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Handler;
import android.provider.Settings;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import com.crashlytics.android.Crashlytics;
import com.google.firebase.iid.FirebaseInstanceId;
import com.google.gson.Gson;

import ibt.pahadisabzi.R;
import ibt.pahadisabzi.constant.Constant;
import ibt.pahadisabzi.model.User;
import ibt.pahadisabzi.model.app_version_responce.AppversionModel;
import ibt.pahadisabzi.model.login_responce.LoginModel;
import ibt.pahadisabzi.model.signup_responce.SignUpModel;
import ibt.pahadisabzi.notification.FirebaseIDService;
import ibt.pahadisabzi.retrofit_provider.RetrofitService;
import ibt.pahadisabzi.retrofit_provider.WebResponse;
import ibt.pahadisabzi.utils.Alerts;
import ibt.pahadisabzi.utils.AppPreference;
import ibt.pahadisabzi.utils.BaseActivity;
import ibt.pahadisabzi.utils.ConnectionDirector;
import io.fabric.sdk.android.Fabric;
import retrofit2.Response;

public class SplashActivity extends BaseActivity {
    public static String mypreference = "sabzishoppe";
    private static final int MY_PERMISSIONS_REQUEST_CODE = 123;
    FirebaseIDService firebaseIDService;
    private String AppVersion = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Fabric.with(this, new Crashlytics());
        setContentView(R.layout.activity_splash);

        mContext = this;
        cd = new ConnectionDirector(mContext);
        retrofitApiClient = RetrofitService.getRetrofit();

        firebaseIDService = new FirebaseIDService();


        Log.e("User id" , AppPreference.getStringPreference(mContext , Constant.User_Id));
        Log.e("Login" , String.valueOf(AppPreference.getBooleanPreference(mContext , Constant.Is_Login)));
        String refreshedToken = FirebaseInstanceId.getInstance().getToken();
        Log.e("Firebase ", "Refreshed token: " + refreshedToken);



        getAppVersion();



    }

    protected void checkPermission() {
        if (ContextCompat.checkSelfPermission(mContext, Manifest.permission.CAMERA)
                + ContextCompat.checkSelfPermission(mContext, Manifest.permission.WRITE_EXTERNAL_STORAGE)
                + ContextCompat.checkSelfPermission(mContext, Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED) {

            // Do something, when permissions not granted
            if (ActivityCompat.shouldShowRequestPermissionRationale((Activity) mContext, Manifest.permission.CAMERA)
                    || ActivityCompat.shouldShowRequestPermissionRationale((Activity) mContext, Manifest.permission.WRITE_EXTERNAL_STORAGE)
                    || ActivityCompat.shouldShowRequestPermissionRationale((Activity) mContext, Manifest.permission.ACCESS_FINE_LOCATION)) {
                // If we should give explanation of requested permissions
                // Show an alert dialog here with request explanation
                AlertDialog.Builder builder = new AlertDialog.Builder(mContext);
                builder.setMessage("Camera, Write External and Location" +
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
                                Manifest.permission.CAMERA,
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

                        if (!(AppPreference.getStringPreference(mContext, Constant.User_Id)).isEmpty()) {

                            tokenApi(FirebaseInstanceId.getInstance().getToken());
                        }
                        if (AppPreference.getBooleanPreference(mContext , Constant.Is_Login)) {
                            Gson gson = new Gson();
                            String userData = AppPreference.getStringPreference(mContext, Constant.User_Data);
                            LoginModel loginModal = gson.fromJson(userData, LoginModel.class);
                            User.setUser(loginModal);



                            Intent intent = new Intent(SplashActivity.this, HomeActivity.class);
                            startActivity(intent);
                            finish();

                        }else {
                            Intent intent = new Intent(SplashActivity.this, SignInActivity.class);
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
                    if (!(AppPreference.getStringPreference(mContext, Constant.User_Id)).isEmpty()) {
                        tokenApi(FirebaseInstanceId.getInstance().getToken());
                    }
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
                        Intent intent = new Intent(SplashActivity.this, SignInActivity.class);
                        startActivity(intent);
                        finish();

                    }
                }
            }, 2000);
        } else {
            cd.show(mContext);
        }
    }


    private void tokenApi(String strToken) {
        String android_id = Settings.Secure.getString(getApplicationContext().getContentResolver(), Settings.Secure.ANDROID_ID);
        String strId = AppPreference.getStringPreference(mContext, Constant.User_Id);
        if (cd.isNetWorkAvailable()) {
            RetrofitService.getSignData(new Dialog(mContext), retrofitApiClient.updateToken(strId, strToken, android_id, "user"), new WebResponse() {
                @Override
                public void onResponseSuccess(Response<?> result) {
                    SignUpModel loginModal = (SignUpModel) result.body();
                    assert loginModal != null;
                    if (!loginModal.getError()) {
                        //Alerts.show(mContext, loginModal.getMessage());
                    } else {
                        Alerts.show(mContext, loginModal.getMessage());
                    }
                }

                @Override
                public void onResponseFailed(String error) {
                    Alerts.show(mContext, error);
                }
            });
        } else {
            cd.show(mContext);
        }
    }



    private void getAppVersion()
    {
        RetrofitService.getAppVersion(new Dialog(mContext), retrofitApiClient.getVersion(), new WebResponse() {
            @Override
            public void onResponseSuccess(Response<?> result) {
                AppversionModel responseBody = (AppversionModel) result.body();
                AppVersion = responseBody.getVersion();
                if (AppVersion.equals("1")) {
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                        checkPermission();
                    } else {
                        testHandler();
                    }
                }else {
                    final String appPackageName = getPackageName(); // getPackageName() from Context or Activity object
                    try {
                        startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("market://details?id=" + appPackageName)));
                    } catch (android.content.ActivityNotFoundException anfe) {
                        startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("https://play.google.com/store/apps/details?id=" + appPackageName)));
                    }
                }
            }

            @Override
            public void onResponseFailed(String error) {
                Alerts.show(mContext, error);
            }
        });
    }
}
