package com.pedrojpx.codeflix.admin.catalog.infrastructure.utils;

import org.springframework.data.jpa.domain.Specification;

public final class SpecificationUtils { //no one can extend from final class

    private SpecificationUtils() {}; //no one can instantiate it

    public static <T> Specification<T> like(final String prop, final String term) {
        return (root, query, cb) -> cb.like(cb.upper(root.get(prop)), "%" + term.toUpperCase() + "%");
    }
}
