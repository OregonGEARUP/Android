package org.oregongoestocollege.itsaplan.data;

import java.util.List;

/**
 * CalendarEventData - POJO to retrieve data from network
 * Oregon GEAR UP App
 *
 * Copyright © 2018 Oregon GEAR UP. All rights reserved.
 */
public class CalendarEventData
{
	public List<String> date;
	public List<String> description;
	public String reminderId;
	public String reminder;
	public int reminderDelta;
}
