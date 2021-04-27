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

        //MainViewModel viewModel = new ViewModelProvider(this).get(MainViewModel.class);
        // TODO: AndroidViewModel을 상속 받은 클래스의 생성 방법, 정리하기
        MainViewModel viewModel = new ViewModelProvider(
                this,
                new ViewModelProvider.AndroidViewModelFactory(getApplication())
        ).get(MainViewModel.class);

        Presenter presenter = new Presenter() {
            @Override
            public void onClickRefreshButton() {
                resetData();
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

        resetData();
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
                    loadData();
                }
                else{
                    Toast.makeText(getApplicationContext(), "통화 기록에 액세스할 수 없습니다.", Toast.LENGTH_SHORT).show();
                }
                break;
            default:
                super.onRequestPermissionsResult(requestCode, permissions, grantResults);
                break;
        }
    }

    private void resetData(){
        if(!checkPermission()){
            requestPermissions(PERMISSIONS, PERMISSION_REQUEST_CODE);
        }

        loadData();
    }

    private void loadData(){
        binding.getViewModel().reqCallLogData(this, true, new ExecutorCallback<Boolean>() {
            @Override
            public void onComplete(ExecutorResult<Boolean> result) {
                if(result instanceof ExecutorResult.Success){

                }
                else if(result instanceof  ExecutorResult.Error){
                    Log.e(TAG, ((ExecutorResult.Error)result).exception.getMessage());
                    Toast.makeText(getApplicationContext(), "통화 기록을 가져올 수 없습니다.", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    public interface Presenter{
        void onClickRefreshButton();
        void onClickWeekModeCheckBox(View view, MaterialCalendarView materialCalendarView);
    }
}