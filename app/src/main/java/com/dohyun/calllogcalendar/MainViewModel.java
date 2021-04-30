package com.dohyun.calllogcalendar;

import android.app.Application;
import android.content.ContextWrapper;
import android.os.Handler;
import android.provider.CallLog;

import androidx.annotation.NonNull;
import androidx.databinding.BaseObservable;
import androidx.databinding.Bindable;
import androidx.databinding.ObservableInt;
import androidx.lifecycle.AndroidViewModel;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.concurrent.Executor;

public class MainViewModel extends AndroidViewModel {
    private final Handler handler;
    private final Executor executor;
    private final CallLogManager callLogManager;
    private Connected connected;
    private Unconnected unconnected;
    private final ObservableInt voiceMailCnt = new ObservableInt();

    public MainViewModel(@NonNull Application application) {
        super(application);
        handler = ((MyApplication)application).mainThreadHandler;
        executor = ((MyApplication)application).executorService;
        callLogManager = ((MyApplication)application).callLogManager;
    }

    public Connected getConnected() {
        if(connected == null){
            connected = new Connected();
        }
        return connected;
    }

    public Unconnected getUnconnected() {
        if(unconnected == null){
            unconnected = new Unconnected();
        }
        return unconnected;
    }

    public ObservableInt getVoiceMailCnt() {
        return voiceMailCnt;
    }

    public void reqCallLogData(ContextWrapper contextWrapper, boolean load, Date startDate, Date endDate, ExecutorCallback<Boolean> callback){
        executor.execute(new Runnable() {
            @Override
            public void run() {
                try{
                    if(load){
                        callLogManager.loadData(contextWrapper);
                    }
                    changeData(callLogManager.getCallLogDataList(), startDate, endDate);

                    ExecutorResult<Boolean> result = new ExecutorResult.Success<>(true);
                    notifyLoadCallLogDataResult(result, callback);

                }catch (Exception e){
                    ExecutorResult<Boolean> result = new ExecutorResult.Error<>(e);
                    notifyLoadCallLogDataResult(result, callback);
                }

            }
        });
    }

    private void changeData(ArrayList<CallLogData> callLogDataList, Date startDate, Date endDate){
        try {
            Calendar cal = Calendar.getInstance();

            cal.setTime(startDate);
            cal.add(Calendar.HOUR, 0);
            cal.add(Calendar.MINUTE, 0);
            cal.add(Calendar.SECOND, 0);
            startDate = new Date(cal.getTimeInMillis());

            cal.setTime(endDate);
            cal.add(Calendar.HOUR, 23);
            cal.add(Calendar.MINUTE, 59);
            cal.add(Calendar.SECOND, 59);
            endDate = new Date(cal.getTimeInMillis());

            int incomingCnt = 0, incomingSec = 0;
            int outgoingCnt = 0, outgoingSec = 0;
            int answExternCnt = 0, answExternSec = 0;
            int totalCnt = 0, totalSec = 0;
            int missed = 0, rejected = 0, blocked = 0, total=0;
            int voiceMailCnt = 0;

            for(CallLogData callLogData: callLogDataList){
                Date date = callLogData.getDate();

                if(date.getTime() >= startDate.getTime() && date.getTime() <= endDate.getTime()){

                    switch (callLogData.getType()){
                        case CallLog.Calls.INCOMING_TYPE:
                            incomingCnt++;
                            incomingSec += callLogData.getDuration();
                            break;
                        case CallLog.Calls.OUTGOING_TYPE:
                            outgoingCnt++;
                            outgoingSec += callLogData.getDuration();
                            break;
                        case CallLog.Calls.ANSWERED_EXTERNALLY_TYPE:
                            answExternCnt++;
                            answExternSec += callLogData.getDuration();
                            break;
                        case CallLog.Calls.MISSED_TYPE:
                            missed++;
                            break;
                        case CallLog.Calls.REJECTED_TYPE:
                            rejected++;
                            break;
                        case CallLog.Calls.BLOCKED_TYPE:
                            blocked++;
                            break;
                        case CallLog.Calls.VOICEMAIL_TYPE:
                            voiceMailCnt++;
                            break;
                    }
                }
            }

            totalCnt = incomingCnt + outgoingCnt + answExternCnt;
            totalSec = incomingSec + outgoingSec + answExternSec;
            total = missed + rejected + blocked;

            this.connected.setIncomingCnt(incomingCnt);
            this.connected.setIncomingSec(incomingSec);
            this.connected.setOutgoingCnt(outgoingCnt);
            this.connected.setOutgoingSec(outgoingSec);
            this.connected.setAnswExternCnt(answExternCnt);
            this.connected.setAnswExternSec(answExternSec);
            this.connected.setTotalCnt(totalCnt);
            this.connected.setTotalSec(totalSec);
            this.unconnected.setMissed(missed);
            this.unconnected.setRejected(rejected);
            this.unconnected.setBlocked(blocked);
            this.unconnected.setTotal(total);
            this.voiceMailCnt.set(voiceMailCnt);

        } catch (Exception e) {
            e.printStackTrace();
            return;
        }
    }

    private void notifyLoadCallLogDataResult(
            final ExecutorResult<Boolean> result,
            final  ExecutorCallback<Boolean> callback){

        handler.post(new Runnable() {
            @Override
            public void run() {
                callback.onComplete(result);
            }
        });
    }

    public class Connected extends BaseObservable {
        private int incomingCnt, incomingSec;
        private int outgoingCnt, outgoingSec;
        private int answExternCnt, answExternSec;
        private int totalCnt, totalSec;

        public Connected() {
            incomingCnt = 0;
            incomingSec = 0;
            outgoingCnt = 0;
            outgoingSec = 0;
            answExternCnt = 0;
            answExternSec = 0;
            totalCnt = 0;
            totalSec = 0;
        }

        @Bindable
        public int getIncomingCnt() {
            return incomingCnt;
        }

        @Bindable
        public int getIncomingSec() {
            return incomingSec;
        }

        @Bindable
        public int getOutgoingCnt() {
            return outgoingCnt;
        }

        @Bindable
        public int getOutgoingSec() {
            return outgoingSec;
        }

        @Bindable
        public int getAnswExternCnt() {
            return answExternCnt;
        }

        @Bindable
        public int getAnswExternSec() {
            return answExternSec;
        }

        @Bindable
        public int getTotalCnt() {
            return totalCnt;
        }

        @Bindable
        public int getTotalSec() {
            return totalSec;
        }

        public void setIncomingCnt(int incomingCnt) {
            this.incomingCnt = incomingCnt;
            notifyPropertyChanged(BR.incomingCnt);
        }

        public void setIncomingSec(int incomingSec) {
            this.incomingSec = incomingSec;
            notifyPropertyChanged(BR.incomingSec);
        }

        public void setOutgoingCnt(int outgoingCnt) {
            this.outgoingCnt = outgoingCnt;
            notifyPropertyChanged(BR.outgoingCnt);
        }

        public void setOutgoingSec(int outgoingSec) {
            this.outgoingSec = outgoingSec;
            notifyPropertyChanged(BR.outgoingSec);
        }

        public void setAnswExternCnt(int answExternCnt) {
            this.answExternCnt = answExternCnt;
            notifyPropertyChanged(BR.answExternCnt);
        }

        public void setAnswExternSec(int answExternSec) {
            this.answExternSec = answExternSec;
            notifyPropertyChanged(BR.answExternSec);
        }

        public void setTotalCnt(int totalCnt) {
            this.totalCnt = totalCnt;
            notifyPropertyChanged(BR.totalCnt);
        }

        public void setTotalSec(int totalSec) {
            this.totalSec = totalSec;
            notifyPropertyChanged(BR.totalSec);
        }
    }

    public class Unconnected extends BaseObservable{
        private int missed;
        private int rejected;
        private int blocked;
        private int total;

        public Unconnected() {
            missed = 0;
            rejected = 0;
            blocked = 0;
            total = 0;
        }

        @Bindable
        public int getMissed() {
            return missed;
        }

        @Bindable
        public int getRejected() {
            return rejected;
        }

        @Bindable
        public int getBlocked() {
            return blocked;
        }

        @Bindable
        public int getTotal() {
            return total;
        }

        public void setMissed(int missed) {
            this.missed = missed;
            notifyPropertyChanged(BR.missed);
        }

        public void setRejected(int rejected) {
            this.rejected = rejected;
            notifyPropertyChanged(BR.rejected);
        }

        public void setBlocked(int blocked) {
            this.blocked = blocked;
            notifyPropertyChanged(BR.blocked);
        }

        public void setTotal(int total) {
            this.total = total;
            notifyPropertyChanged(BR.total);
        }
    }
}
