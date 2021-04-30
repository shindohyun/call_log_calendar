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
import com.prolificinteractive.materialcalendarview.CalendarDay;
import com.prolificinteractive.materialcalendarview.CalendarMode;
import com.prolificinteractive.materialcalendarview.MaterialCalendarView;
import com.prolificinteractive.materialcalendarview.OnDateSelectedListener;
import com.prolificinteractive.materialcalendarview.OnMonthChangedListener;

import java.util.Calendar;
import java.util.Date;

import static android.Manifest.permission.READ_CALL_LOG;
import static com.prolificinteractive.materialcalendarview.MaterialCalendarView.SELECTION_MODE_NONE;
import static com.prolificinteractive.materialcalendarview.MaterialCalendarView.SELECTION_MODE_SINGLE;

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

        MainViewModel viewModel = new ViewModelProvider(
                this,
                new ViewModelProvider.AndroidViewModelFactory(getApplication())
        ).get(MainViewModel.class);

        Presenter presenter = new Presenter() {
            @Override
            public void onClickResetButton(Presenter presenter, CheckBox checkBoxDayMode, CheckBox checkBoxWeekMode, MaterialCalendarView materialCalendarView) {
                if(checkBoxDayMode.isChecked()){
                    checkBoxDayMode.setChecked(false);
                    presenter.onClickDayModeCheckBox(checkBoxDayMode);
                }


                if(checkBoxWeekMode.isChecked()){
                    checkBoxWeekMode.setChecked(false);
                    presenter.onClickWeekModeCheckBox(checkBoxWeekMode, materialCalendarView);
                }
            }

            @Override
            public void onClickLoadButton() {
                loadDataWithPermissionCheck();
            }

            @Override
            public void onClickDayModeCheckBox(View view) {
                if(((CheckBox)view).isChecked()){
                    binding.calendarView.setSelectionMode(SELECTION_MODE_SINGLE);
                }
                else{
                    binding.calendarView.setSelectionMode(SELECTION_MODE_NONE);
                    loadData(false);
                }
            }

            @Override
            public void onClickWeekModeCheckBox(View view, MaterialCalendarView materialCalendarView) {
                if(((CheckBox)view).isChecked()){
                    materialCalendarView.state().edit().setCalendarDisplayMode(CalendarMode.WEEKS).commit();
                }
                else{
                    materialCalendarView.state().edit().setCalendarDisplayMode(CalendarMode.MONTHS).commit();
                }

                loadData(false);
            }
        };

        binding.setViewModel(viewModel);
        binding.setPresenter(presenter);

        // setting calendar
        binding.calendarView.setSelectionMode(SELECTION_MODE_NONE);
        binding.calendarView.setOnDateChangedListener(new OnDateSelectedListener() {
            @Override
            public void onDateSelected(@NonNull MaterialCalendarView widget, @NonNull CalendarDay date, boolean selected) {
                loadData(false);
            }
        });
        binding.calendarView.setOnMonthChangedListener(new OnMonthChangedListener() {
            @Override
            public void onMonthChanged(MaterialCalendarView widget, CalendarDay date) {
                loadData(false);
            }
        });

        loadDataWithPermissionCheck();
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
                    loadData(true);
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

    private void loadDataWithPermissionCheck(){
        if(!checkPermission()){
            requestPermissions(PERMISSIONS, PERMISSION_REQUEST_CODE);
        }

        loadData(true);
    }

    private void loadData(boolean load){
        Date startDate = null;
        Date endDate = null;

        if(binding.checkBoxDayMode.isChecked() && binding.calendarView.getSelectedDate() != null){
            startDate = binding.calendarView.getSelectedDate().getDate();
            endDate = startDate;
        }
        else{
            if(binding.checkBoxWeekMode.isChecked()){
                startDate = binding.calendarView.getCurrentDate().getDate();

                Calendar cal = Calendar.getInstance();
                cal.setTime(startDate);
                cal.add(Calendar.DATE, 6);

                endDate = new Date(cal.getTimeInMillis());
            }
            else{
                startDate = binding.calendarView.getCurrentDate().getDate();

                Calendar cal = Calendar.getInstance();
                cal.setTime(startDate);
                cal.add(Calendar.MONTH, 1);
                cal.add(Calendar.DATE, -1);

                endDate = new Date(cal.getTimeInMillis());
            }
        }

        Log.d(TAG, startDate.toString() + " ~ " + endDate.toString());

        binding.getViewModel().reqCallLogData(this, load, startDate, endDate, new ExecutorCallback<Boolean>() {
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
        void onClickResetButton(Presenter presenter, CheckBox checkBoxDayMode, CheckBox checkBoxWeekMode, MaterialCalendarView materialCalendarView);
        void onClickLoadButton();
        void onClickDayModeCheckBox(View view);
        void onClickWeekModeCheckBox(View view, MaterialCalendarView materialCalendarView);
    }
}