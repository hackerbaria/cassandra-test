package com.arbin.aal.service;

import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.SliceImpl;

import java.util.List;

public class CassandraSliceImpl<T> extends SliceImpl<T> {

    private final String pagingState;

    public CassandraSliceImpl(List<T> content, Pageable pageable, boolean hasNext, String pagingState) {
        super(content, pageable, hasNext);
        this.pagingState = pagingState;
    }

    public String getPagingState() {
        return pagingState;
    }
}
