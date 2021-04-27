package com.dohyun.calllogcalendar;

import android.content.ContextWrapper;
import android.database.Cursor;
import android.provider.CallLog;

import java.util.ArrayList;
import java.util.Date;

public class CallLogManager {
    private ArrayList<CallLogData> callLogDataList;

    public CallLogManager() {
        callLogDataList = new ArrayList<>();
    }

    public ArrayList<CallLogData> getCallLogDataList() {
        return callLogDataList;
    }

    public void loadData(ContextWrapper contextWrapper){
        callLogDataList.clear();

        Cursor cursor = contextWrapper.getContentResolver().query(CallLog.Calls.CONTENT_URI, null, null, null, null);

        int numberIndex = cursor.getColumnIndex(CallLog.Calls.NUMBER);
        int typeIndex = cursor.getColumnIndex(CallLog.Calls.TYPE);
        int dateIndex = cursor.getColumnIndex(CallLog.Calls.DATE);
        int durationIndex = cursor.getColumnIndex(CallLog.Calls.DURATION);

        while (cursor.moveToNext()) {
            String number = cursor.getString(numberIndex);
            int type = Integer.parseInt(cursor.getString(typeIndex));
            Date date = new Date(Long.valueOf(cursor.getString(dateIndex)));
            int duration = Integer.parseInt(cursor.getString(durationIndex));

            callLogDataList.add(new CallLogData(date, type, duration, number));
        }

        cursor.close();
    }
}
