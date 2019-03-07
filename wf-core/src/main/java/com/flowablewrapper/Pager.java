package com.flowablewrapper;

import lombok.Data;

import java.util.List;

@Data
public class Pager<T> {
    private List<T> content;
    private int total;

    public Pager(int totalItems,List<T> content){
        this.content=content;
        this.total=totalItems;
    }
}
