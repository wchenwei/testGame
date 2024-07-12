package com.hm.war.sg;

import com.google.common.collect.Lists;
import com.hm.war.sg.buff.UnitBufferType;

import java.util.List;

public class WarComm {
	public static final int MaxFrame = 3000;
	public static final int MoraleInterval = 10;

	public static int MLNormalSkillId = 20101;
	public static int MLBigSkillId = 20102;


	public static final List<UnitBufferType> NoAtkBuffList =
			Lists.newArrayList(UnitBufferType.StunBuff,UnitBufferType.NoAtkBuff,UnitBufferType.FixedBodyBuff);
}
