package org.oregongoestocollege.itsaplan.data;

import java.util.List;

import android.arch.lifecycle.LiveData;
import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Entity;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.PrimaryKey;
import android.arch.persistence.room.Query;
import android.support.annotation.NonNull;

import static android.arch.persistence.room.OnConflictStrategy.REPLACE;

/**
 * College
 * Oregon GEAR UP App
 *
 * Copyright © 2018 Oregon GEAR UP. All rights reserved.
 */
@Entity
public class College
{
	@PrimaryKey @NonNull
	private String uuid;
	private String name;
	private long applicationDate;
	private double averageNetPrice;
	private double applicationCost;
	private boolean essayDone;
	private boolean recommendationsDone;
	private boolean activitiesChartDone;
	private boolean testsDone;
	private boolean addlFinancialAidDone;
	private boolean addlScholarshipDone;
	private boolean feeDeferralDone;
	private boolean applicationDone;

	public String getUuid()
	{
		return uuid;
	}

	public void setUuid(String uuid)
	{
		this.uuid = uuid;
	}

	public String getName()
	{
		return name;
	}

	public void setName(String name)
	{
		this.name = name;
	}

	public long getApplicationDate()
	{
		return applicationDate;
	}

	public void setApplicationDate(long applicationDate)
	{
		this.applicationDate = applicationDate;
	}

	public double getAverageNetPrice()
	{
		return averageNetPrice;
	}

	public void setAverageNetPrice(double averageNetPrice)
	{
		this.averageNetPrice = averageNetPrice;
	}

	public double getApplicationCost()
	{
		return applicationCost;
	}

	public void setApplicationCost(double applicationCost)
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

	@Dao
	public interface CollegeDao
	{
		@Insert(onConflict = REPLACE)
		void save(College college);

		@Query("SELECT * FROM college WHERE name = :name")
		LiveData<College> load(String name);

		@Query("SELECT * from college")
		LiveData<List<College>> getAll();
	}
}
