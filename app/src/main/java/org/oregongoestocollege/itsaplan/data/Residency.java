package org.oregongoestocollege.itsaplan.data;

import java.util.Date;

import androidx.room.Entity;
import androidx.room.PrimaryKey;
import androidx.annotation.NonNull;

import static com.google.common.base.Preconditions.checkNotNull;

/**
 * Residency
 * Oregon GEAR UP App
 *
 * Copyright © 2018 Oregon GEAR UP. All rights reserved.
 */
@Entity(tableName = "residency_table")
public class Residency
{
	/** preset name for our only Residency entry */
	public static final String NAME_PRIMARY = "primary";

	// more efficient to group the data into text1/text2/date1/date2 but makes
	// the data entry UX more complicated, leaving as is for now, may revisit
	@PrimaryKey
	@NonNull
	private String uid;
	private String text1;
	private String text2;
	private Date date1;
	private Date date2;
	private Date residencyStart;
	private Date residencyEnd;
	private Date parentResidencyStart;
	private Date parentResidencyEnd;
	private Date registerToVote;
	private Date parentsRegisterToVote;
	private Date militaryServiceStart;
	private Date militaryServiceEnd;
	private Date parentMilitaryServiceStart;
	private Date parentMilitaryServiceEnd;
	private Date fileOregonTaxesYear1;
	private Date fileOregonTaxesYear2;
	private Date parentsFileOregonTaxesYear1;
	private Date parentsFileOregonTaxesYear2;
	private String nameEmployer1;
	private String cityEmployer1;
	private Date startEmployer1;
	private Date endEmployer1;
	private String nameEmployer2;
	private String cityEmployer2;
	private Date startEmployer2;
	private Date endEmployer2;
	private String parentNameEmployer1;
	private String parentCityEmployer1;
	private Date parentStartEmployer1;
	private Date parentEndEmployer1;
	private String parentNameEmployer2;
	private String parentCityEmployer2;
	private Date parentStartEmployer2;
	private Date parentEndEmployer2;

	public Residency(@NonNull String uid)
	{
		this.uid = checkNotNull(uid);
	}

	@NonNull
	public String getUid()
	{
		return uid;
	}

//	public void setUid(@NonNull String uid)
//	{
//		this.uid = uid;
//	}

	public String getText1()
	{
		return text1;
	}

	public void setText1(String text1)
	{
		this.text1 = text1;
	}

	public String getText2()
	{
		return text2;
	}

	public void setText2(String text2)
	{
		this.text2 = text2;
	}

	public Date getDate1()
	{
		return date1;
	}

	public void setDate1(Date date1)
	{
		this.date1 = date1;
	}

	public Date getDate2()
	{
		return date2;
	}

	public void setDate2(Date date2)
	{
		this.date2 = date2;
	}

	public Date getResidencyStart()
	{
		return residencyStart;
	}

	public void setResidencyStart(Date residencyStart)
	{
		this.residencyStart = residencyStart;
	}

	public Date getResidencyEnd()
	{
		return residencyEnd;
	}

	public void setResidencyEnd(Date residencyEnd)
	{
		this.residencyEnd = residencyEnd;
	}

	public Date getParentResidencyStart()
	{
		return parentResidencyStart;
	}

	public void setParentResidencyStart(Date parentResidencyStart)
	{
		this.parentResidencyStart = parentResidencyStart;
	}

	public Date getParentResidencyEnd()
	{
		return parentResidencyEnd;
	}

	public void setParentResidencyEnd(Date parentResidencyEnd)
	{
		this.parentResidencyEnd = parentResidencyEnd;
	}

	public Date getRegisterToVote()
	{
		return registerToVote;
	}

	public void setRegisterToVote(Date registerToVote)
	{
		this.registerToVote = registerToVote;
	}

	public Date getParentsRegisterToVote()
	{
		return parentsRegisterToVote;
	}

	public void setParentsRegisterToVote(Date parentsRegisterToVote)
	{
		this.parentsRegisterToVote = parentsRegisterToVote;
	}

	public Date getMilitaryServiceStart()
	{
		return militaryServiceStart;
	}

	public void setMilitaryServiceStart(Date militaryServiceStart)
	{
		this.militaryServiceStart = militaryServiceStart;
	}

	public Date getMilitaryServiceEnd()
	{
		return militaryServiceEnd;
	}

	public void setMilitaryServiceEnd(Date militaryServiceEnd)
	{
		this.militaryServiceEnd = militaryServiceEnd;
	}

	public Date getParentMilitaryServiceStart()
	{
		return parentMilitaryServiceStart;
	}

	public void setParentMilitaryServiceStart(Date parentMilitaryServiceStart)
	{
		this.parentMilitaryServiceStart = parentMilitaryServiceStart;
	}

	public Date getParentMilitaryServiceEnd()
	{
		return parentMilitaryServiceEnd;
	}

	public void setParentMilitaryServiceEnd(Date parentMilitaryServiceEnd)
	{
		this.parentMilitaryServiceEnd = parentMilitaryServiceEnd;
	}

	public Date getFileOregonTaxesYear1()
	{
		return fileOregonTaxesYear1;
	}

	public void setFileOregonTaxesYear1(Date fileOregonTaxesYear1)
	{
		this.fileOregonTaxesYear1 = fileOregonTaxesYear1;
	}

	public Date getFileOregonTaxesYear2()
	{
		return fileOregonTaxesYear2;
	}

	public void setFileOregonTaxesYear2(Date fileOregonTaxesYear2)
	{
		this.fileOregonTaxesYear2 = fileOregonTaxesYear2;
	}

	public Date getParentsFileOregonTaxesYear1()
	{
		return parentsFileOregonTaxesYear1;
	}

	public void setParentsFileOregonTaxesYear1(Date parentsFileOregonTaxesYear1)
	{
		this.parentsFileOregonTaxesYear1 = parentsFileOregonTaxesYear1;
	}

	public Date getParentsFileOregonTaxesYear2()
	{
		return parentsFileOregonTaxesYear2;
	}

	public void setParentsFileOregonTaxesYear2(Date parentsFileOregonTaxesYear2)
	{
		this.parentsFileOregonTaxesYear2 = parentsFileOregonTaxesYear2;
	}

	public String getNameEmployer1()
	{
		return nameEmployer1;
	}

	public void setNameEmployer1(String nameEmployer1)
	{
		this.nameEmployer1 = nameEmployer1;
	}

	public String getCityEmployer1()
	{
		return cityEmployer1;
	}

	public void setCityEmployer1(String cityEmployer1)
	{
		this.cityEmployer1 = cityEmployer1;
	}

	public Date getStartEmployer1()
	{
		return startEmployer1;
	}

	public void setStartEmployer1(Date startEmployer1)
	{
		this.startEmployer1 = startEmployer1;
	}

	public Date getEndEmployer1()
	{
		return endEmployer1;
	}

	public void setEndEmployer1(Date endEmployer1)
	{
		this.endEmployer1 = endEmployer1;
	}

	public String getNameEmployer2()
	{
		return nameEmployer2;
	}

	public void setNameEmployer2(String nameEmployer2)
	{
		this.nameEmployer2 = nameEmployer2;
	}

	public String getCityEmployer2()
	{
		return cityEmployer2;
	}

	public void setCityEmployer2(String cityEmployer2)
	{
		this.cityEmployer2 = cityEmployer2;
	}

	public Date getStartEmployer2()
	{
		return startEmployer2;
	}

	public void setStartEmployer2(Date startEmployer2)
	{
		this.startEmployer2 = startEmployer2;
	}

	public Date getEndEmployer2()
	{
		return endEmployer2;
	}

	public void setEndEmployer2(Date endEmployer2)
	{
		this.endEmployer2 = endEmployer2;
	}

	public String getParentNameEmployer1()
	{
		return parentNameEmployer1;
	}

	public void setParentNameEmployer1(String parentNameEmployer1)
	{
		this.parentNameEmployer1 = parentNameEmployer1;
	}

	public String getParentCityEmployer1()
	{
		return parentCityEmployer1;
	}

	public void setParentCityEmployer1(String parentCityEmployer1)
	{
		this.parentCityEmployer1 = parentCityEmployer1;
	}

	public Date getParentStartEmployer1()
	{
		return parentStartEmployer1;
	}

	public void setParentStartEmployer1(Date parentStartEmployer1)
	{
		this.parentStartEmployer1 = parentStartEmployer1;
	}

	public Date getParentEndEmployer1()
	{
		return parentEndEmployer1;
	}

	public void setParentEndEmployer1(Date parentEndEmployer1)
	{
		this.parentEndEmployer1 = parentEndEmployer1;
	}

	public String getParentNameEmployer2()
	{
		return parentNameEmployer2;
	}

	public void setParentNameEmployer2(String parentNameEmployer2)
	{
		this.parentNameEmployer2 = parentNameEmployer2;
	}

	public String getParentCityEmployer2()
	{
		return parentCityEmployer2;
	}

	public void setParentCityEmployer2(String parentCityEmployer2)
	{
		this.parentCityEmployer2 = parentCityEmployer2;
	}

	public Date getParentStartEmployer2()
	{
		return parentStartEmployer2;
	}

	public void setParentStartEmployer2(Date parentStartEmployer2)
	{
		this.parentStartEmployer2 = parentStartEmployer2;
	}

	public Date getParentEndEmployer2()
	{
		return parentEndEmployer2;
	}

	public void setParentEndEmployer2(Date parentEndEmployer2)
	{
		this.parentEndEmployer2 = parentEndEmployer2;
	}
}
