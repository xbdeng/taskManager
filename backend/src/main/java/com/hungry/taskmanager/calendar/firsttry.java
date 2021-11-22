package com.hungry.taskmanager.calendar;

import net.fortuna.ical4j.data.CalendarBuilder;
import net.fortuna.ical4j.data.CalendarOutputter;
import net.fortuna.ical4j.model.*;
import net.fortuna.ical4j.model.component.VEvent;
import net.fortuna.ical4j.model.component.VLocation;
import net.fortuna.ical4j.model.component.VTimeZone;
import net.fortuna.ical4j.model.component.XComponent;
import net.fortuna.ical4j.model.parameter.Cn;
import net.fortuna.ical4j.model.parameter.Role;
import net.fortuna.ical4j.model.property.*;
import net.fortuna.ical4j.util.FixedUidGenerator;
import net.fortuna.ical4j.util.UidGenerator;

import java.io.FileOutputStream;
import java.io.IOException;
import java.net.URI;
import java.text.ParseException;
import java.util.GregorianCalendar;
import java.util.SimpleTimeZone;

public class firsttry {
    public static void main(String[] args) throws IOException, ParseException {
        //创建新日历
        Calendar calendar = new Calendar();
        calendar.getProperties().add(new ProdId("-//SUSTech//taskmanager 1.0//EN"));
        calendar.getProperties().add(Version.VERSION_2_0);
        calendar.getProperties().add(CalScale.GREGORIAN);

        //创建全天活动
        java.util.Calendar calendar1 = java.util.Calendar.getInstance();
        calendar1.set(java.util.Calendar.MONTH,java.util.Calendar.DECEMBER);
        calendar1.set(java.util.Calendar.DAY_OF_MONTH, 25);

        VEvent christmas = new VEvent(new Date(calendar1.getTime()),"Christmas Day");
        Uid uid = new FixedUidGenerator("123").generateUid();
        christmas.getProperties().add(uid);
//        calendar.getComponents().add(christmas);

        //创建持续时间为 4 小时的会议
        VTimeZone tz = new VTimeZone();
        java.util.TimeZone timeZone = new SimpleTimeZone(0,"standard timezone");

        java.util.Calendar startDate = new GregorianCalendar();
//        startDate.setTimeZone(timeZone);
        startDate.set(2021,11-1,22,22,56,0);

        java.util.Calendar endDate = new GregorianCalendar();
        endDate.set(2021, 11-1,22,23,56,0);

        DateTime dateTime = new DateTime(startDate.getTime());
        DateTime end = new DateTime(endDate.getTime());

        VEvent meeting = new VEvent(dateTime,end,"Progress Meeting");
        Uid uid1 = new FixedUidGenerator("123").generateUid();
        meeting.getProperties().add(uid1);
        Description description1 = new Description();
        description1.setValue("description1");
        Description description2 = new Description();
        meeting.getProperties().add(description1);

        Location location = new Location();
        location.setValue("test location");
        meeting.getProperties().add(location);

        //添加协作者
        Attendee attendee1 = new Attendee(URI.create("mailto:111@qq.com"));
        attendee1.getParameters().add(Role.REQ_PARTICIPANT);
        attendee1.getParameters().add(new Cn("Develop 1"));
        meeting.getProperties().add(attendee1);

        calendar.getComponents().add(meeting);





        FileOutputStream fout = new FileOutputStream("mycalendar.ics");
        CalendarOutputter outputter = new CalendarOutputter();
        outputter.output(calendar,fout);

    }
}
