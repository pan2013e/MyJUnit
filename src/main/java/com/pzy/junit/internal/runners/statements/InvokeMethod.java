package com.pzy.junit.internal.runners.statements;

import com.pzy.junit.runner.model.Statement;
import com.pzy.junit.runners.model.FrameworkMethod;

@SuppressWarnings("ClassCanBeRecord")
public class InvokeMethod implements Statement {

    private final FrameworkMethod testMethod;
    private final Object target;

    public InvokeMethod(FrameworkMethod testMethod, Object target) {
        this.testMethod = testMethod;
        this.target = target;
    }

    @Override
    public void evaluate() throws Throwable {
        testMethod.invokeExplosively(target);
    }

}
