package com.pzy.junit.runner;

@SuppressWarnings({"ClassCanBeRecord", "unused"})
public class Description {

    private final String className;
    private final String methodName;

    public Description(String className, String methodName) {
        this.className = className;
        this.methodName = methodName;
    }

    public String getClassName() {
        return className;
    }

    public String getMethodName() {
        return methodName;
    }

    @Override
    public String toString() {
        if(methodName != null) {
            return className + '#' + methodName;
        } else {
            return className;
        }
    }

}
