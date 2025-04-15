package com.pedrojpx.codeflix.admin.catalog.application;

import com.pedrojpx.codeflix.admin.catalog.IntegrationTest;
import com.pedrojpx.codeflix.admin.catalog.application.category.create.CreateCategoryUseCase;
import com.pedrojpx.codeflix.admin.catalog.infrastructure.category.persistence.CategoryRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import static org.junit.jupiter.api.Assertions.assertNotNull;

@IntegrationTest
public class SampleIntegrationTest {

    @Autowired
    private CreateCategoryUseCase usecase;

    @Autowired
    private CategoryRepository repo;

    @Test
    public void testInjection() {
        assertNotNull(usecase);
        assertNotNull(repo);
    }
}
