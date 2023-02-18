package com.pzy.junit.runners.model;

import com.pzy.junit.Ignore;
import com.pzy.junit.internal.runners.model.ReflectiveCallable;

import java.lang.reflect.Method;

@SuppressWarnings("ClassCanBeRecord")
public class FrameworkMethod {

    private final Method method;

    public FrameworkMethod(Method method) {
        this.method = method;
    }

    public Method getMethod() {
        return method;
    }

    public boolean isIgnored() {
        return method.isAnnotationPresent(Ignore.class);
    }

    @SuppressWarnings("UnusedReturnValue")
    public Object invokeExplosively(final Object target, final Object... params) throws Throwable {
        return new ReflectiveCallable() {
            @Override
            protected Object runReflectiveCall() throws Throwable {
                return method.invoke(target, params);
            }
        }.run();
    }
}
