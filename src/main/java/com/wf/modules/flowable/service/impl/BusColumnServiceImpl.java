package com.wf.modules.flowable.service.impl;

import com.wf.bean.vo.SearchTimeVo;
import com.wf.modules.flowable.dao.BusColumnDao;
import com.wf.modules.flowable.entity.BusColumn;
import com.wf.modules.flowable.service.BusColumnService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.lang.Nullable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

/**
 * 业务字段表接口实现
 * @author meibo
 */
@Slf4j
@Service
@Transactional
public class BusColumnServiceImpl implements BusColumnService {

    @Autowired
    private BusColumnDao busColumnDao;

    @Override
    public BusColumnDao getRepository() {
        return busColumnDao;
    }

    @Override
    public void saveCheck(BusColumn busColumn){
       // if(getRepository().findByUserId(busColumn.getUserId()) !=null){
       //     throw new UserClientException("XXX已经存在");
       // }
    };
    @Override
    public void updateCheck(BusColumn busColumn){};


    @Override
    /**
     * 多条件分页获取(业务字段表)
     * @param busColumn
     * @param searchVo
     * @param pageable
     * @return
     */
    public Page<BusColumn> findByCondition(BusColumn busColumn, SearchTimeVo searchVo, Pageable pageable) {
        return getRepository().findAll(new Specification<BusColumn>() {
            @Nullable
            @Override
            public Predicate toPredicate(Root<BusColumn> root, CriteriaQuery<?> cq, CriteriaBuilder cb) {

                return null;
            }
        }, pageable);
    }
}