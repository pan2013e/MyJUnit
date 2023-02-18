package com.pzy.junit.runners;

import com.pzy.junit.AssumptionViolatedException;
import com.pzy.junit.runner.Description;
import com.pzy.junit.runner.RunNotifier;
import com.pzy.junit.runner.notification.Failure;

public class EachTestNotifier extends RunNotifier {

    private final Description description;
    private final RunNotifier notifier;

    public EachTestNotifier(RunNotifier notifier, Description description) {
        this.notifier = notifier;
        this.description = description;
    }

    public void fireTestSuiteStarted() {
    }

    public void fireTestSuiteFinished() {
    }

    public void addFailure(Throwable e) {
        notifier.listeners.forEach(listener -> listener.testFailure(new Failure(e, description)));
    }

    public void addFailedAssumption(AssumptionViolatedException e) {
        notifier.listeners.forEach(listener -> listener.testAssumptionFailure(new Failure(e, description)));
    }

    public void fireTestStarted() {
        notifier.listeners.forEach(listener -> listener.testStarted(description));
    }

    public void fireTestFinished() {
        notifier.listeners.forEach(listener -> listener.testFinished(description));
    }
}
