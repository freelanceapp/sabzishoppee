package ibt.pahadisabzi.utils;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.widget.Toast;

public class ConnectionDirector {
    Context mcontext;
    public ConnectionDirector(Context mContext){
        this.mcontext = mContext;
    }
    public boolean isNetWorkAvailable(){
        ConnectivityManager connectivityManager = (ConnectivityManager) mcontext.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        if (activeNetworkInfo == null || !activeNetworkInfo.isConnected()){
            Toast.makeText(mcontext, "Internet Connection is not available",Toast.LENGTH_SHORT).show();
        }
        return activeNetworkInfo != null && activeNetworkInfo.isConnected();
    }
}
