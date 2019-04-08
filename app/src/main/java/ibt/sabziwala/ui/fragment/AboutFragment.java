package ibt.sabziwala.ui.fragment;

import android.app.Dialog;
import android.os.Build;
import android.os.Bundle;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import ibt.sabziwala.R;
import ibt.sabziwala.model.contact_responce.ContcatModel;
import ibt.sabziwala.retrofit_provider.RetrofitService;
import ibt.sabziwala.retrofit_provider.WebResponse;
import ibt.sabziwala.utils.Alerts;
import ibt.sabziwala.utils.BaseFragment;
import ibt.sabziwala.utils.ConnectionDirector;
import retrofit2.Response;


public class AboutFragment extends BaseFragment {

    private View view;
    private TextView tv_about;
    public AboutFragment() {
        // Required empty public constructor
    }



    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view =  inflater.inflate(R.layout.fragment_about, container, false);

        tv_about = view.findViewById(R.id.tv_about);
        activity = getActivity();
        mContext = getActivity();
        cd = new ConnectionDirector(mContext);
        retrofitApiClient = RetrofitService.getRetrofit();
        bannerApi();
        return view;
    }

    private void bannerApi() {
        if (cd.isNetWorkAvailable()) {
            RetrofitService.getContactData(new Dialog(mContext), retrofitApiClient.contact(), new WebResponse() {
                @Override
                public void onResponseSuccess(Response<?> result) {
                    ContcatModel contcatModel = (ContcatModel) result.body();
                    if (!contcatModel.getResult()) {
                        Alerts.show(mContext, contcatModel.getMessage());

                        if (contcatModel.getContent().size() == 0)
                        {

                        }else {
                            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                                tv_about.setText(Html.fromHtml(contcatModel.getContent().get(4).getContent(), Html.FROM_HTML_MODE_COMPACT));
                            } else {
                                tv_about.setText(Html.fromHtml(contcatModel.getContent().get(4).getContent()));
                            }
                        }

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
