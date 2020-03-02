package com.att.retain.bill.util;

import com.vindicia.soap.v1_1.selecttypes.NameValuePair;
import com.vindicia.soap.v1_1.selecttypes.TransactionStatusType;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.sql.Timestamp;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Calendar;

public final class BillUtils {

    static Logger log = LoggerFactory.getLogger(BillUtils.class);

    public static int find(String[] s, String val) throws IOException {
        if (null != val) {
            for (int i = 0; i < s.length; i++) {
                if (val.equalsIgnoreCase(s[i])) {
                    String msg = "iHDR_" + val;
                    for (int j = val.length(); j < 40; j++) msg += ' ';
                    //log(msg + "= " + i);
                    return i;
                }
            }
        }
        return -1;
    }

    public static Timestamp timestamp() {
        java.util.Date date = new java.util.Date();
        return (new Timestamp(date.getTime()));
    }

    public static String getCreditCardAccount(TransactionStatusType resultType, String why) {
        String creditCardAccount = null;
        if (resultType.equals(TransactionStatusType.Captured)) {
            creditCardAccount = "4111111111111111";
        }
        if (resultType.equals(TransactionStatusType.Failed)) {
            if (null != why) {
                if ("Decline".equalsIgnoreCase(why))
                    creditCardAccount = "6011555555555553";    // Soft Fail
                else if ("Hard".equalsIgnoreCase(why))
                    creditCardAccount = "4555555555555550";    // Hard Fail
            }
            if (null == creditCardAccount) {
                // Luhn Check Fail Validation
                creditCardAccount = "1111111111111111";
            }
        }
        return creditCardAccount;
    }

    public static String getNow(String dateTimeFormat) {
        //Get current date time
        ZonedDateTime now = ZonedDateTime.now();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern(dateTimeFormat);

        return now.format(formatter);
    }

    /**
     * 2015-09-23T01:39:00.000Z
     */

    public static Calendar getCalendar(String timestamp) {
        // To avoid the following Exception, edit format string to match
        //	java.text.ParseException: Unparseable date: "2015-09-23T01:39:00Z"
        //SimpleDateFormat sdf = new SimpleDateFormat("M/d/yy hh:mm aa");	// 5/12/11 10:00 PM
        // Java 1.7 only:
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd'T'hh:mm:ssXXX");// 2015-09-23T01:39:00Z
        // Java 1.6 & before:
        //SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd'T'hh:mm:ssZ");// 2015-09-23T01:39:00Z
        Calendar calendar = Calendar.getInstance();
        try {
            //log("\nBefore: timestamp: " + timestamp);
            //if ( timestamp.endsWith("Z") )
            //	timestamp = timestamp.substring( 0, timestamp.length()-1 );
            //log( "\nAfter removal of 'Z': " + timestamp );
            //calendar.setTime( sdf.parse(timestamp) );
            calendar.setTime(sdf.parse(timestamp.replaceAll("Z$", "+0000")));
            log.debug("After: calendar: " + sdf.format(calendar.getTime()));
        } catch (ParseException e) {
            //log(e.toString());
        }
        return calendar;
    }

    public static String getBIN(String cc) {

        return cc.substring(0, 6);
    }

    public static String mask(String cc) {

        String maskedCC = getBIN(cc);

        maskedCC += String.format("%" + (cc.length() - 6) + "s", "").replace(' ', 'X');

        return maskedCC;
    }

    public static NameValuePair[] getSystemInfo() {
        String javaVersion = System.getProperty("java.version");
        String osVersion = System.getProperty("os.name") + " "
                + System.getProperty("os.arch") + " "
                + System.getProperty("os.version") + " "
                + System.getProperty("sun.arch.data.model");

        NameValuePair[] nvp = new NameValuePair[2];
        for (int i = 0; i < nvp.length; i++)
            nvp[i] = new NameValuePair();
        nvp[0].setName("javaVersion");
        nvp[0].setValue(javaVersion);
        nvp[1].setName("osVersion");
        nvp[1].setValue(osVersion);

        return nvp;
    }

    public static void pause(int milliseconds) {
        try {
            //thread to sleep for the specified number of milliseconds
            Thread.sleep(milliseconds);
        } catch (java.lang.InterruptedException ie) {
            System.out.println(ie);
        }
    }
}
