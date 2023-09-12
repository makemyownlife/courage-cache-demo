package com.courage.cache.service.disruptor;

public class DataEvent<T> {

    private T data;

    public T getData() {
        return data;
    }

    public void setData(final T data) {
        this.data = data;
    }
    
}