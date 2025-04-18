package com.pedrojpx.codeflix.admin.catalog;

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
        @ComponentScan.Filter(type = FilterType.REGEX, pattern = ".[MySqlGateway]")
})
@DataJpaTest //loads only what is pertinent to database testing
// não usamos o @SpringBootTest este sobe o contexto do Springboot inteiro (útil para testes e2e), mas aqui só precisamos da parte de dados
@ExtendWith(CleanUpExtension.class)
public @interface MySqlGatewayTest {

}
