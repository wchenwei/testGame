package com.hm.leaderboards.activity;

import com.google.common.collect.Lists;
import org.springframework.data.annotation.Transient;

import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.locks.ReentrantReadWriteLock;

/**
 * @Description: 排行列表
 * @author siyunlong  
 * @date 2018年5月28日 下午1:25:00 
 * @version V1.0
 */
public class RankList {
	private int maxLine = 1000;
	private LinkedList<RankLine> rankList = Lists.newLinkedList();
	//读和读互不影响，读和写互斥，写和写互斥
	@Transient
	private transient  ReentrantReadWriteLock lock = new ReentrantReadWriteLock(); 
	
	public RankList(int maxLine) {
		this.maxLine = maxLine;
	}

	public RankList(int maxLine, LinkedList<RankLine> rankList) {
		super();
		this.maxLine = maxLine;
		this.rankList = rankList;
	}


	public RankList() {
		super();
	}
	
	public boolean addLine(RankLine line) {
		try {
			lock.writeLock().lock();
			if(checkLine(line)) {
//				Collections.sort(rankList);
				sort(line);
				if(this.rankList.size() > maxLine) {
					this.rankList.remove(this.rankList.size()-1);
				}
				for (int i = 0; i < Math.min(this.rankList.size(), maxLine); i++) {
					this.rankList.get(i).setRank(i+1);
				}
				return true;
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			lock.writeLock().unlock();
		}
		return false;
	} 
	
	private boolean checkLine(RankLine line) {
		int size = rankList.size();
		if(size > 0) {
			if(size >= maxLine && line.compareTo(rankList.get(size-1))<0) {
				return false;
			}
//			//删除自己原来的数据
//			for (int i = rankList.size()-1; i >= 0; i--) {
//				RankLine temp = rankList.get(i);
//				if(temp.getId() == line.getId()) {
//					rankList.remove(0);
//					return true;
//				}
//			}
		}
		return true;
	}
	
	private void sort(RankLine line) {
		for (int i = rankList.size()-1; i >= 0; i--) {
			RankLine temp = rankList.get(i);
			if(temp.getId() == line.getId()) {
				rankList.remove(i);
				continue;
			}
			if(line.compareTo(temp) <= 0) {
				rankList.add(i+1, line);
				return;
			}
		}
		rankList.add(0,line);
		
	}
	
	public List<RankLine> getRankList() {
		try {
			lock.readLock().lock();
			return rankList;
		}finally {
			lock.readLock().unlock();
		}
	}
	
	/**
	 * 获取玩家排行
	 * @param playerId
	 * @return
	 */
	public RankLine getRankLine(long playerId) {
		try {
			lock.readLock().lock();
			for (int i = rankList.size()-1; i >= 0; i--) {
				RankLine temp = rankList.get(i);
				if(temp.getId() == playerId) {
					return temp;
				}
			}
		}finally {
			lock.readLock().unlock();
		}
		return null;
	}
	
	public int getRank(long playerId) {
		RankLine rankLine = getRankLine(playerId);
		return rankLine != null?rankLine.getRank():-1;
	}

	
}
