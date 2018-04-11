package com.actec.bsms.mongoDemo;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;

import java.io.Serializable;

/**
 * 字典
 *
 * @author zhangst
 * @create 2018-03-16 4:06 PM
 */
@Document(collection="dict")
//@CompoundIndexes({
//        @CompoundIndex(name = "")
//})
public class Dict implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @Indexed
    private String id;
    private Integer value;
    private String label;
    private String description;

    public Dict(String id, Integer value, String label, String description) {
        this.id = id;
        this.value = value;
        this.label = label;
        this.description = description;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public Integer getValue() {
        return value;
    }

    public void setValue(Integer value) {
        this.value = value;
    }

    public String getLabel() {
        return label;
    }

    public void setLabel(String label) {
        this.label = label;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}
