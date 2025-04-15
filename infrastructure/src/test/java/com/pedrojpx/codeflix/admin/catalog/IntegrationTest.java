package com.pedrojpx.codeflix.admin.catalog;

import com.pedrojpx.codeflix.admin.catalog.infrastructure.configuration.WebServerConfig;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.FilterType;
import org.springframework.test.context.ActiveProfiles;

import java.lang.annotation.*;

@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
@Inherited
@ActiveProfiles("test")
@SpringBootTest(classes = WebServerConfig.class)
// não usamos o @SpringBootTest este sobe o contexto do Springboot inteiro (útil para testes e2e), mas aqui só precisamos da parte de dados
@ExtendWith(CleanUpExtension.class)
public @interface IntegrationTest {

}
