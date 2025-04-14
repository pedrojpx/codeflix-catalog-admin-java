package com.pedrojpx.codeflix.admin.catalog.domain.validation.handler;

import com.pedrojpx.codeflix.admin.catalog.domain.exceptions.DomainException;
import com.pedrojpx.codeflix.admin.catalog.domain.validation.Error;
import com.pedrojpx.codeflix.admin.catalog.domain.validation.ValidationHandler;

import java.util.ArrayList;
import java.util.List;

public class GatherNotificationsValidationHandler implements ValidationHandler {

    private final List<Error> errors;

    private GatherNotificationsValidationHandler(final List<Error> errors) {
        this.errors = errors;
    }

    public static GatherNotificationsValidationHandler create() {
        return new GatherNotificationsValidationHandler(new ArrayList<>());
    }

    public static GatherNotificationsValidationHandler create(final Error e) {
        final var notification = new GatherNotificationsValidationHandler(new ArrayList<>());
        notification.append(e);
        return notification;
    }

    public static GatherNotificationsValidationHandler create(final Throwable t) {
        return create(new Error(t.getMessage()));
    }


    @Override
    public ValidationHandler append(Error anError) {
        this.errors.add(anError);
        return this;
    }

    @Override
    public ValidationHandler append(ValidationHandler aHandler) {
        this.errors.addAll(aHandler.getErrors());
        return null;
    }

    @Override
    public ValidationHandler handle(Validation aValidation) {
        try {
            aValidation.validate();
        } catch (final DomainException ex) {
            this.errors.addAll(ex.getErrors());
        } catch (final Throwable t) {
            this.errors.add(new Error(t.getMessage()));
        }

        return this;
    }

    @Override
    public List<Error> getErrors() {
        return errors;
    }
}
