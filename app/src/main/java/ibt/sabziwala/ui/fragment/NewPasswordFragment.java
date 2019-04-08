package ibt.sabziwala.ui.fragment;

import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

import ibt.sabziwala.R;
import ibt.sabziwala.model.change_password_responce.ChangePasswordModel;
import ibt.sabziwala.retrofit_provider.RetrofitService;
import ibt.sabziwala.retrofit_provider.WebResponse;
import ibt.sabziwala.ui.activity.HomeActivity;
import ibt.sabziwala.utils.Alerts;
import ibt.sabziwala.utils.BaseFragment;
import ibt.sabziwala.utils.ConnectionDirector;
import retrofit2.Response;


public class NewPasswordFragment extends BaseFragment {
   private View view;
   private EditText et_new_password, et_renter_password;
   private Button btn_change_password;
   private String strUserId, strNewPassword, strRepassword;
    public NewPasswordFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view =  inflater.inflate(R.layout.fragment_change_password, container, false);
        activity = getActivity();
        mContext = getActivity();
        cd = new ConnectionDirector(mContext);
        retrofitApiClient = RetrofitService.getRetrofit();

        strUserId = getArguments().getString("user_id");
        et_new_password = view.findViewById(R.id.et_change_password);
        et_renter_password = view.findViewById(R.id.et_renter_password);
        btn_change_password = view.findViewById(R.id.btn_change_password);

        btn_change_password.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                changePasswordApi();
            }
        });
        return view;
    }

    private void changePasswordApi() {
        if (cd.isNetWorkAvailable()) {
            strNewPassword = et_new_password.getText().toString();
            strRepassword = et_renter_password.getText().toString();
            if (strNewPassword.length() < 6) {
                et_new_password.setError("Please enter password !!!");
            } else if (strRepassword.length() < 6) {
                et_renter_password.setError("please reter password!!!");
            }else if (!strNewPassword.matches(strRepassword)) {
                et_renter_password.setError("Password not match !!!");
            }
            else {
                RetrofitService.getChangePassword(new Dialog(mContext), retrofitApiClient.changePassword(strUserId, strRepassword), new WebResponse() {
                    @Override
                    public void onResponseSuccess(Response<?> result) {
                        ChangePasswordModel loginModel = (ChangePasswordModel) result.body();

                        if (!loginModel.getError())
                        {
                            Alerts.show(mContext, loginModel.getMessage());
                            clear();
                            Intent intent = new Intent(mContext , HomeActivity.class);
                            mContext.startActivity(intent);
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

    private void clear()
    {
        et_renter_password.setText("");
        et_new_password.setText("");
    }
}
