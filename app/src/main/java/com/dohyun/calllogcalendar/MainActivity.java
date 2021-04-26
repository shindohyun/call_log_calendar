package com.dohyun.calllogcalendar;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;

import android.content.pm.PackageManager;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.CheckBox;
import android.widget.Toast;

import com.dohyun.calllogcalendar.databinding.ActivityMainBinding;
import com.prolificinteractive.materialcalendarview.CalendarMode;
import com.prolificinteractive.materialcalendarview.MaterialCalendarView;

import static android.Manifest.permission.READ_CALL_LOG;

public class MainActivity extends AppCompatActivity {
    private static final String TAG = "MainActivity";
    private static final int PERMISSION_REQUEST_CODE = 1;
    private static final String[] PERMISSIONS = {
            READ_CALL_LOG
    };

    private ActivityMainBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        MainViewModel viewModel = new ViewModelProvider(this).get(MainViewModel.class);
        Presenter presenter = new Presenter() {
            @Override
            public void onClickRefreshButton() {
                if(!checkPermission()){
                    requestPermissions(PERMISSIONS, PERMISSION_REQUEST_CODE);
                }
                // TODO: load call log data
            }

            @Override
            public void onClickWeekModeCheckBox(View view, MaterialCalendarView materialCalendarView) {
                if(((CheckBox)view).isChecked()){
                    materialCalendarView.state().edit().setCalendarDisplayMode(CalendarMode.WEEKS).commit();
                }
                else{
                    materialCalendarView.state().edit().setCalendarDisplayMode(CalendarMode.MONTHS).commit();
                }
            }
        };

        binding.setViewModel(viewModel);
        binding.setPresenter(presenter);

        // setting calendar
        binding.calendarView.setShowOtherDates(MaterialCalendarView.SHOW_OTHER_MONTHS);
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
                    // TODO: load call log data
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

    public interface Presenter{
        void onClickRefreshButton();
        void onClickWeekModeCheckBox(View view, MaterialCalendarView materialCalendarView);
    }
}