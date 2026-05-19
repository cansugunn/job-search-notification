package com.jobsearch.notification.validator;

import java.util.Objects;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.AuthenticationCredentialsNotFoundException;
import org.springframework.stereotype.Component;

import static org.springframework.http.HttpStatus.UNAUTHORIZED;

@Component
public class ExternalSchedulerValidator {

  @Value("${job-search.scheduler.secret}")
  private String secret;

  public void validateSecret(String secret) {
    if (!Objects.equals(secret, this.secret)) {
      throw new AuthenticationCredentialsNotFoundException(UNAUTHORIZED.name());
    }
  }
}
