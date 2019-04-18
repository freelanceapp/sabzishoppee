package ibt.sabziwala.ui.fragment;

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

import ibt.sabziwala.R;
import ibt.sabziwala.constant.Constant;
import ibt.sabziwala.model.User;
import ibt.sabziwala.model.login_responce.LoginModel;
import ibt.sabziwala.retrofit_provider.RetrofitService;
import ibt.sabziwala.retrofit_provider.WebResponse;
import ibt.sabziwala.ui.activity.HomeActivity;
import ibt.sabziwala.utils.Alerts;
import ibt.sabziwala.utils.AppPreference;
import ibt.sabziwala.utils.BaseFragment;
import ibt.sabziwala.utils.ConnectionDirector;
import ibt.sabziwala.utils.pinview.Pinview;
import retrofit2.Response;

import static ibt.sabziwala.ui.activity.LoginActivity.loginfragmentManager;


public class ForgotPasswordOtpFragment extends BaseFragment implements View.OnClickListener{
    private View rootview;
    private Button btn_fplogin;
    private TextView otpTime;
    private LinearLayout resendLayout;
    private Pinview pinview1;
    private String strMobile , strOtp, from;
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        rootview = inflater.inflate(R.layout.fragment_forgot_password,container,false);
        activity = getActivity();
        mContext = getActivity();
        cd = new ConnectionDirector(mContext);
        retrofitApiClient = RetrofitService.getRetrofit();

        from = getArguments().getString("From");
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
                            if (from.equals("Forgot")) {
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
                            } else if (from.equals("Login")){
                                Alerts.show(mContext, loginModel.getMessage());

                                AppPreference.setBooleanPreference(mContext, Constant.Is_Login, true);
                                AppPreference.setStringPreference(mContext, Constant.User_Id, loginModel.getUser().getId());

                                Gson gson = new GsonBuilder().setLenient().create();
                                String data = gson.toJson(loginModel);
                                AppPreference.setStringPreference(mContext, Constant.User_Data, data);
                                User.setUser(loginModel);

                                Intent intent = new Intent(mContext, HomeActivity.class);
                                mContext.startActivity(intent);
                                getActivity().finish();
                            }


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
