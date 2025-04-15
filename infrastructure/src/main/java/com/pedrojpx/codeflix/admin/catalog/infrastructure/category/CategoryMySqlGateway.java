package com.pedrojpx.codeflix.admin.catalog.infrastructure.category;

import com.pedrojpx.codeflix.admin.catalog.domain.category.Category;
import com.pedrojpx.codeflix.admin.catalog.domain.category.CategoryGateway;
import com.pedrojpx.codeflix.admin.catalog.domain.category.CategoryID;
import com.pedrojpx.codeflix.admin.catalog.domain.category.CategorySearchQuery;
import com.pedrojpx.codeflix.admin.catalog.domain.pagination.Pagination;
import com.pedrojpx.codeflix.admin.catalog.infrastructure.category.persistence.CategoryJpaEntity;
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
    public Category save(final Category category) {
        return this.repo.save(CategoryJpaEntity.from(category)).toAggregate();
    }

    @Override
    public void deleteById(CategoryID id) {
        final var idValue = id.getValue();
        if (this.repo.existsById(idValue)) {
            this.repo.deleteById(idValue);
        }
    }

    @Override
    public Optional<Category> findById(CategoryID id) {
        return this.repo.findById(id.getValue()).map(CategoryJpaEntity::toAggregate); //is used bc of Optional: it only does the mapped function if the value actually exists
    }

    @Override
    public Category update(Category category) {
        return this.repo.save(CategoryJpaEntity.from(category)).toAggregate();
    }

    @Override
    public Pagination<Category> findAll(CategorySearchQuery query) {
        return null;
    }
}
