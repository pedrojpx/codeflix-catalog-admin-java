package com.pedrojpx.codeflix.admin.catalog.infrastructure.category;

import com.pedrojpx.codeflix.admin.catalog.domain.category.CategoryGateway;
import com.pedrojpx.codeflix.admin.catalog.infrastructure.category.persistence.CategoryRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

@MySqlGatewayTest
public class CategoryMySqlGatewayTest {

    @Autowired
    private CategoryGateway gateway;

    @Autowired
    private CategoryRepository repo;

    @Test
    public void testInjectedDependencies() {
        Assertions.assertNotNull(gateway);
        Assertions.assertNotNull(repo);
    }


}
