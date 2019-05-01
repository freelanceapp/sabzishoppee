package ibt.pahadisabzi.notification;

import android.app.Dialog;
import android.content.Context;
import android.os.Looper;
import android.provider.Settings;
import android.util.Log;

import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.iid.FirebaseInstanceIdService;

import ibt.pahadisabzi.constant.Constant;
import ibt.pahadisabzi.model.signup_responce.SignUpModel;
import ibt.pahadisabzi.retrofit_provider.RetrofitApiClient;
import ibt.pahadisabzi.retrofit_provider.RetrofitService;
import ibt.pahadisabzi.retrofit_provider.WebResponse;
import ibt.pahadisabzi.utils.Alerts;
import ibt.pahadisabzi.utils.AppPreference;
import ibt.pahadisabzi.utils.ConnectionDetector;
import retrofit2.Response;

public class FirebaseIDService extends FirebaseInstanceIdService {
    private static final String TAG = "FirebaseIDService";
    public Context mContext;
    public RetrofitApiClient retrofitApiClient;
    public ConnectionDetector cd;
    @Override
    public void onTokenRefresh() {
        // Get updated InstanceID token.
        String refreshedToken = FirebaseInstanceId.getInstance().getToken();
        Log.d(TAG, "Refreshed token: " + refreshedToken);
        mContext = this;
        cd = new ConnectionDetector(mContext);
        retrofitApiClient = RetrofitService.getRetrofit();
        Log.e("Token","...");
        // TODO: Implement this method to send any registration to your app's servers.
       sendRegistrationToServer(refreshedToken);
        if (!(AppPreference.getStringPreference(mContext, Constant.User_Id)).isEmpty()) {
            tokenApi(refreshedToken);
        }
    }

    /**
     * Persist token to third-party servers.
     *
     * Modify this method to associate the user's FCM InstanceID token with any server-side account
     * maintained by your application.
     *
     * @param token The new token.
     */
    private void sendRegistrationToServer(String token) {
        // Add custom implementation, as needed.
        Log.e(TAG, "sendRegistrationToServer: " + token);
        if (!(AppPreference.getStringPreference(mContext, Constant.User_Id)).isEmpty()) {
            tokenApi(token);
        }

    }

    private void tokenApi(String strToken) {
        Looper.prepare();
        String android_id = Settings.Secure.getString(getApplicationContext().getContentResolver(), Settings.Secure.ANDROID_ID);
        String strId = AppPreference.getStringPreference(mContext, Constant.User_Id);
        if (cd.isNetworkAvailable()) {
            RetrofitService.getSignData(new Dialog(mContext), retrofitApiClient.updateToken(strId, strToken, android_id, "user"), new WebResponse() {
                @Override
                public void onResponseSuccess(Response<?> result) {
                    SignUpModel loginModal = (SignUpModel) result.body();
                    assert loginModal != null;
                    if (!loginModal.getError()) {
                        Alerts.show(mContext, loginModal.getMessage());
                    } else {
                        Alerts.show(mContext, loginModal.getMessage());
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

