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
import ibt.sabzishoppee.model.signup_responce.SignUpModel;
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


public class ForgotPasswordFragment1 extends BaseFragment implements View.OnClickListener{
    private View rootview;
    private Button btn_fplogin;
    private TextView otpTime;
    private LinearLayout resendLayout;
    private EditText et_forgot_mobile;
    private String strMobile ;
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        rootview = inflater.inflate(R.layout.fragment_forgot_password1,container,false);
        activity = getActivity();
        mContext = getActivity();
        cd = new ConnectionDirector(mContext);
        retrofitApiClient = RetrofitService.getRetrofit();
        init();
        return rootview;
    }

    private void init() {
        ((Button)rootview.findViewById(R.id.btn_fplogin)).setOnClickListener(this);
        btn_fplogin = rootview.findViewById(R.id.btn_fplogin);
        et_forgot_mobile = rootview.findViewById(R.id.et_forgot_mobile);
        otpTime = (TextView)rootview.findViewById(R.id.otpTime);
        resendLayout = (LinearLayout) rootview.findViewById(R.id.resendLayout);
        btn_fplogin.setOnClickListener(this);

    }

    private void startFragment(String tag, Fragment fragment){
        loginfragmentManager
                .beginTransaction()
                .setCustomAnimations(R.anim.left_enter, R.anim.right_out)
                .replace(R.id.login_frame, fragment, tag).commit();
    }



    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.btn_fplogin:
                forgotApi();
                //startFragment(Constant.SignUpFragment,new SignUpFragment());
            break;
        }
    }

    private void forgotApi() {
        if (cd.isNetWorkAvailable()) {
            //strMobile = ((EditText) rootview.findViewById(R.id.et_login_email)).getText().toString();
            strMobile = et_forgot_mobile.getText().toString();
            if (strMobile.isEmpty()) {
                ((EditText) rootview.findViewById(R.id.et_login_password)).setError("Please Enter Mobile Number");
            } else {
                RetrofitService.getForgotPassword(new Dialog(mContext), retrofitApiClient.forgotPasswordApi(strMobile), new WebResponse() {
                    @Override
                    public void onResponseSuccess(Response<?> result) {
                        SignUpModel loginModel = (SignUpModel) result.body();

                        if (!loginModel.getError())
                        {
                            Alerts.show(mContext, loginModel.getMessage());
                            startFragment(Constant.LoginFragment,new LoginFragment());
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
