package ibt.sabzishoppee.ui.activity;

import android.app.Dialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Base64;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RadioGroup;
import android.widget.TextView;

import com.bumptech.glide.Glide;

import de.hdodenhof.circleimageview.CircleImageView;
import ibt.sabzishoppee.R;
import ibt.sabzishoppee.constant.Constant;
import ibt.sabzishoppee.model.login_responce.LoginModel;
import ibt.sabzishoppee.model.signup_responce.SignUpModel;
import ibt.sabzishoppee.retrofit_provider.RetrofitService;
import ibt.sabzishoppee.retrofit_provider.WebResponse;
import ibt.sabzishoppee.ui.fragment.ForgotPasswordFragment;
import ibt.sabzishoppee.utils.Alerts;
import ibt.sabzishoppee.utils.AppPreference;
import ibt.sabzishoppee.utils.BaseActivity;
import ibt.sabzishoppee.utils.ConnectionDirector;
import ibt.sabzishoppee.utils.EmailChecker;
import retrofit2.Response;

public class ProfileActivity extends BaseActivity implements View.OnClickListener {

    private TextView tv_fullname, tv_email, tv_mobile , tv_address, tv_dob, tvGender;
    private RadioGroup rb_gender;
    private CircleImageView ci_profile;
    private Button btn_edit_profile;
    private ImageView btn_profile_btn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        mContext = this;
        cd = new ConnectionDirector(mContext);
        retrofitApiClient = RetrofitService.getRetrofit();
        init();
    }

    public void init()
    {
        tv_fullname = findViewById(R.id.tv_fullname);
        tv_email = findViewById(R.id.tv_email_address);
        tv_address = findViewById(R.id.tv_address);
        tv_mobile = findViewById(R.id.tv_mobile);
        tvGender = findViewById(R.id.tvGender);
        tv_dob = findViewById(R.id.tv_dob);
        ci_profile = findViewById(R.id.ci_profile);
        btn_edit_profile = findViewById(R.id.btn_edit_profile);
        btn_profile_btn = findViewById(R.id.btn_profile_btn);
        btn_edit_profile.setOnClickListener(this);
        btn_profile_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
        signUpApi();
    }


    private void signUpApi() {

        String userId = AppPreference.getStringPreference(mContext , Constant.User_Id);
        if (cd.isNetWorkAvailable()) {
                RetrofitService.getProfile(new Dialog(mContext), retrofitApiClient.getprofile(userId), new WebResponse() {
                    @Override
                    public void onResponseSuccess(Response<?> result) {
                        LoginModel responseBody = (LoginModel) result.body();

                        if (!responseBody.getError())
                        {
                            Alerts.show(mContext , responseBody.getMessage());
                            tv_fullname.setText(responseBody.getUser().getUserName());
                            tv_email.setText(responseBody.getUser().getUserEmail());
                            tv_mobile.setText(responseBody.getUser().getUserContact());
                            tv_dob.setText(responseBody.getUser().getUserDateOfBirth());
                            //tv_address.setText(responseBody.getUser().get());
                            tvGender.setText(responseBody.getUser().getUserGender());
                            if (!responseBody.getUser().getUserProfilePicture().isEmpty()) {
                                String base64String = responseBody.getUser().getUserProfilePicture();
                                String base64Image = base64String.split(",")[1];
                                byte[] decodedString = Base64.decode(base64Image, Base64.DEFAULT);
                                Bitmap decodedByte = BitmapFactory.decodeByteArray(decodedString, 0, decodedString.length);
                                ci_profile.setImageBitmap(decodedByte);
                            }else {
                                ci_profile.setImageResource(R.drawable.profile_img);
                            }
                            //Glide.with(mContext).load(decodedByte).error(R.drawable.profile_img).fitCenter().into(ci_profile);
                        }else {
                            Alerts.show(mContext , responseBody.getMessage());
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

    }

    @Override
    public void onClick(View view) {
        Intent intent = new Intent(ProfileActivity.this, EditProfileActivity.class);
        startActivity(intent);
        finish();
    }
}
