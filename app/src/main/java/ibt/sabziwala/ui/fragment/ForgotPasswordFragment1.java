package ibt.sabziwala.ui.fragment;

import android.app.Dialog;
import android.os.Bundle;
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

import ibt.sabziwala.R;
import ibt.sabziwala.constant.Constant;
import ibt.sabziwala.model.signup_responce.SignUpModel;
import ibt.sabziwala.retrofit_provider.RetrofitService;
import ibt.sabziwala.retrofit_provider.WebResponse;
import ibt.sabziwala.utils.Alerts;
import ibt.sabziwala.utils.BaseFragment;
import ibt.sabziwala.utils.ConnectionDirector;
import retrofit2.Response;

import static ibt.sabziwala.ui.activity.LoginActivity.loginfragmentManager;


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

                            ForgotPasswordOtpFragment forgotPasswordFragment = new ForgotPasswordOtpFragment();
                            Bundle bundle = new Bundle();
                            bundle.putString("From", "Forgot");
                            bundle.putString("Mobile_Number", strMobile);
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
