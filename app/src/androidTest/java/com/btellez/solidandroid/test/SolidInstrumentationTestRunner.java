package com.btellez.solidandroid.test;

import android.test.InstrumentationTestRunner;
import android.test.InstrumentationTestSuite;

import com.btellez.solidandroid.test.model.DeserializeTest;

import junit.framework.TestSuite;


/**
 * <a href="http://d.android.com/tools/testing/testing_android.html">Testing Fundamentals</a>
 */
public class SolidInstrumentationTestRunner extends InstrumentationTestRunner {
    @Override
    public TestSuite getAllTests() {
        InstrumentationTestSuite tests = new InstrumentationTestSuite(this);

        // Add Test's To Be Ran Here:
        tests.addTestSuite(DeserializeTest.class);

        return tests;
    }
}
