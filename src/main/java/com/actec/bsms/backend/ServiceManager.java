package com.actec.bsms.backend;

import com.actec.bsms.repository.dao.*;
import com.actec.bsms.repository.socket.BaseStationSocket;
import com.actec.bsms.repository.socket.RealtimeAlarmSocket;
import com.actec.bsms.utils.ApplicationContextHelper;
import com.actec.bsms.utils.Cache.InitCacheTask;
import com.actec.bsms.utils.TableCache;
import com.actec.bsms.utils.facility.FacilitySyncTask;
import com.actec.bsms.utils.timer.SplitTableTimer;
import io.vertx.core.Vertx;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.*;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class ServiceManager {

    private static Logger logger = LoggerFactory.getLogger(ServiceManager.class);

    private static String USER_TABLE = "sys_user";
    private static String MENU_TABLE = "sys_menu";
    private static String MODULE_TABLE = "sys_module";
    private static String INSPECT_DEVICE_TYPE_TABLE = "inspect_device_type";
    private static String INSPECT_DEVICE_MENU_TABLE = "inspect_device_menu";
    private static String INSPECT_DEVICE_MODULE_TABLE = "inspect_device_module";
    private static String ROLE_TABLE = "sys_role";
    private static String TASK_TABLE = "task";
    private static String INSPECT_TABLE = "inspect";
    private static String WATCH_TABLE = "watch";
    private static String TROUBLE_SHOOT_TABLE = "trouble_shoot";
    private static String FACILITY_TABLE = "facility";
    private static String FACILITY_GROUP_TABLE = "facility_group";
    private static String FACILITY_FACILITY_GROUP_TABLE = "facility_facility_group";
    private static String ALARM_REALTIME_TABLE = "alarm_realtime";

    private static final long PERIOD_DAY = 24 * 60 * 60 * 1000;

    private static TableDao tableDao = ApplicationContextHelper.getBean(TableDao.class);
    private static UserDao userDao = ApplicationContextHelper.getBean(UserDao.class);
    private static RoleDao roleDao = ApplicationContextHelper.getBean(RoleDao.class);
    private static MenuDao menuDao = ApplicationContextHelper.getBean(MenuDao.class);
    private static ModuleDao moduleDao = ApplicationContextHelper.getBean(ModuleDao.class);
    private static InspectDeviceTypeDao inspectDeviceTypeDao = ApplicationContextHelper.getBean(InspectDeviceTypeDao.class);
    private static InspectDeviceMenuDao inspectDeviceMenuDao = ApplicationContextHelper.getBean(InspectDeviceMenuDao.class);
    private static InspectDeviceModuleDao inspectDeviceModuleDao = ApplicationContextHelper.getBean(InspectDeviceModuleDao.class);
    private static TaskDao taskDao = ApplicationContextHelper.getBean(TaskDao.class);
    private static InspectDao inspectDao = ApplicationContextHelper.getBean(InspectDao.class);
    private static WatchDao watchDao = ApplicationContextHelper.getBean(WatchDao.class);
    private static TroubleShootDao troubleShootDao = ApplicationContextHelper.getBean(TroubleShootDao.class);
    private static FacilityDao facilityDao = ApplicationContextHelper.getBean(FacilityDao.class);
    private static FacilityGroupDao facilityGroupDao = ApplicationContextHelper.getBean(FacilityGroupDao.class);
    private static AlarmRealTimeDao alarmRealTimeDao = ApplicationContextHelper.getBean(AlarmRealTimeDao.class);

    private static volatile List<Task> allTasks = new ArrayList<>();

    public static void contextDestroyed() {
        for (Task task : allTasks) {
            if (task != null) {
                task.stopService();
                task.cleanup();
            }
        }
    }

    public static void contextInitialized() {
        checkToInitUserTable();
        checkToInitRoleTable();
        checkToInitMenuTable();
        checkToInitModuleTable();
        checkToInitInspectDeviceTypeTable();
        checkToInitTaskTable();
        checkToInitInspectTable();
        checkToInitWatchTable();
        checkToInitAlarmRealTimeTable();
        checkToInitFacTable();
        checkToInitFacGroupTable();

        startInitRunnable();

        initCacheRunnable();

        createTaskTablePerMonth();

        Vertx.vertx().deployVerticle(new BaseStationSocket());
        Vertx.vertx().deployVerticle(new RealtimeAlarmSocket());
    }

    private static void startInitRunnable() {
        //处理会话订阅数据
        ExecutorService facilitySyncTaskThread = Executors.newSingleThreadExecutor();
        FacilitySyncTask facilitySyncTask = new FacilitySyncTask();
        facilitySyncTaskThread.execute(facilitySyncTask);
    }

    private static void initCacheRunnable() {
        //处理会话订阅数据
        ExecutorService initCacheThread = Executors.newSingleThreadExecutor();
        InitCacheTask initCacheTask = new InitCacheTask();
        initCacheThread.execute(initCacheTask);
    }

    private static void checkToInitUserTable() {
        if (!TableCache.tableExisted(USER_TABLE)) {
            userDao.createTable();
            userDao.initTable();
            userDao.initTable1();
            userDao.initTable2();
            TableCache.addToCache(USER_TABLE);
        } else {
            int columnCount = tableDao.columnExisted("inspect_device_type", USER_TABLE);
            if (columnCount == 0) {
                tableDao.addVarCharColumn("inspect_device_type", USER_TABLE);
            }
        }
    }

    private static void checkToInitRoleTable() {
        if (!TableCache.tableExisted(ROLE_TABLE)) {
            roleDao.createTable();
            roleDao.initTable();
            roleDao.initTable1();
            roleDao.initTable2();
            TableCache.addToCache(ROLE_TABLE);
        } else {
        }
    }

    private static void checkToInitMenuTable() {
        if (!TableCache.tableExisted(MENU_TABLE)) {
            menuDao.createTable();
            menuDao.initTable();
            menuDao.initTable1();
            menuDao.initTable2();
            menuDao.initTable3();
            menuDao.initTable4();
            menuDao.initTable5();
            menuDao.initTable6();
            TableCache.addToCache(MENU_TABLE);
        } else {
            int columnCount = tableDao.columnExisted("description", MENU_TABLE);
            if (columnCount == 0) {
                tableDao.addVarCharColumn("description", MENU_TABLE);
            }
            columnCount = tableDao.columnExisted("input_type", MENU_TABLE);
            if (columnCount == 0) {
                tableDao.addVarCharColumn("input_type", MENU_TABLE);
            }
        }
    }

    private static void checkToInitModuleTable() {
        if (!TableCache.tableExisted(MODULE_TABLE)) {
            moduleDao.createTable();
            moduleDao.initTable();
            moduleDao.initTable1();
            moduleDao.initTable2();
            moduleDao.initTable3();
            moduleDao.initTable4();
            moduleDao.initTable5();
            moduleDao.initTable6();
            moduleDao.initTable7();
            TableCache.addToCache(MODULE_TABLE);
        } else {
            int columnCount = tableDao.columnExisted("description", MODULE_TABLE);
            if (columnCount == 0) {
                tableDao.addVarCharColumn("description", MODULE_TABLE);
            }
            columnCount = tableDao.columnExisted("input_type", MODULE_TABLE);
            if (columnCount == 0) {
                tableDao.addVarCharColumn("input_type", MODULE_TABLE);
            }
        }
    }

    private static void checkToInitInspectDeviceTypeTable() {
        if (!TableCache.tableExisted(INSPECT_DEVICE_TYPE_TABLE)) {
            inspectDeviceTypeDao.createTable();
            inspectDeviceTypeDao.initTable();
            inspectDeviceTypeDao.initTable1();
            inspectDeviceTypeDao.initTable2();
            inspectDeviceTypeDao.initTable3();
            TableCache.addToCache(INSPECT_DEVICE_TYPE_TABLE);
        } else {

        }
        if (!TableCache.tableExisted(INSPECT_DEVICE_MENU_TABLE)) {
            inspectDeviceMenuDao.createTable();
            inspectDeviceMenuDao.initTable();
            inspectDeviceMenuDao.initTable1();
            inspectDeviceMenuDao.initTable2();
            inspectDeviceMenuDao.initTable3();
            inspectDeviceMenuDao.initTable4();
            inspectDeviceMenuDao.initTable5();
            inspectDeviceMenuDao.initTable6();
            TableCache.addToCache(INSPECT_DEVICE_MENU_TABLE);
        } else {

        }
        if (!TableCache.tableExisted(INSPECT_DEVICE_MODULE_TABLE)) {
            inspectDeviceModuleDao.createTable();
            inspectDeviceModuleDao.initTable();
            inspectDeviceModuleDao.initTable1();
            inspectDeviceModuleDao.initTable2();
            inspectDeviceModuleDao.initTable3();
            inspectDeviceModuleDao.initTable4();
            inspectDeviceModuleDao.initTable5();
            inspectDeviceModuleDao.initTable6();
            inspectDeviceModuleDao.initTable7();
            TableCache.addToCache(INSPECT_DEVICE_MODULE_TABLE);
        } else {

        }
    }

    private static void checkToInitTaskTable() {
        if (!TableCache.tableExisted(TASK_TABLE)) {
            taskDao.createTable();
            TableCache.addToCache(TASK_TABLE);
        } else {
            int columnCount = tableDao.columnExisted("apply_by", TASK_TABLE);
            if (columnCount == 0) {
                tableDao.addIntColumn("apply_by", TASK_TABLE);
            }
            columnCount = tableDao.columnExisted("submit_time", TASK_TABLE);
            if (columnCount == 0) {
                tableDao.addDateColumn("submit_time", TASK_TABLE);
            }
            columnCount = tableDao.columnExisted("inspect_device_type_id", TASK_TABLE);
            if (columnCount == 0) {
                tableDao.addIntColumn("inspect_device_type_id", TASK_TABLE);
            }
        }
    }

    private static void checkToInitInspectTable() {
        if (!TableCache.tableExisted(INSPECT_TABLE)) {
            inspectDao.createTable();
            TableCache.addToCache(INSPECT_TABLE);
        } else {
            int columnCount = tableDao.columnExisted("inspect_device_type_id", INSPECT_TABLE);
            if (columnCount == 0) {
                tableDao.addIntColumn("inspect_device_type_id", INSPECT_TABLE);
            }
        }
        if (!TableCache.tableExisted(TROUBLE_SHOOT_TABLE)) {
            troubleShootDao.createTable();
            TableCache.addToCache(TROUBLE_SHOOT_TABLE);
        } else {
            int columnCount = tableDao.columnExisted("inspect_device_type_id", TROUBLE_SHOOT_TABLE);
            if (columnCount == 0) {
                tableDao.addIntColumn("inspect_device_type_id", TROUBLE_SHOOT_TABLE);
            }
        }
    }

    private static void checkToInitWatchTable() {
        if (!TableCache.tableExisted(WATCH_TABLE)) {
            watchDao.createTable();
            TableCache.addToCache(WATCH_TABLE);
        } else {

        }
    }

    private static void checkToInitAlarmRealTimeTable() {
        if (!TableCache.tableExisted(ALARM_REALTIME_TABLE)) {
            alarmRealTimeDao.createTable();
            TableCache.addToCache(ALARM_REALTIME_TABLE);
        } else {

        }
    }

    private static void checkToInitFacTable() {
        if (!TableCache.tableExisted(FACILITY_TABLE)) {
            facilityDao.createTable();
            TableCache.addToCache(FACILITY_TABLE);
        } else {
            int columnCount = tableDao.columnExisted("type", FACILITY_TABLE);
            if (columnCount == 0) {
                tableDao.addIntColumn("type", FACILITY_TABLE);
            }
            columnCount = tableDao.columnExisted("cch_freq", FACILITY_TABLE);
            if (columnCount == 0) {
                tableDao.addVarCharColumn("cch_freq", FACILITY_TABLE);
            }
            columnCount = tableDao.columnExisted("tch_count", FACILITY_TABLE);
            if (columnCount == 0) {
                tableDao.addIntColumn("tch_count", FACILITY_TABLE);
            }
            columnCount = tableDao.columnExisted("rcu_status", FACILITY_TABLE);
            if (columnCount == 0) {
                tableDao.addIntColumn("rcu_status", FACILITY_TABLE);
            }
        }
    }

    private static void checkToInitFacGroupTable() {
        if (!TableCache.tableExisted(FACILITY_FACILITY_GROUP_TABLE)) {
            facilityGroupDao.createFacilityGroupTable();
            TableCache.addToCache(FACILITY_FACILITY_GROUP_TABLE);
        } else {

        }
        if (!TableCache.tableExisted(FACILITY_GROUP_TABLE)) {
            facilityGroupDao.createTable();
            facilityGroupDao.initTable();
            TableCache.addToCache(FACILITY_GROUP_TABLE);
        } else {

        }
    }

    private static void createTaskTablePerMonth() {
        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.HOUR_OF_DAY, 1); //凌晨1点
        calendar.set(Calendar.MINUTE, 0);
        calendar.set(Calendar.SECOND, 0);
        Date date=calendar.getTime(); //第一次执行定时任务的时间
        Timer timer = new Timer();
        SplitTableTimer splitTableTimer = new SplitTableTimer();
        //安排指定的任务在指定的时间开始进行重复的固定延迟执行。
        timer.scheduleAtFixedRate(splitTableTimer,date,PERIOD_DAY);
    }

}
