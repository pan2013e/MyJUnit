package com.pzy.junit;

public class Assertions {

    public static void assertEquals(Object expected, Object actual) {
        if (expected == null && actual == null) {
            return;
        }
        if (expected != null && expected.equals(actual)) {
            return;
        }
        throw new AssertionError("expected:<" + expected + "> but was:<" + actual + ">");
    }

    public static void assertThrows(Class<? extends Throwable> expected, Runnable runnable) {
        try {
            runnable.run();
        } catch (Throwable t) {
            if (expected.isAssignableFrom(t.getClass())) {
                return;
            }
            throw new AssertionError("expected:<" + expected + "> but was:<" + t.getClass() + ">");
        }
        throw new AssertionError("expected:<" + expected + "> but was:<" + null + ">");
    }

}
