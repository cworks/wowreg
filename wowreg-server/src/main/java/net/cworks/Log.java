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

    /**
     * Just one
     */
    public static final Log log = new Log();

    /**
     * Maybe logging to a file...
     */
    private File file;

    /**
     * If chatty then System.out will get log statements
     */
    private boolean chatty = false;

    /**
     * Private constructor
     */
    private Log() {
        file = null;
    }

    /**
     * If enabled the Log will spit statements to System.out
     * @param chatty
     */
    public void setChatty(boolean chatty) {
        this.chatty = chatty;
    }

    /**
     * Mutate method that will set a new file to Log to
     * @param file
     */
    public void setFile(File file) {
        this.file = file;
    }

    /**
     * Log something with an INFO tag...not INFO level.
     * @param text
     */
    public void info(String text) {
        text = now() + " INFO: " + text;
        if(chatty) System.out.println(text);
        if(file != null) {
            tryWrite(text);
        }
    }

    /**
     * Log something with an ERROR tag...not ERROR level
     * @param text
     */
    public void error(String text) {
        text = now() + " ERROR: " + text;
        if(chatty) System.out.println(text);
        if(file != null) {
            tryWrite(text);
        }
    }

    /**
     * Log something with a stacktrace, with an ERROR tag
     * @param text
     * @param ex
     */
    public void error(String text, Exception ex) {
        error(String.format("%s, stacktrace: %s", text, stackString(ex)));
    }

    /**
     * Try to write to log file.
     * @param text
     */
    private void tryWrite(String text) {
        Path path = file.toPath();
        // try with resources closes writer
        try (BufferedWriter writer = Files.newBufferedWriter(
                path, Charset.forName("UTF-8"),
                StandardOpenOption.WRITE, StandardOpenOption.APPEND)){
            writer.write(text);
            writer.newLine();
        } catch (IOException ex) {
            ex.printStackTrace();
        }
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
        return sw.getBuffer().toString();
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
        String result = output.replaceAll("UTC", "+00:00");

        return result;
    }

    /**
     * Right now in a pretty UTC format.
     * @return
     */
    public static String now() {

        return dateToString(new Date());
    }

}



