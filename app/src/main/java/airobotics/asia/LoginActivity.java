package airobotics.asia;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class LoginActivity extends AppCompatActivity {
    private static final String TAG = "LoginActivity";

    //
    Toolbar toolbar;
    EditText emailEditText, passwordEditText;
    CheckBox checkBox;
    ImageButton signinButton;
    Button signupButton;

    //Firebase instance
    private FirebaseAuth mAuth;
    //


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        //initialize view components with XML files
        toolbar = findViewById(R.id.toolbar);
        emailEditText = findViewById(R.id.email);
        passwordEditText = findViewById(R.id.password);
        signinButton = findViewById(R.id.signin_btn);
        signupButton = findViewById(R.id.signup_btn);
        checkBox = findViewById(R.id.checkbox);
        
        //initialize firebaseAuth
        mAuth = FirebaseAuth.getInstance();

        //
        signinButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //if sign in button pressed ..
                Log.d(TAG, "onClick: sign-in button");
                Toast.makeText(LoginActivity.this, "Sign in Button Pressed!", Toast.LENGTH_SHORT).show();
                Intent goNextIntent = new Intent(LoginActivity.this, MainActivity.class);
                startActivity(goNextIntent);
                
            }
        });

        //
        signupButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //sign up button pressed
                Log.d(TAG, "onClick: sign-up button");
               // Toast.makeText(LoginActivity.this, "Sign Up Button Pressed!", Toast.LENGTH_SHORT).show();
                //we are going to next activity so we need a intent
                Intent goNextIntent = new Intent(LoginActivity.this, SignUpActivity.class);
                startActivity(goNextIntent);


            }
        });
    }

    @Override
    protected void onStart() {
        super.onStart();
        // Check if user is signed in (non-null) and update UI accordingly.
        FirebaseUser currentUser = mAuth.getCurrentUser();
        updateUI(currentUser);
    }

    private void updateUI(FirebaseUser currentUser) {

        if (currentUser!=null){
            //user already sign in so we pass intent to mainActivity
            Intent goMainIntent = new Intent(LoginActivity.this, MainActivity.class);
            startActivity(goMainIntent);
            finish();
        }
        // else user not sign in so stay on this activity
    }
}
