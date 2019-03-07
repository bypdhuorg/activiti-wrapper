package com.flowablewrapper.bean.vo;

import com.flowablewrapper.bean.BasePageVO;
import lombok.Data;

@Data
public class FlowProcessDefinitionPageVO extends BasePageVO {

    private String name;
    private String key;
    private String category;

}
