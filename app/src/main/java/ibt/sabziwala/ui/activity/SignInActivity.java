package ibt.sabziwala.ui.activity;

import android.app.Dialog;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

import ibt.sabziwala.R;
import ibt.sabziwala.constant.Constant;
import ibt.sabziwala.model.User;
import ibt.sabziwala.model.login_responce.LoginModel;
import ibt.sabziwala.retrofit_provider.RetrofitService;
import ibt.sabziwala.retrofit_provider.WebResponse;
import ibt.sabziwala.ui.fragment.ForgotPasswordOtpFragment;
import ibt.sabziwala.utils.Alerts;
import ibt.sabziwala.utils.AppPreference;
import ibt.sabziwala.utils.BaseActivity;
import okhttp3.ResponseBody;
import retrofit2.Response;

public class SignInActivity extends BaseActivity {

    private EditText et_login_email;
    private Button buttonLogin;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signin);

        initViews();
    }

    private void initViews() {
        et_login_email = findViewById(R.id.et_login_email);
        buttonLogin = findViewById(R.id.btn_login);
        buttonLogin.setOnClickListener(v -> {
          String mobileNumber = et_login_email.getText().toString().trim();

          if (mobileNumber.isEmpty()){
              Toast.makeText(mContext, "Mobile number should not be empty.", Toast.LENGTH_SHORT).show();
          }else if (mobileNumber.length()<10){
              Toast.makeText(mContext, "Please enter a valid 10 digit mobile number.", Toast.LENGTH_SHORT).show();
          }else{
              RetrofitService.getResponseData(new Dialog(mContext), retrofitApiClient.loginWithMobile(mobileNumber), new WebResponse() {
                  @Override
                  public void onResponseSuccess(Response<?> result) {

                      ResponseBody response = (ResponseBody) result.body();

                      try {
                          JSONObject jsonObject = new JSONObject(response.string());
                          if (!jsonObject.getBoolean("error")){
                              Alerts.show(mContext, jsonObject.getString("message"));
                              if (jsonObject.getString("message").equals("Otp Send to New User successfull"))
                              {
                                  startActivity(new Intent(mContext, VerifyOtpActivity.class)
                                          .putExtra("Mobile_Number", mobileNumber)
                                  .putExtra("User_profile","0"));
                                  finish();
                              }else {
                                  startActivity(new Intent(mContext, VerifyOtpActivity.class)
                                          .putExtra("Mobile_Number", mobileNumber)
                                          .putExtra("User_profile","1"));
                                  finish();
                              }

                          }else{
                              Alerts.show(mContext, jsonObject.getString("message"));
                          }
                      } catch (JSONException e) {
                          e.printStackTrace();
                      } catch (IOException e) {
                          e.printStackTrace();
                      }
                  }

                  @Override
                  public void onResponseFailed(String error) {
                      Alerts.show(mContext, error);
                  }
              });
          }
        });
    }
}
