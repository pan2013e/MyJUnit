package com.pzy.junit.runner;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class RunNotifier {

    public final List<RunListener> listeners = Collections.synchronizedList(new ArrayList<>());

    public void fireTestRunStarted(Description description) {
        listeners.forEach(listener -> listener.testRunStarted(description));
    }

    public void fireTestRunFinished(Result result) {
        listeners.forEach(listener -> listener.testRunFinished(result));
    }

    public void addFirstListener(RunListener listener) {
        listeners.add(0, listener);
    }

    public void fireTestIgnored(Description description) {
        listeners.forEach(listener -> listener.testIgnored(description));
    }

    public void removeListener(RunListener listener) {
        listeners.remove(listener);
    }

}
