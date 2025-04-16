package com.pedrojpx.codeflix.admin.catalog.infrastructure.api;

import com.pedrojpx.codeflix.admin.catalog.ControllerTest;
import com.pedrojpx.codeflix.admin.catalog.application.category.create.CreateCategoryUseCase;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;

@ControllerTest(controllers = CategoryAPI.class)
public class CategoryAPITest {

    @Autowired
    private MockMvc mvc;

    @MockBean
    private CreateCategoryUseCase useCase;

    @Test
    public void test() {

    }
}
