package ibt.sabzishoppee.ui.fragment;

import android.app.Dialog;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import org.json.JSONObject;

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
import ibt.sabzishoppee.utils.EmailChecker;
import okhttp3.ResponseBody;
import retrofit2.Response;

import static ibt.sabzishoppee.ui.activity.LoginActivity.loginfragmentManager;


public class LoginFragment extends BaseFragment implements View.OnClickListener {
    private View rootview;
    private String strEmail, strPassword;
    private EditText et_login_email, et_login_password;
    private String strEmailPattern = "[a-zA-Z0-9._-]+@[a-z]+\\.+[a-z]+";
    private TextView tv_signUp, tv_forgot_password, tvPasswordStrength;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        rootview = inflater.inflate(R.layout.fragment_login_layout, container, false);
        activity = getActivity();
        mContext = getActivity();
        cd = new ConnectionDirector(mContext);
        retrofitApiClient = RetrofitService.getRetrofit();
        init();
        return rootview;
    }

    private void init() {
        Button loginbutton = rootview.findViewById(R.id.btn_login);
        et_login_email = rootview.findViewById(R.id.et_login_email);
        et_login_password = rootview.findViewById(R.id.et_login_password);
        tv_signUp = rootview.findViewById(R.id.tv_signUp);
        tv_forgot_password = rootview.findViewById(R.id.tv_forgot_password);
        tvPasswordStrength = rootview.findViewById(R.id.tvPasswordStrength);
        loginbutton.setOnClickListener(this);
        tv_signUp.setOnClickListener(this);
        tv_forgot_password.setOnClickListener(this);

        et_login_password.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                // Calculate password strength
                calculateStrength(editable.toString());
            }
        });

    }

    private void startFragment(String tag, Fragment fragment) {
        loginfragmentManager
                .beginTransaction()
                .setCustomAnimations(R.anim.right_enter, R.anim.left_out)
                .replace(R.id.login_frame, fragment, tag).commit();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_login:
                loginApi();
                break;
            case R.id.tv_signUp:
                startFragment(Constant.SignUpFragment, new SignUpFragment());
                break;

            case R.id.tv_forgot_password:
                startFragment(Constant.SignUpFragment, new ForgotPasswordFragment1());
                break;


        }
    }

    private void loginApi() {
        if (cd.isNetWorkAvailable()) {
            strEmail = ((EditText) rootview.findViewById(R.id.et_login_email)).getText().toString();
            strPassword = ((EditText) rootview.findViewById(R.id.et_login_password)).getText().toString();
            if (!EmailChecker.isValid(strEmail)) {
                et_login_email.setError("Please enter valid email address !!!");
            } else if (strPassword.isEmpty()) {
                ((EditText) rootview.findViewById(R.id.et_login_password)).setError("Please enter password");
            } else if (strPassword.length() < 6) {
                ((EditText) rootview.findViewById(R.id.et_login_password)).setError("Please enter 6 digit");
            }else {
                RetrofitService.getLoginData(new Dialog(mContext), retrofitApiClient.loginData(strEmail, strPassword), new WebResponse() {
                    @Override
                    public void onResponseSuccess(Response<?> result) {
                        LoginModel loginModel = (LoginModel) result.body();

                        if (!loginModel.getError()) {
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
        } else {
            cd.show(mContext);
        }
    }


    private void calculateStrength(String passwordText) {
        int upperChars = 0, lowerChars = 0, numbers = 0,
                specialChars = 0, otherChars = 0, strengthPoints = 0;
        char c;

        int passwordLength = passwordText.length();

        if (passwordLength ==0)
        {
            tvPasswordStrength.setText("Invalid Password");
            tvPasswordStrength.setTextColor(getResources().getColor(R.color.red_600));

            return;
        }

        //If password length is <= 5 set strengthPoints=1
        if (passwordLength <= 5) {
            strengthPoints =1;
        }
        //If password length is >5 and <= 10 set strengthPoints=2
        else if (passwordLength <= 10) {
            strengthPoints = 2;
        }
        //If password length is >10 set strengthPoints=3
        else
            strengthPoints = 3;
        // Loop through the characters of the password
        for (int i = 0; i < passwordLength; i++) {
            c = passwordText.charAt(i);
            // If password contains lowercase letters
            // then increase strengthPoints by 1
            if (c >= 'a' && c <= 'z') {
                if (lowerChars == 0) strengthPoints++;
                lowerChars = 1;
            }
            // If password contains uppercase letters
            // then increase strengthPoints by 1
            else if (c >= 'A' && c <= 'Z') {
                if (upperChars == 0) strengthPoints++;
                upperChars = 1;
            }
            // If password contains numbers
            // then increase strengthPoints by 1
            else if (c >= '0' && c <= '9') {
                if (numbers == 0) strengthPoints++;
                numbers = 1;
            }
            // If password contains _ or @
            // then increase strengthPoints by 1
            else if (c == '_' || c == '@') {
                if (specialChars == 0) strengthPoints += 1;
                specialChars = 1;
            }
            // If password contains any other special chars
            // then increase strengthPoints by 1
            else {
                if (otherChars == 0) strengthPoints += 2;
                otherChars = 1;
            }
        }

        if (strengthPoints <= 3)
        {
            tvPasswordStrength.setText("Password Strength : LOW");
            tvPasswordStrength.setTextColor(getResources().getColor(R.color.red_d));

        }
        else if (strengthPoints <= 6) {
            tvPasswordStrength.setText("Password Strength : MEDIUM");
            tvPasswordStrength.setTextColor(getResources().getColor(R.color.orange_900));
        }
        else if (strengthPoints <= 9){
            tvPasswordStrength.setText("Password Strength : HIGH");
            tvPasswordStrength.setTextColor(getResources().getColor(R.color.green_dark));
        }
    }
}
