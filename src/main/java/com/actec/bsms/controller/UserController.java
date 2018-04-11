package com.actec.bsms.controller;

import com.actec.bsms.entity.FacilityGroup;
import com.actec.bsms.entity.InspectDeviceType;
import com.actec.bsms.entity.User;
import com.actec.bsms.service.InspectDeviceTypeService;
import com.actec.bsms.service.UserService;
import com.actec.bsms.utils.CodeUtils;
import com.actec.bsms.utils.StringUtils;
import com.actec.bsms.utils.UserUtils;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.TypeReference;
import com.google.common.collect.Lists;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import java.util.List;
import java.util.Map;

/**
 * 用户管理相关接口
 * @author zhangst
 * @create 2017-11-02 5:06 PM
 */
@Path("/user")
@Scope("singleton")
@Produces(MediaType.APPLICATION_JSON)
public class UserController extends BaseController{

    @Autowired
    private UserService userService;

    @Autowired
    private InspectDeviceTypeService inspectDeviceTypeService;

    private static String result = "";

    private static final String userNotExit = "帐号不存在";
    private static final String error = "系统错误";
    private static final String success = "配置成功";

    /**
     * 用户登录
     */
    @GET
    @Path("/login")
    public String login(@QueryParam("loginInfo") String loginInfo) {
        Map<String, String> loginInfoMap = JSON.parseObject(loginInfo, new TypeReference<Map<String,String>>(){});
        String loginName = loginInfoMap.get("loginName");
        String password = loginInfoMap.get("password");
        String deviceId = null;
        try {
            //查找登陆帐号或者手机号是否存在
            User user = userService.findByLoginName(loginName);
            if (null==user) {
                result = userNotExit;
                return JSON.toJSONString(result);
            }
            //密码验证
            if (CodeUtils.validatePassword(password, user.getPassword())) {
                //重复登录验证
//                if (!user.getLoginName().equals("admin")) {
//                    if (!StringUtils.isEmpty(user.getLoginDevice())) {
//                        result = "该帐号已在别处登录";
//                        return JSON.toJSONString(result);
//                    }
//                    deviceId = "已登录";
//                }
                userService.updateLoginInfo(user.getId(), deviceId);
                user = userService.get(user.getId(), true);
                return JSON.toJSONString(user);
            } else {
                result = "密码错误";
                return JSON.toJSONString(result);
            }
        } catch (Exception e) {
            logger.error(e.getMessage());
            result = error;
            return JSON.toJSONString(result);
        }
    }

    /**
     * 密码修改
     */
    @GET
    @Path("/modifyPassword")
    public String modifyPassword(@QueryParam("userId") int userId, @QueryParam("oldPassword") String oldPassword, @QueryParam("newPassword") String newPassword) {
        try {
            User user = userService.get(userId, true);
            if (user==null) {
                result = userNotExit;
                return JSON.toJSONString(result);
            }
            if (CodeUtils.validatePassword(oldPassword, user.getPassword())) {
                userService.modifyPassword(userId, CodeUtils.entryptPassword(newPassword));
                result = "密码修改成功";
                return JSON.toJSONString(result);
            } else {
                result = "当前密码错误";
                return JSON.toJSONString(result);
            }
        } catch (Exception e) {
            logger.error(e.getMessage());
            result = error;
            return JSON.toJSONString(result);
        }
    }

    /**
     * 验证某设备是否已经登录
     */
    @GET
    @Path("/isLogin/{deviceId}")
    public String isLogin(@PathParam("deviceId") String deviceId) {
        User user = userService.findByDevice(deviceId);
        return user==null?"":JSON.toJSONString(user);
    }

    /**
     * 用户注册
     */
    @GET
    @Path("/register")
    public String register(@QueryParam("registerInfo") String regusterInfo) {
        Map<String, String> regusterInfoMap = JSON.parseObject(regusterInfo, new TypeReference<Map<String,String>>(){});
        String loginName = regusterInfoMap.get("loginName");
        String password = regusterInfoMap.get("password");
        String name = regusterInfoMap.get("name");
        String phone = regusterInfoMap.get("phone");
        try {
            int isUserExits = userService.checkRegister(loginName, phone, name);
            if (isUserExits!=0) {
                result = "该登录名或手机号已存在";
                return  JSON.toJSONString(result);
            }
            User user = new User();
            user.setLoginName(loginName);
            user.setPassword(CodeUtils.entryptPassword(password));
            user.setName(name);
            user.setPhone(phone);
            //默认用户权限为普通用户
            user.setRoleId(User.NORMAL);
            userService.save(user);
            result = "注册成功";
            return JSON.toJSONString(result);
        } catch (Exception e) {
            logger.error(e.getMessage());
            result = error;
            return JSON.toJSONString(result);
        }
    }

    /**
     * 修改用户信息
     */
    @GET
    @Path("/modify")
    public String modify(@QueryParam("userInfo") String userInfo) {
        Map<String, String> userInfoMap = JSON.parseObject(userInfo, new TypeReference<Map<String,String>>(){});
        String loginName = userInfoMap.get("loginName");
        int userId = Integer.parseInt(userInfoMap.get("userId"));
        String name = userInfoMap.get("name");
        String phone = userInfoMap.get("phone");
        try {
            int isUserExits = userService.checkRegister(loginName, phone, name);
            if (isUserExits!=0) {
                result = "该登录名或手机号已存在";
                return  JSON.toJSONString(result);
            }
            User user = userService.get(userId, true);
            user.setLoginName(loginName);
            user.setName(name);
            user.setPhone(phone);
            userService.save(user);
            result = "修改成功";
            return JSON.toJSONString(result);
        } catch (Exception e) {
            logger.error(e.getMessage());
            result = error;
            return JSON.toJSONString(result);
        }
    }

    /**
     * 退出登录
     */
    @GET
    @Path("/logout")
    public String logout(@QueryParam("userId")int userId) {
        try {
            User user = userService.get(userId, true);
            user.setLoginDevice("");
            userService.save(user);
            result = "登出成功";
            return JSON.toJSONString(result);
        }catch (Exception e) {
            logger.error(e.getMessage());
            result = error;
            return JSON.toJSONString(result);
        }
    }

    /**
     * 设置用户归属设备组
     */
    @GET
    @Path("/setUserFacility")
    public String setUserFacility(@QueryParam("userId")int userId, @QueryParam("facilityGroupId")int facilityGroupId) {
        try {
            User user = userService.get(userId, true);
            user.setFacilityGroupId(facilityGroupId);
            userService.save(user);
            result = success;
            return JSON.toJSONString(result);
        }catch (Exception e) {
            logger.error(e.getMessage());
            result = error;
            return JSON.toJSONString(result);
        }
    }

    /**
     * 设置用户权限级别
     */
    @GET
    @Path("/setUserRole")
    public String setUserRole(@QueryParam("userId")int userId, @QueryParam("roleId")int roleId) {
        try {
            User user = userService.get(userId, true);
            user.setRoleId(roleId);
            userService.save(user);
            result = success;
            return JSON.toJSONString(result);
        }catch (Exception e) {
            logger.error(e.getMessage());
            result = error;
            return JSON.toJSONString(result);
        }
    }

    /**
     * 重置密码
     */
    @GET
    @Path("/resetPassword")
    public String resetPassword(@QueryParam("userId")int userId) {
        try {
            User user = userService.get(userId, true);
            //重置用户密码为12345678
            user.setPassword(CodeUtils.entryptPassword("12345678"));
            userService.save(user);
            result = "重置成功";
            return JSON.toJSONString(result);
        }catch (Exception e) {
            logger.error(e.getMessage());
            result = "重置失败";
            return JSON.toJSONString(result);
        }
    }

    /**
     * 批量设置用户权限
     */
    @GET
    @Path("/setUserRoles")
    public String setUserRoles(@QueryParam("userLists")String userLists) {
        try {
            List<User> userList = JSON.parseArray(userLists, User.class);
            List<User> updateUserList = Lists.newArrayList();
            for (int i=0;i<userList.size();i++) {
                User user = userService.get(userList.get(i).getId(), true);
                if (user.getRoleId() != userList.get(i).getRoleId()) {
                    user.setRoleId(userList.get(i).getRoleId());
                    if (userList.get(i).getRoleId()==2) {
                        //为管理员
                        user.setFacilityGroupId(FacilityGroup.ALL_FACILITY);
                        List<InspectDeviceType> inspectDeviceTypeList = inspectDeviceTypeService.findAll();
                        String inspectDeviceType = "";
                        for (int j=0;j<inspectDeviceTypeList.size();j++) {
                            inspectDeviceType += inspectDeviceTypeList.get(j).getId() + ",";
                        }
                        user.setInspectDeviceType(inspectDeviceType.substring(0, inspectDeviceType.length()-1));
                    }
                    updateUserList.add(user);
//                    userService.save(user);
                }
            }
            //批量修改
            userService.batchUpdate(updateUserList);
            result = success;
            return JSON.toJSONString(result);
        }catch (Exception e) {
            logger.error(e.getMessage());
            result = "配置失败";
            return JSON.toJSONString(result);
        }
    }

    /**
     * 设置用户所属巡检设备类型
     */
    @GET
    @Path("/setUserInspectDeviceType")
    public String setUserInspectDeviceType(@QueryParam("userId")int userId, @QueryParam("inspectDeviceType")String inspectDeviceType) {
        try {
            User user = userService.get(userId, true);
            user.setInspectDeviceType(inspectDeviceType);
            userService.save(user);
            result = success;
            return JSON.toJSONString(result);
        }catch (Exception e) {
            logger.error(e.getMessage());
            result = "配置失败";
            return JSON.toJSONString(result);
        }
    }

    /**
     * 获取用户当前所属巡检设备类型
     */
    @GET
    @Path("/getUserInspectDeviceType")
    public String getUserInspectDeviceType(@QueryParam("userId")int userId) {
        try {
            User user = userService.get(userId, true);
            String inspectTypeString = user.getInspectDeviceType();
            List<InspectDeviceType> inspectDeviceTypeList = Lists.newArrayList();
            if (inspectTypeString!=null) {
                String[] inspectTypes = inspectTypeString.split(",");
                for (int i=0;i<inspectTypes.length;i++) {
                    inspectDeviceTypeList.add(inspectDeviceTypeService.findById(Integer.parseInt(inspectTypes[i])));
                }
            }
            return JSON.toJSONString(inspectDeviceTypeList);
        }catch (Exception e) {
            logger.error(e.getMessage());
            result = "配置失败";
            return JSON.toJSONString(result);
        }
    }

    /**
     * 查找具备指定巡检设备类型的普通用户（管理员功能）
     */
    @GET
    @Path("/findUserByInspectDeviceType")
    public String findUserByInspectDeviceType(@QueryParam("userId")int userId, @QueryParam("inspectDeviceTypeId")int inspectDeviceTypeId) {
        try {
            User user = userService.get(userId, true);
            List<User> userList = UserUtils.selectByInspectDeviceType(userService.findByRoleId(user.getRoleId()), inspectDeviceTypeId);
            return JSON.toJSONString(userList);
        } catch (Exception e) {
            logger.error(e.getMessage());
            result = error;
            return JSON.toJSONString(result);
        }
    }

    /**
     * 获取当前用户下级的所有用户信息
     */
    @GET
    @Path("/findUserList")
    public String findUserList(@QueryParam("userId")int userId) {
        try {
            User user = userService.get(userId, true);
            List<User> userList = userService.findByRoleId(user.getRoleId());
            List<User> userListResult = Lists.newArrayList();
            for (int i=0;i<userList.size();i++) {
                User userResult = userList.get(i);
                String inspectTypes = userResult.getInspectDeviceType();
                if (!StringUtils.isEmpty(inspectTypes) && !inspectTypes.equals("0")) {
                    String[] inspectTypeString = inspectTypes.split(",");
                    List<InspectDeviceType> inspectDeviceTypeList = Lists.newArrayList();
                    String inspectDeviceTypeName = "";
                    for (int j=0;j<inspectTypeString.length;j++) {
                        InspectDeviceType inspectDeviceType = inspectDeviceTypeService.get(Integer.parseInt(inspectTypeString[j]));
                        inspectDeviceTypeList.add(inspectDeviceType);
                        inspectDeviceTypeName = inspectDeviceTypeName + inspectDeviceType.getName() + ",";
                    }
                    inspectDeviceTypeName = inspectDeviceTypeName.substring(0, inspectDeviceTypeName.length()-1);
                    userResult.setInspectDeviceTypeList(inspectDeviceTypeList);
                    userResult.setInspectDeviceTypeName(inspectDeviceTypeName);
                }
                userListResult.add(userResult);
            }
            return JSON.toJSONString(userListResult);
        }catch (Exception e) {
            logger.error(e.getMessage());
            result = error;
            return JSON.toJSONString(result);
        }
    }

    /**
     * 获取指定用户信息
     */
    @GET
    @Path("/get")
    public String get(@QueryParam("userId")int userId) {
        try {
            User user = userService.get(userId, true);
            return JSON.toJSONString(user);
        }catch (Exception e) {
            logger.error(e.getMessage());
            result = error;
            return JSON.toJSONString(result);
        }
    }

}
