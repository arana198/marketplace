package com.marketplace.common.annotation;

import com.marketplace.common.security.UserRole;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target(value = ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface IsActive {

     String[] roles() default {UserRole.ROLE_BROKER, UserRole.ROLE_USER, UserRole.ROLE_COMPANY_ADMIN};
}