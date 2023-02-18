package com.pzy.junit.runner;

import com.pzy.junit.internal.builders.AllDefaultPossibilitiesBuilder;

public class JUnitCore {

    private final RunNotifier notifier = new RunNotifier();

    private void removeListener(RunListener listener) {
        notifier.removeListener(listener);
    }

    @SuppressWarnings("UnusedReturnValue")
    public Result run(Runner runner) {
        Result result = new Result();
        RunListener listener = result.createListener();
        notifier.addFirstListener(listener);
        try {
            notifier.fireTestRunStarted(runner.getDescription());
            runner.run(notifier);
            notifier.fireTestRunFinished(result);
        } finally {
            removeListener(listener);
        }
        return result;
    }

    @SuppressWarnings("UnusedReturnValue")
    public Result run(Class<?> clazz) {
        return run(new AllDefaultPossibilitiesBuilder().runnerForClass(clazz));
    }

}
