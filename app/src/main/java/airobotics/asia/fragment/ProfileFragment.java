package airobotics.asia.fragment;


import android.content.Intent;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;

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


    public ProfileFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view =inflater.inflate(R.layout.fragment_profile, container, false);
        Log.d(TAG, "onCreateView: profile fragment created ");

        imageView = view.findViewById(R.id.imageView);
        titleTextView = view.findViewById(R.id.current_location_tv_id);
        latTitleTextView = view.findViewById(R.id.lat_tv);
        longTitleTextView = view.findViewById(R.id.long_tv);
        latValueTextView = view.findViewById(R.id.lat_textView_id);
        longValueTextView = view.findViewById(R.id.longi_textView_id);
        signOutButton = view.findViewById(R.id.sign_out_btn);

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

}
