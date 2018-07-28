package org.oregongoestocollege.itsaplan.data;

import java.util.List;

import android.text.TextUtils;

import com.google.gson.annotations.SerializedName;

/**
 * Checkpoint
 * Oregon GEAR UP App
 *
 * Copyright © 2017 Oregon GEAR UP. All rights reserved.
 */
public class Checkpoint
{
	public String id;
	public String requiredCP;
	public String title;
	public String description;
	public String url;
	public String urlText;
	public String routeFileName;
	@SerializedName("type")
	public EntryType entryType;
	public List<Instance> instances;
	public List<KeyValue> criteria;

	public Checkpoint()
	{

	}

	public Checkpoint(String id, EntryType entryType)
	{
		this.id = id;
		this.entryType = entryType;
	}

	public boolean required()
	{
		return "yes".equals(requiredCP);
	}

	public boolean isCompleted(int stageIndex, int checkpointIndex)
	{
		if (!required())
			return true;

		// TODO - check based on EntryType
		switch (entryType)
		{
		case info:
		case checkbox:
		case route:
		case nextstage:
			return true;
		case radio:
			// todo
			break;
		case field:
		case dateOnly:
			// todo
			break;
		case dateAndText:
			// todo
			break;
		}

		return true;
	}

	public boolean meetsCriteria()
	{
		boolean meets = true;

		if (criteria != null)
		{
			// check to see that all criteria are met
			for (KeyValue criterion : criteria)
			{
				// empty keys are a match
				if (TextUtils.isEmpty(criterion.key))
					continue;

				// check that value for the key matches the expected value
				UserEntries entries = UserEntries.getInstance();
				String expectedValue = criterion.value;
				String actualValue = entries.getValue(criterion.key);

				meets = TextUtils.isEmpty(expectedValue) || (!TextUtils.isEmpty(actualValue) && expectedValue.compareToIgnoreCase(actualValue) == 0);

				if (!meets)
					break;
			}
		}

		// make sure we have a route destination
		if (meets && TextUtils.isEmpty(routeFileName))
			meets = false;

		return meets;
	}

	public String getVerifiedUrlText()
	{
		return (!TextUtils.isEmpty(url) && !TextUtils.isEmpty(urlText)) ? urlText : null;
	}
}
