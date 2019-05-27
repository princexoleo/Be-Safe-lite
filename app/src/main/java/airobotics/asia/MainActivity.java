package airobotics.asia;

import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager.widget.ViewPager;

import android.os.Bundle;

import com.google.android.material.appbar.AppBarLayout;
import com.google.android.material.tabs.TabLayout;

import airobotics.asia.fragment.ProfileFragment;
import airobotics.asia.fragment.SettingsFragment;
import airobotics.asia.ui.adapter.TabAdapter;

public class MainActivity extends AppCompatActivity {

    private static final String TAG = "MainActivity";

    private TabAdapter adapter;
    private TabLayout tabLayout;
    private ViewPager viewPager;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //initialize view with xml
        tabLayout = findViewById(R.id.tabLayout);
        viewPager = findViewById(R.id.view_pager);

        adapter = new TabAdapter(getSupportFragmentManager());
        adapter.addFragment(new ProfileFragment(),"Profile");
        adapter.addFragment(new SettingsFragment(),"Settings");

        viewPager.setAdapter(adapter);
        tabLayout.setupWithViewPager(viewPager);
    }
}
