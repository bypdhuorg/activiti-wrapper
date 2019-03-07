package com.flowablewrapper.bean.db.business;

import com.baomidou.mybatisplus.annotations.TableName;
import com.flowablewrapper.base.BaseEntity;
import io.swagger.annotations.ApiModel;
import lombok.Data;
import lombok.ToString;

import javax.persistence.Entity;
import javax.persistence.Table;

/**
 * GitLab prject申请流程
 * @author meibo
 */
@Data
@Entity
@ToString
@Table(name = "biz_gitproject")
@TableName("biz_gitproject")
@ApiModel(value = "GitLab prject申请流程")
public class BizGitProject extends BaseEntity {

}
