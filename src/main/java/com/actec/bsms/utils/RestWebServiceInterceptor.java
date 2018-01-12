package com.actec.bsms.utils;

import javax.ws.rs.container.ContainerRequestContext;
import java.io.IOException;

/**
 * 配拦截器
 *
 * @author zhangst
 * @create 2017-11-03 11:51 AM
 */

public class RestWebServiceInterceptor {

    public void filter(ContainerRequestContext requestContext) throws IOException {
        //.......
        System.out.println("进行拦截");
    }

}
