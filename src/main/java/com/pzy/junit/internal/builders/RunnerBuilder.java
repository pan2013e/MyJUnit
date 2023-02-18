package com.pzy.junit.internal.builders;

import com.pzy.junit.runner.Runner;

public interface RunnerBuilder {

    Runner runnerForClass(Class<?> testClass) throws Throwable;

    Runner safeRunnerForClass(Class<?> testClass);

}
