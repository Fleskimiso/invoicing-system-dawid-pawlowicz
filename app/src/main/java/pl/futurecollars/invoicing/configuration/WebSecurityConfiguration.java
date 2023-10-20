package pl.futurecollars.invoicing.configuration;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.web.access.channel.ChannelProcessingFilter;
import org.springframework.security.web.csrf.CookieCsrfTokenRepository;

@Configuration
@EnableWebSecurity
public class WebSecurityConfiguration extends WebSecurityConfigurerAdapter {

  @Autowired
  private CorsFilter corsFilter;

  @Value("${invoicing-system.csrf.enabled:true}")
  private boolean isCsrfEnabled;

  @Override
  protected void configure(HttpSecurity http) throws Exception {

    http.httpBasic()
        .and()
        .addFilterBefore(corsFilter, ChannelProcessingFilter.class);

    if (isCsrfEnabled) {
      http.csrf(csrf -> csrf.csrfTokenRepository(CookieCsrfTokenRepository.withHttpOnlyFalse()));
    } else {
      http.csrf().disable();
    }
  }

}
