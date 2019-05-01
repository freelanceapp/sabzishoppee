package ibt.pahadisabzi.ui.activity;

import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

import ibt.pahadisabzi.R;
import ibt.pahadisabzi.retrofit_provider.RetrofitService;
import ibt.pahadisabzi.retrofit_provider.WebResponse;
import ibt.pahadisabzi.utils.Alerts;
import ibt.pahadisabzi.utils.BaseActivity;
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
       /* AppSignatureHashHelper appSignatureHashHelper = new AppSignatureHashHelper(this);

        // This code requires one time to get Hash keys do comment and share key
        Log.e("Key", appSignatureHashHelper.getAppSignatures().get(0));*/

        buttonLogin.setOnClickListener(v -> {
          String mobileNumber = et_login_email.getText().toString().trim();

          if (mobileNumber.isEmpty()){
              Toast.makeText(mContext, "Mobile number should not be empty.", Toast.LENGTH_SHORT).show();
          }else if (mobileNumber.length()<10){
              Toast.makeText(mContext, "Please enter a valid 10 digit mobile number.", Toast.LENGTH_SHORT).show();
          }else if (cd.isNetWorkAvailable()) {

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
          }else {
            cd.show(mContext);
        }
        });
    }
}
