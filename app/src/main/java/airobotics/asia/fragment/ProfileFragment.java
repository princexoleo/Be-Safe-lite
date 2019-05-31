package airobotics.asia.fragment;


import android.Manifest;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
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
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;

import com.nabinbhandari.android.permissions.PermissionHandler;
import com.nabinbhandari.android.permissions.Permissions;

import java.util.ArrayList;

import javax.annotation.Nullable;

import airobotics.asia.BluetoothConnection;
import airobotics.asia.LoginActivity;
import airobotics.asia.R;
import airobotics.asia.model.LocationService;

/**
 * A simple {@link Fragment} subclass.
 */
public class ProfileFragment extends Fragment {
    private static final String TAG = "ProfileFragment";
    private String [] permission ={Manifest.permission.SEND_SMS,Manifest.permission.ACCESS_FINE_LOCATION,Manifest.permission.ACCESS_COARSE_LOCATION};
    String rationale = "Please provide location permission so that you can access";

    private ImageView imageView;
    private TextView titleTextView, latTitleTextView, longTitleTextView, latValueTextView, longValueTextView;
    private Button signOutButton, connectButton;

    //
    private String longitudeStr =null;
    private String latitudeStr =null;
    private String userEmergencyPhoneNumber =null;
    private static String locationSwitchValue="true";
    private  static String bluetoothSwitchValue="true";

    //Firebase
    private FirebaseFirestore database;
    private DocumentReference documentReference;
    private FirebaseAuth mAuth;
    private FirebaseUser currentUser;
    
    //
    private BroadcastReceiver broadcastReceiver;
    private  Context mContext;
     boolean check =false;

    public ProfileFragment(Context mContext) {
        this.mContext = mContext;
    }

    public ProfileFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view =inflater.inflate(R.layout.fragment_profile, container, false);
       // mContext = container.getContext();
        Log.d(TAG, "onCreateView: profile fragment created ");

        initComponents(view);

        //initialize FirebaseFireStore to get Information
        database =FirebaseFirestore.getInstance();
        mAuth =FirebaseAuth.getInstance();
        currentUser = mAuth.getCurrentUser();



         // enable_button();
               if (locationSwitchValue.equals("true")){
                   enable_tracking_location();
               }else{
                   stopTrackingLocation();
               }

        //
        enable_bluetooth_services();
        //

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
                                   locationSwitchValue = task.getResult().get("location_switch").toString();
                                   bluetoothSwitchValue = task.getResult().get("bluetooth_switch").toString();
                                   updateUI();
                                 //  updateLocationUI(latitudeStr,longitudeStr);
                               }
                           }
                       }
                   });

           addRealTimeUpdates();
       }
       else{
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
    
    private void enable_bluetooth_services(){
        Log.d(TAG, "enable_bluetooth_services: Start");
        connectButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent goConnect = new Intent(mContext, BluetoothConnection.class);
                startActivity(goConnect);
            }
        });
        
    }
    
    private void stop_bluetooth_services(){
        Log.d(TAG, "stop_bluetooth_services: Stop");
        
    }

    public static void changeSwitchValue(String value, String name){

        if (name.equals("location")){
            locationSwitchValue=value;
        }else{
            bluetoothSwitchValue=value;
        }

    }

    private  void stopTrackingLocation() {

        Log.d(TAG, "enable_tracking_location: stop ");
        Intent i=new Intent(mContext,LocationService.class);
        mContext.stopService(i);
        Toast.makeText(mContext, "Stop Tracking Location ..", Toast.LENGTH_SHORT).show();

    }

    private  void enable_tracking_location() {
          Log.d(TAG, "enable_tracking_location: start ");
          Intent i=new Intent(mContext,LocationService.class);
           mContext.startService(i);
          Toast.makeText(mContext, "Start Tracking Location ..", Toast.LENGTH_SHORT).show();
    }



    private void addRealTimeUpdates(){
        DocumentReference docRef = database.collection("users").document(currentUser.getUid());

        docRef.addSnapshotListener(new EventListener<DocumentSnapshot>() {
            @Override
            public void onEvent(@Nullable DocumentSnapshot documentSnapshot, @Nullable FirebaseFirestoreException e) {
                if (e!=null){
                    Log.d(TAG, "onEvent: ERROR "+e.getMessage());
                }
                if (documentSnapshot !=null && documentSnapshot.exists()){
                    Log.d(TAG, "onEvent: Current data "+documentSnapshot.getData());
                    latitudeStr = documentSnapshot.get("lat").toString();
                    longitudeStr = documentSnapshot.get("lon").toString();
                    updateLocationUI(latitudeStr,longitudeStr);
                }
            }
        });
    }

    private void updateLocationUI(String latitudeStr, String longitudeStr) {

        latValueTextView.setText(latitudeStr);
        longValueTextView.setText(longitudeStr);
        Log.d(TAG, "updateLocationUI: location textview updated");
    }


    private void initComponents(View view) {

        imageView = view.findViewById(R.id.imageView);
        titleTextView = view.findViewById(R.id.current_location_tv_id);
        latTitleTextView = view.findViewById(R.id.lat_tv);
        longTitleTextView = view.findViewById(R.id.long_tv);
        latValueTextView = view.findViewById(R.id.lat_textView_id);
        longValueTextView = view.findViewById(R.id.longi_textView_id);
        signOutButton = view.findViewById(R.id.sign_out_btn);
        connectButton = view.findViewById(R.id.connect_button);

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
    
    
    //runtimne permission check
//    private boolean runtime_permission() {
//        if (Build.VERSION.SDK_INT >=25 && ContextCompat.checkSelfPermission(mContext,Manifest.permission.ACCESS_FINE_LOCATION) !=PackageManager.PERMISSION_GRANTED &&
//                ContextCompat.checkSelfPermission(mContext,Manifest.permission.ACCESS_COARSE_LOCATION)!=PackageManager.PERMISSION_GRANTED){
//            requestPermissions(new String[]{Manifest.permission.ACCESS_FINE_LOCATION,Manifest.permission.ACCESS_COARSE_LOCATION},100);
//            return  false;
//        }
//        return true;
//
//    }
    
    //
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode==100){

            if (grantResults[0]==PackageManager.PERMISSION_GRANTED &&
                    grantResults[1]==PackageManager.PERMISSION_GRANTED){
                enable_tracking_location();
            }else{
                //runtime_permission();
            }
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

    @Override
    public void onResume() {
        super.onResume();
        Log.d(TAG, "onResume: called");
        if (broadcastReceiver==null)
        {
            broadcastReceiver =new BroadcastReceiver() {
                @Override
                public void onReceive(Context context, Intent intent) {

                    longitudeStr= String.valueOf( intent.getExtras().get("longitude"));
                    latitudeStr=String.valueOf( intent.getExtras().get("latitude"));
                    //set new location to textView
                   updateLocationUI(latitudeStr,longitudeStr);
                    
                    Log.i(TAG, "onReceive: broad "+longitudeStr+"  "+latitudeStr);

                    //updateUserLocation(userLatitude,userLongitude);
                }
            };
        }
        getContext().registerReceiver(broadcastReceiver,new IntentFilter("location_updated"));
    }

    @Override
    public void onDestroy() {
        super.onDestroy();

        if (broadcastReceiver!=null){
            getContext().unregisterReceiver(broadcastReceiver);
        }
    }
    
    
}
