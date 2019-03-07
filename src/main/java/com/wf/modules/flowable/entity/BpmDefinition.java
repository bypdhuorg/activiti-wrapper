package com.wf.modules.flowable.entity;

import com.wf.base.BaseEntity;
import com.wf.common.enums.EnumBpmDefinitionStatus;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.persistence.*;

/**
 * 流程定义 业务数据
 * @author meibo
 */
@Data
@Entity
@Table(name = "wf_bpm_definition")
@ApiModel(value = "流程定义")
public class BpmDefinition extends BaseEntity {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "流程名称")
    @Column(length = 64)
    private String name;

    @ApiModelProperty(value = "流程业务主键")
    @Column(length = 64,name="bpm_key")
    private String key;

    @ApiModelProperty(value = "流程描述")
    @Column(length = 1024)
    private String description;

    @ApiModelProperty(value = "所属分类ID")
    @Column(length = 64)
    private String category;


    @ApiModelProperty(value = "关联业务表")
    @Column(length = 64)
    private String bunissnessTable;

    @ApiModelProperty(value = "流程状态。草稿、发布、禁用")
    @Column(name="bpm_status",columnDefinition="varchar(20) COMMENT '流程状态。草稿、发布、禁用'")
    @Enumerated(EnumType.STRING)
    private EnumBpmDefinitionStatus status;


    @ApiModelProperty(value = "模型ID")
    @Column(length = 64)
    private String modelId;


    @ApiModelProperty(value = "flowable流程定义ID")
    @Column(length = 64)
    private String actDefId;

    @ApiModelProperty(value = "flowable模型ID")
    @Column(length = 64)
    private String actModelId;

    @ApiModelProperty(value = "flowable流程发布ID")
    @Column(length = 64)
    private String actDeployId;

    @ApiModelProperty(value = "当前版本号")
    @Column(name="bpm_version")
    private Integer version;

    @ApiModelProperty(value = "是否是主版本")
    private Boolean isMain;

    @ApiModelProperty(value = "流程业务配置")
    @Column(columnDefinition="TEXT")
    private String defSetting;
}