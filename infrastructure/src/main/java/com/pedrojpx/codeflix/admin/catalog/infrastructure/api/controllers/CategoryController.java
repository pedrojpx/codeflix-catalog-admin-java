package com.pedrojpx.codeflix.admin.catalog.infrastructure.api.controllers;

import com.pedrojpx.codeflix.admin.catalog.domain.pagination.Pagination;
import com.pedrojpx.codeflix.admin.catalog.infrastructure.api.CategoryAPI;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class CategoryController implements CategoryAPI {
    @Override
    public ResponseEntity<?> createCategory() {
        return null;
    }

    @Override
    public Pagination<?> listCategories(String search, String page, String perPage, String sort, String dir) {
        return null;
    }
}
