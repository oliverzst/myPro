package com.actec.bsms.config;

import com.actec.bsms.common.filter.CorsFilter;
import com.actec.bsms.controller.*;
import com.actec.bsms.mongoDemo.DictController;
import org.glassfish.jersey.server.ResourceConfig;
import org.glassfish.jersey.server.spring.scope.RequestContextFilter;

/**
 * jersey的配置文件，主要是指定扫描文件
 *
 * @author zhangst
 * @create 2017-12-04 3:42 PM
 */
public class JerseyConfig extends ResourceConfig {

    public JerseyConfig() {
        register(RequestContextFilter.class);
        register(CorsFilter.class);
        register(FacilityController.class);
        register(MenuController.class);
        register(TaskController.class);
        register(UserController.class);
        register(ModuleController.class);
        register(InspectDeviceTypeController.class);
        register(TroubleShootController.class);
        register(StatisticsController.class);
        register(DictController.class);
        register(SerialController.class);
    }

}
