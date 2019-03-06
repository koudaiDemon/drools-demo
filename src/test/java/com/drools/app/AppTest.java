package com.drools.app;

import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

/**
 * @author cWww
 * @Title
 * @Description
 * @date: 2019/2/25  15:25
 */
public class AppTest {

    private App app;

    @Before
    public void before(){
        app = new App();
    }

    @Test
    public void testStateful() {
        app.testStateful();
    }

    @Test
    public void testStateless() {
        app.testStateless();
    }

    @Test
    public void testPassiveQuery() {
        app.passiveQuery();
    }

    @Test
    public void testFromAndLock() {
        app.fromAndLock();
    }
}