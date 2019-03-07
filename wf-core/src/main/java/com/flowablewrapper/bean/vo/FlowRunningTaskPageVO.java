package com.flowablewrapper.bean.vo;

import com.flowablewrapper.bean.BasePageVO;
import lombok.Data;

@Data
public class FlowRunningTaskPageVO extends BasePageVO {

    private String buId;

    private String userId;

    private String taskName;

    private String assigned;

    private String definitionKey;

    private String processInstanceId;

}
