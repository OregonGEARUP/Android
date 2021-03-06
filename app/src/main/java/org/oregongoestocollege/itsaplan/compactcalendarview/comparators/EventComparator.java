package org.oregongoestocollege.itsaplan.compactcalendarview.comparators;

import java.util.Comparator;

import org.oregongoestocollege.itsaplan.compactcalendarview.domain.Event;

public class EventComparator implements Comparator<Event> {

    @Override
    public int compare(Event lhs, Event rhs) {
        return lhs.getTimeInMillis() < rhs.getTimeInMillis() ? -1 : lhs.getTimeInMillis() == rhs.getTimeInMillis() ? 0 : 1;
    }
}
