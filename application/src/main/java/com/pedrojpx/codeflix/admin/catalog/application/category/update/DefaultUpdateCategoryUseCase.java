package com.pedrojpx.codeflix.admin.catalog.application.category.update;

import com.pedrojpx.codeflix.admin.catalog.domain.category.Category;
import com.pedrojpx.codeflix.admin.catalog.domain.category.CategoryGateway;
import com.pedrojpx.codeflix.admin.catalog.domain.category.CategoryID;
import com.pedrojpx.codeflix.admin.catalog.domain.exceptions.DomainException;
import com.pedrojpx.codeflix.admin.catalog.domain.validation.Error;
import com.pedrojpx.codeflix.admin.catalog.domain.validation.handler.GatherNotificationsValidationHandler;
import io.vavr.API;
import io.vavr.control.Either;

public class DefaultUpdateCategoryUseCase extends UpdateCategoryUseCase{

    private CategoryGateway gateway;

    public DefaultUpdateCategoryUseCase(final CategoryGateway gw) {
        this.gateway = gw;
    }

    @Override
    public Either<GatherNotificationsValidationHandler, UpdateCategoryOutput> execute(UpdateCategoryInput input) {
        final var id = CategoryID.from(input.id());
        final var name = input.name();
        final var desc = input.description();
        final var isActive = input.isActive();

        final var cat = this.gateway.findById(id).orElseThrow(
                () -> DomainException.with(new Error("Category ID %s was not found".formatted(id.getValue())))
        );
        final var notification = GatherNotificationsValidationHandler.create();
        cat.update(name, desc, isActive).validate(notification);

        return notification.hasError() ? API.Left(notification) : update(cat);
    }

    private Either<GatherNotificationsValidationHandler, UpdateCategoryOutput> update(final Category toUpdate) {
        return API.Try(() -> this.gateway.update(toUpdate))
                .toEither()
                .bimap(GatherNotificationsValidationHandler::create, UpdateCategoryOutput::from);
    }
}
