package com.jobsearch.notification.service;

import jakarta.validation.constraints.NotBlank;
import java.util.Optional;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.validation.annotation.Validated;

@Validated
public abstract class SystemTokenCacheService {

  protected static final String TOKEN_KEY = "system:admin-token";
  protected static final String LOCK_KEY = "system:admin-token:lock";

  @Value("${job-search.system-token.ttl-seconds:3500}")
  protected long tokenTtlSeconds;

  @Value("${job-search.system-token.lock-ttl-seconds:10}")
  public long lockTtlSeconds;

  public abstract Optional<String> find();

  public abstract void save(@NotBlank String token);

  public abstract void evict();

  public abstract boolean tryLock();

  public abstract void unlock();

  public abstract boolean isLocked();
}
