package ibt.pahadisabzi.ui.activity;

import android.content.Intent;
import android.content.pm.PackageManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import ibt.pahadisabzi.BuildConfig;
import ibt.pahadisabzi.R;
import ibt.pahadisabzi.model.User;

public class InviteFriendActivity extends AppCompatActivity implements View.OnClickListener {

    TextView tv_share_deatils;
    LinearLayout share_whatsapp, share_facebook, share_gmail, share_more;
    ImageView back_btn;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_invite_friend);

        init();
    }

    public void init()
    {
        tv_share_deatils = findViewById(R.id.tv_share_deatils);

        tv_share_deatils.setText("Hi! I'm "+ User.getUser().getUser().getUserName()+ ", I am enjoying my services with \n Pahadi Sabzi");

        share_facebook = findViewById(R.id.share_facebook);
        share_whatsapp = findViewById(R.id.share_whatsapp);
        share_gmail = findViewById(R.id.share_gmail);
        share_more = findViewById(R.id.share_more);

        share_facebook.setOnClickListener(this);
        share_whatsapp.setOnClickListener(this);
        share_gmail.setOnClickListener(this);
        share_more.setOnClickListener(this);

        back_btn = findViewById(R.id.back_btn);
        back_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
    }

    @Override
    public void onClick(View view) {
        switch (view.getId())
        {
            case R.id.share_whatsapp :
                try {
                    Intent shareIntent = new Intent(Intent.ACTION_SEND);
                    shareIntent.setType("text/plain");
                    shareIntent.setPackage("com.whatsapp");
                    shareIntent.putExtra(Intent.EXTRA_SUBJECT, "Pahadi Sabzi");
                    String shareMessage= "\nLet me recommend you this application\n\n";
                    shareMessage = shareMessage + "https://play.google.com/store/apps/details?id=" + BuildConfig.APPLICATION_ID +"\n\n";
                    shareIntent.putExtra(Intent.EXTRA_TEXT, shareMessage);
                    startActivity(Intent.createChooser(shareIntent, "choose one"));
                } catch(Exception e) {
                    //e.toString();
                }
                break;

            case R.id.share_facebook :
                SharingToSocialMedia("com.facebook.katana");
                break;

            case R.id.share_gmail :
                SharingToSocialMedia("com.instagram.android");
                break;

            case R.id.share_more :
                try {
                    Intent shareIntent = new Intent(Intent.ACTION_SEND);
                    shareIntent.setType("text/plain");
                    shareIntent.putExtra(Intent.EXTRA_SUBJECT, "Pahadi Sabzi");
                    String shareMessage= "\nLet me recommend you this application\n\n";
                    shareMessage = shareMessage + "https://play.google.com/store/apps/details?id=" + BuildConfig.APPLICATION_ID +"\n\n";
                    shareIntent.putExtra(Intent.EXTRA_TEXT, shareMessage);
                    startActivity(Intent.createChooser(shareIntent, "choose one"));
                } catch(Exception e) {
                    //e.toString();
                }
                break;
        }
    }

    public void SharingToSocialMedia(String application) {
        try {
            Intent intent = new Intent();
            intent.setAction(Intent.ACTION_SEND);
            intent.setType("text/plain");
            intent.putExtra(Intent.EXTRA_SUBJECT, "Pahadi Sabzi");
            String shareMessage = "\nLet me recommend you this application\n\n";
            shareMessage = shareMessage + "https://play.google.com/store/apps/details?id=" + BuildConfig.APPLICATION_ID + "\n\n";
            intent.putExtra(Intent.EXTRA_TEXT, shareMessage);
            boolean installed = checkAppInstall(application);
            if (installed) {
                intent.setPackage(application);
                startActivity(intent);
            } else {
                Toast.makeText(getApplicationContext(), "Installed application first", Toast.LENGTH_LONG).show();
            }
        }catch (Exception e)
        {
            Log.e("Exception "," "+ e.toString());
        }
    }


    private boolean checkAppInstall(String uri) {
        PackageManager pm = getPackageManager();
        try {
            pm.getPackageInfo(uri, PackageManager.GET_ACTIVITIES);
            return true;
        } catch (PackageManager.NameNotFoundException e) {
        }

        return false;
    }
}
