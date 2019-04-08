package ibt.sabzishoppee.ui.fragment;

import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import ibt.sabzishoppee.R;
import ibt.sabzishoppee.constant.Constant;
import ibt.sabzishoppee.model.User;
import ibt.sabzishoppee.model.login_responce.LoginModel;
import ibt.sabzishoppee.retrofit_provider.RetrofitService;
import ibt.sabzishoppee.retrofit_provider.WebResponse;
import ibt.sabzishoppee.ui.activity.HomeActivity;
import ibt.sabzishoppee.utils.Alerts;
import ibt.sabzishoppee.utils.AppPreference;
import ibt.sabzishoppee.utils.BaseFragment;
import ibt.sabzishoppee.utils.ConnectionDirector;
import ibt.sabzishoppee.utils.pinview.Pinview;
import retrofit2.Response;

import static ibt.sabzishoppee.ui.activity.LoginActivity.loginfragmentManager;


public class ForgotPasswordOtpFragment extends BaseFragment implements View.OnClickListener{
    private View rootview;
    private Button btn_fplogin;
    private TextView otpTime;
    private LinearLayout resendLayout;
    private Pinview pinview1;
    private String strMobile , strOtp;
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        rootview = inflater.inflate(R.layout.fragment_forgot_password,container,false);
        activity = getActivity();
        mContext = getActivity();
        cd = new ConnectionDirector(mContext);
        retrofitApiClient = RetrofitService.getRetrofit();

        strMobile = getArguments().getString("Mobile_Number");
        init();
        return rootview;
    }

    private void init() {
        ((Button)rootview.findViewById(R.id.btn_fplogin)).setOnClickListener(this);
        btn_fplogin = rootview.findViewById(R.id.btn_fplogin);
        pinview1 = rootview.findViewById(R.id.pinview1);
        otpTime = (TextView)rootview.findViewById(R.id.otpTime);
        resendLayout = (LinearLayout) rootview.findViewById(R.id.resendLayout);
        btn_fplogin.setOnClickListener(this);

        otptime();
    }

    private void startFragment(String tag, Fragment fragment){
        loginfragmentManager
                .beginTransaction()
                .setCustomAnimations(R.anim.left_enter, R.anim.right_out)
                .replace(R.id.login_frame, fragment, tag).commit();
    }

    private void otptime() {
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
        }
    }

    private void otpApi() {
        if (cd.isNetWorkAvailable()) {
            //strMobile = ((EditText) rootview.findViewById(R.id.et_login_email)).getText().toString();
            strOtp = pinview1.getValue();
            if (strOtp.isEmpty()) {
                ((EditText) rootview.findViewById(R.id.et_login_password)).setError("Please enter otp");
            } else {
                RetrofitService.getLoginData(new Dialog(mContext), retrofitApiClient.otpApi(strMobile, strOtp), new WebResponse() {
                    @Override
                    public void onResponseSuccess(Response<?> result) {
                        LoginModel loginModel = (LoginModel) result.body();

                        if (!loginModel.getError())
                        {
                            Alerts.show(mContext, loginModel.getMessage());

                            AppPreference.setBooleanPreference(mContext, Constant.Is_Login , true);
                            AppPreference.setStringPreference(mContext, Constant.User_Id , loginModel.getUser().getId());

                            Gson gson = new GsonBuilder().setLenient().create();
                            String data = gson.toJson(loginModel);
                            AppPreference.setStringPreference(mContext, Constant.User_Data, data);
                            User.setUser(loginModel);

                            NewPasswordFragment forgotPasswordFragment = new NewPasswordFragment();
                            Bundle bundle = new Bundle();
                            bundle.putString("user_id", loginModel.getUser().getId());
                            forgotPasswordFragment.setArguments(bundle);
                            startFragment(Constant.ForgotPasswordOtpFragment,forgotPasswordFragment);



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