package airobotics.asia.fragment;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.preference.Preference;
import androidx.preference.PreferenceFragmentCompat;
import androidx.preference.PreferenceManager;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;

import java.util.HashMap;
import java.util.Map;

import javax.annotation.Nullable;

import airobotics.asia.R;


public class PrefsFragment extends PreferenceFragmentCompat {

    private static final String TAG = "PrefsFragment";

    SharedPreferences preferences;
    String userNameStr;
    String userPhoneString;
    String usernEmrPhoneString;
    String usernEmrMessageString;
    boolean locationServices, bluetoothServices;
    Map<String,Object>preferenceMap= new HashMap<>();

    //Firebase
    FirebaseAuth mAuth;
    FirebaseFirestore database;

    //
    Preference userNamePre,userPhonePre, userEmrPhonePre,userEmrMsgPre, locationSwitch, bluetoothSwitch;

    @Override
    public void onCreatePreferences(Bundle savedInstanceState, String rootKey) {
        setPreferencesFromResource(R.xml.pref_main,rootKey);

        preferences = PreferenceManager.getDefaultSharedPreferences(getActivity());

        userNamePre = findPreference(getString(R.string.user_name_pre));
        userPhonePre = findPreference(getString(R.string.user_phone_pre));
        userEmrPhonePre = findPreference(getString(R.string.user_emr_phone_pre));
        userEmrMsgPre = findPreference(getString(R.string.user_emr_msg_pre));
        locationSwitch = findPreference(getString(R.string.location_switch_pre));
        bluetoothSwitch = findPreference(getString(R.string.bluetooth_switch_pre));

        mAuth = FirebaseAuth.getInstance();
        database = FirebaseFirestore.getInstance();

        userNamePre.setOnPreferenceChangeListener(new Preference.OnPreferenceChangeListener() {
            @Override
            public boolean onPreferenceChange(Preference preference, Object newValue) {
                userNameStr =newValue.toString();
                updateDatabaseSpecificValue(userNameStr,"name");
                return true;
            }
        });

        userPhonePre.setOnPreferenceChangeListener(new Preference.OnPreferenceChangeListener() {
            @Override
            public boolean onPreferenceChange(Preference preference, Object newValue) {
                userPhoneString =newValue.toString();
                updateDatabaseSpecificValue(userPhoneString,"phone");
                return true;
            }
        });

        userEmrPhonePre.setOnPreferenceChangeListener(new Preference.OnPreferenceChangeListener() {
            @Override
            public boolean onPreferenceChange(Preference preference, Object newValue) {
                usernEmrPhoneString =newValue.toString();
                updateDatabaseSpecificValue(usernEmrPhoneString,"emr_phone");
                return true;
            }
        });

        userEmrMsgPre.setOnPreferenceChangeListener(new Preference.OnPreferenceChangeListener() {
            @Override
            public boolean onPreferenceChange(Preference preference, Object newValue) {
                usernEmrMessageString =newValue.toString();
                updateDatabaseSpecificValue(usernEmrMessageString,"emr_msg");
                return true;
            }
        });

        locationSwitch.setOnPreferenceChangeListener(new Preference.OnPreferenceChangeListener() {
            @Override
            public boolean onPreferenceChange(Preference preference, Object newValue) {
                String newVa =String.valueOf(newValue);
                if (newVa.equals("true"))
                {
                    ProfileFragment.changeSwitchValue(newVa,"location");

                }
                updateDatabaseSpecificValue(newVa,"location_switch");
                return true;
            }
        });
        bluetoothSwitch.setOnPreferenceChangeListener(new Preference.OnPreferenceChangeListener() {
            @Override
            public boolean onPreferenceChange(Preference preference, Object newValue) {
                String newVa =String.valueOf(newValue);
                if (newVa.equals("true"))
                {
                    ProfileFragment.changeSwitchValue(newVa,"bluetooth");

                }
                updateDatabaseSpecificValue(newVa,"bluetooth_switch");
                return true;
            }
        });



    }


    private void updateDatabaseSpecificValue(String newValue, final String fieldName){

        if (newValue != null){

            database.collection("users").document(mAuth.getCurrentUser().getUid())
                    .update(fieldName,newValue)
                    .addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            Toast.makeText(getActivity(), " "+fieldName+" updated Successfully ", Toast.LENGTH_SHORT).show();
                        }
                    });
        }

    }

}
