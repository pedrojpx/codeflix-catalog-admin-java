package com.pedrojpx.codeflix.admin.catalog.application.category.create;

import com.pedrojpx.codeflix.admin.catalog.domain.category.Category;
import com.pedrojpx.codeflix.admin.catalog.domain.category.CategoryGateway;
import com.pedrojpx.codeflix.admin.catalog.domain.validation.handler.Notification;
import io.vavr.API;
import io.vavr.control.Either;

import java.util.Objects;

public class DefaultCreateCategoryUseCase extends CreateCategoryUseCase {

    private final CategoryGateway gateway;

    public DefaultCreateCategoryUseCase(final CategoryGateway gateway) {
        this.gateway = Objects.requireNonNull(gateway);
    }

    @Override
    public Either<Notification, CreateCategoryOutput> execute(final CreateCategoryInput input) {
        final var cat = Category.newCategory(input.name(), input.description(), input.isActive());
        final var notification = Notification.create();
        cat.validate(notification);

        //this "hasError" catches category entity creation errors
        return notification.hasError() ? API.Left(notification) : save(cat);
    }

    private Either<Notification, CreateCategoryOutput> save(final Category cat) {
        //this try catches category gateway saving errors
        return API.Try(() -> this.gateway.save(cat))
                .toEither()
                .bimap(Notification::create, CreateCategoryOutput::from);
    }
}
