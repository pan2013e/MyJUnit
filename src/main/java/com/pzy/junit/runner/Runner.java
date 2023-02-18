package com.pzy.junit.runner;

public interface Runner {

    void run(RunNotifier notifier);

    Description getDescription();

}
