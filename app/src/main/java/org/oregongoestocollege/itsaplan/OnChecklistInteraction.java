package org.oregongoestocollege.itsaplan;

/**
 * Oregon GEAR UP App
 * Copyright © 2017 Oregon GEAR UP. All rights reserved.
 */
public interface OnChecklistInteraction
{
	void onShowBlock(int blockIndex, String blockFileName);
	void onShowStage(int blockIndex, int stageIndex);
}
