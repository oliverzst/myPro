package com.actec.bsms.service;

import com.actec.bsms.entity.FacilityGroup;
import com.actec.bsms.entity.InspectDeviceType;
import com.actec.bsms.entity.User;
import com.actec.bsms.repository.dao.InspectDeviceTypeDao;
import com.actec.bsms.repository.dao.UserDao;
import com.actec.bsms.service.cache.UserCache;
import com.actec.bsms.utils.DateUtils;
import com.actec.bsms.utils.StringUtils;
import com.actec.bsms.utils.UserUtils;
import com.alibaba.fastjson.JSON;
import com.google.common.collect.Lists;
import org.apache.commons.collections.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

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
    @Autowired
    private InspectDeviceTypeDao inspectDeviceTypeDao;

    public User get(int id){
        return userDao.get(id);
    }

    public User get(int id, boolean isFromSql){
        User user = userCache.get(""+id);
        if (isFromSql && null==user) {
            user = userDao.get(id);
        }
        return user;
    }

    public User findByDevice(String deviceId) { return userDao.findByDevice(deviceId); }

    public User findByLoginName(String loginName) { return userDao.findByLoginName(loginName); }

    public int checkRegister(String loginName, String phone, String name) { return userDao.checkRegister(loginName, phone, name); }

    public void updateLoginInfo(int id, String deviceId) {
        userDao.updateLoginInfo(id, deviceId, DateUtils.getNowDate());
//        userDao.updateLoginInfo(id, deviceId, new Date());
    }

    public void modifyPassword(int id, String password) { userDao.modifyPassword(id, password);}

    @Transactional(propagation = Propagation.REQUIRED,isolation = Isolation.DEFAULT,timeout=36000,rollbackFor=Exception.class)
    public void save(User user) {
        if (null!=user) {
            if (user.getId()==0) {
                user.setCreateDate(DateUtils.getNowDate());
                user.setUpdateDate(DateUtils.getNowDate());
                userDao.insert(user);
                User lastInsertUser = userDao.findLastInsertUser();
                userCache.put(""+lastInsertUser.getId(), lastInsertUser, -1);
            } else {
                user.setUpdateDate(DateUtils.getNowDate());
                userDao.update(user);
                userCache.put(""+user.getId(), get(user.getId()), -1);
            }
        }
    }

    @Transactional(propagation = Propagation.REQUIRED,isolation = Isolation.DEFAULT,timeout=36000,rollbackFor=Exception.class)
    public void batchUpdate(List<User> userList) {
        if (!CollectionUtils.isEmpty(userList)) {
            userDao.batchUpdate(userList);
            userCache.putAllUsers(userList);
        }
    }

    @Transactional(propagation = Propagation.REQUIRED,isolation = Isolation.DEFAULT,timeout=36000,rollbackFor=Exception.class)
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

    //批量设置用户权限
    public void setUserRoles(String userLists) {
        List<User> userList = JSON.parseArray(userLists, User.class);
        List<User> updateUserList = Lists.newArrayList();
        for (int i=0;i<userList.size();i++) {
            User user = get(userList.get(i).getId(), true);
            if (user.getRoleId() != userList.get(i).getRoleId()) {
                user.setRoleId(userList.get(i).getRoleId());
                if (userList.get(i).getRoleId()==2) {
                    //为管理员
                    user.setFacilityGroupId(FacilityGroup.ALL_FACILITY);
                    List<InspectDeviceType> inspectDeviceTypeList = inspectDeviceTypeDao.findAllList(new InspectDeviceType());
                    StringBuilder inspectDeviceType = new StringBuilder();
                    for (int j=0;j<inspectDeviceTypeList.size();j++) {
                        inspectDeviceType.append(inspectDeviceTypeList.get(j).getId()).append(",");
                    }
                    user.setInspectDeviceType(inspectDeviceType.substring(0, inspectDeviceType.length()-1));
                }
                updateUserList.add(user);
//                    userService.save(user);
            }
        }
        //批量修改
        batchUpdate(updateUserList);
    }

    //删除设备巡检类型同时删除关联用户的配置
    public void deleteInspectDeviceType(int inspectDeviceTypeId) {
        List<User> userList = UserUtils.selectByInspectDeviceType(findByRoleId(User.MANAGER), inspectDeviceTypeId);
        for (int i=0;i<userList.size();i++) {
            User user = userList.get(i);
            //每个用户存在多种设备巡检类型，它们用逗号隔开
            String inspectDeviceTypes = user.getInspectDeviceType();
            //为保证正确找到需删除的设备巡检类型，在前后加","
            inspectDeviceTypes = ","+inspectDeviceTypes+",";
            String replaceString = ","+inspectDeviceTypeId+",";
            inspectDeviceTypes = inspectDeviceTypes.replaceAll(replaceString, "");
            //如果删除的不是第一个，则删除头部","
            if (inspectDeviceTypes.substring(0,1).equals(",")) {
                inspectDeviceTypes = inspectDeviceTypes.substring(1,inspectDeviceTypes.length());
            }
            //如果删除的不是最后一个，则删除尾部","
            if (inspectDeviceTypes.substring(inspectDeviceTypes.length()-1,inspectDeviceTypes.length()).equals(",")) {
                inspectDeviceTypes = inspectDeviceTypes.substring(0,inspectDeviceTypes.length()-1);
            }
            user.setInspectDeviceType(inspectDeviceTypes);
            save(user);
        }
    }

    //获取指定用户当前所属巡检设备类型
    public List<InspectDeviceType> getUserInspectDeviceType(int userId) {
        User user = get(userId, true);
        String inspectTypeString = user.getInspectDeviceType();
        List<InspectDeviceType> inspectDeviceTypeList = Lists.newArrayList();
        if (inspectTypeString!=null) {
            String[] inspectTypes = inspectTypeString.split(",");
            for (int i=0;i<inspectTypes.length;i++) {
                inspectDeviceTypeList.add(inspectDeviceTypeDao.findById(Integer.parseInt(inspectTypes[i])));
            }
        }
        return inspectDeviceTypeList;
    }

    //获取指定用户下级的所有用户信息
    public List<User> findSubUserList(int userId) {
        User user = get(userId, true);
        List<User> userList = findByRoleId(user.getRoleId());
        List<User> userListResult = Lists.newArrayList();
        //应前端需求，封装InspectDeviceTypeList和InspectDeviceTypeName字段
        for (int i=0;i<userList.size();i++) {
            User userResult = userList.get(i);
            String inspectTypes = userResult.getInspectDeviceType();
            if (!StringUtils.isEmpty(inspectTypes) && !inspectTypes.equals("0")) {
                String[] inspectTypeString = inspectTypes.split(",");
                List<InspectDeviceType> inspectDeviceTypeList = Lists.newArrayList();
                String inspectDeviceTypeName = "";
                for (int j=0;j<inspectTypeString.length;j++) {
                    InspectDeviceType inspectDeviceType = inspectDeviceTypeDao.get(Integer.parseInt(inspectTypeString[j]));
                    inspectDeviceTypeList.add(inspectDeviceType);
                    inspectDeviceTypeName = inspectDeviceTypeName + inspectDeviceType.getName() + ",";
                }
                inspectDeviceTypeName = inspectDeviceTypeName.substring(0, inspectDeviceTypeName.length()-1);
                userResult.setInspectDeviceTypeList(inspectDeviceTypeList);
                userResult.setInspectDeviceTypeName(inspectDeviceTypeName);
            }
            userListResult.add(userResult);
        }
        return userListResult;
    }

}
