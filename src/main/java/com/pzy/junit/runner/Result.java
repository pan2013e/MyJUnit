package com.pzy.junit.runner;

import com.pzy.junit.runner.notification.Failure;

import java.util.concurrent.atomic.AtomicInteger;

public class Result {

    private final AtomicInteger failed = new AtomicInteger(0);
    private final AtomicInteger count = new AtomicInteger(0);
    private long runTime = 0;
    private long startTime;

    public int getFailed() {
        return failed.get();
    }

    public int getRunCount() {
        return count.get();
    }

    public long getRunTime() {
        return runTime;
    }

    @Override
    public String toString() {
        return String.format("Tests finished in %d ms. %d total, %d failed.", getRunTime(), getRunCount(), getFailed());
    }

    RunListener createListener() {
        return new RunListener() {
            @Override
            public void testRunStarted(Description description) {
                startTime = System.currentTimeMillis();
            }

            @Override
            public void testRunFinished(Result result) {
                long endTime = System.currentTimeMillis();
                runTime = endTime - startTime;
                System.out.println(result);
            }

            @Override
            public void testStarted(Description description) {

            }

            @Override
            public void testFinished(Description description) {
                count.getAndIncrement();
            }

            @Override
            public void testFailure(Failure failure) {
                failed.getAndIncrement();
                System.out.println(failure.getDescription() + " failed: " + failure.getName());
                failure.printStackTrace();
            }

            @Override
            public void testAssumptionFailure(Failure failure) {
                failed.getAndIncrement();
                System.out.println("Test failed: " + failure.getDescription() + " " + failure.getName());
                failure.printStackTrace();
            }

            @Override
            public void testIgnored(Description description) {

            }
        };
    }

}
