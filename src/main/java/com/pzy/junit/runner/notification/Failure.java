package com.pzy.junit.runner.notification;

import com.pzy.junit.runner.Description;

import java.util.ArrayList;
import java.util.List;

@SuppressWarnings({"ClassCanBeRecord", "unused"})
public class Failure {

    private final Throwable cause;

    private final Description description;

    public Failure(Throwable cause, Description description) {
        this.cause = cause;
        this.description = description;
    }

    public Description getDescription() {
        return description;
    }

    public Throwable getCause() {
        return cause;
    }

    public void printStackTrace() {
        StackTraceElement[] ste = cause.getStackTrace();
        List<StackTraceElement> list = new ArrayList<>();
        for (StackTraceElement stackTraceElement : ste) {
            if (!stackTraceElement.getClassName().contains("junit")) {
                list.add(stackTraceElement);
            }
        }
        cause.setStackTrace(list.toArray(new StackTraceElement[0]));
        cause.printStackTrace();
    }

    public String getName() {
        return cause.getClass().getName();
    }
}
