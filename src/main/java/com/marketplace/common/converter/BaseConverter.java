package com.marketplace.common.converter;

public interface BaseConverter<T, K> {
    K convert(T source);
}
