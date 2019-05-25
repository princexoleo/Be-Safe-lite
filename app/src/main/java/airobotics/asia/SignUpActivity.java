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

public class SignUpActivity extends AppCompatActivity {
    private static final String TAG = "SignUpActivity";

    //
    Toolbar toolbar;
    EditText emailEditText, passwordEditText, emergencyPhoneEditText;
    CheckBox checkBox;
    ImageButton signupButton;
    Button signinButton;
    //

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);

        //initialize view components with XML files
        toolbar = findViewById(R.id.toolbar);
        emailEditText = findViewById(R.id.email);
        passwordEditText = findViewById(R.id.password);
        emergencyPhoneEditText = findViewById(R.id.emergency_phone);
        signinButton = findViewById(R.id.r_signin_btn);
        signupButton = findViewById(R.id.r_signup_btn);
        checkBox = findViewById(R.id.checkbox);

        //
        signinButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //if sign in button pressed ..
                Log.d(TAG, "onClick: sign-in button");
                //we are going to next activity so we need a intent
                Intent goNextIntent = new Intent(SignUpActivity.this, MainActivity.class);
                startActivity(goNextIntent);
                finish();

            }
        });

        //
        signupButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //sign up button pressed
                Log.d(TAG, "onClick: sign-up button");
                 Toast.makeText(SignUpActivity.this, "Sign Up Button Pressed!", Toast.LENGTH_SHORT).show();

            }
        });
    }
}
