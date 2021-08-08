package org.darkowl.jaxb;

import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

import javax.xml.bind.DatatypeConverter;

public class DateAdapter {

    public static Date parseDate(final String s) {
        return DatatypeConverter.parseDate(s).getTime();
    }

    public static String printDate(final Date dt) {
        final Calendar cal = new GregorianCalendar();
        cal.setTime(dt);
        return DatatypeConverter.printDate(cal);
    }
}