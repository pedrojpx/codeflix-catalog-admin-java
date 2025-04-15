package com.pedrojpx.codeflix.admin.catalog.application.category.create;

import com.pedrojpx.codeflix.admin.catalog.IntegrationTest;
import com.pedrojpx.codeflix.admin.catalog.application.category.create.CreateCategoryInput;
import com.pedrojpx.codeflix.admin.catalog.application.category.create.CreateCategoryOutput;
import com.pedrojpx.codeflix.admin.catalog.application.category.create.CreateCategoryUseCase;
import com.pedrojpx.codeflix.admin.catalog.domain.category.CategoryGateway;
import com.pedrojpx.codeflix.admin.catalog.infrastructure.category.persistence.CategoryRepository;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.SpyBean;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.any;

@IntegrationTest
public class CreateCategoryUseCaseIT {

    @Autowired
    private CreateCategoryUseCase useCase;

    @Autowired
    private CategoryRepository repo;

    @SpyBean
    private CategoryGateway gateway;

    @Test
    public void testInjection() {
        assertNotNull(useCase);
        assertNotNull(repo);
    }

    @Test
    public void givenValidInput_whenCallsCreateCategory_ShouldReturnCategoryId() {

        final var expectedName = "Filmes";
        final var expectedDescription = "Categoria de Filmes";
        final var expectedIsActive = true;

        final var input = CreateCategoryInput.with(expectedName, expectedDescription, expectedIsActive);

        assertEquals(0, repo.count());
        final CreateCategoryOutput output = useCase.execute(input).get();
        assertEquals(1, repo.count());

        assertNotNull(output);
        assertNotNull(output.id());

        final var found = repo.findById(output.id().getValue()).get();

        assertEquals(output.id().getValue(),found.getId());
        assertEquals(expectedName,found.getName());
        assertEquals(expectedDescription,found.getDescription());
        assertEquals(expectedIsActive,found.isActive());
        assertNotNull(found.getCreatedAt());
        assertNotNull(found.getUpdatedAt());
        assertNull(found.getDeletedAt());
    }

    @Test
    public void givenInvalidName_whenCallsCreateCategory_ShouldReturnDomainException() {
        final String expectedName = null;
        final var expectedDescription = "Categoria de Filmes";
        final var expectedIsActive = true;
        final var expectedErrorMessage = "'name' should not be null";
        final var expectedErrorCount = 1;

        assertEquals(0, repo.count());

        final var input = CreateCategoryInput.with(expectedName, expectedDescription, expectedIsActive);
        final var notifications = useCase.execute(input).getLeft();

        assertEquals(0, repo.count());
        assertEquals(expectedErrorCount, notifications.getErrors().size());
        assertEquals(expectedErrorMessage, notifications.firstError().message());
        Mockito.verify(gateway, Mockito.times(0)).save(any());

    }

    @Test
    public void givenValidInputWithInactiveCategory_whenCallsCreateCategory_ShouldReturnCategoryId() {

        final var expectedName = "Filmes";
        final var expectedDescription = "Categoria de Filmes";
        final var expectedIsActive = false;


        assertEquals(0, repo.count());
        final var input = CreateCategoryInput.with(expectedName, expectedDescription, expectedIsActive);
        final var output = useCase.execute(input).get();
        assertEquals(1, repo.count());

        assertNotNull(output);
        assertNotNull(output.id());

        final var found = repo.findById(output.id().getValue()).get();

        assertEquals(output.id().getValue(),found.getId());
        assertEquals(expectedName,found.getName());
        assertEquals(expectedDescription,found.getDescription());
        assertEquals(expectedIsActive,found.isActive());
        assertNotNull(found.getCreatedAt());
        assertNotNull(found.getUpdatedAt());
        assertNotNull(found.getDeletedAt());
    }

    @Test
    public void givenValidInput_whenCallsGatewayThrowException_ShouldReturnException() {

        final var expectedName = "Filmes";
        final var expectedDescription = "Categoria de Filmes";
        final var expectedIsActive = true;
        final var expectedErrorMessage = "Gateway error";
        final var expectedErrorCount = 1;

        assertEquals(0, repo.count());
        final var input = CreateCategoryInput.with(expectedName, expectedDescription, expectedIsActive);

        /*
        Usando o doThrow, o Mockito realiza a ação ao invés de chamar a função do gateway
        Usando o when, o Mockito chama a implementação do gateway e executa no lugar. Usar o when quando há implementação faz a implementação ser chamada e então se faz ação descrita rodar.

        Em resumo: doThrow...when para SpyBean e when...thenThrow para mock
         */
        Mockito.doThrow(new IllegalStateException("Gateway error")).when(gateway).save(any());
//        Mockito.when(gateway.save(Mockito.any())).thenThrow(new IllegalStateException("Gateway error"));

        final var notification = useCase.execute(input).getLeft();
        assertEquals(0, repo.count());

        assertEquals(expectedErrorCount, notification.getErrors().size());
        assertEquals(expectedErrorMessage, notification.firstError().message());
    }
}
