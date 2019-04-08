package ibt.sabziwala.firebaseUtils;

import android.content.Context;
import android.provider.Settings;
import android.util.Log;

import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.iid.FirebaseInstanceIdService;

import ibt.sabziwala.constant.Constant;
import ibt.sabziwala.model.User;
import ibt.sabziwala.model.signup_responce.SignUpModel;
import ibt.sabziwala.retrofit_provider.RetrofitApiClient;
import ibt.sabziwala.retrofit_provider.RetrofitService;
import ibt.sabziwala.utils.AppPreference;
import ibt.sabziwala.utils.ConnectionDetector;
import retrofit2.Call;
import retrofit2.Callback;


public class MyFirebaseInstanceIDService extends FirebaseInstanceIdService {

    private static final String TAG = "MyFirebaseIIDService";
    private RetrofitApiClient retrofitApiClient;
    Context context;
    private ConnectionDetector cd;
    private String android_id;
    @Override
    public void onTokenRefresh() {

        context = this;

        cd = new ConnectionDetector(context);
        retrofitApiClient = RetrofitService.getRetrofit();

        // Get updated InstanceID token.
        String refreshedToken = FirebaseInstanceId.getInstance().getToken();
        Log.e(TAG, "Refreshed token: " + refreshedToken);
        AppPreference.setTokenPreference(getApplicationContext(), Constant.DEVICE_TOKEN, refreshedToken);

        if (User.getUser() == null || User.getUser().getUser().getId() == null)
            return;

        android_id = Settings.Secure.getString(context.getContentResolver(),
                Settings.Secure.ANDROID_ID);
        String userId = User.getUser().getUser().getId();

        Call<SignUpModel> updateFb = retrofitApiClient.updateToken(userId,android_id, refreshedToken);
        sendRegistrationToServer(updateFb);

    }

    private void sendRegistrationToServer(final Call<SignUpModel> method) {
        // TODO: Implement this method to send token to your app server.
        if (!cd.isNetworkAvailable()) {
            return;
        }
        method.enqueue(new Callback<SignUpModel>() {
            @Override
            public void onResponse(Call<SignUpModel> call, retrofit2.Response<SignUpModel> response) {
                if (response.isSuccessful() && response.body() != null) {
                    Log.e(TAG, "sendRegistrationToServer: " + response.message());

                } else {
                    Log.e(TAG, "sendRegistrationToServer: " + response.message());
                }
            }

            @Override
            public void onFailure(Call<SignUpModel> call, Throwable throwable) {
                Log.e(TAG, "sendRegistrationToServer: failed......  " + throwable.getMessage());
            }
        });
    }
}