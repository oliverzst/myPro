package com.actec.bsms.controller;

import com.actec.bsms.entity.User;
import com.actec.bsms.service.UserService;
import com.actec.bsms.utils.CodeUtils;
import com.actec.bsms.utils.UserUtils;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.TypeReference;
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

    private static final String userNotExit = "帐号不存在";
    private static final String error = "系统错误";
    private static final String success = "配置成功";
    private static final String passwordError = "密码错误";
    private static final String passwordModifySuccess = "密码修改成功";
    private static final String nowModifyError = "当前密码错误";
    private static final String phoneExit = "该登录名或手机号已存在";
    private static final String registerSuccess = "注册成功";
    private static final String logoutSuccess = "登出成功";

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
                return JSON.toJSONString(userNotExit);
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
                return JSON.toJSONString(passwordError);
            }
        } catch (Exception e) {
            logger.error(e.getMessage());
            return JSON.toJSONString(error);
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
                return JSON.toJSONString(userNotExit);
            }
            if (CodeUtils.validatePassword(oldPassword, user.getPassword())) {
                userService.modifyPassword(userId, CodeUtils.entryptPassword(newPassword));
                return JSON.toJSONString(passwordModifySuccess);
            } else {
                return JSON.toJSONString(nowModifyError);
            }
        } catch (Exception e) {
            logger.error(e.getMessage());
            return JSON.toJSONString(error);
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
        Map<String, String> registerInfoMap = JSON.parseObject(regusterInfo, new TypeReference<Map<String,String>>(){});
        String loginName = registerInfoMap.get("loginName");
        String password = registerInfoMap.get("password");
        String name = registerInfoMap.get("name");
        String phone = registerInfoMap.get("phone");
        try {
            if (userService.checkRegister(loginName, phone, name)!=0) {
                return JSON.toJSONString(phoneExit);
            }
            User user = new User();
            user.setLoginName(loginName);
            user.setPassword(CodeUtils.entryptPassword(password));
            user.setName(name);
            user.setPhone(phone);
            //默认用户权限为普通用户
            user.setRoleId(User.NORMAL);
            userService.save(user);
            return JSON.toJSONString(registerSuccess);
        } catch (Exception e) {
            logger.error(e.getMessage());
            return JSON.toJSONString(error);
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
                return  JSON.toJSONString(phoneExit);
            }
            User user = userService.get(userId, true);
            user.setLoginName(loginName);
            user.setName(name);
            user.setPhone(phone);
            userService.save(user);
            return JSON.toJSONString(success);
        } catch (Exception e) {
            logger.error(e.getMessage());
            return JSON.toJSONString(error);
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
            return JSON.toJSONString(logoutSuccess);
        }catch (Exception e) {
            logger.error(e.getMessage());
            return JSON.toJSONString(error);
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
            return JSON.toJSONString(success);
        }catch (Exception e) {
            logger.error(e.getMessage());
            return JSON.toJSONString(error);
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
            return JSON.toJSONString(success);
        }catch (Exception e) {
            logger.error(e.getMessage());
            return JSON.toJSONString(error);
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
            return JSON.toJSONString(success);
        }catch (Exception e) {
            logger.error(e.getMessage());
            return JSON.toJSONString(error);
        }
    }

    /**
     * 批量设置用户权限
     */
    @GET
    @Path("/setUserRoles")
    public String setUserRoles(@QueryParam("userLists")String userLists) {
        try {
            userService.setUserRoles(userLists);
            return JSON.toJSONString(success);
        }catch (Exception e) {
            logger.error(e.getMessage());
            return JSON.toJSONString(error);
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
            return JSON.toJSONString(success);
        }catch (Exception e) {
            logger.error(e.getMessage());
            return JSON.toJSONString(error);
        }
    }

    /**
     * 获取用户当前所属巡检设备类型
     */
    @GET
    @Path("/getUserInspectDeviceType")
    public String getUserInspectDeviceType(@QueryParam("userId")int userId) {
        try {
            return JSON.toJSONString(userService.getUserInspectDeviceType(userId));
        }catch (Exception e) {
            logger.error(e.getMessage());
            return JSON.toJSONString(error);
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
            return JSON.toJSONString(error);
        }
    }

    /**
     * 获取当前用户下级的所有用户信息
     */
    @GET
    @Path("/findUserList")
    public String findUserList(@QueryParam("userId")int userId) {
        try {
            return JSON.toJSONString(userService.findSubUserList(userId));
        }catch (Exception e) {
            logger.error(e.getMessage());
            return JSON.toJSONString(error);
        }
    }

    /**
     * 获取指定用户信息
     */
    @GET
    @Path("/get")
    public String get(@QueryParam("userId")int userId) {
        try {
            return JSON.toJSONString(userService.get(userId, true));
        }catch (Exception e) {
            logger.error(e.getMessage());
            return JSON.toJSONString(error);
        }
    }

}
