package comks_kim.github.etherwifi;

// import android.app.FragmentTransaction;
import android.Manifest;
import android.content.Context;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;

import comks_kim.github.etherwifi.adapter.ViewPagerAdapter;


public class MainActivity extends AppCompatActivity {
    BottomNavigationView bottomNavigationView;
    ViewPager viewPager;
    Fragment selectedFragment = null;
    MenuItem prevMenuItem;
    ViewPagerAdapter viewPagerAdapter;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        ActivityCompat.requestPermissions(this, new String[]{
                Manifest.permission.ACCESS_FINE_LOCATION }, 1);

        bottomNavigationView = (BottomNavigationView)
                findViewById(R.id.navigation);

        viewPager = (ViewPager) findViewById(R.id.viewpager);
//        viewPager.setAdapter(pageAdapter);
        viewPagerAdapter = new ViewPagerAdapter(getSupportFragmentManager());
        setupViewPager(viewPager);

        bottomNavigationView.setOnNavigationItemSelectedListener
                (new BottomNavigationView.OnNavigationItemSelectedListener() {
                    @Override
                    public boolean onNavigationItemSelected(@NonNull MenuItem item) {

                        switch (item.getItemId()) {
                            case R.id.action_wifi_list:
                                viewPager.setCurrentItem(0);
                                break;
                            case R.id.action_sharing_list:
                                viewPager.setCurrentItem(1);
                                break;
                            case R.id.action_wallet:
                                viewPager.setCurrentItem(2);
                                break;
                        }

                        return false;
                    }
                });

        viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }
            @Override
            public void onPageSelected(int position) {
                if (prevMenuItem != null) {
                    prevMenuItem.setChecked(false);
                } else {
                    bottomNavigationView.getMenu().getItem(0).setChecked(false);
                }
                Log.d("page", "onPageSelected: " + position);
                prevMenuItem = bottomNavigationView.getMenu().getItem(position);
                prevMenuItem.setChecked(true);
                viewPagerAdapter.getItemPositioin(viewPagerAdapter.getItem(position));
            }
            @Override
            public void onPageScrollStateChanged(int state) {
            }
        });
    }
    private void setupViewPager(ViewPager viewPager) {
        selectedFragment = new WiFiListFragment();
        viewPagerAdapter.addFragment(selectedFragment);
        selectedFragment = new SharingListFragment();
        viewPagerAdapter.addFragment(selectedFragment);
        selectedFragment = new WalletFragment();
        viewPagerAdapter.addFragment(selectedFragment);
        viewPager.setAdapter(viewPagerAdapter);
    }



}
