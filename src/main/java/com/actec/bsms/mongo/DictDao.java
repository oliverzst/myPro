package com.actec.bsms.mongo;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * 字典接口
 *
 * @author zhangst
 * @create 2018-03-16 4:31 PM
 */
@Repository("dictDao")
public class DictDao {

    /**
     * 由springBoot自动注入，默认配置会产生mongoTemplate这个bean
     */
    @Autowired
    private MongoTemplate mongoTemplate;

    /**
     * 查找全部
     */
    public List<Dict> findAll() {
        return mongoTemplate.findAll(Dict.class);
    }

    /**
     * 根据id得到对象
     */
    public Dict get(String id) {
        return mongoTemplate.findOne(new Query(Criteria.where("id").is(id)), Dict.class);
    }

    /**
     * 插入一个用户
     */
    public void insert(Dict dict) {
        mongoTemplate.insert(dict);
    }

    /**
     * 根据id删除一个用户
     */
    public void remove(String id) {
        Criteria criteria = Criteria.where("id").is(id);
        Query query = new Query(criteria);
        mongoTemplate.remove(query,Dict.class);
    }

    /**
     * 分页查找
     *
     * user代表过滤条件
     *
     * pageable代表分页bean
     */
    public List<Dict> findByPage(Dict dict, Pageable pageable) {
        Query query = new Query();
        if (dict != null && dict.getLabel() != null) {
            //模糊查询
            query = new Query(Criteria.where("label").regex("^" + dict.getLabel()));
        }
        List<Dict> list = mongoTemplate.find(query.with(pageable), Dict.class);
        return list;
    }

    /**
     * 根据id更新
     */
    public void update(Dict dict) {
        Criteria criteria = Criteria.where("id").is(dict.getId());
        Query query = new Query(criteria);
        Update update = Update.update("label", dict.getLabel()).set("value", dict.getValue()).set("description", dict.getDescription());
        mongoTemplate.updateMulti(query, update, Dict.class);
    }

    /**
     * 插入一个集合
     */
    public void insertAll(List<Dict> dictList) {
        mongoTemplate.insertAll(dictList);
    }

}
