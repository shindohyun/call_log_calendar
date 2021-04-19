package com.dohyun.calllogcalendar;

import java.util.Date;

public class CallLogData {
    private Date date;
    /*
    INCOMING_TYPE
    OUTGOING_TYPE
    MISSED_TYPE
    VOICEMAIL_TYPE
    REJECTED_TYPE
    BLOCKED_TYPE
    ANSWERED_EXTERNALLY_TYPE
    */
    private int type;
    private int duration;
    private String number;

    public CallLogData(){

    }

    public CallLogData(Date date, int type, int duration, String number) {
        this.date = date;
        this.type = type;
        this.duration = duration;
        this.number = number;
    }

    public Date getDate() {
        return date;
    }

    public int getType() {
        return type;
    }

    public int getDuration() {
        return duration;
    }

    public String getNumber() {
        return number;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public void setType(int type) {
        this.type = type;
    }

    public void setDuration(int duration) {
        this.duration = duration;
    }

    public void setNumber(String number) {
        this.number = number;
    }
}
