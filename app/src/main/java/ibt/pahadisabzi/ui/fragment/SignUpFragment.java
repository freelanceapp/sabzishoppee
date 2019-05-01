package ibt.pahadisabzi.ui.fragment;

import android.app.Dialog;
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
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import ibt.pahadisabzi.R;
import ibt.pahadisabzi.constant.Constant;
import ibt.pahadisabzi.model.signup_responce.SignUpModel;
import ibt.pahadisabzi.retrofit_provider.RetrofitService;
import ibt.pahadisabzi.retrofit_provider.WebResponse;
import ibt.pahadisabzi.utils.Alerts;
import ibt.pahadisabzi.utils.BaseFragment;
import ibt.pahadisabzi.utils.ConnectionDirector;
import ibt.pahadisabzi.utils.EmailChecker;
import retrofit2.Response;

import static ibt.pahadisabzi.ui.activity.LoginActivity.loginfragmentManager;


public class SignUpFragment extends BaseFragment implements View.OnClickListener {
    private View rootview;
    private Button btn_signUp;
    private EditText fullname, emailAddress, password, cPassword, cPhone;
    private String strName, strMobile, strEmailAddress, strPassword, strConfirmPassword;
    private String emailPattern = "[a-zA-Z0-9._-]+@[a-z]+\\.+[a-z]+";

    private TextView tvConfirmPasswordStrength, tvPasswordStrength;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        rootview = inflater.inflate(R.layout.fragment_signup_layout, container, false);
        activity = getActivity();
        mContext = getActivity();
        cd = new ConnectionDirector(mContext);
        retrofitApiClient = RetrofitService.getRetrofit();
        init();
        return rootview;
    }

    private void init() {
        btn_signUp = rootview.findViewById(R.id.btn_signUp);
        fullname = rootview.findViewById(R.id.et_fullname);
        emailAddress = rootview.findViewById(R.id.et_email_address);
        password = rootview.findViewById(R.id.et_password);
        cPassword = rootview.findViewById(R.id.et_cpassword);
        cPhone = rootview.findViewById(R.id.et_mobile);
        tvConfirmPasswordStrength = rootview.findViewById(R.id.tvConfirmPasswordStrength);
        tvPasswordStrength = rootview.findViewById(R.id.tvPasswordStrength);
        btn_signUp.setOnClickListener(this);
        ((LinearLayout) rootview.findViewById(R.id.tv_Login)).setOnClickListener(this);


        password.addTextChangedListener(new TextWatcher() {
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

        cPassword.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                // Calculate password strength
                String passwrd = password.getText().toString();
                if (editable.length() > 0 && passwrd.length() > 0) {
                    if(cPassword.getText().toString().matches(passwrd)){
                        tvConfirmPasswordStrength.setText("Password Match");
                        tvConfirmPasswordStrength.setTextColor(getResources().getColor(R.color.green_dark));
                    }else {
                        tvConfirmPasswordStrength.setText("Password Not Match");
                        tvConfirmPasswordStrength.setTextColor(getResources().getColor(R.color.red_d));
                    }
                }

            }
        });

    }

    private void startFragment(String tag, Fragment fragment) {
        loginfragmentManager
                .beginTransaction()
                .setCustomAnimations(R.anim.left_enter, R.anim.right_out)
                .replace(R.id.login_frame, fragment, tag).commit();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_signUp:
                signUpApi();
               /* Intent intent = new Intent(mContext, HomeActivity.class);
                startActivity(intent);
                activity.finish();*/
                break;
            case R.id.tv_Login:
                startFragment(Constant.LoginFragment, new LoginFragment());
                break;
        }
    }

    private void signUpApi() {
        if (cd.isNetWorkAvailable()) {
            strName = fullname.getText().toString();
            strEmailAddress = emailAddress.getText().toString();
            strPassword = password.getText().toString();
            strConfirmPassword = cPassword.getText().toString();
            strMobile = cPhone.getText().toString();
            if (strMobile.length() != 10) {
                cPhone.setError("Please enter Mobile !!!");
            } else if (!isValidEmailId(strEmailAddress)) {
                emailAddress.setError("Please enter a valid email address !!!");
            } else if (!EmailChecker.isValid(strEmailAddress)) {
                emailAddress.setError("Please enter valid email address !!!");
            } else if (strPassword.length() < 6) {
                password.setError("Please enter password !!!");
            } else if (strConfirmPassword.length() < 6) {
                password.setError("please reter password!!!");
            } else if (!strPassword.matches(strConfirmPassword)) {
                password.setError("Password not match !!!");
            } else {
                RetrofitService.getSignData(new Dialog(mContext), retrofitApiClient.signUp(strName, strEmailAddress , strMobile, strPassword), new WebResponse() {
                    @Override
                    public void onResponseSuccess(Response<?> result) {
                        SignUpModel responseBody = (SignUpModel) result.body();

                        if (!responseBody.getError())
                        {
                            Alerts.show(mContext , responseBody.getMessage());

                            ForgotPasswordFragment forgotPasswordFragment = new ForgotPasswordFragment();
                            Bundle bundle = new Bundle();
                            bundle.putString("Mobile_Number", strMobile);
                            forgotPasswordFragment.setArguments(bundle);
                            startFragment(Constant.ForgotPasswordFragment, forgotPasswordFragment);

                        }else {
                            Alerts.show(mContext , responseBody.getMessage());
                        }

                    }

                    @Override
                    public void onResponseFailed(String error) {
                        Alerts.show(mContext, error);
                    }
                });
            }
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

    private boolean isValidEmailId(String email){
        Pattern pattern;
        Matcher matcher;
        final String EMAIL_PATTERN = "^[_A-Za-z0-9-]+(\\.[_A-Za-z0-9-]+)*@[A-Za-z0-9]+(\\.[A-Za-z0-9]+)*(\\.[A-Za-z]{2,})$";
        pattern = Pattern.compile(EMAIL_PATTERN);
        matcher = pattern.matcher(email);
        return matcher.matches();
    }
}
