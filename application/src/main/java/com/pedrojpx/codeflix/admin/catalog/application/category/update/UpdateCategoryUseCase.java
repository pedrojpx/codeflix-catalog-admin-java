package com.pedrojpx.codeflix.admin.catalog.application.category.update;

import com.pedrojpx.codeflix.admin.catalog.application.UseCase;
import com.pedrojpx.codeflix.admin.catalog.domain.validation.handler.GatherNotificationsValidationHandler;
import io.vavr.control.Either;

public abstract class UpdateCategoryUseCase extends UseCase<UpdateCategoryInput, Either<GatherNotificationsValidationHandler, UpdateCategoryOutput>> {
}
