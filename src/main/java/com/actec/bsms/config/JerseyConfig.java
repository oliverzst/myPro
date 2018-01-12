package com.actec.bsms.config;

import com.actec.bsms.common.filter.CorsFilter;
import com.actec.bsms.controller.*;
import org.glassfish.jersey.server.ResourceConfig;
import org.glassfish.jersey.server.spring.scope.RequestContextFilter;

/**
 * jersey的配置文件，主要是指定扫描的包
 *
 * @author zhangst
 * @create 2017-12-04 3:42 PM
 */
public class JerseyConfig extends ResourceConfig {

    public JerseyConfig() {
        register(RequestContextFilter.class);
        register(CorsFilter.class);
        //配置restful package. Jar包无法识别 只能war包
//        packages("com.actec.bsms.controller");
        register(FacilityController.class);
        register(MenuController.class);
        register(TaskController.class);
        register(UserController.class);
        register(ModuleController.class);
        register(InspectDeviceTypeController.class);
        register(TroubleShootController.class);
        register(StatisticsController.class);
    }

}
