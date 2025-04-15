package com.pedrojpx.codeflix.admin.catalog.infrastructure.configuration.usecases;

import com.pedrojpx.codeflix.admin.catalog.application.category.create.CreateCategoryUseCase;
import com.pedrojpx.codeflix.admin.catalog.application.category.create.DefaultCreateCategoryUseCase;
import com.pedrojpx.codeflix.admin.catalog.application.category.delete.DefaultDeleteCategoryUseCase;
import com.pedrojpx.codeflix.admin.catalog.application.category.delete.DeleteCategoryUseCase;
import com.pedrojpx.codeflix.admin.catalog.application.category.retrieve.get.DefaultGetCategoryByIdUseCase;
import com.pedrojpx.codeflix.admin.catalog.application.category.retrieve.get.GetCategoryByIdUseCase;
import com.pedrojpx.codeflix.admin.catalog.application.category.retrieve.list.DefaultListCategoriesUseCase;
import com.pedrojpx.codeflix.admin.catalog.application.category.retrieve.list.ListCategoriesUseCase;
import com.pedrojpx.codeflix.admin.catalog.application.category.update.DefaultUpdateCategoryUseCase;
import com.pedrojpx.codeflix.admin.catalog.application.category.update.UpdateCategoryUseCase;
import com.pedrojpx.codeflix.admin.catalog.domain.category.CategoryGateway;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class CategoryUseCaseConfig {

    private final CategoryGateway gateway;

    //how does spring know where to get the gateway from in order to initialize the beans??
    public CategoryUseCaseConfig(CategoryGateway gateway) {
        this.gateway = gateway;
    }

    @Bean
    public CreateCategoryUseCase createCategoryUseCase() {
        return new DefaultCreateCategoryUseCase(gateway);
    }

    @Bean
    public UpdateCategoryUseCase updateCategoryUseCase() {
        return new DefaultUpdateCategoryUseCase(gateway);
    }

    @Bean
    public GetCategoryByIdUseCase getCategoryByIdUseCase() {
        return new DefaultGetCategoryByIdUseCase(gateway);
    }

    @Bean
    public ListCategoriesUseCase listCategoriesUseCase() {
        return new DefaultListCategoriesUseCase(gateway);
    }

    @Bean
    public DeleteCategoryUseCase deleteCategoryUseCase() {
        return new DefaultDeleteCategoryUseCase(gateway);
    }
}
