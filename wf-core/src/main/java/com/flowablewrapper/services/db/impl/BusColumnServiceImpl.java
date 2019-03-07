package com.flowablewrapper.services.db.impl;

import com.flowablewrapper.dao.BusColumnRepository;
import com.flowablewrapper.services.db.BusColumnService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


/**
 * 业务库 字段接口实现
 * @author meibo
 */
@Service
@Slf4j
@Transactional
public class BusColumnServiceImpl  implements BusColumnService {

    @Autowired
    private BusColumnRepository busColumnRepository;

    @Override
    public BusColumnRepository getRepository() {
        return busColumnRepository;
    }


}

