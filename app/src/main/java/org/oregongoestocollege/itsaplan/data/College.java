package org.oregongoestocollege.itsaplan.data;

import java.util.Date;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

/**
 * College
 * Oregon GEAR UP App
 *
 * Copyright © 2018 Oregon GEAR UP. All rights reserved.
 */
@Entity(tableName = "college_table")
public class College
{
	@PrimaryKey(autoGenerate = true)
	private int uid;
	private String name;
	private Date applicationDate;
	private String averageNetPrice;
	private String applicationCost;
	private boolean essayDone;
	private boolean recommendationsDone;
	private boolean activitiesChartDone;
	private boolean testsDone;
	private boolean addlFinancialAidDone;
	private boolean addlScholarshipDone;
	private boolean feeDeferralDone;
	private boolean applicationDone;

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

	public String getAverageNetPrice()
	{
		return averageNetPrice;
	}

	public void setAverageNetPrice(String averageNetPrice)
	{
		this.averageNetPrice = averageNetPrice;
	}

	public String getApplicationCost()
	{
		return applicationCost;
	}

	public void setApplicationCost(String applicationCost)
	{
		this.applicationCost = applicationCost;
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

	public boolean isAddlFinancialAidDone()
	{
		return addlFinancialAidDone;
	}

	public void setAddlFinancialAidDone(boolean addlFinancialAidDone)
	{
		this.addlFinancialAidDone = addlFinancialAidDone;
	}

	public boolean isAddlScholarshipDone()
	{
		return addlScholarshipDone;
	}

	public void setAddlScholarshipDone(boolean addlScholarshipDone)
	{
		this.addlScholarshipDone = addlScholarshipDone;
	}

	public boolean isFeeDeferralDone()
	{
		return feeDeferralDone;
	}

	public void setFeeDeferralDone(boolean feeDeferralDone)
	{
		this.feeDeferralDone = feeDeferralDone;
	}

	public boolean isApplicationDone()
	{
		return applicationDone;
	}

	public void setApplicationDone(boolean applicationDone)
	{
		this.applicationDone = applicationDone;
	}

	public boolean hasApplicationDate()
	{
		return applicationDate != null && applicationDate.getTime() > 0;
	}
}
