package com.wf.modules.flowable.service.impl;

import com.wf.modules.flowable.dao.BpmInstanceDao;
import com.wf.modules.flowable.entity.BpmInstance;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * 流程实例接口实现
 * @author meibo
 */
@Slf4j
@Service
public class BpmInstanceBaseService {

    @Autowired
    private BpmInstanceDao bpmInstanceDao;

    
    public BpmInstanceDao getRepository() {
        return bpmInstanceDao;
    }

    
    public void saveCheck(BpmInstance bpmInstance){
       // if(getRepository().findByUserId(bpmInstance.getUserId()) !=null){
       //     throw new UserClientException("XXX已经存在");
       // }
    };
    
    public void updateCheck(BpmInstance bpmInstance){};


}