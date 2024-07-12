package com.hm.model.activity;

import java.util.Set;

/**
 * 和服时合并活动排行
 * @author siyunlong
 *
 */
public interface IMergeRankAct {
	//和服时要合并的排行榜
	Set<String> getMergeRankNames();
}
