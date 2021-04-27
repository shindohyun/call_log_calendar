package com.dohyun.calllogcalendar;

import android.app.Application;
import android.content.ContextWrapper;
import android.os.Handler;

import androidx.annotation.NonNull;
import androidx.databinding.BaseObservable;
import androidx.databinding.ObservableInt;
import androidx.lifecycle.AndroidViewModel;

import java.util.ArrayList;
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

    public void reqCallLogData(ContextWrapper contextWrapper, boolean load, ExecutorCallback<Boolean> callback){
        executor.execute(new Runnable() {
            @Override
            public void run() {
                try{
                    if(load){
                        callLogManager.loadData(contextWrapper);
                    }
                    changeData(callLogManager.getCallLogDataList());

                    ExecutorResult<Boolean> result = new ExecutorResult.Success<>(true);
                    notifyLoadCallLogDataResult(result, callback);

                }catch (Exception e){
                    ExecutorResult<Boolean> result = new ExecutorResult.Error<>(e);
                    notifyLoadCallLogDataResult(result, callback);
                }

            }
        });
    }

    private void changeData(ArrayList<CallLogData> callLogDataList){
        for(CallLogData callLogData: callLogDataList){
            // TODO: 조건 검색
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

        public int getIncomingCnt() {
            return incomingCnt;
        }

        public int getIncomingSec() {
            return incomingSec;
        }

        public int getOutgoingCnt() {
            return outgoingCnt;
        }

        public int getOutgoingSec() {
            return outgoingSec;
        }

        public int getAnswExternCnt() {
            return answExternCnt;
        }

        public int getAnswExternSec() {
            return answExternSec;
        }

        public int getTotalCnt() {
            return totalCnt;
        }

        public int getTotalSec() {
            return totalSec;
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

        public int getMissed() {
            return missed;
        }

        public int getRejected() {
            return rejected;
        }

        public int getBlocked() {
            return blocked;
        }

        public int getTotal() {
            return total;
        }
    }
}
