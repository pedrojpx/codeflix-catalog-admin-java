package com.pedrojpx.codeflix.admin.catalog;

import org.junit.jupiter.api.extension.BeforeEachCallback;
import org.junit.jupiter.api.extension.ExtensionContext;
import org.springframework.data.repository.CrudRepository;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.Collection;

class CleanUpExtension implements BeforeEachCallback {

    @Override
    public void beforeEach(final ExtensionContext extensionContext) throws Exception {
        final var repos = SpringExtension.getApplicationContext(extensionContext).getBeansOfType(CrudRepository.class).values();
        cleanUp(repos);
    }

    private void cleanUp(final Collection<CrudRepository> repos) {
        repos.forEach(CrudRepository::deleteAll);
    }
}
