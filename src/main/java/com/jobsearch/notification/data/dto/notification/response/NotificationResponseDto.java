package com.jobsearch.notification.data.dto.notification.response;

import com.jobsearch.notification.data.enums.NotificationType;

import java.time.LocalDateTime;

public record NotificationResponseDto(String id,
                                      String jobId,
                                      String jobTitle,
                                      String message,
                                      NotificationType type,
                                      Boolean sent,
                                      LocalDateTime createdAt) {

}
