package ibt.sabzishoppee.ui.activity;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import ibt.sabzishoppee.R;
import ibt.sabzishoppee.constant.Constant;
import ibt.sabzishoppee.ui.fragment.LoginFragment;

public class LoginActivity extends AppCompatActivity {
    public static FragmentManager loginfragmentManager;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);


        if (savedInstanceState == null){
            loginfragmentManager = getSupportFragmentManager();
            loginfragmentManager.beginTransaction()
                    .replace(R.id.login_frame,new LoginFragment()
                            , Constant.LoginFragment).commit();
        }
        replaceFragment();
    }

    private void replaceFragment(){
        loginfragmentManager = getSupportFragmentManager();
        loginfragmentManager.beginTransaction()
                .replace(R.id.login_frame,new LoginFragment()
                        , Constant.LoginFragment).commit();
    }



    public void onBackPressed() {

        Fragment Login_Password = loginfragmentManager.findFragmentByTag(Constant.LoginFragment);
        Fragment SignUp_Fragment = loginfragmentManager.findFragmentByTag(Constant.SignUpFragment);
        Fragment ForgotPassword_Fragment = loginfragmentManager.findFragmentByTag(Constant.ForgotPasswordFragment);

        if (SignUp_Fragment != null)
            replaceFragment();
        else if (Login_Password != null)
            replaceFragment();
        else if (ForgotPassword_Fragment != null)
            replaceFragment();
        else
            super.onBackPressed();
    }

}
