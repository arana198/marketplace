package com.marketplace.common.converter;

import java.io.IOException;

public interface BaseConverter<T, K> {
     K convert(T source) throws IOException;
}
