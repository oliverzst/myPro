package com.actec.bsms.service;

import com.actec.bsms.utils.RestWebServiceInterceptor;
import org.glassfish.jersey.server.ResourceConfig;

/**
 * 配置资源注册类
 *
 * @author zhangst
 * @create 2017-11-03 11:48 AM
 */

public class RestWebService extends ResourceConfig {

    public RestWebService() {
        register(RestWebServiceInterceptor.class);
    }

}
