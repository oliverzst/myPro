package com.actec.bsms.mongoDemo;

import com.actec.bsms.controller.BaseController;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import java.util.List;

/**
 * 字典控制器
 *
 * @author zhangst
 * @create 2018-03-16 5:17 PM
 */
@Path("/dict")
@Scope("singleton")
@Produces(MediaType.APPLICATION_JSON)
public class DictController extends BaseController {

    @Autowired
    DictService dictService;

    @GET
    @Path("/get")
    public Dict getDict(@QueryParam("id")String id) {
        return dictService.get(id);
    }

    @GET
    @Path("/delete")
    public String delete(@QueryParam("id")String id) {
        dictService.remove(id);
        return "delete sucess";
    }

    @GET
    @Path("/add")
    public String insert(@QueryParam("id")String id, @QueryParam("label")String label, @QueryParam("value")int value, @QueryParam("description")String description) {
        Dict dict =new Dict(id, value, label, description);
        dictService.insert(dict);
        return "sucess";
    }

    @GET
    @Path("/findAll")
    public List<Dict> find(){
        return dictService.findAll();
    }

    @GET
    @Path("/update")
    public String update(@QueryParam("id")String id, @QueryParam("label")String label, @QueryParam("value")int value, @QueryParam("description")String description){
        Dict dict = dictService.get(id);
        dict.setValue(value);
        dict.setLabel(label);
        dict.setDescription(description);
        dictService.update(dict);
        return "sucess";
    }

}
