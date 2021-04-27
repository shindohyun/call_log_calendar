package com.dohyun.calllogcalendar;

public abstract class ExecutorResult<T> {
    private ExecutorResult() {}

    public static final class Success<T> extends ExecutorResult<T> {
        public T data;

        public Success(T data) {
            this.data = data;
        }
    }

    public static final class Error<T> extends ExecutorResult<T> {
        public Exception exception;

        public Error(Exception exception) {
            this.exception = exception;
        }
    }
}