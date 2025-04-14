package com.pedrojpx.codeflix.admin.catalog.infrastructure.category;

import org.junit.jupiter.api.extension.BeforeEachCallback;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.api.extension.ExtensionContext;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.FilterType;
import org.springframework.data.repository.CrudRepository;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.lang.annotation.*;
import java.util.Collection;

@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
@Inherited
@ActiveProfiles("test")
@ComponentScan(includeFilters = { // o DataJPATest não pegaria o gateway, por isso precisamos usar o @ComponentScan para adicioná-lo
        @ComponentScan.Filter(type = FilterType.REGEX, pattern = ".*[MySqlGateway]")
})
@DataJpaTest
// não usamos o @SpringBootTest este sobe o contexto do Springboot inteiro (útil para testes e2e), mas aqui só precisamos da parte de dados
@ExtendWith(MySqlGatewayTest.CleanUpExtension.class)
public @interface MySqlGatewayTest {

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
}
