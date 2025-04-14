package com.pedrojpx.codeflix.admin.catalog.infrastructure.category;

import com.pedrojpx.codeflix.admin.catalog.domain.category.Category;
import com.pedrojpx.codeflix.admin.catalog.domain.category.CategoryGateway;
import com.pedrojpx.codeflix.admin.catalog.domain.category.CategoryID;
import com.pedrojpx.codeflix.admin.catalog.domain.category.CategorySearchQuery;
import com.pedrojpx.codeflix.admin.catalog.domain.pagination.Pagination;
import com.pedrojpx.codeflix.admin.catalog.infrastructure.category.persistence.CategoryRepository;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class CategoryMySqlGateway implements CategoryGateway {

    private final CategoryRepository repo;

    public CategoryMySqlGateway(final CategoryRepository repo) {
        this.repo = repo;
    }


    @Override
    public Category save(Category category) {
        return null;
    }

    @Override
    public void deleteById(CategoryID id) {

    }

    @Override
    public Optional<Category> findById(CategoryID id) {
        return Optional.empty();
    }

    @Override
    public Category update(Category category) {
        return null;
    }

    @Override
    public Pagination<Category> findAll(CategorySearchQuery query) {
        return null;
    }
}
