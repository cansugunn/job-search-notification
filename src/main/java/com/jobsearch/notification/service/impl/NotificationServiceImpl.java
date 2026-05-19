package com.jobsearch.notification.service.impl;

import com.jobsearch.notification.data.dto.notification.response.NotificationResponseDto;
import com.jobsearch.notification.data.mapper.NotificationMapper;
import com.jobsearch.notification.data.repository.NotificationRepository;
import com.jobsearch.notification.service.NotificationService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.validation.annotation.Validated;

@Slf4j
@Validated
@Service
@RequiredArgsConstructor
public class NotificationServiceImpl implements NotificationService {

  private final NotificationRepository notificationRepository;

  private final NotificationMapper notificationMapper;

  @Override
  public Page<NotificationResponseDto> findNotifications(String userId, Pageable pageable) {
    return notificationRepository.findByUserIdOrderByCreatedAtDesc(userId, pageable)
                                 .map(notificationMapper::toNotificationResponseDto);
  }
}
