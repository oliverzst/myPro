package com.actec.bsms.repository.dao;


import org.apache.ibatis.annotations.Param;


/** 
 * @author donglin.wang
 * 
 * @date 2017年06月14日  下午22:14:44

 */
@MyBatisDao
public interface AlarmRealTimeDao {
	void createTable();
//	void save(@Param("alm") AlarmRealTime alarmRealTime);
//	void update(@Param("alm") AlarmRealTime alarmRealTime);
//	AlarmRealTime getById(@Param("id") String id);
//	List<AlarmRealTime> findByOfficeId(@Param("officeId") String officeId);
//	List<AlarmRealTime> findByDate(@Param("begin") int begin, @Param("end") int end, @Param("officeId") String officeId);
//	void deleteById(@Param("id") String id);
//    List<AlarmRealTime> findPair(@Param("alm") AlarmRealTime alarmRealTime);
//	List<AlarmRealTime> findCompleList(@Param("param") Map<String, Object> map);
//	int getAlmCountByLevel(@Param("level") int level, @Param("officeId") String officeId);
//	List<AlarmRealTime> findGisAlarms(@Param("object") String object, @Param("officeId") String officeId);
//	List<AlarmRealTime> findByObjDesc(@Param("object") String object, @Param("description") String description);

	//一般告警
	public int countAlarmNormal(@Param("officeIds") String officeIds);
	//轻微告警
	public int countAlarmSlight(@Param("officeIds") String officeIds);
	//严重告警
	public int countAlarmNotice(@Param("officeIds") String officeIds);

}
 