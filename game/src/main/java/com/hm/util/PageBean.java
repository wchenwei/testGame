package com.hm.util;

import java.util.Collections;
import java.util.List;

/**
 * 分页
 * Title: PageBean.java
 * Description:
 * Copyright: Copyright (c) 2014
 * Company: Hammer Studio
 * @author 李飞
 * @date 2015年6月17日 上午11:18:37
 * @version 1.0
 */
public class PageBean<T> {

	private int firstPage = 1;//首页

	private int nowPage;// 当前页

	private int perPage;// 上一页

	private int nextPage;// 下一页

	private int totalCount;// 总记录条数

	private int pageSize;// 每页显示条数

	private int totalPage=1;// 总页数

	private List<T> data;//分页后的记录集合

	private boolean hasPre;// 是否有上一页

	private boolean hasNext;// 是否有下一页

	private int LastPage;//末页
	
	private int quitTime;//现在距离上次退出部落时间的时长

	/**
	 * 获得首页
	 * @return
	 */
	public int getFirstPage() {
		return firstPage;
	}

	/**
	 * 获得末页
	 * @return
	 */
	public int getLastPage() {
		LastPage = this.getTotalPage();
		return LastPage;
	}

	/**
	 * 构造方法
	 * @param nowPage 当前页数
	 * @param pageSize 每页显示条数
	 * @param totalCount 总记录条数
	 */
	public PageBean(int nowPage, int pageSize, int totalCount) {
		this.nowPage = nowPage;
		this.pageSize = pageSize;
		this.totalCount = totalCount;
		this.totalPage = this.getTotalPage();
	}
	
	public PageBean(int nowPage, int pageSize) {
		this.nowPage = nowPage;
		this.pageSize = pageSize;
	}
	
	public PageBean(int nowPage ,int pageSize, int totalCount,List<T> data) {
		this.nowPage = nowPage;
		this.pageSize = pageSize;
		this.totalCount = totalCount;
		this.totalPage = this.getTotalPage();
		if(this.getTotalPage()==0){
			this.nowPage=0;
		}
		this.data=data;
		this.LastPage = this.getTotalPage();
	}
	
	 public PageBean() {
	        this(1, 0, 0, Collections.EMPTY_LIST);
	 }
	
	/**
     * 默认的页面可装载的数据条目总和
     */
    public static final int DEFAULT_DATA_SIZE_IN_ONE_PAGE = 10;
    
	/**
     * 获取页面的第一条数据在所有数据中的索引
     * 默认每页可装载的数据最大为10，第2页的第一条数据在所有数据中的索引为50
     */
    public static int getStartOfPage(int pageNo) {
        return getStartOfPage(pageNo, DEFAULT_DATA_SIZE_IN_ONE_PAGE);
    }
    
    /**
     * 获取页面的第一条数据在所有数据中的索引 例如，每页可装载的数据最大数为30，第3页的第一条数据在所有数据中的索引为91
     */
    public static int getStartOfPage(int pageNo, int pageSize) {
        if (1 > pageNo)
            throw new IllegalArgumentException("当前页数不能小于1!");
        return (pageNo-1) * pageSize;
    }

	/**
	 * 获得总页数
	 * 
	 * @return
	 */
	public int getTotalPage() {
		if(pageSize == 0) return 0; 
		int totalPage = totalCount / pageSize;
		/** 如果总条数除每页显示条数有余数 即每页显示5条 总共有12条 12/5等于2余2 那么实际上应该有3页 */
		if (totalCount % pageSize > 0)
			totalPage++;
		this.totalPage = totalPage;
		return totalPage;
	}
	
	

	/**
	 * 是否有上一页
	 * 
	 * @return
	 */
	public boolean isHasPre() {
		hasPre = (totalCount > pageSize && nowPage > 1);
		return hasPre;
	}

	/**
	 * 获得上一页
	 * @return
	 */
	public int getPerPage() {
		perPage = this.nowPage - 1;
		return perPage;
	}

	/**
	 * 是否有下一页
	 * 
	 * @return
	 */
	public boolean isHasNext() {
		this.hasNext = (getTotalPage() > 1 && nowPage < totalPage);
		return hasNext;
	}

	/**
	 * 获得下一页
	 * @return
	 */
	public int getNextPage() {
		nextPage = this.nowPage + 1;
		return nextPage;
	}

	/**
	 * 获得分页后的记录集合
	 * @return
	 */
	public List<T> getData() {
		return data;
	}

	public void setData(List<T> data) {
		this.data = data;
	}

	/**
	 * 获得当前页
	 * @return
	 */
	public int getNowPage() {
		return nowPage;
	}

	public void setNowPage(int nowPage) {
		this.nowPage = nowPage;
	}

	/**
	 * 获得每页显示记录条数
	 * @return
	 */
	public int getPageSize() {
		return pageSize;
	}

	public void setPageSize(int pageSize) {
		this.pageSize = pageSize;
	}

	/**
	 * 获得总记录条数
	 * @return
	 */
	public int getTotalCount() {
		return totalCount;
	}

	public void setTotalCount(int totalCount) {
		this.totalCount = totalCount;
	}

	public String toString(){
		return "totalPage:"+getTotalPage()+" totalCount:"+getTotalCount()+" nowPage:"+getNowPage()+" pageSize:"+getPageSize();
	}

	public int getQuitTime() {
		return quitTime;
	}

	public void setQuitTime(int quitTime) {
		this.quitTime = quitTime;
	}

	public static void main(String[] args) {
		System.out.println(getStartOfPage(2, 15));
		System.out.println(getStartOfPage(2));
	}
}
