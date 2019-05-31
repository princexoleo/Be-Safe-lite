package airobotics.asia;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;

import com.nabinbhandari.android.permissions.PermissionHandler;
import com.nabinbhandari.android.permissions.Permissions;

import java.util.ArrayList;

public class WalkthroughActivity extends AppCompatActivity {

    Button nextBtn,skipBtn;

    private String [] permission ={Manifest.permission.SEND_SMS,Manifest.permission.ACCESS_FINE_LOCATION,Manifest.permission.ACCESS_COARSE_LOCATION};
    String rationale = "Please provide location permission so that you can access";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_walkthrough);

        nextBtn=findViewById(R.id.next_btn_id);
        skipBtn=findViewById(R.id.skip_btn_id);

        Permissions.Options options=new Permissions.Options()
                .setRationaleDialogTitle("info")
                .setSettingsDialogTitle("warning");



        Permissions.check(WalkthroughActivity.this, permission, rationale, options, new PermissionHandler() {
            @Override
            public void onGranted() {
                nextBtn.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent goLogin=new Intent(WalkthroughActivity.this,LoginActivity.class);
                        startActivity(goLogin);
                        finish();
                    }
                });

            }
        });

    }
}
