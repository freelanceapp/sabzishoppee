package ibt.sabziwala.ui.activity;

import android.app.Dialog;
import android.content.Intent;
import android.os.CountDownTimer;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import ibt.sabziwala.R;
import ibt.sabziwala.constant.Constant;
import ibt.sabziwala.model.User;
import ibt.sabziwala.model.login_responce.LoginModel;
import ibt.sabziwala.retrofit_provider.RetrofitService;
import ibt.sabziwala.retrofit_provider.WebResponse;
import ibt.sabziwala.utils.Alerts;
import ibt.sabziwala.utils.AppPreference;
import ibt.sabziwala.utils.BaseActivity;
import ibt.sabziwala.utils.pinview.Pinview;
import retrofit2.Response;

import static ibt.sabziwala.ui.activity.LoginActivity.loginfragmentManager;

public class VerifyOtpActivity extends BaseActivity implements View.OnClickListener {

    private Button btn_fplogin;
    private TextView otpTime, tvChangeMobile;
    private LinearLayout resendLayout;
    private Pinview pinview1;
    private String strMobile , strOtp;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_verify_otp);

        strMobile = getIntent().getExtras().getString("Mobile_Number");
        initViews();
    }

    private void initViews() {
        ((Button)findViewById(R.id.btn_fplogin)).setOnClickListener(this);
        btn_fplogin = findViewById(R.id.btn_fplogin);
        pinview1 = findViewById(R.id.pinview1);
        tvChangeMobile = findViewById(R.id.tvChangeMobile);
        otpTime = (TextView)findViewById(R.id.otpTime);
        resendLayout = (LinearLayout) findViewById(R.id.resendLayout);
        btn_fplogin.setOnClickListener(this);
        tvChangeMobile.setOnClickListener(this);

        otpTime();
    }

    private void startFragment(String tag, Fragment fragment){
        loginfragmentManager
                .beginTransaction()
                .setCustomAnimations(R.anim.left_enter, R.anim.right_out)
                .replace(R.id.login_frame, fragment, tag).commit();
    }

    private void otpTime() {
        new CountDownTimer(60000, 1000) {
            public void onTick(long millisUntilFinished) {
                otpTime.setVisibility(View.VISIBLE);
                otpTime.setText("seconds remaining: " + millisUntilFinished / 1000);
            }

            public void onFinish() {
                otpTime.setVisibility(View.GONE);
                resendLayout.setVisibility(View.VISIBLE);
            }
        }.start();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.btn_fplogin:
                otpApi();
                //startFragment(Constant.SignUpFragment,new SignUpFragment());
                break;
            case R.id.tvChangeMobile:
                startActivity(new Intent(mContext, SignInActivity.class));
                finish();
                break;
        }
    }

    private void otpApi() {
        if (cd.isNetWorkAvailable()) {
            //strMobile = ((EditText) findViewById(R.id.et_login_email)).getText().toString();
            strOtp = pinview1.getValue();
            if (strOtp.isEmpty()) {
                ((EditText) findViewById(R.id.et_login_password)).setError("Please enter otp");
            } else {
                RetrofitService.getLoginData(new Dialog(mContext), retrofitApiClient.otpApi(strMobile, strOtp), new WebResponse() {
                    @Override
                    public void onResponseSuccess(Response<?> result) {
                        LoginModel loginModel = (LoginModel) result.body();

                        if (!loginModel.getError())
                        {
                            
                                Alerts.show(mContext, loginModel.getMessage());

                                AppPreference.setBooleanPreference(mContext, Constant.Is_Login, true);
                                AppPreference.setStringPreference(mContext, Constant.User_Id, loginModel.getUser().getId());

                                Gson gson = new GsonBuilder().setLenient().create();
                                String data = gson.toJson(loginModel);
                                AppPreference.setStringPreference(mContext, Constant.User_Data, data);
                                User.setUser(loginModel);

                                Intent intent = new Intent(mContext, HomeActivity.class);
                                mContext.startActivity(intent);
                                finish();


                        }else {
                            Alerts.show(mContext, loginModel.getMessage());
                        }
                    }

                    @Override
                    public void onResponseFailed(String error) {
                        Alerts.show(mContext, error);
                    }
                });
            }
        }else {
            cd.show(mContext);
        }
    }
    
}
