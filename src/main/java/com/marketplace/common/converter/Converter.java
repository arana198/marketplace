package com.marketplace.common.converter;

import org.springframework.hateoas.ResourceSupport;

public interface Converter<T, K extends ResourceSupport> {
     T convert(K source);

     K convert(T source);
}
