package com.dohyun.calllogcalendar;

import android.os.Build;
import android.transition.TransitionManager;
import android.view.View;
import android.widget.CheckBox;

import androidx.databinding.ObservableBoolean;
import androidx.lifecycle.ViewModel;

import com.prolificinteractive.materialcalendarview.CalendarMode;
import com.prolificinteractive.materialcalendarview.MaterialCalendarView;

public class MainViewModel extends ViewModel {
    public void onClickWeekModeCheckBox(View view, MaterialCalendarView materialCalendarView){
        if(((CheckBox)view).isChecked()){
            materialCalendarView.state().edit().setCalendarDisplayMode(CalendarMode.WEEKS).commit();
        }
        else{
            materialCalendarView.state().edit().setCalendarDisplayMode(CalendarMode.MONTHS).commit();
        }
    }
}
