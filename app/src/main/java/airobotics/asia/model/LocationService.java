package airobotics.asia.model;

import android.Manifest;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.os.IBinder;
import androidx.appcompat.app.AppCompatActivity;
import android.util.Log;
import android.widget.Toast;

import androidx.core.app.ActivityCompat;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;


import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;


import airobotics.asia.R;

import static androidx.constraintlayout.widget.Constraints.TAG;

/**
 * Created by 5943 6417 on 14-09-2016.
 */
public class LocationService extends Service {
    private static final String TAG = "LocationService";
    public static final String BROADCAST_ACTION = "Hello World";
    private static final int TWO_MINUTES = 1000 * 60 * 1;
    public LocationManager locationManager;
    public MyLocationListener listener;
    public Location previousBestLocation = null;
    private FirebaseAuth mAuth;
    private FirebaseFirestore firebaseFirestore;

    Context context;

    Intent intent;
    int counter = 0;

    @Override
    public void onCreate() {
        super.onCreate();
        intent = new Intent(BROADCAST_ACTION);
        context = this;
        mAuth= FirebaseAuth.getInstance();
        firebaseFirestore= FirebaseFirestore.getInstance();
    }

    @Override
    public void onStart(Intent intent, int startId) {
        locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        listener = new MyLocationListener();
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return;
        }
        locationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 4000, 0, listener);
        locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 4000, 0, listener);
    }

    @Override
    public IBinder onBind(Intent intent){
        return null;
    }

    protected boolean isBetterLocation(Location location, Location currentBestLocation) {
        if (currentBestLocation == null) {
            // A new location is always better than no location
            return true;
        }

        // Check whether the new location fix is newer or older
        long timeDelta = location.getTime() - currentBestLocation.getTime();
        boolean isSignificantlyNewer = timeDelta > TWO_MINUTES;
        boolean isSignificantlyOlder = timeDelta < -TWO_MINUTES;
        boolean isNewer = timeDelta > 0;

        // If it's been more than two minutes since the current location, use the new location
        // because the user has likely moved
        if (isSignificantlyNewer) {
            return true;
            // If the new location is more than two minutes older, it must be worse
        } else if (isSignificantlyOlder) {
            return false;
        }

        // Check whether the new location fix is more or less accurate
        int accuracyDelta = (int) (location.getAccuracy() - currentBestLocation.getAccuracy());
        boolean isLessAccurate = accuracyDelta > 0;
        boolean isMoreAccurate = accuracyDelta < 0;
        boolean isSignificantlyLessAccurate = accuracyDelta > 200;

        // Check if the old and new location are from the same provider
        boolean isFromSameProvider = isSameProvider(location.getProvider(),
                currentBestLocation.getProvider());

        // Determine location quality using a combination of timeliness and accuracy
        if (isMoreAccurate) {
            return true;
        } else if (isNewer && !isLessAccurate) {
            return true;
        } else if (isNewer && !isSignificantlyLessAccurate && isFromSameProvider) {
            return true;
        }
        return false;
    }



    /** Checks whether two providers are the same */
    private boolean isSameProvider(String provider1, String provider2) {
        if (provider1 == null) {
            return provider2 == null;
        }
        return provider1.equals(provider2);
    }



    @Override
    public void onDestroy() {
        // handler.removeCallbacks(sendUpdatesToUI);
        super.onDestroy();
        Log.v("STOP_SERVICE", "DONE");
        locationManager.removeUpdates(listener);
    }

    public static Thread performOnBackgroundThread(final Runnable runnable) {
        final Thread t = new Thread() {
            @Override
            public void run() {
                try {
                    runnable.run();
                } finally {

                }
            }
        };
        t.start();
        return t;
    }




    public class MyLocationListener implements LocationListener{


//        private String userLocality = "";
//        private String userCity = "";
//        private String userCountryName = "";
//        private String userPostalCode = "";
//        private String userCountryCode = "";

        private String currentUser=mAuth.getCurrentUser().getUid();


        public void onLocationChanged(final Location loc)
        {
            Log.i("**********", "Location changed");
            if(isBetterLocation(loc, previousBestLocation)) {
                loc.getLatitude();
                loc.getLongitude();
                Log.d(TAG, "onLocationChanged: "+loc.getLongitude()+" lati"+loc.getLatitude()+" provi"+loc.getProvider());
                //Toast.makeText(context, "Latitude" + loc.getLatitude() + "\nLongitude"+loc.getLongitude(),Toast.LENGTH_SHORT).show();
                intent.putExtra("Latitude", loc.getLatitude());
                intent.putExtra("Longitude", loc.getLongitude());
                intent.putExtra("Provider", loc.getProvider());
                sendBroadcast(intent);


                final Double lat = loc.getLatitude();
                final Double longi = loc.getLongitude();
               // userLocality = getCityName(loc);
             //   AccountFragment.setUserCityNameTv(userLocality);

                String emr_messsage="https://www.google.com/maps/search/?api=1&amp;query="+lat+","+longi;
               // user_emergency_message = emr_messsage;
               // User.setEmr_message(emr_messsage);

                Map<String, Object> locationMap = new HashMap<>();
                locationMap.put("lat", String.valueOf(lat));
                locationMap.put("lon", String.valueOf(longi));
//                locationMap.put("city", userCity);
//                locationMap.put("locality", userLocality);
//                locationMap.put("postal_code", userPostalCode);
//                locationMap.put("country_name", userCountryName);
//                locationMap.put("country_code", userCountryCode);
                locationMap.put("emr_message",emr_messsage);
                firebaseFirestore.collection("users").document(currentUser).update(locationMap)
                        .addOnSuccessListener(new OnSuccessListener<Void>() {
                            @Override
                            public void onSuccess(Void aVoid) {
                              //  AccountFragment.updateUserLocation(String.valueOf(lat), String.valueOf(longi));
                                Log.d(TAG, "locationUpdated: success");
                            }
                        });


            }
        }

        public void onProviderDisabled(String provider)
        {
            Toast.makeText( getApplicationContext(), "Location Disabled", Toast.LENGTH_SHORT ).show();
        }


        public void onProviderEnabled(String provider)
        {
            Toast.makeText( getApplicationContext(), "Location Enabled", Toast.LENGTH_SHORT).show();
        }


        public void onStatusChanged(String provider, int status, Bundle extras)
        {

        }

//        private String getCityName(Location myLocation) {
//            String myCity="";
//            Geocoder geocoder=new Geocoder(getApplicationContext(), Locale.getDefault());
//            try{
//                List<Address> addresses = geocoder.getFromLocation(myLocation.getLatitude(),myLocation.getLongitude(),1);
//                try {
//                    String address=addresses.get(0).getAddressLine(0);
//                }
//                catch (IndexOutOfBoundsException e)
//                {
//                    e.printStackTrace();
//                    // Toast.makeText(this, " Array out of memory "+e.getMessage(), Toast.LENGTH_SHORT).show();
//                }
//                if (addresses !=null)
//                {
//                    try {
//                        myCity=addresses.get(0).getLocality();
//                        userCountryName=addresses.get(0).getCountryName();
//                        userCity=addresses.get(0).getLocality();
//                        userPostalCode=addresses.get(0).getPostalCode();
//                        userCountryCode=addresses.get(0).getCountryCode();
//                    }catch (IndexOutOfBoundsException e)
//                    {
//                        e.printStackTrace();
//                        // Toast.makeText(this, " Array out of memory "+e.getMessage(), Toast.LENGTH_SHORT).show();
//                    }
//                }else {
//                    myCity="nothing Found!";
//                }
//            }catch (IOException e){
//                e.printStackTrace();
//            }
//            return myCity;
//        }

    }
}