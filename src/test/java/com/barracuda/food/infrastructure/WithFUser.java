package com.barracuda.food.infrastructure;

import org.springframework.security.test.context.support.WithSecurityContext;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

@WithSecurityContext(factory = FSecurityContextFactory.class)
@Retention(RetentionPolicy.RUNTIME)
public @interface WithFUser {

}
