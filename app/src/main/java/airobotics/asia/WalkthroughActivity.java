package airobotics.asia;

import android.Manifest;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.nabinbhandari.android.permissions.PermissionHandler;
import com.nabinbhandari.android.permissions.Permissions;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class WalkthroughActivity extends AppCompatActivity {

    Button nextBtn,skipBtn;

    private String [] permission ={Manifest.permission.SEND_SMS,
            Manifest.permission.ACCESS_FINE_LOCATION,
            Manifest.permission.ACCESS_COARSE_LOCATION
    };
    String rationale = "Please provide location permission so that you can access";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_walkthrough);



        if (checkAndRequestPermission()){
            initApp();
        }



    }

    private void initApp() {
        nextBtn=findViewById(R.id.next_btn_id);
        skipBtn=findViewById(R.id.skip_btn_id);
        nextBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent goMain = new Intent(WalkthroughActivity.this, LoginActivity.class);
                startActivity(goMain);
                finish();
            }
        });
    }


    public boolean checkAndRequestPermission(){
        List<String> list = new ArrayList<>();

        for (String prem: permission){
            if (ContextCompat.checkSelfPermission(this,prem) != PackageManager.PERMISSION_GRANTED){
                list.add(prem);
            }
        }

        if (!list.isEmpty()){
            ActivityCompat.requestPermissions(this,
                    list.toArray(new String[list.size()]),
                    100);
            return true;
        }

        return true;
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {

        if (requestCode == 100){
            HashMap<String,Integer> permissionResults = new HashMap<>();
            int deinedCount=0;

            for (int i=0; i<grantResults.length;i++){

                if (grantResults[i] == PackageManager.PERMISSION_DENIED){
                    permissionResults.put(permission[i],grantResults[i]);
                    deinedCount++;
                }
            }

            if (deinedCount ==0){
                initApp();
            }else{

                for (Map.Entry<String, Integer> entry :permissionResults.entrySet())
                {
                    String permName =entry.getKey();
                    int permResult = entry.getValue();


                    if (ActivityCompat.shouldShowRequestPermissionRationale(this,permName))
                    {
                        showDialog("","This app needs Location and SMS permission",
                                "Yes,Grant permission",
                                    new DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(DialogInterface dialog, int which) {
                                            dialog.dismiss();
                                            checkAndRequestPermission();
                                        }
                                    },
                                "No, EXIT APP", new DialogInterface.OnClickListener() {

                                        @Override
                                        public void onClick(DialogInterface dialog, int which)
                                        {
                                            dialog.dismiss();
                                        }
                                    },false );
//                    }else {
//                        showDialog("","You have deined permission.Allow all permission at [Settings]=>[Permission]",
//                                "Go to settings",
//                                new DialogInterface.OnClickListener() {
//                                    @Override
//                                    public void onClick(DialogInterface dialog, int which) {
//                                        dialog.dismiss();
//                                        Intent intent = new Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS,
//                                                Uri.fromParts("package",getPackageName(),null));
//                                        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
//                                        startActivity(intent);
//                                        finish();
//                                    }
//                                },
//                                "No, EXIT APP", new DialogInterface.OnClickListener() {
//
//                                    @Override
//                                    public void onClick(DialogInterface dialog, int which)
//                                    {
//                                        dialog.dismiss();
//                                        finish();
//                                    }
//                                },false );
                       // break;

                    }
                }
            }
        }
    }

    public AlertDialog showDialog(String title, String msg, String positiveLabel,
                                  DialogInterface.OnClickListener positiveOnCLick,
                                  String negativeLabel, DialogInterface.OnClickListener negativeOnCLick,
                                  boolean isCancelAble)
    {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle(title);
        builder.setCancelable(isCancelAble);
        builder.setMessage(msg);
        builder.setPositiveButton(positiveLabel, positiveOnCLick);
        builder.setNegativeButton(negativeLabel, negativeOnCLick);

        AlertDialog alert = builder.create();
        alert.show();
        return alert;
    }
}
