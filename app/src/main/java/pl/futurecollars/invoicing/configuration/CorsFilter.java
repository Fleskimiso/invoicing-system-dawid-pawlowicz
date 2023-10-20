package pl.futurecollars.invoicing.configuration;

import java.io.IOException;
import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

@Component
public class CorsFilter extends OncePerRequestFilter {

  @Value("${invoicing-system.allowedOrigins}")
  private String allowedOrigins;

  @Override
  protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
      throws ServletException, IOException {
    addHeaderIfAbsent("Access-Control-Allow-Origin", allowedOrigins, response);
    addHeaderIfAbsent("Access-Control-Allow-Methods", "GET, POST, PUT, DELETE, OPTIONS", response);
    addHeaderIfAbsent("Access-Control-Max-Age", "3600", response);
    addHeaderIfAbsent("Access-Control-Allow-Headers", "content-type, x-xsrf-token", response);
    addHeaderIfAbsent("Access-Control-Expose-Headers", "*", response);
    addHeaderIfAbsent("Access-Control-Allow-Credentials", "true", response);

    if ("OPTIONS".equals(request.getMethod())) {
      response.setStatus(HttpServletResponse.SC_OK);
    } else {
      filterChain.doFilter(request, response);
    }
  }

  private void addHeaderIfAbsent(String header, String value, HttpServletResponse response) {
    if (!response.getHeaderNames().contains(header)) {
      response.addHeader(header, value);
    }
  }

}
