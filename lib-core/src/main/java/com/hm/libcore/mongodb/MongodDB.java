package com.hm.libcore.mongodb;

import cn.hutool.core.util.ReflectUtil;
import com.google.common.collect.Lists;
import com.hm.libcore.db.mongo.ClassChanged;
import com.hm.libcore.db.mongo.DBEntity;
import com.mongodb.DBObject;
import com.mongodb.client.ListIndexesIterable;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.model.IndexOptions;
import lombok.extern.slf4j.Slf4j;
import org.bson.Document;
import org.springframework.data.annotation.Transient;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.data.mongodb.core.FindAndModifyOptions;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.aggregation.Aggregation;
import org.springframework.data.mongodb.core.aggregation.AggregationOperation;
import org.springframework.data.mongodb.core.aggregation.AggregationResults;
import org.springframework.data.mongodb.core.aggregation.GroupOperation;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;

import java.lang.reflect.Field;
import java.util.Collection;
import java.util.List;
import java.util.Set;
import java.util.function.Consumer;

@Slf4j
public class MongodDB {

	private MongoTemplate mongoTemplate;
	
	public MongoTemplate getMongoTemplate() {
		return mongoTemplate;
	}
	public MongodDB(MongoTemplate mongoTemplate) {
		this.mongoTemplate = mongoTemplate;
	}
	public <T> T get(Object id,Class<T> entityClass) {
		return mongoTemplate.findById(id,entityClass);  
	}	
	public <T> T get(Object id,Class<T> entityClass,String collectionName) {
		return mongoTemplate.findById(id,entityClass,collectionName);  
	}	
	public <T> List<T> query(Query query,Class<T> entityClass) {
		return mongoTemplate.find(query, entityClass);
	}
	public long count(Query query,Class<?> entityClass) {
		return mongoTemplate.count(query, entityClass);
	}
	public long count(Query query,String collectionName) {
		return mongoTemplate.count(query, collectionName);
	}
	public long count(String collectionName) {
		return mongoTemplate.count(new Query(), collectionName);
	}
	public <T> List<T> query(Query query,Class<T> entityClass,String collectionName) {
		return mongoTemplate.find(query, entityClass, collectionName);
	}
	public <T> List<T> queryAll(Class<T> entityClass) {
		return mongoTemplate.find(new Query(), entityClass);
	}
	public <T> List<T> queryAll(Class<T> entityClass,String collectionName) {
		return mongoTemplate.findAll(entityClass, collectionName);
	}
	public <T> T queryOne(Query query,Class<T> entityClass) {
		return mongoTemplate.findOne(query, entityClass);
	}
	public <T> void delete(Object id,Class<T> entityClass) {
		Query query = Query.query(Criteria.where("_id").is(id));
		mongoTemplate.remove(query,entityClass);
	}
	
	public <T> boolean isExists(Query query,Class<T> entityClass) {
		return mongoTemplate.exists(query, entityClass);
	}
	public <T> void remove(T entityClass) {
		mongoTemplate.remove(entityClass);
	}
	public <T> void remove(T entityClass,String collectionName) {
		mongoTemplate.remove(entityClass, collectionName);
	}
	public <T> void dropCollection(Class<T> entityClass) {
		mongoTemplate.dropCollection(entityClass);
	}
	public void dropCollection(String collectionName) {
		mongoTemplate.dropCollection(collectionName);
	}
	public MongoCollection<Document> getCollection(String collectionName) {
		return mongoTemplate.getCollection(collectionName);
	}
	
	//==================insert,save的区别=======================================
	/**
	 * 若新增的数据中存在主键 ，insert() 会提示错误，而save() 则更改原来的内容为新内容。
	 */
	public void insert(Object object,String collectionName) {
		mongoTemplate.insert(object, collectionName);
	}
	public void insert(Object object) {
		mongoTemplate.insert(object);
	}
	public <T> void insertAll(Collection<T> object) {
		mongoTemplate.insertAll(object);
	}
	public void save(Object object) {
        try {
            MongoMonitorUtils.addObjCount(object);
            mongoTemplate.save(object);
        } catch (Exception e) {
            e.printStackTrace();
        }
	}
	public void save(Object object,String collectionName) {
		MongoMonitorUtils.addCount(collectionName);
		mongoTemplate.save(object, collectionName);
	}
	
	public int getIncrementKey(String keyName,long value) {
		MongoMonitorUtils.addKeyCount(keyName);
		Query query = new Query(Criteria.where("_id").is(keyName));
		Update update = new Update();
		update.inc("seqId", value);
		FindAndModifyOptions options = new FindAndModifyOptions();
		options.upsert(true);
		options.returnNew(true);
		SequenceId sequ = mongoTemplate.findAndModify(query, update, options, SequenceId.class,"Primary");
		return sequ.getSeqId();
	}
	
	public <T extends DBEntity> void update(T entity) {
		try {
			synchronized (entity) {
				// 设置修改条件
		        Query query = new Query();
		        Criteria criteria = new Criteria("_id");
		        criteria.is(entity.getId());
		        query.addCriteria(criteria);
		        // 设置修改内容
		        Update update = new Update();
		        for (Field field : ReflectUtil.getFields(entity.getClass())) {
		        	String fieldName = field.getName();
		        	if("id".equals(fieldName) 
		        			|| field.getAnnotation(Transient.class) != null) {
		        		continue;
		        	}
		        	field.setAccessible(true);
					Object obj = field.get(entity);
					if(obj == null) {
						continue;
					}
					if(obj instanceof ClassChanged) {
						ClassChanged changeObj = (ClassChanged)obj;
						if(changeObj.Changed()) {
							update.set(fieldName, obj);
							changeObj.ClearChangedFlag();
						}
					}else{
						update.set(fieldName, obj);
					}
				}
		        // 参数：查询条件，更改结果，集合名
		        mongoTemplate.upsert(query, update, entity.getClass());
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		MongoMonitorUtils.addObjCount(entity);
	}
	
	//=====================================================
	public <T extends DBEntity> void createIndex(Class<T> entityClass) {
		String simpleName = entityClass.getSimpleName();
		String first = simpleName.substring(0, 1).toLowerCase();
		String name = first+simpleName.substring(1);
		createIndex(name, getIndexNameList(entityClass));
	}
	public void createIndex(String collName,List<String> createIndexs) {
		List<String> indexList = getCollectionIndex(collName);
		MongoCollection<Document> collection = mongoTemplate.getCollection(collName);
		for (String indexName : createIndexs) {
			if(!indexList.contains(indexName)) {
				IndexOptions index = new IndexOptions();
				index.background(true);
				log.info("创建索引:"+collName+"  index:"+indexName);
				collection.createIndex(new Document(indexName, 1),index);
			}
		}
	}
	public void createIndex(String collName,String indexName) {
		List<String> indexList = getCollectionIndex(collName);
		MongoCollection<Document> collection = mongoTemplate.getCollection(collName);
			if(!indexList.contains(indexName)) {
				IndexOptions index = new IndexOptions();
				index.background(true);
				log.info("创建索引:"+collName+"  index:"+indexName);
				collection.createIndex(new Document(indexName, 1),index);
			}
	}
	public <T> List<String> getCollectionIndex(String collName) {
		final List<String> indexList = Lists.newArrayList();
		MongoCollection<Document> collection = mongoTemplate.getCollection(collName);
		ListIndexesIterable<Document> indexs = collection.listIndexes();
		indexs.forEach(new Consumer<Document>() {
			@Override
			public void accept(Document t) {
				Document k = (Document)t.get("key");
				for (String v : k.keySet()) {
					indexList.add(v);
				}
			}
		});
		return indexList;
	}
	
	private <T extends DBEntity> List<String> getIndexNameList(Class<T> entityClass) {
		List<String> indexNames = Lists.newArrayList();
		for (Field field : entityClass.getDeclaredFields()) {
        	String fieldName = field.getName();
        	if(field.getAnnotation(Indexed.class) != null) {
        		indexNames.add(fieldName);
        	}
		}
		for (Field field : entityClass.getSuperclass().getDeclaredFields()) {
			String fieldName = field.getName();
        	if(field.getAnnotation(Indexed.class) != null) {
        		indexNames.add(fieldName);
        	}
		}
		return indexNames;
	}
	
	public Set<String> getCollectionNames() {
		return mongoTemplate.getCollectionNames();
	}
	
	
	/**
	 * group by 查询
	 * @param entityClass
	 * @param queryKey
	 * @param criteria
	 * @return
	 */
	public <T extends DBEntity> List<KeyCount> queryGroupByKey(Class<T> entityClass,String queryKey,Criteria criteria) {
		GroupOperation groupOperation = Aggregation.group(queryKey).count().as("count");
		Aggregation agg = null;
		if(criteria == null) {
			agg = Aggregation.newAggregation(entityClass,groupOperation);
		}else{
			agg = Aggregation.newAggregation(entityClass,Aggregation.match(criteria),groupOperation);
		}
 		AggregationResults<KeyCount> results = mongoTemplate.aggregate(agg,entityClass,KeyCount.class);
 		List<KeyCount> mapList = results.getMappedResults();
		return mapList;
	}
	//group by 聚合函数查询
	public <T extends DBEntity> List<DBObject> queryGroupBy(Class<T> entityClass,AggregationOperation... operations) {
		Aggregation agg = Aggregation.newAggregation(entityClass,operations);
 		AggregationResults<DBObject> results = mongoTemplate.aggregate(agg,entityClass,DBObject.class);
 		List<DBObject> mapList = results.getMappedResults();
		return mapList;
	}
	
	
	public <T> boolean deleteBatchIds(List<Object> ids, Class<T> class1) {
		Query query = Query.query(Criteria.where("_id").in(ids));
		query.with(Sort.by(Direction.DESC,"_id"));
		mongoTemplate.remove(query,class1);
		return true;
	}
	
	public <T> boolean updateBatchIds(List<Object> ids,Update update,Class<T> entityClass) {
		Query query = Query.query(Criteria.where("_id").in(ids));
		getMongoTemplate().updateMulti(query, update, entityClass);
		
		getMongoTemplate().updateMulti(query, update, entityClass);
		return true;
	}
	
	public <T> boolean updateBatchQuery(Query query,Update update,Class<T> entityClass) {
		getMongoTemplate().updateMulti(query, update, entityClass);
		return true;
	}

	public <T> void deleteByQuery(Query query, String collName) {
		mongoTemplate.remove(query, collName);
	}

}


