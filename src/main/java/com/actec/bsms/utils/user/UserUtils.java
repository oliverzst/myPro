/**
 * Copyright &copy; 2012-2014 <a href="https://github.com/infosoul/mserver">Freelance</a> All rights reserved.
 */
package com.actec.bsms.utils.user;

import com.actec.bsms.entity.User;
import com.google.common.collect.Lists;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * 用户工具类
 * @author zhangst
 * @version 2013-12-05
 */
@Component
public class UserUtils {

	public static List<User> selectByInspectDeviceType(List<User> userList, int inspectDeviceTypeId) {
		List<User> userListResult = Lists.newArrayList();
		for (int i=0;i<userList.size();i++) {
			User userResult = userList.get(i);
			String inspectTypes = userResult.getInspectDeviceType();
			if (inspectTypes!=null) {
				String[] inspectTypeString = inspectTypes.split(",");
				for (int j=0;j<inspectTypeString.length;j++) {
					if (Integer.parseInt(inspectTypeString[j])==inspectDeviceTypeId) {
						userListResult.add(userResult);
					}
				}
			}
		}
		return userListResult;
	}

}
