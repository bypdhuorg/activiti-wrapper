package com.flowablewrapper.bean;

import lombok.Data;

import java.util.List;

@Data
public class basePageDTO<T> {
    private Long total;

    private List<T> content;
}
