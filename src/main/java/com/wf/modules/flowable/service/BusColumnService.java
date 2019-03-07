package com.wf.modules.flowable.service;

import com.wf.base.BaseService;
import com.wf.bean.vo.SearchTimeVo;
import com.wf.modules.flowable.entity.BusColumn;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

/**
 * 业务字段表接口
 * @author meibo
 */
public interface BusColumnService extends BaseService<BusColumn,String> {

    /**
     * 多条件分页获取(业务字段表)
     * @param busColumn
     * @param searchVo
     * @param pageable
     * @return
     */
    Page<BusColumn> findByCondition(BusColumn busColumn, SearchTimeVo searchVo, Pageable pageable);
}