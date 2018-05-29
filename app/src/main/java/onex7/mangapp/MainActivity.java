package onex7.mangapp;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;
import android.widget.TextView;

import onex7.mangapp.Fragment.AnalisisFragment;
import onex7.mangapp.Fragment.SensorFragment;

public class MainActivity extends AppCompatActivity {

    Fragment currentFragment = null;
    FragmentTransaction ft;

    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
            = new BottomNavigationView.OnNavigationItemSelectedListener() {

        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            switch (item.getItemId()) {
                case R.id.navigation_home:
                    currentFragment = new SensorFragment();
                    ft = getSupportFragmentManager().beginTransaction();
                    ft.replace(R.id.frame_layout, currentFragment);
                    ft.commit();
                    return true;
                case R.id.navigation_dashboard:
                    currentFragment = new AnalisisFragment();
                    ft = getSupportFragmentManager().beginTransaction();
                    ft.replace(R.id.frame_layout, currentFragment);
                    ft.commit();
                    return true;
            }
            return false;
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        currentFragment = new SensorFragment();
        ft = getSupportFragmentManager().beginTransaction();
        ft.replace(R.id.frame_layout, currentFragment);
        ft.commit();

        BottomNavigationView navigation = findViewById(R.id.navigation);
        navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);
    }

}
