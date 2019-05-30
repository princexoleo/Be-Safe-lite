package airobotics.asia.fragment;


import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import airobotics.asia.LoginActivity;
import airobotics.asia.R;

/**
 * A simple {@link Fragment} subclass.
 */
public class ProfileFragment extends Fragment {
    private static final String TAG = "ProfileFragment";

    private ImageView imageView;
    private TextView titleTextView, latTitleTextView, longTitleTextView, latValueTextView, longValueTextView;
    private Button signOutButton;

    //
    private String longitudeStr =null;
    private String latitudeStr =null;
    private String userEmergencyPhoneNumber =null;

    //Firebase
    private FirebaseFirestore database;
    private DocumentReference documentReference;
    private FirebaseAuth mAuth;
    private FirebaseUser currentUser;


    public ProfileFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view =inflater.inflate(R.layout.fragment_profile, container, false);
        Log.d(TAG, "onCreateView: profile fragment created ");

        initComponents(view);

        //initialize FirebaseFireStore to get Information
        database =FirebaseFirestore.getInstance();
        mAuth =FirebaseAuth.getInstance();
        currentUser = mAuth.getCurrentUser();

        //read data from FirebaseFireStore
        //first we need a DocumentReference
       if(currentUser != null){
           Log.i(TAG, "onCreateView: currentUser :"+currentUser.getUid());
           documentReference = database.collection("users").document(currentUser.getUid());
           documentReference.get()
                   .addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                       @Override
                       public void onComplete(@NonNull Task<DocumentSnapshot> task) {

                           if (task.isSuccessful())
                           {
                               //task successful that's means data found
                               if (task.getResult().exists()){
                                   latitudeStr= task.getResult().get("lat").toString();
                                   longitudeStr = task.getResult().get("lon").toString();
                                   updateUI();
                               }
                           }
                       }
                   });
       }else{
           Log.d(TAG, "onCreateView: currentUser: null");
           latitudeStr =null;
           longitudeStr = null;
           updateUI();
       }



        signOutButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FirebaseAuth.getInstance().signOut();
                Toast.makeText(getActivity(), "Sign out Successfully ", Toast.LENGTH_SHORT).show();
                Intent goLoginActivity = new Intent(getActivity(), LoginActivity.class);
                startActivity(goLoginActivity);

            }
        });

        return view;
    }

    private void initComponents(View view) {

        imageView = view.findViewById(R.id.imageView);
        titleTextView = view.findViewById(R.id.current_location_tv_id);
        latTitleTextView = view.findViewById(R.id.lat_tv);
        longTitleTextView = view.findViewById(R.id.long_tv);
        latValueTextView = view.findViewById(R.id.lat_textView_id);
        longValueTextView = view.findViewById(R.id.longi_textView_id);
        signOutButton = view.findViewById(R.id.sign_out_btn);
    }

    private void updateUI() {

        if (latitudeStr!= null && longitudeStr!= null){
            //set the value to textView
            Log.d(TAG, "updateUI:lat :"+latitudeStr+" lon:"+longitudeStr);
            latValueTextView.setText(latitudeStr);
            longValueTextView.setText(longitudeStr);


        }else{
            Log.d(TAG, "updateUI: null");
            latValueTextView.setText("null");
            longValueTextView.setText("null");
        }
    }

    @Override
    public void onStart() {
        super.onStart();

        mAuth =FirebaseAuth.getInstance();
        currentUser = mAuth.getCurrentUser();

        if (currentUser == null)
        {
            Log.d(TAG, "onStart: currentUser check null");
            //users maybe signOut
            //something is wrong
        }else{
            Log.d(TAG, "onStart: currentUser check "+currentUser.getUid());
        }
    }
}
