package com.pedrojpx.codeflix.admin.catalog.application.category.create;

import com.pedrojpx.codeflix.admin.catalog.application.UseCase;
import com.pedrojpx.codeflix.admin.catalog.domain.validation.handler.GatherNotificationsValidationHandler;
import io.vavr.control.Either;

public abstract class CreateCategoryUseCase extends UseCase<CreateCategoryInput, Either<GatherNotificationsValidationHandler, CreateCategoryOutput>> {

}
