package org.oregongoestocollege.itsaplan.compactcalendarview;

import java.text.DateFormatSymbols;
import java.util.Arrays;
import java.util.Locale;

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
public class WeekUtils {

    static String[] getWeekdayNames(Locale locale, int day, boolean useThreeLetterAbbreviation){
        DateFormatSymbols dateFormatSymbols = new DateFormatSymbols(locale);
        String[] dayNames = dateFormatSymbols.getShortWeekdays();
        if (dayNames == null) {
            throw new IllegalStateException("Unable to determine weekday names from default locale");
        }
        if (dayNames.length != 8) {
            throw new IllegalStateException("Expected weekday names from default locale to be of size 7 but: "
                    + Arrays.toString(dayNames) + " with size " + dayNames.length + " was returned.");
        }

        String[] weekDayNames = new String[7];
        String[] weekDaysFromSunday = {dayNames[1], dayNames[2], dayNames[3], dayNames[4], dayNames[5], dayNames[6], dayNames[7]};
        for (int currentDay = day - 1, i = 0; i <= 6; i++, currentDay++) {
            currentDay = currentDay >= 7 ? 0 : currentDay;
            weekDayNames[i] = weekDaysFromSunday[currentDay];
        }

        if (!useThreeLetterAbbreviation) {
            for (int i = 0; i < weekDayNames.length; i++) {
                weekDayNames[i] = weekDayNames[i].substring(0, 1);
            }
        }

        return weekDayNames;
    }


}
