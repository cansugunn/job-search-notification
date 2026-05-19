package com.jobsearch.notification.validator;

import jakarta.validation.constraints.NotEmpty;
import java.util.Objects;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.AuthenticationCredentialsNotFoundException;
import org.springframework.stereotype.Component;
import org.springframework.validation.annotation.Validated;

import static org.springframework.http.HttpStatus.UNAUTHORIZED;

@Validated
@Component
public class ExternalSchedulerValidator {

  @Value("${job-search.scheduler.secret}")
  private String secret;

  public void validateSecret(@NotEmpty String secret) {
    if (!Objects.equals(secret, this.secret)) {
      throw new AuthenticationCredentialsNotFoundException(UNAUTHORIZED.name());
    }
  }
}
