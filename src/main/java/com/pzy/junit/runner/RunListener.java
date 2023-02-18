package com.pzy.junit.runner;

import com.pzy.junit.runner.notification.Failure;

public interface RunListener {

    void testRunStarted(Description description);

    void testRunFinished(Result result);

    void testStarted(Description description);

    void testFinished(Description description);

    void testFailure(Failure failure);

    void testAssumptionFailure(Failure failure);

    void testIgnored(Description description);

}
