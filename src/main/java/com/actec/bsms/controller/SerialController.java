package com.actec.bsms.controller;

import com.actec.bsms.service.SerialService;
import com.alibaba.fastjson.JSON;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;

/**
 * 串口通信控制器
 *
 * @author zhangst
 * @create 2018-04-10 6:23 PM
 */
@Path("/serial")
@Scope("singleton")
@Produces(MediaType.APPLICATION_JSON)
public class SerialController extends BaseController {

    @Autowired
    SerialService serialService;

    @GET
    @Path("/sendMessage")
    public String sendMessage(@QueryParam("message")String message) {
        try {
            serialService.sendMessage(message);
            return JSON.toJSONString(successResult);
        } catch (Exception e) {
            logger.debug(e.getMessage());
            return JSON.toJSONString(failResult);
        }
    }

}
