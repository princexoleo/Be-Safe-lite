package airobotics.asia;

import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager.widget.ViewPager;

import android.os.Bundle;

import com.google.android.material.appbar.AppBarLayout;
import com.google.android.material.tabs.TabLayout;

import airobotics.asia.fragment.PrefsFragment;
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

        //Recive Intent values
       // String documentID =getIntent().getStringExtra("documentID");

        //initialize view with xml
        tabLayout = findViewById(R.id.tabLayout);
        viewPager = findViewById(R.id.view_pager);

        adapter = new TabAdapter(getSupportFragmentManager());
        // create Fragment Object
        ProfileFragment profileFragment = new ProfileFragment(MainActivity.this);
        // create a bundle to pass data
       // Bundle bundle = new Bundle();
      //  bundle.putString("bundleID",documentID);
        //pass bundle
        //profileFragment.setArguments(bundle);
        //now pass the  ProfileFragments object
        adapter.addFragment(profileFragment,"Profile");
        adapter.addFragment(new PrefsFragment(),"Settings");

        viewPager.setAdapter(adapter);
        tabLayout.setupWithViewPager(viewPager);
    }
}
