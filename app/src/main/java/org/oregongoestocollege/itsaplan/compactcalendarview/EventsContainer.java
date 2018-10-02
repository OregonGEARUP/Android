package org.oregongoestocollege.itsaplan.compactcalendarview;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.oregongoestocollege.itsaplan.compactcalendarview.comparators.EventComparator;
import org.oregongoestocollege.itsaplan.compactcalendarview.domain.Event;

/**
 * The MIT License (MIT)
 *
 * Copyright (c) [2018] [Sundeepk]
 * https://github.com/SundeepK/CompactCalendarView
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all
 * copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 */
public class EventsContainer {

    private Map<String, List<Events>> eventsByMonthAndYearMap = new HashMap<>();
    private Comparator<Event> eventsComparator = new EventComparator();
    private Calendar eventsCalendar;

    public EventsContainer(Calendar eventsCalendar) {
        this.eventsCalendar = eventsCalendar;
    }

    void addEvent(Event event) {
        eventsCalendar.setTimeInMillis(event.getTimeInMillis());
        String key = getKeyForCalendarEvent(eventsCalendar);
        List<Events> eventsForMonth = eventsByMonthAndYearMap.get(key);
        if (eventsForMonth == null) {
            eventsForMonth = new ArrayList<>();
        }
        Events eventsForTargetDay = getEventDayEvent(event.getTimeInMillis());
        if (eventsForTargetDay == null) {
            List<Event> events = new ArrayList<>();
            events.add(event);
            eventsForMonth.add(new Events(event.getTimeInMillis(), events));
        } else {
            eventsForTargetDay.getEvents().add(event);
        }
        eventsByMonthAndYearMap.put(key, eventsForMonth);
    }

    void removeAllEvents() {
        eventsByMonthAndYearMap.clear();
    }

    void addEvents(List<Event> events) {
        int count = events.size();
        for (int i = 0; i < count; i++) {
            addEvent(events.get(i));
        }
    }

    List<Event> getEventsFor(long epochMillis) {
        Events events = getEventDayEvent(epochMillis);
        if (events == null) {
            return new ArrayList<>();
        } else {
            return events.getEvents();
        }
    }

    List<Events> getEventsForMonthAndYear(int month, int year){
        return eventsByMonthAndYearMap.get(year + "_" + month);
    }

    List<Event> getEventsForMonth(long eventTimeInMillis){
        eventsCalendar.setTimeInMillis(eventTimeInMillis);
        String keyForCalendarEvent = getKeyForCalendarEvent(eventsCalendar);
        List<Events> events = eventsByMonthAndYearMap.get(keyForCalendarEvent);
        List<Event> allEventsForMonth = new ArrayList<>();
        if (events != null) {
            for(Events eve : events){
                if (eve != null) {
                    allEventsForMonth.addAll(eve.getEvents());
                }
            }
        }
        Collections.sort(allEventsForMonth, eventsComparator);
        return allEventsForMonth;
    }

    private Events getEventDayEvent(long eventTimeInMillis){
        eventsCalendar.setTimeInMillis(eventTimeInMillis);
        int dayInMonth = eventsCalendar.get(Calendar.DAY_OF_MONTH);
        String keyForCalendarEvent = getKeyForCalendarEvent(eventsCalendar);
        List<Events> eventsForMonthsAndYear = eventsByMonthAndYearMap.get(keyForCalendarEvent);
        if (eventsForMonthsAndYear != null) {
            for (Events events : eventsForMonthsAndYear) {
                eventsCalendar.setTimeInMillis(events.getTimeInMillis());
                int dayInMonthFromCache = eventsCalendar.get(Calendar.DAY_OF_MONTH);
                if (dayInMonthFromCache == dayInMonth) {
                    return events;
                }
            }
        }
        return null;
    }

    void removeEventByEpochMillis(long epochMillis) {
        eventsCalendar.setTimeInMillis(epochMillis);
        int dayInMonth = eventsCalendar.get(Calendar.DAY_OF_MONTH);
        String key = getKeyForCalendarEvent(eventsCalendar);
        List<Events> eventsForMonthAndYear = eventsByMonthAndYearMap.get(key);
        if (eventsForMonthAndYear != null) {
            Iterator<Events> calendarDayEventIterator = eventsForMonthAndYear.iterator();
            while (calendarDayEventIterator.hasNext()) {
                Events next = calendarDayEventIterator.next();
                eventsCalendar.setTimeInMillis(next.getTimeInMillis());
                int dayInMonthFromCache = eventsCalendar.get(Calendar.DAY_OF_MONTH);
                if (dayInMonthFromCache == dayInMonth) {
                    calendarDayEventIterator.remove();
                    break;
                }
            }
            if (eventsForMonthAndYear.isEmpty()) {
                eventsByMonthAndYearMap.remove(key);
            }
        }
    }

    void removeEvent(Event event) {
        eventsCalendar.setTimeInMillis(event.getTimeInMillis());
        String key = getKeyForCalendarEvent(eventsCalendar);
        List<Events> eventsForMonthAndYear = eventsByMonthAndYearMap.get(key);
        if (eventsForMonthAndYear != null) {
            Iterator<Events> eventsForMonthYrItr = eventsForMonthAndYear.iterator();
            while(eventsForMonthYrItr.hasNext()) {
                Events events = eventsForMonthYrItr.next();
                int indexOfEvent = events.getEvents().indexOf(event);
                if (indexOfEvent >= 0) {
                    if (events.getEvents().size() == 1) {
                        eventsForMonthYrItr.remove();
                    } else {
                        events.getEvents().remove(indexOfEvent);
                    }
                    break;
                }
            }
            if (eventsForMonthAndYear.isEmpty()) {
                eventsByMonthAndYearMap.remove(key);
            }
        }
    }

    void removeEvents(List<Event> events) {
        int count = events.size();
        for (int i = 0; i < count; i++) {
            removeEvent(events.get(i));
        }
    }

    //E.g. 4 2016 becomes 2016_4
    private String getKeyForCalendarEvent(Calendar cal) {
        return cal.get(Calendar.YEAR) + "_" + cal.get(Calendar.MONTH);
    }

}
