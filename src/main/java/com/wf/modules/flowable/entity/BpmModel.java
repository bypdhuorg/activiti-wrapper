package com.wf.modules.flowable.entity;

import com.fasterxml.jackson.databind.node.ObjectNode;
import com.wf.base.BaseEntity;
import io.swagger.annotations.ApiModelProperty;
import io.swagger.annotations.ApiModel;
import lombok.Data;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;
import javax.persistence.Transient;

/**
 * 模型定义 业务数据
 * @author meibo
 */
@Data
@Entity
@Table(name = "wf_bpm_model")
@ApiModel(value = "模型定义")
public class BpmModel extends BaseEntity {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "名称")
    @Column(length = 64)
    private String name;

    @ApiModelProperty(value = "标识Key")
    @Column(length = 64,name="bpm_key")
    private String key;

    @ApiModelProperty(value = "备注")
    @Column(length = 1024)
    private String description;

    @ApiModelProperty(value = "所属分类ID")
    @Column(length = 64)
    private String category;

    @ApiModelProperty(value = "flowable模型ID")
    @Column(length = 64)
    private String actModelId;


    @ApiModelProperty(value = "当前版本号")
    @Column(name="bpm_version")
    private Integer version;

    @ApiModelProperty(value = "是否是主版本")
    private Boolean isMain;

    /**
     * json xml 忽略
     */
    @Transient
    private String jsonXml;

    @Transient
    private boolean newVersion;

    @Transient
    private ObjectNode objectNode;
}