package com.actec.bsms.service.cache;

import com.actec.bsms.entity.User;
import com.actec.bsms.repository.dao.UserDao;
import org.apache.commons.collections.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * 测试缓存
 *
 * @author zhangst
 * @create 2018-01-12 9:38 AM
 */

@Service
public class UserCache extends IRedisService<User> {
    private static final String REDIS_KEY = "USER_REDIS_KEY";

    @Autowired
    UserDao userDao;

    @Override
    protected String getRedisKey() {
        return this.REDIS_KEY;
    }

    public void init() {
        //初始化，将所有未完成的Task存入缓存中
        this.empty();
        List<User> userList = userDao.findAllList(new User());
        if (!CollectionUtils.isEmpty(userList)) {
            for (User user: userList) {
                this.put(String.valueOf(user.getId()), user, -1);
            }
        }
    }

}
