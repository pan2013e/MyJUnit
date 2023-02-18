package com.pzy.junit.runners;

import com.pzy.junit.AssumptionViolatedException;
import com.pzy.junit.internal.InternalException;
import com.pzy.junit.runner.Description;
import com.pzy.junit.runner.RunNotifier;
import com.pzy.junit.runner.Runner;
import com.pzy.junit.runner.model.Statement;

public abstract class ParentRunner<T> implements Runner {

    protected final Class<?> testClass;

    public ParentRunner(Class<?> testClass) {
        this.testClass = testClass;
    }

    @Override
    public void run(RunNotifier notifier) {
        EachTestNotifier testNotifier = new EachTestNotifier(notifier, getDescription());
        testNotifier.fireTestSuiteStarted();
        try {
            Statement statement = classBlock(notifier);
            statement.evaluate();
        } catch (InternalException e) {
            e.printStackTrace();
        } catch (AssumptionViolatedException e) {
            testNotifier.addFailedAssumption(e);
        } catch (Throwable e) {
            testNotifier.addFailure(e);
        } finally {
            testNotifier.fireTestSuiteFinished();
        }
    }

    private Statement classBlock(RunNotifier notifier) {
        Statement statement = childrenInvoker(notifier);
        if(!areAllChildrenIgnored()) {
            statement = withBeforeClasses(statement);
            statement = withAfterClasses(statement);
            statement = withClassRules(statement);
        }
        return statement;
    }

    protected abstract Statement withBeforeClasses(Statement statement);

    protected abstract Statement withAfterClasses(Statement statement);

    protected abstract Statement withClassRules(Statement statement);

    protected abstract boolean areAllChildrenIgnored();

    private void runChildren(RunNotifier notifier) {
        for(final T each: getFilteredChildren()) {
            runChild(each, notifier);
        }
    }

    protected abstract void runChild(T child, RunNotifier notifier);

    protected abstract T[] getFilteredChildren();

    protected Statement childrenInvoker(final RunNotifier notifier) {
        return () -> runChildren(notifier);
    }

    @SuppressWarnings("ResultOfMethodCallIgnored")
    protected final Statement withInterruptIsolation(final Statement statement) {
        return () -> {
            try {
                statement.evaluate();
            } finally {
                Thread.interrupted();
            }
        };
    }

    public abstract Description getDescription();

}
