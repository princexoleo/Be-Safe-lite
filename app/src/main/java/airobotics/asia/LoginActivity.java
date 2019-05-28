package airobotics.asia;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
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
        signupButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //if sign in button pressed ..
                Log.d(TAG, "onClick: sign-up button");
                // Toast.makeText(LoginActivity.this, "Sign Up Button Pressed!", Toast.LENGTH_SHORT).show();
                //we are going to next activity so we need a intent
                Intent goNextIntent = new Intent(LoginActivity.this, SignUpActivity.class);
                startActivity(goNextIntent);

            }
        });

        //
        signinButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //sign up button pressed
                Log.d(TAG, "onClick: sign-in button");

                //collect user given email and password String from EditText
                String given_email = emailEditText.getText().toString();
                String given_password = passwordEditText.getText().toString();

                Log.i(TAG, "loginInfo: email: "+given_email+" password: "+given_password);

                if (!TextUtils.isEmpty(given_email) && !TextUtils.isEmpty(given_password)){

                    mAuth.signInWithEmailAndPassword(given_email, given_password)
                            .addOnCompleteListener(LoginActivity.this, new OnCompleteListener<AuthResult>() {
                                @Override
                                public void onComplete(@NonNull Task<AuthResult> task) {

                                    if (task.isSuccessful()) {
                                        // Sign in success, update UI with the signed-in user's information
                                        Log.d(TAG, "signInWithEmail: success");
                                        // updateUi
                                        FirebaseUser user = mAuth.getCurrentUser();
                                        updateUI(user);

                                    } else {
                                        Log.d(TAG, "signInWithEmail: failed! ");
                                        Toast.makeText(LoginActivity.this, "Authentication Failed!", Toast.LENGTH_SHORT).show();
                                        updateUI(null);
                                    }

                                }
                            });

                }else{
                    Toast.makeText(LoginActivity.this, "Empty fields not allowed", Toast.LENGTH_SHORT).show();
                }
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
