package com.pedrojpx.codeflix.admin.catalog.application.category.create;

import com.pedrojpx.codeflix.admin.catalog.domain.category.CategoryGateway;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Objects;

import static org.junit.jupiter.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.AdditionalAnswers.returnsFirstArg;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.argThat;

@ExtendWith(MockitoExtension.class)
public class CreateCategoryUseCaseTest {

    @InjectMocks
    private DefaultCreateCategoryUseCase useCase;

    @Mock
    private CategoryGateway gateway;

    //1. teste do caminho feliz
    //2. teste com propriedade invalida (name)
    //3. teste criando uma categoria inativa
    //4. teste simulando um erro genérico vindo do gateway

    @Test
    public void givenValidInput_whenCallsCreateCategory_ShouldReturnCategoryId() {

        final var expectedName = "Filmes";
        final var expectedDescription = "Categoria de Filmes";
        final var expectedIsActive = true;

        final var input = CreateCategoryInput.with(expectedName, expectedDescription, expectedIsActive);

        //gateway is made to return the argument sent so that we can check if the arguments passed were correct
        //(remember we are testing the `UseCase` here! Not the gateway!
        Mockito.when(gateway.save(Mockito.any())).thenAnswer(returnsFirstArg());

        final var output = useCase.execute(input).get();

        assertNotNull(output);
        assertNotNull(output.id());

        //checks that `create` was called once AND with the following data
        Mockito.verify(gateway, Mockito.times(1)).save(argThat(category ->
                     Objects.equals(expectedName, category.getName())
                        && Objects.equals(expectedDescription, category.getDescription())
                        && Objects.equals(expectedIsActive, category.isActive())
                        && Objects.nonNull(category.getId())
                        && Objects.nonNull(category.getCreatedAt())
                        && Objects.nonNull(category.getUpdatedAt())
                        && Objects.isNull(category.getDeletedAt())
        ));
    }
    @Test
    public void givenInvalidName_whenCallsCreateCategory_ShouldReturnDomainException() {

        final String expectedName = null;
        final var expectedDescription = "Categoria de Filmes";
        final var expectedIsActive = true;
        final var expectedErrorMessage = "'name' should not be null";
        final var expectedErrorCount = 1;

        final var input = CreateCategoryInput.with(expectedName, expectedDescription, expectedIsActive);

        final var notifications = useCase.execute(input).getLeft();

        assertEquals(expectedErrorCount, notifications.getErrors().size());
        assertEquals(expectedErrorMessage, notifications.firstError().message());
        Mockito.verify(gateway, Mockito.times(0)).save(any());
    }

    @Test
    public void givenValidInputWithInactiveCategory_whenCallsCreateCategory_ShouldReturnCategoryId() {

        final var expectedName = "Filmes";
        final var expectedDescription = "Categoria de Filmes";
        final var expectedIsActive = false;

        final var input = CreateCategoryInput.with(expectedName, expectedDescription, expectedIsActive);

        //gateway is made to return the argument sent so that we can check if the arguments passed were correct
        //(remember we are testing the `UseCase` here! Not the gateway!
        Mockito.when(gateway.save(Mockito.any())).thenAnswer(returnsFirstArg());

        final var output = useCase.execute(input).get();

        assertNotNull(output);
        assertNotNull(output.id());

        //checks that `create` was called once AND with the following data
        Mockito.verify(gateway, Mockito.times(1)).save(argThat(category ->
                     Objects.equals(expectedName, category.getName())
                        && Objects.equals(expectedDescription, category.getDescription())
                        && Objects.equals(expectedIsActive, category.isActive())
                        && Objects.nonNull(category.getId())
                        && Objects.nonNull(category.getCreatedAt())
                        && Objects.nonNull(category.getUpdatedAt())
                        && Objects.nonNull(category.getDeletedAt())
        ));
    }

    @Test
    public void givenValidInput_whenCallsGatewayThrowException_ShouldReturnException() {

        final var expectedName = "Filmes";
        final var expectedDescription = "Categoria de Filmes";
        final var expectedIsActive = true;
        final var expectedErrorMessage = "Gateway error";
        final var expectedErrorCount = 1;

        final var input = CreateCategoryInput.with(expectedName, expectedDescription, expectedIsActive);

        //gateway is made to return the argument sent so that we can check if the arguments passed were correct
        //(remember we are testing the `UseCase` here! Not the gateway!
        Mockito.when(gateway.save(Mockito.any())).thenThrow(new IllegalStateException("Gateway error"));

        final var notification = useCase.execute(input).getLeft();

        assertEquals(expectedErrorCount, notification.getErrors().size());
        assertEquals(expectedErrorMessage, notification.firstError().message());

        //checks that `create` was called once AND with the following data
        Mockito.verify(gateway, Mockito.times(1)).save(argThat(category ->
                     Objects.equals(expectedName, category.getName())
                        && Objects.equals(expectedDescription, category.getDescription())
                        && Objects.equals(expectedIsActive, category.isActive())
                        && Objects.nonNull(category.getId())
                        && Objects.nonNull(category.getCreatedAt())
                        && Objects.nonNull(category.getUpdatedAt())
                        && Objects.isNull(category.getDeletedAt())
        ));
    }


}
