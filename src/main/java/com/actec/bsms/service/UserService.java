package com.actec.bsms.service;

import com.actec.bsms.entity.User;
import com.actec.bsms.repository.dao.UserDao;
import com.actec.bsms.service.cache.UserCache;
import com.actec.bsms.utils.DateUtils;
import com.actec.bsms.utils.user.UserUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Created by zst on 2017/11/3.
 */
@Service
public class UserService {

    @Autowired
    private UserDao userDao;
    @Autowired
    private UserCache userCache;

    public User get(int id, boolean isFromSql){
        User user = userCache.get(""+id);
        if (isFromSql && null==user) {
            user = userDao.get(id);
        }
        return user;
    }

    public User findByDevice(String deviceId) { return userDao.findByDevice(deviceId); }

    public List<User> findByLoginName(String loginName) { return userDao.findByLoginName(loginName); }

    public List<User> checkRegister(String loginName, String phone, String name) { return userDao.checkRegister(loginName, phone, name); }

    public void updateLoginInfo(int id, String deviceId) {
        userDao.updateLoginInfo(id, deviceId, DateUtils.getNowDate());
//        userDao.updateLoginInfo(id, deviceId, new Date());
    }

    public void modifyPassword(int id, String password) { userDao.modifyPassword(id, password);}

    public void save(User user) {
        if (null!=user) {
            if (user.getId()==0) {
                user.setCreateDate(DateUtils.getNowDate());
                user.setUpdateDate(DateUtils.getNowDate());
                userDao.insert(user);
                User newUser = userDao.findNewUser();
                userCache.put(""+newUser.getId(), newUser, -1);
            } else {
                user.setUpdateDate(DateUtils.getNowDate());
                userDao.update(user);
                userCache.put(""+user.getId(), user, -1);
            }
        }
    }

    public void delete(int userId) {
        User user = userDao.get(userId);
        if (null!=user) {
            userDao.delete(user);
            userCache.remove(""+user.getId());
        }
    }

    public List<User> findByRoleId(int roleId) {
        return userDao.findByRoleId(roleId);
    }

    public void deleteInspectDeviceType(int inspectDeviceTypeId) {
        List<User> userList = UserUtils.selectByInspectDeviceType(findByRoleId(2), inspectDeviceTypeId);
        for (int i=0;i<userList.size();i++) {
            User user = userList.get(i);
            String inspectDeviceTypes = user.getInspectDeviceType();
            inspectDeviceTypes = ","+inspectDeviceTypes+",";
            String replaceString = ","+inspectDeviceTypeId+",";
            inspectDeviceTypes = inspectDeviceTypes.replaceAll(replaceString, "");
            if (inspectDeviceTypes.substring(0,1).equals(",")) {
                inspectDeviceTypes = inspectDeviceTypes.substring(1,inspectDeviceTypes.length());
            }
            if (inspectDeviceTypes.substring(inspectDeviceTypes.length()-1,inspectDeviceTypes.length()).equals(",")) {
                inspectDeviceTypes = inspectDeviceTypes.substring(0,inspectDeviceTypes.length()-1);
            }
            user.setInspectDeviceType(inspectDeviceTypes);
            save(user);
        }
    }

}
