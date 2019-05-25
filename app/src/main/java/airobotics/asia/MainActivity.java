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

public class MainActivity extends AppCompatActivity {
    private static final String TAG = "MainActivity";

    //
    Toolbar toolbar;
    EditText emailEditText, passwordEditText;
    CheckBox checkBox;
    ImageButton signinButton;
    Button signupButton;
    //


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //initialize view components with XML files
        toolbar = findViewById(R.id.toolbar);
        emailEditText = findViewById(R.id.email);
        passwordEditText = findViewById(R.id.password);
        signinButton = findViewById(R.id.signin);
        signupButton = findViewById(R.id.signup);
        checkBox = findViewById(R.id.checkbox);

        //
        signinButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //if sign in button pressed ..
                Log.d(TAG, "onClick: sign-in button");
                Toast.makeText(MainActivity.this, "Sign in Button Pressed!", Toast.LENGTH_SHORT).show();
            }
        });

        //
        signupButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //sign up button pressed
                Log.d(TAG, "onClick: sign-up button");
               // Toast.makeText(MainActivity.this, "Sign Up Button Pressed!", Toast.LENGTH_SHORT).show();
                //we are going to next activity so we need a intent
                

            }
        });
    }
}
