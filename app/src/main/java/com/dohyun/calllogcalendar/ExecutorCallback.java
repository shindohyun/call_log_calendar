package com.dohyun.calllogcalendar;

public interface ExecutorCallback<T> {
    void onComplete(ExecutorResult<T> result);
}
