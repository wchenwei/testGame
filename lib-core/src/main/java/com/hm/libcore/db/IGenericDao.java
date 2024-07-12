package com.hm.libcore.db;

import java.io.Serializable;
import java.util.Collection;
import java.util.List;
import java.util.Queue;

import org.springframework.dao.DataAccessException;

/**
 * Title: IGenericDao.java
 * Description:顶级DAO接口
 * Copyright: Copyright (c) 2014
 * Company: Hammer Studio
 * @author 李飞
 * @date 2014-7-23
 * @version 1.0
 */
public interface IGenericDao {

	
	/**
	 * 增加一条数据
	 * @param sqlId
	 * @param entity
	 * @return
	 * @throws DataAccessException
	 */
	Object add(String sqlId, Object entity)throws DataAccessException;
	
	
	/**
	 * 更新数据
	 * @param sqlId
	 * @param entity
	 * @throws DataAccessException
	 */
	void update(String sqlId, Object entity)throws DataAccessException;
	
	/**
	 * 删除数据
	 * @param sqlId
	 * @param entity
	 * @throws DataAccessException
	 */
	void delete(String sqlId, Object entity)throws DataAccessException;
	
	/**
	 * 获取单条数据
	 * @param <T>
	 * @param sqlId
	 * @param id
	 * @return
	 * @throws DataAccessException
	 */
	<T> T get(String sqlId, Serializable id)throws DataAccessException;
	
	/**
	 * 获取查询结果集条数
	 * @param sqlId
	 * @param param
	 * @return
	 * @throws DataAccessException
	 */
	long getTotalRows(String sqlId, Object param)throws DataAccessException;
	
	/**
	 * 查询返回结果集
	 * @param <T>
	 * @param sqlId
	 * @param param
	 * @return
	 * @throws DataAccessException
	 */
	<T> List<T> query(String sqlId, Object param)throws DataAccessException;
	
	/**
	 * 查询返回结果集第一条数据
	 * @param <T>
	 * @param sqlId
	 * @param param
	 * @return
	 * @throws DataAccessException
	 */
	<T> T queryUnique(String sqlId, Object param)throws DataAccessException;
	
	/**
	 * 批量执行SQL语句Collection
	 * @param <T>
	 * @param sqlId
	 * @param list
	 * @return
	 * @throws DataAccessException
	 */
	<T> Queue<T> batchExecute(final String sqlId,final Collection<T> list)throws DataAccessException;
	
	/**
	 * 批量执行SQL语句Queue
	 * @param <T>
	 * @param sqlId
	 * @param queue
	 * @return
	 * @throws DataAccessException
	 */
	<T> Queue<T> batchExecute(final String sqlId,final Queue<T> queue)throws DataAccessException;
	
	
//	<T> T doInJdbc(JdbcCallback jdbcCallback,String method)throws DataAccessException;
}
