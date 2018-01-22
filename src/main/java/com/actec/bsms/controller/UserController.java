package com.actec.bsms.controller;

import com.actec.bsms.entity.FacilityGroup;
import com.actec.bsms.entity.InspectDeviceType;
import com.actec.bsms.entity.User;
import com.actec.bsms.service.InspectDeviceTypeService;
import com.actec.bsms.service.SystemService;
import com.actec.bsms.service.UserService;
import com.actec.bsms.utils.StringUtils;
import com.actec.bsms.utils.user.UserUtils;
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

    @GET
    @Path("/login")
    public String login(@QueryParam("loginInfo") String loginInfo) {
        Map<String, String> loginInfoMap = JSON.parseObject(loginInfo, new TypeReference<Map<String,String>>(){});
        String loginName = loginInfoMap.get("loginName");
        String password = loginInfoMap.get("password");
        String deviceId = null;
        try {
            List<User> userList = userService.findByLoginName(loginName);
            if (userList.size()==0) {
                result = "账号不存在";
                return JSON.toJSONString(result);
            }
            User user = userList.get(0);
            if (SystemService.validatePassword(password, user.getPassword())) {
                if (!user.getLoginName().equals("admin")) {
                    if (!StringUtils.isEmpty(user.getLoginDevice())) {
                        result = "该帐号已在别处登录";
                        return JSON.toJSONString(result);
                    }
                    deviceId = "已登录";
                }
                userService.updateLoginInfo(user.getId(), deviceId);
                user = userService.get(user.getId(), true);
                return JSON.toJSONString(user);
            } else {
                result = "密码错误";
                return JSON.toJSONString(result);
            }
        } catch (Exception e) {
            logger.error(e.getMessage());
            result = "系统错误";
            return JSON.toJSONString(result);
        }
    }

    @GET
    @Path("/modifyPassword")
    public String modifyPassword(@QueryParam("userId") int userId, @QueryParam("oldPassword") String oldPassword, @QueryParam("newPassword") String newPassword) {
        try {
            User user = userService.get(userId, true);
            if (user==null) {
                result = "账号不存在";
                return JSON.toJSONString(result);
            }
            if (SystemService.validatePassword(oldPassword, user.getPassword())) {
                userService.modifyPassword(userId, SystemService.entryptPassword(newPassword));
                result = "密码修改成功";
                return JSON.toJSONString(result);
            } else {
                result = "当前密码错误";
                return JSON.toJSONString(result);
            }
        } catch (Exception e) {
            logger.error(e.getMessage());
            result = "系统错误";
            return JSON.toJSONString(result);
        }
    }

    @GET
    @Path("/isLogin/{deviceId}")
    public String isLogin(@PathParam("deviceId") String deviceId) {
        User user = userService.findByDevice(deviceId);
        return user==null?"":JSON.toJSONString(user);
    }

    @GET
    @Path("/register")
    public String register(@QueryParam("registerInfo") String regusterInfo) {
        Map<String, String> regusterInfoMap = JSON.parseObject(regusterInfo, new TypeReference<Map<String,String>>(){});
        String loginName = regusterInfoMap.get("loginName");
        String password = regusterInfoMap.get("password");
        String name = regusterInfoMap.get("name");
        String phone = regusterInfoMap.get("phone");
        try {
            List<User> userList = userService.checkRegister(loginName, phone, name);
            if (userList.size()!=0) {
                result = "该登录名或手机号已存在";
                return  JSON.toJSONString(result);
            }
            User user = new User();
            user.setLoginName(loginName);
            user.setPassword(SystemService.entryptPassword(password));
            user.setName(name);
            user.setPhone(phone);
            user.setRoleId(3);
            userService.save(user);
            result = "注册成功";
            return JSON.toJSONString(result);
        } catch (Exception e) {
            logger.error(e.getMessage());
            result = "系统错误，注册失败";
            return JSON.toJSONString(result);
        }
    }

    @GET
    @Path("/modify")
    public String modify(@QueryParam("userInfo") String userInfo) {
        Map<String, String> userInfoMap = JSON.parseObject(userInfo, new TypeReference<Map<String,String>>(){});
        String loginName = userInfoMap.get("loginName");
        int userId = Integer.parseInt(userInfoMap.get("userId"));
        String name = userInfoMap.get("name");
        String phone = userInfoMap.get("phone");
        try {
            List<User> userList = userService.checkRegister(loginName, phone, name);
            if (userList.size()!=0) {
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
            result = "系统错误，修改失败";
            return JSON.toJSONString(result);
        }
    }

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
            result = "系统错误";
            return JSON.toJSONString(result);
        }
    }

    @GET
    @Path("/setUserFacility")
    public String setUserFacility(@QueryParam("userId")int userId, @QueryParam("facilityGroupId")int facilityGroupId) {
        try {
            User user = userService.get(userId, true);
            user.setFacilityGroupId(facilityGroupId);
            userService.save(user);
            result = "配置成功";
            return JSON.toJSONString(result);
        }catch (Exception e) {
            logger.error(e.getMessage());
            result = "系统错误，配置失败";
            return JSON.toJSONString(result);
        }
    }

    @GET
    @Path("/setUserRole")
    public String setUserRole(@QueryParam("userId")int userId, @QueryParam("roleId")int roleId) {
        try {
            User user = userService.get(userId, true);
            user.setRoleId(roleId);
            userService.save(user);
            result = "配置成功";
            return JSON.toJSONString(result);
        }catch (Exception e) {
            logger.error(e.getMessage());
            result = "配置失败";
            return JSON.toJSONString(result);
        }
    }

    @GET
    @Path("/resetPassword")
    public String resetPassword(@QueryParam("userId")int userId) {
        try {
            User user = userService.get(userId, true);
            //重置用户密码为12345678
            user.setPassword(SystemService.entryptPassword("12345678"));
            userService.save(user);
            result = "重置成功";
            return JSON.toJSONString(result);
        }catch (Exception e) {
            logger.error(e.getMessage());
            result = "重置失败";
            return JSON.toJSONString(result);
        }
    }

    @GET
    @Path("/setUserRoles")
    public String setUserRoles(@QueryParam("userLists")String userLists) {
        try {
            List<User> userList = JSON.parseArray(userLists, User.class);
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
                    userService.save(user);
                }
            }
            result = "配置成功";
            return JSON.toJSONString(result);
        }catch (Exception e) {
            logger.error(e.getMessage());
            result = "配置失败";
            return JSON.toJSONString(result);
        }
    }

    @GET
    @Path("/setUserInspectDeviceType")
    public String setUserInspectDeviceType(@QueryParam("userId")int userId, @QueryParam("inspectDeviceType")String inspectDeviceType) {
        try {
            User user = userService.get(userId, true);
            user.setInspectDeviceType(inspectDeviceType);
            userService.save(user);
            result = "配置成功";
            return JSON.toJSONString(result);
        }catch (Exception e) {
            logger.error(e.getMessage());
            result = "配置失败";
            return JSON.toJSONString(result);
        }
    }

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

    @GET
    @Path("/findUserByInspectDeviceType")
    public String findUserByInspectDeviceType(@QueryParam("userId")int userId, @QueryParam("inspectDeviceTypeId")int inspectDeviceTypeId) {
        try {
            User user = userService.get(userId, true);
            List<User> userList = UserUtils.selectByInspectDeviceType(userService.findByRoleId(user.getRoleId()), inspectDeviceTypeId);
            return JSON.toJSONString(userList);
        } catch (Exception e) {
            logger.error(e.getMessage());
            result = "系统错误";
            return JSON.toJSONString(result);
        }
    }

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
            result = "系统错误";
            return JSON.toJSONString(result);
        }
    }

    @GET
    @Path("/get")
    public String get(@QueryParam("userId")int userId) {
        try {
            User user = userService.get(userId, true);
            return JSON.toJSONString(user);
        }catch (Exception e) {
            logger.error(e.getMessage());
            result = "系统错误";
            return JSON.toJSONString(result);
        }
    }

}
