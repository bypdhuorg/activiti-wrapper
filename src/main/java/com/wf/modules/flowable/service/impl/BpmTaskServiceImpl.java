package com.wf.modules.flowable.service.impl;

import cn.hutool.core.date.DateUtil;
import com.wf.bean.vo.SearchTimeVo;
import com.wf.modules.flowable.dao.BpmTaskDao;
import com.wf.modules.flowable.entity.BpmTask;
import com.wf.modules.flowable.service.BpmTaskService;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.lang.Nullable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.criteria.*;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * 流程任务接口实现
 * @author meibo
 */
@Slf4j
@Service
@Transactional
public class BpmTaskServiceImpl implements BpmTaskService {

    @Autowired
    private BpmTaskDao bpmTaskDao;

    @Override
    public BpmTaskDao getRepository() {
        return bpmTaskDao;
    }

    @Override
    public void saveCheck(BpmTask bpmTask){
       // if(getRepository().findByUserId(bpmTask.getUserId()) !=null){
       //     throw new UserClientException("XXX已经存在");
       // }
    };
    @Override
    public void updateCheck(BpmTask bpmTask){};

    @Override
    /**
     * 多条件分页获取(流程任务)
     * @param bpmTask
     * @param searchVo
     * @param pageable
     * @return
     */
    public Page<BpmTask> findByCondition(BpmTask bpmTask, SearchTimeVo searchVo, Pageable pageable) {
        return getRepository().findAll(new Specification<BpmTask>() {
            @Nullable
            @Override
            public Predicate toPredicate(Root<BpmTask> root, CriteriaQuery<?> cq, CriteriaBuilder cb) {

                Path<Date> createTimeField=root.get("createTime");

                List<Predicate> list = new ArrayList<Predicate>();

                //创建时间
                if(searchVo.getStartDate()!=null){
                    Date start = DateUtil.date(searchVo.getStartDate());
                    list.add(cb.greaterThanOrEqualTo(createTimeField, start));
                }
                if(searchVo.getEndDate()!=null){
                    Date end = DateUtil.date(searchVo.getEndDate());
                    list.add(cb.lessThanOrEqualTo(createTimeField, end));
                }

                Predicate[] arr = new Predicate[list.size()];
                cq.where(list.toArray(arr));
                return null;
            }
        }, pageable);
    }
}