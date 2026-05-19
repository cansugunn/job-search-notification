package com.jobsearch.notification.service;

import com.jobsearch.notification.data.dto.notification.response.NotificationResponseDto;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface NotificationService {

  Page<NotificationResponseDto> findNotifications(@NotEmpty String userId,
                                                  @NotNull Pageable pageable);

}
