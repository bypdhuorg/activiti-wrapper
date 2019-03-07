package com.flowablewrapper.bean;

import lombok.Data;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Value;

@Data
public class BasePageVO<T> {
    /**
     * 页码(from 1)
     */
    @Value("${page.defaultpage}")
    private Integer page;

    /**
     * 每页条数
     */
    @Value("${page.defaultpageSize}")
    private Integer pageSize;

    @Value("${page.defaultsortby}")
    private String sortby;

    @Value("${page.defaultdirection}")
    private SortDirection direction;

    public void handleSort(String orderSort) {
        if (StringUtils.isEmpty(orderSort)) {

        } else {
            String[] orderSorts = StringUtils.stripAll(orderSort.split(","));
            String sortZero=orderSorts[0];
            if (sortZero.startsWith("-")) {
                this.setDirection(SortDirection.desc);
                this.setSortby(sortZero.substring(1, sortZero.length()));
            }else{
                this.setDirection(SortDirection.asc);
                this.setSortby(sortZero);
            }

        }
    }

}
