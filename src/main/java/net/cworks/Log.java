/**
 * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * *
 * Baked with love by corbett
 * Project: wowreg
 * Package: net.cworks
 * Class: Log
 * Created: 11/17/14 9:19 PM
 * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * *
 */
package net.cworks;

import java.io.BufferedWriter;
import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardOpenOption;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.TimeZone;

/**
 * A very poor-mans Log that breaks all the rules but is good enough for
 * Galactic Merchant needs.
 *
 * @author corbett
 */
public class Log {

    private static abstract class Logger {
        public static final short OFF   = 100;
        public static final short FATAL = 7;
        public static final short ERROR = 6;
        public static final short WARN  = 5;
        public static final short INFO  = 4;
        public static final short DEBUG = 3;
        public static final short TRACE = 2;
        public static final short ALL   = 1;

        private short mask;
        private Logger next;

        public Logger(short mask) {
            this.mask = mask;
        }

        public Logger setNext(Logger next) {
            this.next = next;
            return next;
        }

        public void message(String message, short priority) {
            if(priority >= mask) {
                writeMessage(message);
            }
            if(next != null) {
                next.message(message, priority);
            }
        }

        abstract protected void writeMessage(String message);
    }

    private static class SystemLogger extends Logger {

        public SystemLogger(short mask) {
            super(mask);
        }

        @Override
        protected void writeMessage(String message) {
            System.out.println(message);
        }
    }

    private static class FileLogger extends Logger {

        private File file;

        public FileLogger(File file, short mask) {
            super(mask);
            this.file = file;
        }

        @Override
        protected void writeMessage(String message) {
            if(file == null) {
                return;
            }
            Path path = file.toPath();
            // try with resources closes writer
            try (BufferedWriter writer = Files.newBufferedWriter(
                    path, Charset.forName("UTF-8"),
                    StandardOpenOption.CREATE,
                    StandardOpenOption.WRITE,
                    StandardOpenOption.APPEND)){
                writer.write(message);
                writer.newLine();
            } catch (IOException ex) {
                ex.printStackTrace();
            }
        }
    }

    private static File getLogFile() {
        String file = System.getProperty("log.file");
        if(file == null) {
            return null;
        }

        return new File(file);
    }

    private static short getLogLevel() {
        String level = System.getProperty("log.level");
        if(level == null) {
            return Logger.INFO;
        }

        switch (level.toUpperCase()) {
            case "OFF"   : return Logger.OFF;
            case "FATAL" : return Logger.FATAL;
            case "ERROR" : return Logger.ERROR;
            case "WARN"  : return Logger.WARN;
            case "INFO"  : return Logger.INFO;
            case "DEBUG" : return Logger.DEBUG;
            case "TRACE" : return Logger.TRACE;
            case "ALL"   : return Logger.ALL;
            default: return Logger.INFO;
        }
    }

    private Class clazz = null;
    private Logger logger = null;

    private Log(Logger logger) {
        this.logger = logger;
    }

    private Log(Logger logger, Class clazz) {
        this.logger = logger;
        this.clazz = clazz;
    }

    public static Log create(Class clazz) {
        return new Log(getChain(), clazz);
    }

    public static Log create() {
        return new Log(getChain());
    }

    public synchronized void trace(String text) {
        String message = format("TRACE", text);
        logger.message(message, Logger.TRACE);
    }

    public synchronized void debug(String text) {
        String message = format("DEBUG", text);
        logger.message(message, Logger.DEBUG);
    }

    public synchronized void info(String text) {
        String message = format("INFO", text);
        logger.message(message, Logger.INFO);
    }

    public synchronized void warn(String text) {
        String message = format("WARN", text);
        logger.message(message, Logger.WARN);
    }

    public synchronized void error(String text) {
        String message = format("ERROR", text);
        logger.message(message, Logger.ERROR);
    }

    public synchronized void fatal(String text) {
        String message = format("FATAL", text);
        logger.message(message, Logger.FATAL);
    }

    public synchronized void trace(String text, Exception ex) {
        trace(String.format("%s,%s", text, stackString(ex)));
    }

    public synchronized void debug(String text, Exception ex) {
        debug(String.format("%s,%s", text, stackString(ex)));
    }

    public synchronized void info(String text, Exception ex) {
        info(String.format("%s,%s", text, stackString(ex)));
    }

    public synchronized void warn(String text, Exception ex) {
        warn(String.format("%s,%s", text, stackString(ex)));
    }

    public synchronized void error(String text, Exception ex) {
        error(String.format("%s,%s", text, stackString(ex)));
    }

    public synchronized void fatal(String text, Exception ex) {
        trace(String.format("%s,%s", text, stackString(ex)));
    }

    /**
     * Unroll an exception's stacktrace into a String
     * @param ex
     * @return
     */
    private String stackString(Exception ex) {
        StringWriter sw = new StringWriter();
        PrintWriter pw = new PrintWriter(sw, true);
        ex.printStackTrace(pw);
        return sw.getBuffer().toString().trim();
    }

    /**
     * Pretty format a date instance.
     * @param date
     * @return
     */
    public static String dateToString(Date date) {
        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSSz");
        TimeZone tz = TimeZone.getTimeZone("UTC");
        df.setTimeZone(tz);
        String output = df.format(date);
        return output.replaceAll("UTC", "+00:00");
    }

    /**
     * Right now in a pretty UTC format.
     * @return
     */
    public static String now() {
        return dateToString(new Date());
    }

    private String format(String level, String text) {
        if(clazz == null) {
            return now() + "," + level + ","
                + Thread.currentThread().getName() + "," + text;
        }

        return now() + "," + level + "," + clazz.getName() + ","
            + Thread.currentThread().getName() + "," + text;
    }

    private static Logger getChain() {
        Logger systemLogger = new SystemLogger(getLogLevel());
        File file = getLogFile();
        if(file != null) {
            Logger fileLogger = new FileLogger(file, getLogLevel());
            systemLogger.setNext(fileLogger);
        }
        return systemLogger;
    }


}



