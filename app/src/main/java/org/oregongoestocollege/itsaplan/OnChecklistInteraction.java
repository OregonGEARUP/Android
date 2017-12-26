package org.oregongoestocollege.itsaplan;

import org.oregongoestocollege.itsaplan.data.BlockInfo;
import org.oregongoestocollege.itsaplan.data.Stage;

/**
 * Oregon GEAR UP App
 * Copyright © 2017 Oregon GEAR UP. All rights reserved.
 */
public interface OnChecklistInteraction
{
	void onShowBlock(BlockInfo blockInfo);
	void onShowStage(Stage stage);
}
