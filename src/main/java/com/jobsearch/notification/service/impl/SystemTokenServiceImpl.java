package com.jobsearch.notification.service.impl;

import com.jobsearch.notification.data.dto.supabase.SupabaseAuthRequest;
import com.jobsearch.notification.data.dto.supabase.SupabaseAuthResponse;
import com.jobsearch.notification.service.SystemTokenCacheService;
import com.jobsearch.notification.service.SystemTokenService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClient;

@Slf4j
@Service
@RequiredArgsConstructor
public class SystemTokenServiceImpl implements SystemTokenService {

  private static final long LOCK_POLL_MS = 100;

  @Value("${job-search.supabase.url}")
  private String supabaseUrl;

  @Value("${job-search.supabase.api-key}")
  private String supabaseApiKey;

  @Value("${job-search.system-user.email}")
  private String systemUserEmail;

  @Value("${job-search.system-user.password}")
  private String systemUserPassword;

  private final SystemTokenCacheService tokenCacheService;

  @Override
  public synchronized String getToken() {
    return tokenCacheService.find().orElseGet(this::acquireToken);
  }

  @Override
  public void evict() {
    tokenCacheService.evict();
  }

  private String acquireToken() {
    if (tokenCacheService.tryLock()) {
      try {
        String token = fetchFromSupabase();
        tokenCacheService.save(token);
        return token;
      } finally {
        tokenCacheService.unlock();
      }
    }

    waitForLock();
    return tokenCacheService.find().orElseGet(this::fetchFromSupabase);
  }

  private String fetchFromSupabase() {
    log.info("Fetching system token from Supabase for user={}", systemUserEmail);
    SupabaseAuthResponse response = RestClient.create()
        .post()
        .uri(supabaseUrl + "/auth/v1/token?grant_type=password")
        .header("apikey", supabaseApiKey)
        .contentType(MediaType.APPLICATION_JSON)
        .body(new SupabaseAuthRequest(systemUserEmail, systemUserPassword))
        .retrieve()
        .body(SupabaseAuthResponse.class);
    return response.accessToken();
  }

  private void waitForLock() {
    long deadline = System.currentTimeMillis() + tokenCacheService.lockTtlSeconds * 1000;
    while (tokenCacheService.isLocked()) {
      if (System.currentTimeMillis() >= deadline) {
        log.warn("Timed out waiting for system token lock");
        break;
      }
      try {
        Thread.sleep(LOCK_POLL_MS);
      } catch (InterruptedException e) {
        Thread.currentThread().interrupt();
        break;
      }
    }
  }
}
