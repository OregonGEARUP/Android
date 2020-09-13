package org.oregongoestocollege.itsaplan.data;

import java.util.Date;

import androidx.room.Entity;
import androidx.room.PrimaryKey;
import androidx.annotation.NonNull;

import static com.google.common.base.Preconditions.checkNotNull;

/**
 * TestResult
 * Oregon GEAR UP App
 *
 * Copyright © 2020 Oregon GEAR UP. All rights reserved.
 */
@Entity(tableName = "testresult_table")
public class TestResult
{
	/** preset name for the ACT test results */
	public static final String NAME_ACT = "act";
	/** preset name for the SAT test results */
	public static final String NAME_SAT = "sat";

	@PrimaryKey
	@NonNull
	private String name;
	// date of exam
	private Date date;
	// ACT: composite and SAT: total
	private String composite;
	// ACT: math and SAT: math
	private String math;
	// ACT: science and SAT: n/a
	private String science;
	// ACT: reading and SAT: reading/writing
	private String reading;
	// ACT: writing and SAT: essay
	private String writing;
	// ACT: english and SAT: n/a
	private String english;

	public TestResult(@NonNull String name)
	{
		this.name = checkNotNull(name);
	}

	@NonNull
	public String getName()
	{
		return name;
	}

//	public void setName(@NonNull String name)
//	{
//		this.name = checkNotNull(name);
//	}

	public Date getDate()
	{
		return date;
	}

	public void setDate(Date date)
	{
		this.date = date;
	}

	public String getComposite()
	{
		return composite;
	}

	public void setComposite(String composite)
	{
		this.composite = composite;
	}

	public String getMath()
	{
		return math;
	}

	public void setMath(String math)
	{
		this.math = math;
	}

	public String getScience()
	{
		return science;
	}

	public void setScience(String science)
	{
		this.science = science;
	}

	public String getReading()
	{
		return reading;
	}

	public void setReading(String reading)
	{
		this.reading = reading;
	}

	public String getWriting()
	{
		return writing;
	}

	public void setWriting(String writing)
	{
		this.writing = writing;
	}

	public String getEnglish()
	{
		return english;
	}

	public void setEnglish(String english)
	{
		this.english = english;
	}

	public boolean hasTestDate()
	{
		return date != null && date.getTime() > 0;
	}
}
