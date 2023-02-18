package com.pzy.junit;

public class AssumptionViolatedException extends AssertionError {

    public AssumptionViolatedException(String message) {
        super(message);
    }

    @Override
    public void printStackTrace() {
        super.printStackTrace();
    }

}
