package com.dohyun.calllogcalendar;

import androidx.databinding.BaseObservable;
import androidx.databinding.ObservableInt;
import androidx.lifecycle.ViewModel;

public class MainViewModel extends ViewModel {
    private Connected connected;
    private Unconnected unconnected;
    private final ObservableInt voiceMailCnt = new ObservableInt();

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

    public void loadCallLogData(){

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
