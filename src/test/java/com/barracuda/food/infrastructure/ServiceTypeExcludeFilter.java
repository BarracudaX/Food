package com.barracuda.food.infrastructure;

import org.springframework.boot.test.autoconfigure.filter.StandardAnnotationCustomizableTypeExcludeFilter;
import org.springframework.stereotype.Service;

import java.util.LinkedHashSet;
import java.util.Set;

public class ServiceTypeExcludeFilter extends StandardAnnotationCustomizableTypeExcludeFilter<ServiceTest> {

    private static final Set<Class<?>> INCLUDES = new LinkedHashSet<>();

    static {
        INCLUDES.add(Service.class);
    }

    protected ServiceTypeExcludeFilter(Class<?> testClass) {
        super(testClass);
    }

    @Override
    protected Set<Class<?>> getDefaultIncludes() {
        return INCLUDES;
    }
}
