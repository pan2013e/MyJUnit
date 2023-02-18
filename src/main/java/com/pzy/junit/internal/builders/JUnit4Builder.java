package com.pzy.junit.internal.builders;

import com.pzy.junit.runner.Runner;
import com.pzy.junit.runners.BlockJUnit4ClassRunner;

public class JUnit4Builder implements RunnerBuilder {

    @Override
    public Runner runnerForClass(Class<?> testClass) {
        return new BlockJUnit4ClassRunner(testClass);
    }

    @Override
    public Runner safeRunnerForClass(Class<?> testClass) {
        try {
            return runnerForClass(testClass);
        } catch (Throwable e) {
            return null;
        }
    }

}