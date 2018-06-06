package comks_kim.github.etherwifi.init_page;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;

import comks_kim.github.etherwifi.MainActivity;
import comks_kim.github.etherwifi.R;

public class WalletCreate extends AppCompatActivity implements View.OnClickListener{
    Button create_bt;
    Button dolater_bt;
    Intent intent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        requestWindowFeature(Window.FEATURE_NO_TITLE);
//        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
//                WindowManager.LayoutParams.FLAG_FULLSCREEN);

        setContentView(R.layout.wallet_create);

        create_bt = findViewById(R.id.create_button);
        dolater_bt = findViewById(R.id.dolater_button);

        create_bt.setOnClickListener(this);
        dolater_bt.setOnClickListener(this);
    }

    public void onClick(View v) {

        switch (v.getId()) {
            case R.id.create_button:
                intent = new Intent(WalletCreate.this, WalletView.class);
                startActivity(intent);
                finish();
                break;

            case R.id.dolater_button:
                intent = new Intent(WalletCreate.this, MainActivity.class);
                startActivity(intent);
                finish();
                break;
        }

    }

}
