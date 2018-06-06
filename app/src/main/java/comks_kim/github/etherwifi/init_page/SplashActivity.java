package comks_kim.github.etherwifi.init_page;

/**
 * Created by jehug on 2018-06-04.
 */

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;

import comks_kim.github.etherwifi.MainActivity;
import comks_kim.github.etherwifi.utils.Preference;

/**
 * Created by Hun-joyce on 2018-01-18.
 */

public class SplashActivity extends AppCompatActivity {
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Preference.getInstance().setContext(this);

        Handler hd = new Handler();
        hd.postDelayed(new Runnable() {
            @Override
            public void run() {
                //손해보험사 완료가 되면 INS_COUNT == 4
                if(Preference.getInstance().getInt("WALLET_NUMBER") != 1) {
                    startActivity(new Intent(SplashActivity.this, WalletCreate.class));
                    finish();
                }else {
                    startActivity(new Intent(SplashActivity.this, MainActivity.class));
                    finish();
                }
                finish();

            }
        }, 1500);
    }
}
