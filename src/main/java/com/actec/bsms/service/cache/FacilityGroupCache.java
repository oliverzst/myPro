package com.actec.bsms.service.cache;

import com.actec.bsms.entity.FacilityGroup;
import com.actec.bsms.repository.dao.FacilityGroupDao;
import org.apache.commons.collections.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * 测试缓存
 *
 * @author zhangst
 * @create 2018-01-11 3:38 PM
 */

@Service
public class FacilityGroupCache extends IRedisService<FacilityGroup> {
    private static final String REDIS_KEY = "FACILITY_REDIS_KEY";

    @Autowired
    FacilityGroupDao facilityGroupDao;

    @Override
    protected String getRedisKey() {
        return this.REDIS_KEY;
    }

    public void init() {
        //初始化，将所有未完成的Task存入缓存中
        this.empty();
        List<FacilityGroup> facilityGroupList = facilityGroupDao.findAll();
        if (!CollectionUtils.isEmpty(facilityGroupList)) {
            for (FacilityGroup facilityGroup: facilityGroupList) {
                this.put(String.valueOf(facilityGroup.getId()), facilityGroup, -1);
            }
        }
    }

}
