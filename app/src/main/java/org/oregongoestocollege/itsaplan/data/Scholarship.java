package org.oregongoestocollege.itsaplan.data;

import java.util.Date;

import android.arch.persistence.room.Entity;
import android.arch.persistence.room.PrimaryKey;
import android.support.annotation.NonNull;

/**
 * Scholarship
 * Oregon GEAR UP App
 *
 * Copyright © 2018 Oregon GEAR UP. All rights reserved.
 */
@Entity(tableName = "scholarship_table")
public class Scholarship
{
	@PrimaryKey(autoGenerate = true)
	private int uid;
	private String name;
	private Date applicationDate;
	private String website;
	private String otherInfo;
	private boolean essayDone;
	private boolean recommendationsDone;
	private boolean activitiesChartDone;
	private boolean testsDone;
	private boolean applicationDone;

	@NonNull
	public int getUid()
	{
		return uid;
	}

	public void setUid(int uid)
	{
		this.uid = uid;
	}

	public String getName()
	{
		return name;
	}

	public void setName(String name)
	{
		this.name = name;
	}

	public Date getApplicationDate()
	{
		return applicationDate;
	}

	public void setApplicationDate(Date applicationDate)
	{
		this.applicationDate = applicationDate;
	}

	public String getWebsite()
	{
		return website;
	}

	public void setWebsite(String website)
	{
		this.website = website;
	}

	public String getOtherInfo()
	{
		return otherInfo;
	}

	public void setOtherInfo(String otherInfo)
	{
		this.otherInfo = otherInfo;
	}

	public boolean isEssayDone()
	{
		return essayDone;
	}

	public void setEssayDone(boolean essayDone)
	{
		this.essayDone = essayDone;
	}

	public boolean isRecommendationsDone()
	{
		return recommendationsDone;
	}

	public void setRecommendationsDone(boolean recommendationsDone)
	{
		this.recommendationsDone = recommendationsDone;
	}

	public boolean isActivitiesChartDone()
	{
		return activitiesChartDone;
	}

	public void setActivitiesChartDone(boolean activitiesChartDone)
	{
		this.activitiesChartDone = activitiesChartDone;
	}

	public boolean isTestsDone()
	{
		return testsDone;
	}

	public void setTestsDone(boolean testsDone)
	{
		this.testsDone = testsDone;
	}

	public boolean isApplicationDone()
	{
		return applicationDone;
	}

	public void setApplicationDone(boolean applicationDone)
	{
		this.applicationDone = applicationDone;
	}
}
