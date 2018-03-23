package com.actec.bsms.controller;

import com.actec.bsms.utils.Global;
import org.springframework.context.annotation.Scope;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;

/**
 * 系统配置相关接口
 *
 * @author zhangst
 * @create 2017-11-24 2:25 PM
 */
@Path("/config")
@Scope("singleton")
@Produces(MediaType.APPLICATION_JSON)
public class ConfigController extends BaseController {

    /**
     * 设置网管IP地址
     */
    @GET
    @Path("/setNmpIp")
    public void setNmpIp(@QueryParam("nmpIp")String nmpIp) {
        Global.writeProperties("/application.properties", "nmp.ip", nmpIp);
    }

}
