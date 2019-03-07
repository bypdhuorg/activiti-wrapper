package com.flowablewrapper.bean.db;

import com.baomidou.mybatisplus.annotations.TableName;
import com.flowablewrapper.base.BaseEntity;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.ToString;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

/**
 *流程定义
 * @author meibo
 */
@Data
@Entity
@ToString
@Table(name = "bpm_model")
@TableName("bpm_model")
@ApiModel(value = "模块定义")
public class BpmModel extends BaseEntity {

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




}
