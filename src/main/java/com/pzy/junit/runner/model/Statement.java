package com.pzy.junit.runner.model;

@FunctionalInterface
public interface Statement {

    void evaluate() throws Throwable;

}
