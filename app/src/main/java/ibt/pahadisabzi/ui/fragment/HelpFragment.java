package ibt.pahadisabzi.ui.fragment;

import android.app.Dialog;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import ibt.pahadisabzi.R;
import ibt.pahadisabzi.model.contact_responce.ContcatModel;
import ibt.pahadisabzi.retrofit_provider.RetrofitService;
import ibt.pahadisabzi.retrofit_provider.WebResponse;
import ibt.pahadisabzi.utils.Alerts;
import ibt.pahadisabzi.utils.BaseFragment;
import ibt.pahadisabzi.utils.ConnectionDirector;
import retrofit2.Response;


public class HelpFragment extends BaseFragment {

    private View view;
    private TextView tvAppName, tvNumber, tvEmailAddress, tvAddress;
    public HelpFragment() {
        // Required empty public constructor
    }



    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view =  inflater.inflate(R.layout.fragment_help, container, false);

        init();

        return view;
    }

    private void init()
    {

        mContext = getActivity();
        cd = new ConnectionDirector(mContext);
        retrofitApiClient = RetrofitService.getRetrofit();

        tvAppName = view.findViewById(R.id.tv_app_name);
        tvNumber = view.findViewById(R.id.tv_number);
        tvEmailAddress = view.findViewById(R.id.tv_email);
        tvAddress = view.findViewById(R.id.tv_address);

        bannerApi();
    }

    private void bannerApi() {
        if (cd.isNetWorkAvailable()) {
            RetrofitService.getContactData(new Dialog(mContext), retrofitApiClient.contact(), new WebResponse() {
                @Override
                public void onResponseSuccess(Response<?> result) {
                    ContcatModel contcatModel = (ContcatModel) result.body();
                    if (!contcatModel.getResult()) {
                        tvAppName.setText(contcatModel.getContent().get(2).getContent());
                        tvEmailAddress.setText(contcatModel.getContent().get(1).getContent());
                        tvNumber.setText(contcatModel.getContent().get(0).getContent());
                        tvAddress.setText(contcatModel.getContent().get(3).getContent());

                    }else {

                    }
                }

                @Override
                public void onResponseFailed(String error) {
                    Alerts.show(mContext, error);
                }
            });
        } else {
            cd.show(mContext);
        }
    }

}
