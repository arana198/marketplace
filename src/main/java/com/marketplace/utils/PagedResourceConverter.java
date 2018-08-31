package com.marketplace.utils;

import org.springframework.data.domain.Page;
import org.springframework.hateoas.PagedResources;
import org.springframework.hateoas.PagedResources.PageMetadata;

public class PagedResourceConverter {
     public static <T> PagedResources<T> convert(final Page<T> page) {
          PageMetadata pageMetadata = new PageMetadata(page.getSize(), page.getNumber(), page.getTotalElements(), page.getTotalPages());
          return new PagedResources<T>(page.getContent(), pageMetadata);
     }
}
