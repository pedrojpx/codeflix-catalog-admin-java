package com.pedrojpx.codeflix.admin.catalog.application;

public abstract class UnitUseCase<IN> {

    public abstract void execute(IN input);
}