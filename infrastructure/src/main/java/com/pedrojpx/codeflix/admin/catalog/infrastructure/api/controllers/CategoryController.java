package com.pedrojpx.codeflix.admin.catalog.infrastructure.api.controllers;

import com.pedrojpx.codeflix.admin.catalog.application.category.create.CreateCategoryUseCase;
import com.pedrojpx.codeflix.admin.catalog.domain.pagination.Pagination;
import com.pedrojpx.codeflix.admin.catalog.infrastructure.api.CategoryAPI;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;

import java.util.Objects;

@RestController
public class CategoryController implements CategoryAPI {

    private final CreateCategoryUseCase useCase;

    public CategoryController(CreateCategoryUseCase useCase) {
        this.useCase = Objects.requireNonNull(useCase);
    }

    @Override
    public ResponseEntity<?> createCategory() {
        return null;
    }

    @Override
    public Pagination<?> listCategories(String search, String page, String perPage, String sort, String dir) {
        return null;
    }
}
