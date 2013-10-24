package test;

import static org.junit.Assert.assertEquals;

import org.junit.Test;
import org.junit.Ignore;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

@RunWith(JUnit4.class)
public class SampleTest {

    @Test
    public void thisAlwaysPasses() {
    }

    @Test
    @Ignore
    public void thisIsIgnored() {
    }
}
