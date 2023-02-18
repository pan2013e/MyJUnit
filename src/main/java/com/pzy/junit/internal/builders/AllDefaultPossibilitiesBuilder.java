package com.pzy.junit.internal.builders;

import com.pzy.junit.runner.Runner;

import java.util.Arrays;
import java.util.List;

public class AllDefaultPossibilitiesBuilder implements RunnerBuilder {

    @Override
    public Runner runnerForClass(Class<?> testClass) {
        //noinspection ArraysAsListWithZeroOrOneArgument
        List<RunnerBuilder> builders = Arrays.asList(
                new JUnit4Builder());
        for (RunnerBuilder each : builders) {
            Runner runner = each.safeRunnerForClass(testClass);
            if (runner != null) {
                return runner;
            }
        }
        return null;
    }

    @Override
    public Runner safeRunnerForClass(Class<?> testClass) {
        return runnerForClass(testClass);
    }

}
