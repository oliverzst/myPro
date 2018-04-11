package com.actec.bsms.mongoDemo;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @author zhangst
 * @create 2018-03-16 4:42 PM
 */
@Service("dictService")
public class DictService {

    @Autowired
    private DictDao dictDao;
    
    public List<Dict> findAll() {
        return dictDao.findAll();
    }

    public Dict get(String id) {
        return dictDao.get(id);
    }

    public void update(Dict Dict) {
        dictDao.update(Dict);
    }

    public void insert(Dict Dict) {
        dictDao.insert(Dict);
    }

    public void insertAll(List<Dict> Dicts) {
        dictDao.insertAll(Dicts);
    }

    public void remove(String id) {
        dictDao.remove(id);
    }

    public List<Dict> findByPage(Dict Dict, Pageable pageable) {
        return dictDao.findByPage(Dict, pageable);
    }
    
}
