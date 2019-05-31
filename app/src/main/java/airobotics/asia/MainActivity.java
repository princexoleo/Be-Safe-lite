package airobotics.asia;

import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager.widget.ViewPager;

import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.appbar.AppBarLayout;
import com.google.android.material.tabs.TabLayout;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;

import javax.annotation.Nullable;

import airobotics.asia.fragment.PrefsFragment;
import airobotics.asia.fragment.ProfileFragment;
import airobotics.asia.fragment.SettingsFragment;
import airobotics.asia.ui.adapter.TabAdapter;

public class MainActivity extends AppCompatActivity {

    private static final String TAG = "MainActivity";

    private TabAdapter adapter;
    private TabLayout tabLayout;
    private ViewPager viewPager;

    private TextView userNameTv;
    private String userName;

    //
    private FirebaseFirestore db;
    private FirebaseAuth mAuth;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //Recive Intent values
       // String documentID =getIntent().getStringExtra("documentID");

        //initialize view with xml
        tabLayout = findViewById(R.id.tabLayout);
        viewPager = findViewById(R.id.view_pager);
        userNameTv =findViewById(R.id.textView2);
        //collect user Name from Database
       mAuth= FirebaseAuth.getInstance();
         db = FirebaseFirestore.getInstance();

        db.collection("users").document(mAuth.getCurrentUser().getUid())
                .get()
                .addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                    @Override
                    public void onSuccess(DocumentSnapshot documentSnapshot) {
                        userName =documentSnapshot.get("name").toString();
                       updateUI(userName);
                    }
                });

        addRealTimeUpdates();

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

    private void updateUI(String userName) {

        userNameTv.setText(userName);
    }
    private void addRealTimeUpdates(){
        DocumentReference docRef = db.collection("users").document(mAuth.getCurrentUser().getUid());

        docRef.addSnapshotListener(new EventListener<DocumentSnapshot>() {
            @Override
            public void onEvent(@Nullable DocumentSnapshot documentSnapshot, @Nullable FirebaseFirestoreException e) {
                if (e!=null){
                    Log.d(TAG, "onEvent: ERROR "+e.getMessage());
                }
                if (documentSnapshot !=null && documentSnapshot.exists()){
                    Log.d(TAG, "onEvent: Current data "+documentSnapshot.getData());
                   updateUI(documentSnapshot.get("name").toString());
                }
            }
        });
    }
}
