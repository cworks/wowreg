package net.cworks;

import org.junit.Test;

public class LogTest {

    @Test
    public void testLog() {
        Log log = Log.create();
        log.info("Just some text without a Class");
        log.warn("Just some Warning Text");
        log.error("Just an ERROR! Without a Class");
        log.error("Just an ERROR with an Exception! Without a Class");
        log.fatal("Oh NO a FATAL something!");

    }

    @Test
    public void testLogClass() {
        Log log = Log.create(LogTest.class);
        log.info("Just some text");
        log.warn("Just some Warning Text");
        log.error("Just an ERROR!");
        log.error("Just an ERROR with an Exception", new Exception("#epicfail"));
        log.fatal("Oh NO a FATAL something!");
    }

}
