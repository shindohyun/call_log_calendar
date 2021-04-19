package com.dohyun.calllogcalendar;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.pm.PackageManager;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import static android.Manifest.permission.READ_CALL_LOG;

public class MainActivity extends AppCompatActivity {
    private static final String TAG = "MainActivity";
    private static final int PERMISSION_REQUEST_CODE = 1;
    private static final String[] PERMISSIONS = {
            READ_CALL_LOG
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        if(!checkPermission()){
            requestPermissions(PERMISSIONS, PERMISSION_REQUEST_CODE);
        }

        MyApplication app = (MyApplication)getApplication();
        app.callLogManager.loadData(this);
    }

    private boolean checkPermission() {
        boolean result = true;

        for(String permission : PERMISSIONS) {
            if (checkSelfPermission(permission) == PackageManager.PERMISSION_DENIED) {
                Log.i(TAG, "check permission denied: " + permission);
                result = false;
            }
        }

        return result;
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        switch (requestCode){
            case PERMISSION_REQUEST_CODE:
                boolean result = true;
                for(int i = 0; i < permissions.length; i++){
                    if(grantResults[i] == PackageManager.PERMISSION_DENIED){
                        Log.i(TAG, "request permission denied: " + permissions[i]);
                        result = false;
                    }
                }

                if(result){
                    //TODO: Start
                }
                else{
                    Toast.makeText(getApplicationContext(), "앱을 종료합니다.", Toast.LENGTH_LONG).show();
                    finishAffinity();
                }
                break;
            default:
                super.onRequestPermissionsResult(requestCode, permissions, grantResults);
                break;
        }
    }
}