package com.pzy.junit;

import com.pzy.junit.runner.JUnitCore;

public class JUnitStarter {

    public static void main(String[] args) throws ClassNotFoundException {
        new JUnitCore().run(Class.forName(args[0]));
    }

}
