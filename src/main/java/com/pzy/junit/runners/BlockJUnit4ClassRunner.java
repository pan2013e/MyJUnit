package com.pzy.junit.runners;

import com.pzy.junit.*;
import com.pzy.junit.internal.InternalException;
import com.pzy.junit.internal.runners.model.ReflectiveCallable;
import com.pzy.junit.internal.runners.statements.InvokeMethod;
import com.pzy.junit.runner.Description;
import com.pzy.junit.runner.RunNotifier;
import com.pzy.junit.runner.model.Statement;
import com.pzy.junit.runners.model.FrameworkMethod;

import java.lang.annotation.Annotation;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Predicate;

@SuppressWarnings("unused")
public class BlockJUnit4ClassRunner extends ParentRunner<FrameworkMethod> {

    public BlockJUnit4ClassRunner(Class<?> testClass) {
        super(testClass);
    }

    @Override
    protected void runChild(FrameworkMethod method, RunNotifier notifier) {
        Description description = describeChild(method);
        if(isIgnored(method)) {
            notifier.fireTestIgnored(description);
        } else {
            Statement statement = () -> methodBlock(method).evaluate();
            runLeaf(statement, description, notifier);
        }
    }

    private void runLeaf(Statement statement, Description description, RunNotifier notifier) {
        EachTestNotifier testNotifier = new EachTestNotifier(notifier, description);
        testNotifier.fireTestStarted();
        try {
            statement.evaluate();
        } catch (InternalException e) {
            e.printStackTrace();
        } catch (AssumptionViolatedException e) {
            testNotifier.addFailedAssumption(e);
        } catch (Throwable e) {
            testNotifier.addFailure(e);
        } finally {
            testNotifier.fireTestFinished();
        }
    }

    protected Statement methodBlock(final FrameworkMethod method) {
        Object test;
        try {
            test = new ReflectiveCallable() {
                @Override
                protected Object runReflectiveCall() {
                    return createTest(method);
                }
            }.run();
        } catch (Throwable e) {
            return () -> {
                throw e;
            };
        }
        Statement statement = methodInvoker(method, test);
        statement = possiblyExpectingExceptions(method, test, statement);
        statement = withPotentialTimeout(method, test, statement);
        statement = withBefores(method, test, statement);
        statement = withAfters(method, test, statement);
        statement = withRules(method, test, statement);
        statement = withInterruptIsolation(statement);
        return statement;
    }

    protected Statement withRules(FrameworkMethod method, Object test, Statement statement) {
        return statement;
    }

    protected Statement withAfters(FrameworkMethod method, Object test, Statement statement) {
        FrameworkMethod[] afters = getAnnotatedMethods(After.class);
        return () -> {
            statement.evaluate();
            for(FrameworkMethod after : afters) {
                methodInvoker(after, test).evaluate();
            }
        };
    }

    protected Statement withBefores(FrameworkMethod method, Object test, Statement statement) {
        FrameworkMethod[] befores = getAnnotatedMethods(Before.class);
        return () -> {
            for(FrameworkMethod before : befores) {
                methodInvoker(before, test).evaluate();
            }
            statement.evaluate();
        };
    }

    protected Statement withPotentialTimeout(FrameworkMethod method, Object test, Statement statement) {
        Test t = method.getMethod().getAnnotation(Test.class);
        int timeout = t.timeout();
        return () -> {
            long t1 = System.currentTimeMillis();
            statement.evaluate();
            long t2 = System.currentTimeMillis();
            if(timeout > 0 && t2 - t1 > timeout * 1000L) {
                throw new Throwable("timeout");
            }
        };
    }

    protected Statement possiblyExpectingExceptions(FrameworkMethod method, Object test, Statement statement) {
        Test t = method.getMethod().getAnnotation(Test.class);
        Class<? extends Throwable>[] expected = t.expected();
        return () -> {
            try {
                statement.evaluate();
            } catch (Throwable e) {
                for (Class<? extends Throwable> aClass : expected) {
                    if(aClass.isAssignableFrom(e.getClass())) {
                        return;
                    }
                }
                throw e;
            }
        };
    }

    protected Statement methodInvoker(FrameworkMethod method, Object test) {
        return new InvokeMethod(method, test);
    }

    private Object createTest(FrameworkMethod method) {
        Class<?> testClass = method.getMethod().getDeclaringClass();
        try {
            Constructor<?>[] constructors = testClass.getDeclaredConstructors();
            for(Constructor<?> constructor: constructors) {
                if(constructor.getParameterCount() == 0) {
                    constructor.setAccessible(true);
                    return constructor.newInstance();
                }
            }
            throw new InternalException("constructors with parameters are not allowed");
        } catch (InstantiationException | IllegalAccessException | InvocationTargetException e) {
            e.printStackTrace();
        }
        return null;
    }

    private boolean isIgnored(FrameworkMethod method) {
        return method.getMethod().isAnnotationPresent(Ignore.class);
    }

    private Description describeChild(FrameworkMethod method) {
        return new Description(testClass.getName(), method.getMethod().getName());
    }

    @Override
    protected Statement withBeforeClasses(Statement statement) {
        FrameworkMethod[] befores = getAnnotatedMethods(BeforeClass.class);
        return () -> {
            for(FrameworkMethod before: befores) {
                if(!Modifier.isStatic(before.getMethod().getModifiers())) {
                    throw new InternalException("BeforeClass methods must be static");
                }
                methodInvoker(before, null).evaluate();
            }
            statement.evaluate();
        };
    }

    @Override
    protected Statement withAfterClasses(Statement statement) {
        FrameworkMethod[] afters = getAnnotatedMethods(AfterClass.class);
        return () -> {
            statement.evaluate();
            for(FrameworkMethod after: afters) {
                if(!Modifier.isStatic(after.getMethod().getModifiers())) {
                    throw new InternalException("AfterClass methods must be static");
                } else {
                    methodInvoker(after, null).evaluate();
                }
            }
        };
    }

    @Override
    protected Statement withClassRules(Statement statement) {
        return statement;
    }

    protected FrameworkMethod[] getAnnotatedMethods(Class<? extends Annotation> annotationClass) {
        Method[] methods = testClass.getDeclaredMethods();
        List<FrameworkMethod> frameworkMethods = new ArrayList<>();
        for(Method method: methods) {
            if(method.isAnnotationPresent(annotationClass)) {
                method.setAccessible(true);
                frameworkMethods.add(new FrameworkMethod(method));
            }
        }
        return frameworkMethods.toArray(new FrameworkMethod[0]);
    }

    protected FrameworkMethod[] getAnnotatedMethods(Predicate<Method> filter) {
        Method[] methods = testClass.getDeclaredMethods();
        List<FrameworkMethod> frameworkMethods = new ArrayList<>();
        for(Method method: methods) {
            if(filter.test(method)) {
                method.setAccessible(true);
                frameworkMethods.add(new FrameworkMethod(method));
            }
        }
        return frameworkMethods.toArray(new FrameworkMethod[0]);
    }


    @Override
    protected FrameworkMethod[] getFilteredChildren() {
        return getAnnotatedMethods(method ->
                method.isAnnotationPresent(Test.class) &&
                !method.isAnnotationPresent(Ignore.class));
    }

    @Override
    protected boolean areAllChildrenIgnored() {
        FrameworkMethod[] children = getFilteredChildren();
        for(FrameworkMethod child: children) {
            if(!isIgnored(child)) {
                return false;
            }
        }
        return true;
    }

    @Override
    public Description getDescription() {
        return new Description(testClass.getName(), null);
    }

}
