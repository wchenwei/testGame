package com.hm.db;

import com.hm.libcore.mongodb.MongoUtils;
import com.hm.libcore.mongodb.MongodDB;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;

import java.util.Collection;
import java.util.List;

/**
 * ClassName: CommonDbUtil. <br/>
 * Function: 公共dbutil. <br/>
 * Reason: TODO ADD REASON(可选). <br/>
 * date: 2018年5月23日 下午5:47:43 <br/>
 *
 * @author zxj
 * @version @param <T>
 */
public class CommonDbUtil<T> {
    public static boolean insert(Object entity) {
        getMongoTemplate().save(entity);
        return true;
    }

    public static <T> boolean insertAll(Collection<T> object) {
        getMongoTemplate().insertAll(object);
        return true;
    }

    public static boolean update(Object entity) {
        getMongoTemplate().save(entity);
        return true;
    }

    public static <T> boolean deleteById(Object id, Class<T> entityClass) {
        getMongoTemplate().delete(id, entityClass);
        return true;
    }

    public static <T> Object selectById(Object id, Class<T> entityClass) {
        return getMongoTemplate().get(id, entityClass.getClass());
    }

    public static <T> List<T> selectByIds(List<Object> ids, Class<T> entityClass) {
        Query query = Query.query(Criteria.where("_id").in(ids));
        return getMongoTemplate().query(query, entityClass);
    }

    public static <T> boolean deleteBatchIds(List<Object> ids, Class<T> entityClass) {
        getMongoTemplate().deleteBatchIds(ids, entityClass);
        return true;
    }

    @SuppressWarnings("deprecation")
//	public static<T> List<T> queryAll(Class<T> entityClass) {
//		Query query = new Query().with(new Sort(new Order(Direction.DESC,"_id")));
//		return getMongoTemplate().query(query, entityClass);
//	}
//	@SuppressWarnings("deprecation")
//	public static<T> int count(Class<T> entityClass) {
//		Query query = new Query().with(new Sort(new Order(Direction.DESC,"_id")));
//		return (int)getMongoTemplate().count(query, entityClass);
//	}
//	@SuppressWarnings("deprecation")
//	public static<T> List<T> queryAllByWhere(Criteria criteria, Class<T> entityClass) {
//		Query query = new Query().with(new Sort(new Order(Direction.DESC,"_id")));
//		query.addCriteria(criteria);
//		return getMongoTemplate().query(query, entityClass);
//	}
    private static MongodDB getMongoTemplate() {
        return ChatMongoUtils.getChatMongoDB();
    }
}
