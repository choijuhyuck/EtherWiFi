package comks_kim.github.etherwifi.init_page;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import comks_kim.github.etherwifi.MainActivity;
import comks_kim.github.etherwifi.R;
import comks_kim.github.etherwifi.utils.Preference;

public class WalletView extends AppCompatActivity {
    Button complete_bt;
    Intent intent;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.wallet_view);
        Preference.getInstance().setContext(this);


        Preference.getInstance().putInt("WALLET_NUMBER",1);

        complete_bt = findViewById(R.id.complete_button);

        complete_bt.setOnClickListener(
                new Button.OnClickListener() {
                    public void onClick(View v) {
                        intent = new Intent(WalletView.this, MainActivity.class);
                        startActivity(intent);
                        finish();
                    }
                }
        );



    }

}
