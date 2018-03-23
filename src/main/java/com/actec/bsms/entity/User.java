/**
 * Copyright &copy; 2012-2014 <a href="https://github.com/infosoul/mserver">Freelance</a> All rights reserved.
 */
package com.actec.bsms.entity;

import com.alibaba.fastjson.annotation.JSONField;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

/**
 * 用户Entity
 * @author Freelance
 * @version 2013-12-05
 */
public class User extends DataEntity<User> implements Serializable{

	private static final long serialVersionUID = 1L;

	public static int ADMIN = 1;
	public static int MANAGER = 2;
	public static int NORMAL = 3;

	private String loginName;// 登录名
	private String password;// 密码
	private String name;	// 姓名
	private String phone;	// 电话
	private String loginDevice;	// 登陆设备 为空则表示未登录
	private Date loginDate;	// 最后登陆日期

	private int facilityGroupId; //归属设备组
	
	private int roleId;	// 用户权限

	private String inspectDeviceType;

	private String inspectDeviceTypeName;

	private List<InspectDeviceType> inspectDeviceTypeList;

	private Role role;

	private FacilityGroup facilityGroup;

	public User() {
		super();
	}
	
	public User(int id){
		super(id);
	}

	public User(int id, String loginName){
		super(id);
		this.loginName = loginName;
	}

	public User(Role role){
		super();
		this.roleId = role.getId();
	}

	public int getId() {
		return id;
	}

	public String getLoginName() {
		return loginName;
	}

	public void setLoginName(String loginName) {
		this.loginName = loginName;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getPhone() {
		return phone;
	}

	public void setPhone(String phone) {
		this.phone = phone;
	}


	public String getRemarks() {
		return remarks;
	}

	public Date getCreateDate() {
		return createDate;
	}

	public String getLoginDevice() {
		return loginDevice;
	}

	public void setLoginDevice(String loginDevice) {
		this.loginDevice = loginDevice;
	}

	@JSONField(format = "yyyy-MM-dd HH:mm:ss")
	public Date getLoginDate() {
		return loginDate;
	}

	public void setLoginDate(Date loginDate) {
		this.loginDate = loginDate;
	}

	public int getRoleId() {
		return roleId;
	}

	public void setRoleId(int roleId) {
		this.roleId = roleId;
	}

	public boolean isAdmin(){
		return isAdmin(this.id);
	}
	
	public static boolean isAdmin(int id){
		return id == 1;
	}

	public static boolean isNullUser(User user) {
		if(user == null)
			return true;
		return false;
	}

	public String getInspectDeviceType() {
		return inspectDeviceType;
	}

	public void setInspectDeviceType(String inspectDeviceType) {
		this.inspectDeviceType = inspectDeviceType;
	}

	public List<InspectDeviceType> getInspectDeviceTypeList() {
		return inspectDeviceTypeList;
	}

	public void setInspectDeviceTypeList(List<InspectDeviceType> inspectDeviceTypeList) {
		this.inspectDeviceTypeList = inspectDeviceTypeList;
	}

	public int getFacilityGroupId() {
		return facilityGroupId;
	}

	public void setFacilityGroupId(int facilityGroupId) {
		this.facilityGroupId = facilityGroupId;
	}

	public Role getRole() {
		return role;
	}

	public void setRole(Role role) {
		this.role = role;
	}

	public FacilityGroup getFacilityGroup() {
		return facilityGroup;
	}

	public void setFacilityGroup(FacilityGroup facilityGroup) {
		this.facilityGroup = facilityGroup;
	}

	public String getInspectDeviceTypeName() {
		return inspectDeviceTypeName;
	}

	public void setInspectDeviceTypeName(String inspectDeviceTypeName) {
		this.inspectDeviceTypeName = inspectDeviceTypeName;
	}
}