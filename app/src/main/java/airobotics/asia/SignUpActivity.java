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
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;

public class SignUpActivity extends AppCompatActivity {
    private static final String TAG = "SignUpActivity";

    //
    Toolbar toolbar;
    EditText emailEditText, passwordEditText, emergencyPhoneEditText, nameEditText;
    CheckBox checkBox;
    ImageButton signupButton;
    Button signinButton;
    //
    private Map<String,Object> userInfoMap = new HashMap<>();
    //
    FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);

        //initialize view components with XML files
        toolbar = findViewById(R.id.toolbar);
        emailEditText = findViewById(R.id.email);
        nameEditText = findViewById(R.id.r_name);
        passwordEditText = findViewById(R.id.password);
        emergencyPhoneEditText = findViewById(R.id.emergency_phone);
        signinButton = findViewById(R.id.r_signin_btn);
        signupButton = findViewById(R.id.r_signup_btn);
        checkBox = findViewById(R.id.checkbox);

        //
        mAuth = FirebaseAuth.getInstance();

        //
        signinButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //if sign in button pressed ..
                Log.d(TAG, "onClick: sign-in button");
                //we are going to next activity so we need a intent
                Intent goNextIntent = new Intent(SignUpActivity.this, LoginActivity.class);
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
                // Toast.makeText(SignUpActivity.this, "Sign Up Button Pressed!", Toast.LENGTH_SHORT).show();

                //collect user information from EditText
                String reg_email = emailEditText.getText().toString();
                String reg_name = nameEditText.getText().toString();
                String reg_password = passwordEditText.getText().toString();
                String reg_emergency_phone_number = emergencyPhoneEditText.getText().toString();


                //now chcek any box empty or not
                if( !TextUtils.isEmpty(reg_name) &&  !TextUtils.isEmpty(reg_email) &&
                        !TextUtils.isEmpty(reg_password) && !TextUtils.isEmpty(reg_emergency_phone_number) )
                {
                    //everything is are okay
                    //new users information store in Map
                    userInfoMap.put("name",reg_name);
                    userInfoMap.put("emr_phone",reg_emergency_phone_number);
                    userInfoMap.put("location_switch","true");
                    userInfoMap.put("bluetooth_switch","true");
                    userInfoMap.put("lat","90.00");
                    userInfoMap.put("lon","89.25");
                    //
                    createAccount(reg_email,reg_password);

                }else{
                    //empty fields
                    Toast.makeText(SignUpActivity.this, "Empty fields are not allowed!", Toast.LENGTH_SHORT).show();
                }

            }
        });
    }

    private void createAccount(String reg_email, String reg_password) {

        mAuth.createUserWithEmailAndPassword(reg_email,reg_password)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {

                        if (task.isSuccessful()){
                            Log.d(TAG, "createUser: success");
                            FirebaseUser user = mAuth.getCurrentUser();
                            updateUI(user);
                        }else{
                            Log.d(TAG, "createUser: failed ");
                            Toast.makeText(SignUpActivity.this, "Authentication failed!", Toast.LENGTH_SHORT).show();
                            updateUI(null);
                        }

                    }
                });
    }

    private void updateUI(FirebaseUser user) {

        //check user
        if (user !=null){
            //account created successfully
            //now we have to store user name and emergency phoneNumber in cloudStore
            //Initialize FirebaseFirestore
            FirebaseFirestore database = FirebaseFirestore.getInstance();

            //add map information to database with generated ID

            database.collection("users").document(user.getUid())
                    .set(userInfoMap)
                    .addOnSuccessListener(new OnSuccessListener<Void>() {
                        @Override
                        public void onSuccess(Void aVoid) {
                            Log.d(TAG, "userInfo added with current Users UID : ");
                            //information added successfully
                            //now take users to next activity
                            Intent goNextActivity = new Intent(SignUpActivity.this,MainActivity.class);
                            startActivity(goNextActivity);
                            finish();
                        }
                    });

        }else{
            // acount created fail so make a snackBar to show message Try again
            //or something is wrong
        }
    }
}
