package ibt.sabziwala.ui.fragment;

import android.app.Dialog;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

import ibt.sabziwala.R;
import ibt.sabziwala.model.contact_us_responce.ContactUsModel;
import ibt.sabziwala.retrofit_provider.RetrofitService;
import ibt.sabziwala.retrofit_provider.WebResponse;
import ibt.sabziwala.utils.Alerts;
import ibt.sabziwala.utils.BaseFragment;
import ibt.sabziwala.utils.ConnectionDirector;
import retrofit2.Response;

public class ContactUsFragment extends BaseFragment {

    private View view;

    private EditText etContactName, etContactEmail, etContactSubject, etContactMessage;

    private String strContactName, strContactEmail, strContactSubject, strContactMessage;

    private Button btnSubmit;

    public ContactUsFragment() {
        // Required empty public constructor
    }



    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view =  inflater.inflate(R.layout.fragment_contact_us, container, false);

        init();

        return view;
    }

    private void init()
    {
        mContext = getActivity();
        cd = new ConnectionDirector(mContext);
        retrofitApiClient = RetrofitService.getRetrofit();
        etContactName = view.findViewById(R.id.et_contact_name);
        etContactEmail = view.findViewById(R.id.et_contact_email);
        etContactSubject = view.findViewById(R.id.et_contact_subject);
        etContactMessage = view.findViewById(R.id.et_contact_message);
        btnSubmit = view.findViewById(R.id.btn_contact_submit);
        btnSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                contactUsApi();
            }
        });

    }


    private void contactUsApi() {
        if (cd.isNetWorkAvailable()) {
            strContactName = etContactName.getText().toString();
            strContactEmail = etContactEmail.getText().toString();
            strContactSubject = etContactSubject.getText().toString();
            strContactMessage = etContactMessage.getText().toString();

            if (strContactName.isEmpty()) {
                etContactName.setError("Please enter name");
            }else  if (strContactEmail.isEmpty()) {
                etContactEmail.setError("Please enter email");
            }else  if (strContactSubject.isEmpty()) {
                etContactSubject.setError("Please enter subject");
            }else  if (strContactMessage.isEmpty()) {
                etContactMessage.setError("Please enter message");
            }
            else {
                RetrofitService.contactUs(new Dialog(mContext), retrofitApiClient.contactUs(strContactName, strContactEmail, strContactSubject, strContactMessage), new WebResponse() {
                    @Override
                    public void onResponseSuccess(Response<?> result) {
                        ContactUsModel loginModel = (ContactUsModel) result.body();

                        if (!loginModel.getResult())
                        {
                            Alerts.show(mContext, loginModel.getMessage());
                        }else {
                            Alerts.show(mContext, loginModel.getMessage());
                        }
                        cleardata();
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

    private void cleardata()
    {
        etContactName.setText("");
        etContactEmail.setText("");
        etContactSubject.setText("");
        etContactMessage.setText("");
    }
}
