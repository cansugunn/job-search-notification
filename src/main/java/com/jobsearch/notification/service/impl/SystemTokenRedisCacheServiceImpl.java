package com.jobsearch.notification.service.impl;

import com.jobsearch.notification.service.SystemTokenCacheService;
import java.time.Duration;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;

import static java.util.Optional.empty;
import static java.util.Optional.ofNullable;

@Slf4j
@Service
@RequiredArgsConstructor
public class SystemTokenRedisCacheServiceImpl extends SystemTokenCacheService {

  private final StringRedisTemplate redisTemplate;

  @Override
  public Optional<String> find() {
    try {
      return ofNullable(redisTemplate.opsForValue().get(TOKEN_KEY));
    } catch (Exception e) {
      log.warn("Redis cache get failed for key={}: {}", TOKEN_KEY, e.getMessage());
      return empty();
    }
  }

  @Override
  public void save(String token) {
    try {
      redisTemplate.opsForValue().set(TOKEN_KEY, token, Duration.ofSeconds(tokenTtlSeconds));
      log.info("System admin token stored in Redis (ttl={}s)", tokenTtlSeconds);
    } catch (Exception e) {
      log.warn("Redis cache set failed for key={}: {}", TOKEN_KEY, e.getMessage());
    }
  }

  @Override
  public void evict() {
    redisTemplate.delete(TOKEN_KEY);
    log.info("System admin token evicted from Redis");
  }

  @Override
  public boolean tryLock() {
    return Boolean.TRUE.equals(
        redisTemplate.opsForValue().setIfAbsent(LOCK_KEY, "1", Duration.ofSeconds(lockTtlSeconds)));
  }

  @Override
  public void unlock() {
    redisTemplate.delete(LOCK_KEY);
  }

  @Override
  public boolean isLocked() {
    return Boolean.TRUE.equals(redisTemplate.hasKey(LOCK_KEY));
  }
}
