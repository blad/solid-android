package com.btellez.solidandroid.test;

import android.test.InstrumentationTestRunner;
import android.test.InstrumentationTestSuite;

import junit.framework.TestSuite;

import com.btellez.solidandroid.test.activity.MainUserActivityTest;

/**
 * <a href="http://d.android.com/tools/testing/testing_android.html">Testing Fundamentals</a>
 */
public class SimpleApplicationInstrumentationTestRunner extends InstrumentationTestRunner {
    @Override
    public TestSuite getAllTests() {
        InstrumentationTestSuite tests = new InstrumentationTestSuite(this);

        // Add Test's To Be Ran Here:
        tests.addTestSuite(MainUserActivityTest.class);

        return tests;
    }
}
