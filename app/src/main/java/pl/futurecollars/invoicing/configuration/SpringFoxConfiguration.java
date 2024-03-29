package pl.futurecollars.invoicing.configuration;

import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import springfox.documentation.builders.ApiInfoBuilder;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.service.Contact;
import springfox.documentation.service.Tag;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;

@Slf4j
@Configuration
public class SpringFoxConfiguration {

  @Bean
  public Docket api() {
    log.info("creating docket");
    return new Docket(DocumentationType.SWAGGER_2)
        .select()
        .apis(RequestHandlerSelectors.basePackage("pl.futurecollars"))
        .paths(PathSelectors.any())
        .build()
        .tags(new Tag("invoice-controller", "Controller used to manage invoices"))
        .tags(new Tag("company-controller", "Controller used to manage companies"))
        .apiInfo(apiInfo());
  }

  private ApiInfo apiInfo() {
    return new ApiInfoBuilder()
        .description("An application to manage invoices")
        .license("MIT license")
        .title("Invoice Manager")
        .contact(
            new Contact("Dawid", "https://github.com/Fleskimiso/invoicing-system-dawid-pawlowicz", "")
        )
        .version("1.0.1")
        .build();
  }

}
