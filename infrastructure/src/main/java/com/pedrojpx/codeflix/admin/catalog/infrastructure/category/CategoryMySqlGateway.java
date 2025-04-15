package com.pedrojpx.codeflix.admin.catalog.infrastructure.category;

import com.pedrojpx.codeflix.admin.catalog.domain.category.Category;
import com.pedrojpx.codeflix.admin.catalog.domain.category.CategoryGateway;
import com.pedrojpx.codeflix.admin.catalog.domain.category.CategoryID;
import com.pedrojpx.codeflix.admin.catalog.domain.category.CategorySearchQuery;
import com.pedrojpx.codeflix.admin.catalog.domain.pagination.Pagination;
import com.pedrojpx.codeflix.admin.catalog.infrastructure.category.persistence.CategoryJpaEntity;
import com.pedrojpx.codeflix.admin.catalog.infrastructure.category.persistence.CategoryRepository;
import com.pedrojpx.codeflix.admin.catalog.infrastructure.utils.SpecificationUtils;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
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
        //Specification utiliza a Criterion API do JPA
        //Pageable e PageRequest do Spring Data

        //Paginação
        final var page = PageRequest.of(query.page(), query.perPage(), Sort.by(Sort.Direction.fromString(query.direction()), query.sort()));

        //Busca dinâmica
        final var specs = Optional.ofNullable(query.terms())
                .filter(str -> !str.isBlank())
                .map(str ->
                        SpecificationUtils.<CategoryJpaEntity>like("name", str)
                                .or(SpecificationUtils.<CategoryJpaEntity>like("description", str))
                )
                .orElse(null);

        final var result = this.repo.findAll(specs, page);

        return new Pagination<>(
                result.getNumber(),
                result.getSize(),
                result.getTotalElements(),
                result.map(CategoryJpaEntity::toAggregate).toList()
        );
    }
}
